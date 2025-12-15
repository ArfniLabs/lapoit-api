package com.lapoit.api.service;

import com.lapoit.api.dto.playgame.PlayGameRow;
import com.lapoit.api.dto.playgame.PlayGameStoreViewResponse;
import com.lapoit.api.mapper.GameBlindMapper;
import com.lapoit.api.mapper.PlayGameMapper;
import com.lapoit.api.util.GameProgressCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayGameQueryService {

    private final PlayGameMapper playGameMapper;
    private final GameBlindMapper gameBlindMapper;
    private final GameProgressCalculator calculator;

    public List<PlayGameStoreViewResponse> getGamesByStore(Long storeId) {

        List<PlayGameRow> games = playGameMapper.findByStoreId(storeId);

        return games.stream()
                .map(game -> calculator.toResponse(game,
                        gameBlindMapper.findByGameId(game.getGameId())))
                .toList();
    }
}

