package com.lapoit.api.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private Long gameId;
    private String gameName;
    private Integer gameStack;      // 기본 스택
    private Integer gamePrice;      // 바인 금액
    private Integer gameLimit;      // 바인 제한 횟수
    private Integer buyinEndLevel;  // 레지 마감 레벨
    private Integer gameEndLevel;   // 종료 레벨
}
