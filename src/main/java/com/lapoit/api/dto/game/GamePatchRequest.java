package com.lapoit.api.dto.game;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GamePatchRequest {
    private String gameName;
    private Integer gameStack;
    private Integer gamePrice;
    private Integer gameLimit;
    private Integer buyinEndLevel;
    private Integer gameEndLevel;
}
