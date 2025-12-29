package com.lapoit.api.dto.history;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyDauDto {
    private LocalDate date;
    private Long dau;
}
