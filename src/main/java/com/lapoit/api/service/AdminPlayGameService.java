package com.lapoit.api.service;

import com.lapoit.api.domain.*;
import com.lapoit.api.dto.playgame.*;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminPlayGameService {

    private final GameMapper gameMapper;
    private final GameBlindMapper gameBlindMapper;
    private final GameReEntryMapper gameReEntryMapper;
    private final PlayGameMapper playGameMapper;
    private final UserGameMapper userGameMapper;
    private final UserMapper userMapper;
    private final SseService sseService;

    public PlayGameResponse createPlayGame(AdminPlayGameCreateRequest dto) {

        if (!gameMapper.existsById(dto.getGameId())) {
            throw new CustomException(ErrorCode.GAME_NOT_FOUND);
        }

        playGameMapper.insertPlayGame(dto);
        Long playGameId = playGameMapper.selectLastInsertId();

        // ⭐ 전체 데이터 다시 조회해서 반환
        return playGameMapper.findPlayGameById(playGameId);
    }

    public PlayGameResponse startPlayGame(Long playGameId) {
        playGameMapper.startPlayGame(playGameId);

        // 게임 시작 SSE 메시지발행
        sseService.sendToPlayGame(String.valueOf(playGameId), "GAME_STARTED", Map.of("playGameId", playGameId));
        return playGameMapper.findPlayGameById(playGameId);
    }

    public PlayGameResponse pausePlayGame(Long playGameId) {
        String status = playGameMapper.findStatusById(playGameId);

        if (!"STARTED".equals(status)) {
            throw new CustomException(ErrorCode.GAME_ALREADY_PAUSE);
        }

        playGameMapper.pausePlayGame(playGameId);

        // 게임 정지 SSE 메시지 발행
        sseService.sendToPlayGame(String.valueOf(playGameId), "GAME_PAUSED", Map.of("playGameId", playGameId));
        return playGameMapper.findPlayGameById(playGameId);
    }

    public PlayGameResponse resumePlayGame(Long playGameId) {
        String status = playGameMapper.findStatusById(playGameId);

        if (!"PAUSED".equals(status)) {
            throw new CustomException(ErrorCode.GAME_ALREADY_RESUME);
        }

        playGameMapper.resumePlayGame(playGameId);

        // 게임 재개 SSE 메시지 전송
        sseService.sendToPlayGame(String.valueOf(playGameId), "GAME_RESUMED", Map.of("playGameId", playGameId));
        return playGameMapper.findPlayGameById(playGameId);
    }

    public PlayGameResponse finishPlayGame(Long playGameId) {

        String status = playGameMapper.findStatusById(playGameId);

        // STARTED 또는 PAUSED 상태에서만 종료 가능
        if (!"STARTED".equals(status) && !"PAUSED".equals(status) && !"WAIT".equals(status) ) {
            throw new CustomException(ErrorCode.GAME_ALREADY_FINISHED);
        }

        playGameMapper.finishGame(playGameId);

        return playGameMapper.findPlayGameById(playGameId);
    }



    // ===============================
    // ✅ 유저 게임 참가
    // ===============================
    public AdminJoinGameResponse joinUser(Long playGameId, Long userId) {

        // 1️⃣ 게임 존재 확인
        PlayGameResponse game = playGameMapper.findPlayGameById(playGameId);
        if (game == null) {
            throw new CustomException(ErrorCode.GAME_NOT_FOUND);
        }

        Game gameInfo = gameMapper.findById(game.getGameId());

        // 2️⃣ 종료된 게임 체크
        if ("FINISHED".equals(game.getGameStatus())) {
            throw new CustomException(ErrorCode.GAME_ALREADY_FINISHED);
        }

        // 3️⃣ 중복 참가 방지 (같은 playGame)
        if (userGameMapper.existsByPlayGameIdAndUserId(playGameId, userId)) {
            throw new CustomException(ErrorCode.GAME_ALREADY_JOINED);
        }

        // 4️⃣ 유저 조회
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // ===============================
        // ✅ 5️⃣ 출석 체크 로직
        // ===============================
        String today = LocalDate.now().toString(); // yyyy-MM-dd

        boolean alreadyAttendedToday =
                userGameMapper.existsAttendanceToday(userId, today);

        // 6️⃣ 참가 INSERT (attendance_date는 항상 오늘)
        userGameMapper.insertUserGame(
                playGameId,
                userId,
                game.getGameId(),
                game.getStoreId(),
                today
        );

        // 8️⃣ 스택 + 인원 증가
        playGameMapper.addStackOnJoin(
                playGameId,
                gameInfo.getGameStack()
        );

        // 인원 추가 sse 발행
        sseService.sendToPlayGame(String.valueOf(playGameId), "PLAYER_JOIN", Map.of("playGameId", playGameId));


        // 9️⃣ 응답
        return AdminJoinGameResponse.builder()
                .userId(user.getId())
                .nickname(user.getUserNickname())
                .name(user.getUserName())
                .build();
    }


    public PlayGameResponse outPlayer(Long playGameId, Long userGameId) {

        // 1️⃣ 게임 상태 체크
        String status = playGameMapper.findStatusById(playGameId);
        if (!"STARTED".equals(status)) {
            throw new CustomException(ErrorCode.GAME_NOT_STARTED);
        }

        // 2️⃣ 유저 게임 조회
        UserGame userGame = userGameMapper.findByUserGameId(userGameId);
        if (userGame == null || !userGame.getPlayGameId().equals(playGameId)) {
            throw new CustomException(ErrorCode.USER_NOT_IN_GAME);
        }

        // 3️⃣ 이미 탈락한 경우
        if ("DIE".equals(userGame.getStatus())) {
            throw new CustomException(ErrorCode.USER_ALREADY_OUT);
        }

        // 4️⃣ 탈락 처리
        userGameMapper.markDie(userGameId);
        playGameMapper.decreaseNowPeople(playGameId);

        // SSE
        sseService.sendToPlayGame(
                String.valueOf(playGameId),
                "PLAYER_OUT",
                Map.of("userGameId", userGameId)
        );

        return playGameMapper.findPlayGameById(playGameId);
    }



    @Transactional
    public void rebuy(Long playGameId, Long userGameId) {

        String gameStatus = playGameMapper.findStatusById(playGameId);
        if (!"STARTED".equals(gameStatus)) {
            throw new CustomException(ErrorCode.GAME_NOT_STARTED);
        }

        UserGame userGame = userGameMapper.findByUserGameId(userGameId);
        if (userGame == null || !userGame.getPlayGameId().equals(playGameId)) {
            throw new CustomException(ErrorCode.USER_NOT_IN_GAME);
        }

        int nextRebuyCount = userGame.getRebuyinCount() + 1;

        GameReEntry reEntry =
                gameReEntryMapper.findByGameIdAndCount(
                        userGame.getGameId(),
                        nextRebuyCount
                );

        if (reEntry == null) {
            throw new CustomException(ErrorCode.REBUYIN_COUNT_FULL);
        }

        userGameMapper.increaseRebuyCount(userGameId);
        playGameMapper.increaseRebuyinCount(playGameId);
        playGameMapper.addStack(playGameId, reEntry.getReEntryStack());

        if ("DIE".equals(userGame.getStatus())) {
            userGameMapper.reviveUser(userGameId);
            playGameMapper.increaseNowPeople(playGameId);
        }

        sseService.sendToPlayGame(
                String.valueOf(playGameId),
                "REBUY",
                Map.of("userGameId", userGameId)
        );
    }


    @Transactional
    public void cancelRebuy(Long playGameId, Long userGameId) {

        UserGame userGame = userGameMapper.findByUserGameId(userGameId);
        if (userGame == null) {
            throw new CustomException(ErrorCode.USER_NOT_IN_GAME);
        }

        int currentRebuyCount = userGame.getRebuyinCount();


        GameReEntry reEntry =
                gameReEntryMapper.findByGameIdAndCount(
                        userGame.getGameId(),
                        currentRebuyCount
                );

        userGameMapper.decreaseRebuyCount(userGameId);
        playGameMapper.decreaseRebuyinCount(playGameId);
        playGameMapper.subtractStack(playGameId, reEntry.getReEntryStack());

        if ("ALIVE".equals(userGame.getStatus()) && currentRebuyCount == 1) {
            userGameMapper.markDie(userGameId);
            playGameMapper.decreaseNowPeople(playGameId);
        }

        sseService.sendToPlayGame(
                String.valueOf(playGameId),
                "REBUY",
                Map.of("userGameId", userGameId)
        );
    }



    @Transactional
    public AdminJoinGameResponse joinGuest(Long playGameId, String guestName) {

        // 1️⃣ 게임 존재 확인
        PlayGameResponse game = playGameMapper.findPlayGameById(playGameId);
        if (game == null) {
            throw new CustomException(ErrorCode.GAME_NOT_FOUND);
        }

        if ("FINISHED".equals(game.getGameStatus())) {
            throw new CustomException(ErrorCode.GAME_ALREADY_FINISHED);
        }


        Game gameInfo = gameMapper.findById(game.getGameId());

        String today = LocalDate.now().toString();

        // 2️⃣ 비회원 참가 INSERT
        userGameMapper.insertGuestUserGame(
                playGameId,
                game.getGameId(),
                game.getStoreId(),
                guestName,
                today
        );

        // 3️⃣ 스택 + 인원 증가
        playGameMapper.addStackOnJoin(
                playGameId,
                gameInfo.getGameStack()
        );

        // 4️⃣ SSE 발행
        sseService.sendToPlayGame(
                String.valueOf(playGameId),
                "PLAYER_JOIN",
                Map.of(
                        "playGameId", playGameId,
                        "guest", true,
                        "guestName", guestName
                )
        );

        // 5️⃣ 응답
        return AdminJoinGameResponse.builder()
                .userId(null)
                .name(guestName)
                .nickname(null)
                .guest(true)
                .build();
    }


    @Transactional
    public void nextLevel(Long playGameId) {

        PlayGameResponse game = playGameMapper.findPlayGameById(playGameId);
        if (game == null) {
            throw new CustomException(ErrorCode.GAME_NOT_FOUND);
        }

        // STARTED 상태에서만 자동 레벨업
        if (!"STARTED".equals(game.getGameStatus())) {
            return;
        }

        int currentLevel = game.getGameLevel();
        int nextLevel = currentLevel + 1;

        // 다음 블라인드 존재 확인
        if (!gameBlindMapper.existsByGameIdAndLevel(game.getGameId(), nextLevel)) {
            // 더 이상 레벨 없음 → 게임 종료
            playGameMapper.finishGame(playGameId);
            return;
        }

        // 🔥 일반 레벨 → 레벨 증가
        playGameMapper.updateLevel(
                playGameId,
                nextLevel,
                LocalDateTime.now()
        );

        sseService.sendToPlayGame(
                String.valueOf(playGameId),
                "LEVEL_CHANGED",
                Map.of(
                        "playGameId", playGameId,
                        "level", nextLevel
                )
        );
    }


    @Transactional
    public void updatePayment(Long userGameId, UserGamePaymentRequest request) {

        UserGame userGame = userGameMapper.findByUserGameId(userGameId);
        if (userGame == null) {
            throw new CustomException(ErrorCode.USER_NOT_IN_GAME);
        }

        // 결제 여부에 따라 paidAt 자동 처리
        LocalDateTime paidAt = request.isPaid()
                ? LocalDateTime.now()
                : null;

        userGameMapper.updatePayment(
                userGameId,
                request.isPaid(),
                request.getPaymentMethod(),   // enum → VARCHAR
                request.getPaymentMemo(),
                paidAt
        );
    }



}

