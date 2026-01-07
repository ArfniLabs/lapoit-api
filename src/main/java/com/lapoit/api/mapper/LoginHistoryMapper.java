package com.lapoit.api.mapper;

import com.lapoit.api.dto.history.DailyDauDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LoginHistoryMapper {
    int insertLoginHistory(@Param("userId") Long userId);

    long countDistinctUsersBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
    List<DailyDauDto> countDailyDistinctUsersBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
