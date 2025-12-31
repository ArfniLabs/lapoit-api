package com.lapoit.api.dto.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyJoinCountDto {
    private LocalDate date;
    private Long joinCount;
}