package com.lapoit.api.dto.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreRankingResponse {

    private Long storeId;
    private List<StoreRankingItem> rankings;
}