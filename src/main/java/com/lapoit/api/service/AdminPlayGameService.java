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


    public PlayGameResponse outPlayer(Long playGameId, Long userId) {

        // 1. 게임 상태 체크
        String status = playGameMapper.findStatusById(playGameId);
        if (!"STARTED".equals(status)) {
            throw new CustomException(ErrorCode.GAME_NOT_STARTED);
        }

        // 2. 참가 여부 확인
        if (!userGameMapper.existsByPlayGameIdAndUserId(playGameId, userId)) {
            throw new CustomException(ErrorCode.USER_NOT_IN_GAME);
        }

        // 3. 이미 탈락한 유저인지
        if (userGameMapper.isOutPlayer(playGameId, userId)) {
            throw new CustomException(ErrorCode.USER_ALREADY_OUT);
        }

        // 4. 유저 OUT 처리
        userGameMapper.outPlayer(playGameId, userId);

        // 5. 현재 인원 감소
        playGameMapper.decreaseNowPeople(playGameId);

        // 유저 아웃 sse 알림
        sseService.sendToPlayGame(String.valueOf(playGameId), "PLAYER_OUT", Map.of("playGameId", playGameId));

        // 6. 최신 게임 정보 반환
        return playGameMapper.findPlayGameById(playGameId);
    }


    @Transactional
    public void rebuy(Long playGameId, Long userId) {

        // 0️⃣ 게임 상태 체크
        String gameStatus = playGameMapper.findStatusById(playGameId);
        if (!"STARTED".equals(gameStatus)) {
            throw new CustomException(ErrorCode.GAME_NOT_STARTED);
        }

        // 1️⃣ 유저 게임 상태 조회
        UserGame userGame =
                userGameMapper.findByPlayGameIdAndUserId(playGameId, userId);

        int nextRebuyCount = userGame.getRebuyinCount() + 1;

        // 2️⃣ 리바인 정책 조회
        GameReEntry reEntry =
                gameReEntryMapper.findByGameIdAndCount(
                        userGame.getGameId(),
                        nextRebuyCount
                );

        if (reEntry == null) {
            throw new CustomException(ErrorCode.REBUYIN_COUNT_FULL);
        }

        // 3️⃣ 유저 리바인 횟수 증가
        userGameMapper.increaseRebuyCount(userGame.getUserGameId());

        // 4️⃣ 게임 전체 리바인 카운트 증가
        playGameMapper.increaseRebuyinCount(playGameId);

        // 5️⃣ 스택 증가
        playGameMapper.addStack(playGameId, reEntry.getReEntryStack());

        // 6️⃣ 탈락 유저라면 부활
        if ("OUT".equals(userGame.getStatus())) {
            userGameMapper.reviveUser(userGame.getUserGameId());
            playGameMapper.increaseNowPeople(playGameId);
        }

        // 유저 리바인 SSE 알람
        sseService.sendToPlayGame(String.valueOf(playGameId), "REBUY", Map.of("playGameId", playGameId));
    }

    @Transactional
    public void cancelRebuy(Long playGameId, Long userId) {

        // 1️⃣ 유저 게임 조회
        UserGame userGame =
                userGameMapper.findByPlayGameIdAndUserId(playGameId, userId);

        if (userGame == null) {
            throw new IllegalArgumentException("유저가 해당 게임에 참가하지 않음");
        }

        int currentRebuyCount = userGame.getRebuyinCount();

        if (currentRebuyCount <= 0) {
            throw new IllegalStateException("취소할 리바인이 없음");
        }

        // 2️⃣ 취소 대상 리바인 정책 조회
        GameReEntry reEntry =
                gameReEntryMapper.findByGameIdAndCount(
                        userGame.getGameId(),
                        currentRebuyCount
                );

        if (reEntry == null) {
            throw new IllegalStateException("리바인 정책 없음");
        }

        // 3️⃣ 유저 리바인 횟수 감소
        userGameMapper.decreaseRebuyCount(userGame.getUserGameId());

        // 4️⃣ 게임 전체 리바인 카운트 감소
        playGameMapper.decreaseRebuyinCount(playGameId);

        // 5️⃣ 스택 감소
        playGameMapper.subtractStack(playGameId, reEntry.getReEntryStack());

        // 6️⃣ 부활 리바인 취소라면 다시 탈락 처리
        if ("ALIVE".equals(userGame.getStatus()) && currentRebuyCount == 1) {
            userGameMapper.markDie(userGame.getUserGameId());
            playGameMapper.decreaseNowPeople(playGameId);
        }

        sseService.sendToPlayGame(String.valueOf(playGameId), "REBUY", Map.of("playGameId", playGameId));
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



}

