package com.lapoit.api.service;

import com.lapoit.api.domain.Game;
import com.lapoit.api.domain.GameBlind;
import com.lapoit.api.dto.playgame.PlayGamePrizeDto;
import com.lapoit.api.dto.playgame.PlayGameDetailResponse;
import com.lapoit.api.dto.playgame.PlayGameRow;
import com.lapoit.api.dto.playgame.PlayGameStoreViewResponse;
import com.lapoit.api.dto.playgame.UserGamePlayerDto;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.*;
import com.lapoit.api.util.GameProgressCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayGameQueryService {

    private final GameMapper gameMapper;
    private final PlayGameMapper playGameMapper;
    private final GameBlindMapper gameBlindMapper;
    private final GameProgressCalculator calculator;
    private final UserGameMapper userGameMapper;
    private final GameProgressCalculator gameProgressCalculator;
    private final PlayGamePrizeMapper prizeMapper;

    public List<PlayGameStoreViewResponse> getGamesByStore(Long storeId) {

        List<PlayGameRow> games = playGameMapper.findByStoreId(storeId);

        return games.stream()
                .map(game -> calculator.toResponse(game,
                        gameBlindMapper.findByGameId(game.getGameId())))
                .toList();
    }


    public PlayGameDetailResponse getGameDetail(Long playGameId) {

        // 1️⃣ 진행 게임 조회
        PlayGameRow playGame =
                playGameMapper.findById(playGameId);

        if (playGame == null) {
            throw new CustomException(ErrorCode.GAME_NOT_FOUND);
        }

        // 2️⃣ 블라인드 전체 조회
        List<GameBlind> blinds =
                gameBlindMapper.findByGameId(playGame.getGameId());

        // 3️⃣ 게임 진행 계산 (레벨/남은시간/평균스택)
        PlayGameStoreViewResponse progress =
                gameProgressCalculator.toResponse(playGame, blinds);

        // 4️⃣ GAME 설정 정보
        Game game =
                gameMapper.findById(playGame.getGameId());

        // 5️⃣ 참가자 목록
        List<UserGamePlayerDto> players =
                userGameMapper.findPlayersByPlayGameId(playGameId);

        List<PlayGamePrizeDto> prizes =
                prizeMapper.findByPlayGameId(playGameId)
                        .stream()
                        .map(p -> {
                            PlayGamePrizeDto dto = new PlayGamePrizeDto();
                            dto.setRank(p.getPrizeRank());
                            dto.setAmount(p.getPrizeAmount());
                            return dto;
                        })
                        .toList();

        return PlayGameDetailResponse.of(
                progress,
                game,
                players,
                prizes
        );



    }


}

