package com.lapoit.api.dto.playgame;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AdminPlayGameResponse {

    private Long playGameId;
    private Long gameId;
    private Long storeId;

    private String gameSubtitle;
    private LocalDate gameDay;

    private LocalDateTime startAt;
    private LocalDateTime levelStartAt;

    private Integer gameLevel;
    private Integer totalPeople;
    private Integer nowPeople;
    private Integer rebuyinCount;

    private Integer totalStack;
    private Integer averageStack;

    private String gameStatus;
}
