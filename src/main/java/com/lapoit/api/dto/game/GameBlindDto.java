package com.lapoit.api.dto.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameBlindDto {
    private Integer level;
    private Integer smallBlind;
    private Integer bigBlind;
    private Integer ante;
    private Integer duration;

    // 🔥 브레이크용
    private Boolean isBreak;        // true면 브레이크 레벨
}
