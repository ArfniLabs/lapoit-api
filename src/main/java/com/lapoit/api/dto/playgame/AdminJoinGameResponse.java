package com.lapoit.api.dto.playgame;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminJoinGameResponse {

    private Long userId;
    private String nickname;
    private String name;
}

