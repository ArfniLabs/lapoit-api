package com.lapoit.api.dto.rank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRankingDto {
    private Integer rank;
    private Long userId;
    private String nickname;
    private Integer score;
}

