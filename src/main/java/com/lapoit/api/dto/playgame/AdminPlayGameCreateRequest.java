package com.lapoit.api.dto.playgame;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminPlayGameCreateRequest {

    private Long storeId;
    private Long gameId;
    private String gameSubtitle;
}

