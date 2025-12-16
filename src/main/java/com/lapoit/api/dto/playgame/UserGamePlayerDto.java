package com.lapoit.api.dto.playgame;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class UserGamePlayerDto {

    private Long userId;
    private String nickname;
    private String name;
}
