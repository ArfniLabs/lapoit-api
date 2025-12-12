package com.lapoit.api.dto.game;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameBlindPatchRequest {

    private Integer smallBlind;
    private Integer bigBlind;
    private Integer ante;
    private Integer duration;
}