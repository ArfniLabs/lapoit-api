package com.lapoit.api.dto.game;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GameResponseDto {
    private Long gameId;
    private String gameName;
    private Integer gameStack;
    private Integer gamePrice;
    private Integer gameLimit;
    private Integer buyinEndLevel;
    private Integer gameEndLevel;

    private List<GameBlindDto> blinds;
    private List<GameReEntryDto> reEntries;
}

