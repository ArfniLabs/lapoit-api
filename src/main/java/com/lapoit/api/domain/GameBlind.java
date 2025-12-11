package com.lapoit.api.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameBlind {
    private Long gameBlindId;
    private Long gameId;

    private Integer level;
    private Integer bigBlind;
    private Integer smallBlind;
    private Integer ante;
    private Integer duration; // 블라인드 시간 (분)
}

