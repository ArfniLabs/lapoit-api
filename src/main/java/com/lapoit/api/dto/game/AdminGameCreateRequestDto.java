package com.lapoit.api.dto.game;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
public class AdminGameCreateRequestDto {
    private String gameName;
    private Integer gameStack;
    private Integer gamePrice;
    private Integer gameLimit;
    private Integer buyinEndLevel;
    private Integer gameEndLevel;

    private List<GameReEntryDto> reEntries;
    private List<GameBlindDto> blinds;
}

