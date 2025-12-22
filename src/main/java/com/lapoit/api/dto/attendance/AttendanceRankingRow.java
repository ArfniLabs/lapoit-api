package com.lapoit.api.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttendanceRankingRow {

    private Long userId;
    private String name;
    private String nickname;
    private int attendanceCount;
}

