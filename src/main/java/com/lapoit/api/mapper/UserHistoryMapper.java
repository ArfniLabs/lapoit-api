package com.lapoit.api.mapper;

import com.lapoit.api.domain.UserHistory;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserHistoryMapper {

    List<UserHistory> findUserHistory(
            @Param("userId") Long userId,
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    void insert(UserHistory build);
}
