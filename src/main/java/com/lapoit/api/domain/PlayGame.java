package com.lapoit.api.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayGame {
    private Long playGameId;
    private Long gameId;
    private Long storeId;

    private String gameDay;          // DATE, yyyy-MM-dd
    private String gameSubtitle;     // 데일리 2부 같은 이름

    private String startAt;          // 게임 시작 시간
    private String levelStartAt;     // 현재 레벨 시작 시간
    private Integer gameLevel;       // 현재 레벨

    private Integer totalPeople;
    private Integer nowPeople;
    private Integer rebuyinCount;

    private Integer totalStack;
    private Integer averageStack;

    private String gameStatus;       // WAIT / STARTED / PAUSED / FINISHED

    private String stopTime;         // 마지막 정지 시각
    private Integer totalStopTime;   // 누적 정지 시간(초)
}

