package com.lapoit.api.mapper;

import com.lapoit.api.domain.GameBlind;
import com.lapoit.api.dto.game.GameBlindDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameBlindMapper {

    void insertBlind(GameBlind blind);

    void deleteByGameId(Long gameId);

    void patchGameBlind(
            @Param("gameId") Long gameId,
            @Param("dto") GameBlindDto dto
    );


    List<GameBlind> findByGameId(Long gameId);
}

