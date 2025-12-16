package com.lapoit.api.service;

import com.lapoit.api.domain.Game;
import com.lapoit.api.domain.User;
import com.lapoit.api.dto.playgame.AdminJoinGameResponse;
import com.lapoit.api.dto.playgame.AdminPlayGameCreateRequest;
import com.lapoit.api.dto.playgame.AdminPlayGameResponse;
import com.lapoit.api.dto.playgame.PlayGameResponse;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.GameMapper;
import com.lapoit.api.mapper.PlayGameMapper;
import com.lapoit.api.mapper.UserGameMapper;
import com.lapoit.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminPlayGameService {

    private final GameMapper gameMapper;
    private final PlayGameMapper playGameMapper;
    private final UserGameMapper userGameMapper;
    private final UserMapper userMapper;

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

        return playGameMapper.findPlayGameById(playGameId);
    }

    public PlayGameResponse pausePlayGame(Long playGameId) {
        String status = playGameMapper.findStatusById(playGameId);

        if (!"STARTED".equals(status)) {
            throw new CustomException(ErrorCode.GAME_ALREADY_PAUSE);
        }

        playGameMapper.pausePlayGame(playGameId);
        return playGameMapper.findPlayGameById(playGameId);
    }

    public PlayGameResponse resumePlayGame(Long playGameId) {
        String status = playGameMapper.findStatusById(playGameId);

        if (!"PAUSED".equals(status)) {
            throw new CustomException(ErrorCode.GAME_ALREADY_RESUME);
        }

        playGameMapper.resumePlayGame(playGameId);
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
        Game gameInfo = gameMapper.findById(game.getGameId());
        if (game == null) {
            throw new CustomException(ErrorCode.GAME_NOT_FOUND);
        }

        // 2️⃣ 종료된 게임 체크
        if ("FINISHED".equals(game.getGameStatus())) {
            throw new CustomException(ErrorCode.GAME_ALREADY_FINISHED);
        }

        // 3️⃣ play_game_id 기준 중복 참가 방지
        boolean alreadyJoined =
                userGameMapper.existsByPlayGameIdAndUserId(playGameId, userId);
        if (alreadyJoined) {
            throw new CustomException(ErrorCode.GAME_ALREADY_JOINED);
        }

        // 4️⃣ 유저 조회
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 5️⃣ 참가 INSERT
        userGameMapper.insertUserGame(
                playGameId,
                userId,
                game.getGameId(),
                game.getStoreId()
        );



        playGameMapper.addStackOnJoin(
                playGameId,
                gameInfo.getGameStack() // startingStack
        );

        // 7️⃣ 응답 반환
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

        // 6. 최신 게임 정보 반환
        return playGameMapper.findPlayGameById(playGameId);
    }
}

