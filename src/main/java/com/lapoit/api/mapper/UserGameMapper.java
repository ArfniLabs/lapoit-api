package com.lapoit.api.mapper;

import com.lapoit.api.dto.playgame.UserGamePlayerDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserGameMapper {

    boolean existsByPlayGameIdAndUserId(
            @Param("playGameId") Long playGameId,
            @Param("userId") Long userId
    );

    void insertUserGame(
            @Param("playGameId") Long playGameId,
            @Param("userId") Long userId,
            @Param("gameId") Long gameId,
            @Param("storeId") Long storeId
    );

    List<UserGamePlayerDto> findPlayersByPlayGameId(
            @Param("playGameId") Long playGameId
    );


    boolean isOutPlayer(
            @Param("playGameId") Long playGameId,
            @Param("userId") Long userId
    );

    void outPlayer(
            @Param("playGameId") Long playGameId,
            @Param("userId") Long userId
    );
}
