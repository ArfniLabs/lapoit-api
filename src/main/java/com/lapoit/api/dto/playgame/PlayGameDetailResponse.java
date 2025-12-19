package com.lapoit.api.dto.playgame;

import com.lapoit.api.domain.Game;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PlayGameDetailResponse {

    private Long playGameId;
    private String gameSubtitle;
    private String gameName;

    private LocalDateTime startAt;
    private Integer currentLevel;
    private LocalDateTime levelStartAt;

    private Integer levelDurationMinutes;
    private Integer levelRemainingSeconds;

    private BlindDto currentBlind;
    private BlindDto nextBlind;

    private Integer totalPeople;
    private Integer nowPeople;
    private Integer rebuyinCount;
    private Integer totalStack;
    private Integer averageStack;

    // GAME 설정
    private Integer startingStack;
    private Integer buyinPrice;
    private Integer buyinLimitCount;
    private Integer buyinEndLevel;

    // 참가자
    private List<UserGamePlayerDto> players;

    public static PlayGameDetailResponse of(
            PlayGameStoreViewResponse progress,
            Game game,
            List<UserGamePlayerDto> players
    ) {
        return PlayGameDetailResponse.builder()
                .playGameId(progress.getPlayGameId())
                .gameSubtitle(progress.getGameSubtitle())
                .gameName(progress.getGameName())

                .startAt(progress.getStartAt())
                .currentLevel(progress.getCurrentLevel())
                .levelStartAt(progress.getLevelStartAt())
                .levelDurationMinutes(progress.getLevelDurationMinutes())
                .levelRemainingSeconds(progress.getLevelRemainingSeconds())

                .currentBlind(progress.getCurrentBlind())
                .nextBlind(progress.getNextBlind())

                .totalPeople(progress.getTotalPeople())
                .nowPeople(progress.getNowPeople())
                .rebuyinCount(progress.getRebuyinCount())
                .totalStack(progress.getTotalStack())
                .averageStack(progress.getAverageStack())

                .startingStack(game.getGameStack())
                .buyinPrice(game.getGamePrice())
                .buyinLimitCount(game.getGameLimit())
                .buyinEndLevel(game.getBuyinEndLevel())

                .players(players)
                .build();
    }
}

