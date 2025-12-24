package com.lapoit.api.mapper;


import com.lapoit.api.dto.rank.StoreRankingItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface RankingMapper {
    List<StoreRankingItem> findStoreRanking(
            @Param("storeId") Long storeId
    );
}

