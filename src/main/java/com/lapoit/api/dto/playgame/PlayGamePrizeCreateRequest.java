package com.lapoit.api.dto.playgame;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayGamePrizeCreateRequest {
    private List<PlayGamePrizeDto> prizes; // rank, amount
}
