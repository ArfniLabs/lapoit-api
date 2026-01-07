package com.lapoit.api.mapper;

import com.lapoit.api.domain.PlayGamePrize;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlayGamePrizeMapper {

    void insertPrize(PlayGamePrize prize);

    List<PlayGamePrize> findByPlayGameId(Long playGameId);

    void deleteByPlayGameId(Long playGameId);
}
