package com.lapoit.api.dto.playgame;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlayGameRow {

    private Long playGameId;
    private Long storeId;

    private Long gameId;
    private String gameName;
    private String gameSubtitle;

    private String gameStatus;

    private Integer gameLevel;

    private LocalDateTime startAt;
    private LocalDateTime levelStartAt;

    private Integer totalPeople;
    private Integer nowPeople;
    private Integer rebuyinCount;

    private Integer totalStack;
    private Integer averageStack;

    private Integer totalStopTime; // 초 단위
    private Integer levelStopTime;
}
