package com.lapoit.api.dto.playgame;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlindDto {

    private Integer level;
    private Integer bigBlind;
    private Integer smallBlind;
    private Integer ante;
}
