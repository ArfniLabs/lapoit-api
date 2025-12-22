package com.lapoit.api.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserAttendanceResponse {

    private Long userId;
    private int year;
    private int month;

    private int attendanceCount;
    private List<LocalDate> attendanceDates;
}

