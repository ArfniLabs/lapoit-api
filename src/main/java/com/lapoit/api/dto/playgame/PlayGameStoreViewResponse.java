package com.lapoit.api.dto.playgame;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PlayGameStoreViewResponse {

    private Long playGameId;
    private Long storeId;

    private String gameSubtitle;
    private String gameName;
    private String gameStatus;

    private LocalDateTime startAt;

    private Integer currentLevel;
    private LocalDateTime levelStartAt;
    private Integer levelStopTime;

    private Integer levelDurationMinutes;
    private Integer levelRemainingSeconds;

    private BlindDto currentBlind;
    private BlindDto nextBlind;

    private Integer totalPeople;
    private Integer nowPeople;
    private Integer rebuyinCount;
    private Integer totalStack;
    private Integer averageStack;
}

