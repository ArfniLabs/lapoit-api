package com.lapoit.api.mapper;

import com.lapoit.api.domain.UserScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserScoreMapper {
    List<UserScore> findByUserId(@Param("userId") Long userId);

    UserScore findByUserIdAndStoreId(@Param("userId") Long userId,@Param("storeId") Long storeId);

    void save(UserScore newScore);
}
