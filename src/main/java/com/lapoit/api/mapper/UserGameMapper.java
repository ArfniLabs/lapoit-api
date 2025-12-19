package com.lapoit.api.mapper;

import com.lapoit.api.domain.UserGame;
import com.lapoit.api.dto.attendance.AttendanceRankingRow;
import com.lapoit.api.dto.playgame.UserGamePlayerDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
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
            @Param("storeId") Long storeId,
            @Param("attendanceDate") String attendanceDate
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

    UserGame findByPlayGameIdAndUserId(
            @Param("playGameId") Long playGameId,
            @Param("userId") Long userId
    );

    void increaseRebuyCount(
            @Param("userGameId") Long userGameId
    );

    void updateStatus(
            @Param("userGameId") Long userGameId,
            @Param("status") String status
    );

    void reviveUser(@Param("userGameId") Long userGameId);

    void decreaseRebuyCount(@Param("userGameId") Long userGameId);

    void markDie(@Param("userGameId") Long userGameId);


    boolean existsAttendanceToday(
            @Param("userId") Long userId,
            @Param("today") String today
    );

    // ===============================
    // 출석 조회 (개인)
    // ===============================
    List<LocalDate> findAttendanceDatesByUserAndMonth(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // ===============================
    // 출석 랭킹 (지점별 / 관리자)
    // ===============================
    List<AttendanceRankingRow> findAttendanceRankingByStore(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
