package com.lapoit.api.dto.rank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRankingItem {

    private Integer rank;    // 순위
    private Long userId;     // 유저 PK
    private String nickname; // 유저 닉네임
    private Integer score;   // 해당 지점 승점
}
