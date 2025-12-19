package com.lapoit.api.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AttendanceRankingResponse {

    private long storeId;
    private int year;
    private int month;

    private List<AttendanceRankingRow> rankings;
}

