package com.lapoit.api.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayGamePrize {

    private Long prizeId;
    private Long playGameId;

    private Integer prizeRank;
    private Integer prizeAmount;
}
