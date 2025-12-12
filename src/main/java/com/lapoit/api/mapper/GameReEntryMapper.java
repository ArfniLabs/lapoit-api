package com.lapoit.api.mapper;

import com.lapoit.api.domain.GameReEntry;
import com.lapoit.api.dto.game.GameReEntryPatchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameReEntryMapper {

    void insertReEntry(GameReEntry reEntry);

    void deleteByGameId(Long gameId);

    void patchGameReEntry(
            @Param("gameId") Long gameId,
            @Param("count") Integer count,
            @Param("dto") GameReEntryPatchRequest dto
    );


    List<GameReEntry> findByGameId(Long gameId);
}
