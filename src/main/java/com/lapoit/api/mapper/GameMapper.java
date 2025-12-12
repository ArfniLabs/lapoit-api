package com.lapoit.api.mapper;

import com.lapoit.api.domain.Game;
import com.lapoit.api.dto.game.GamePatchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameMapper {

    void insertGame(Game game);

    void updateGame(Game game);

    void deleteGame(Long gameId);

    Game findById(Long gameId);

    void patchGame(
            @Param("gameId") Long gameId,
            @Param("dto") GamePatchRequest dto
    );

    List<Game> findAll();
}

