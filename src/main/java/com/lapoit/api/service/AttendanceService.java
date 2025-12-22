package com.lapoit.api.service;

import com.lapoit.api.dto.attendance.AttendanceRankingResponse;
import com.lapoit.api.dto.attendance.AttendanceRankingRow;
import com.lapoit.api.dto.attendance.UserAttendanceResponse;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.UserGameMapper;
import com.lapoit.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {

    private final UserGameMapper userGameMapper;
    private final UserMapper userMapper;

    /* ===============================
       개인 출석 조회
       =============================== */
    public UserAttendanceResponse getUserAttendance(
            Long userId, int year, int month
    ) {

        if (userMapper.findById(userId) == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<LocalDate> dates =
                userGameMapper.findAttendanceDatesByUserAndMonth(
                        userId, start, end
                );

        return UserAttendanceResponse.builder()
                .userId(userId)
                .year(year)
                .month(month)
                .attendanceCount(dates.size())
                .attendanceDates(dates)
                .build();
    }

    /* ===============================
       관리자 지점별 출석 랭킹
       =============================== */
    public AttendanceRankingResponse getStoreAttendanceRanking(
            long storeId, int year, int month
    ) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<AttendanceRankingRow> rankings =
                userGameMapper.findAttendanceRankingByStore(
                        storeId, start, end
                );

        return AttendanceRankingResponse.builder()
                .storeId(storeId)
                .year(year)
                .month(month)
                .rankings(rankings)
                .build();
    }
}

