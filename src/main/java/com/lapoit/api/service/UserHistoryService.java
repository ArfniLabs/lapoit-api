package com.lapoit.api.service;

import com.lapoit.api.dto.history.DailyDauDto;
import com.lapoit.api.mapper.LoginHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserHistoryService {

    private final LoginHistoryMapper loginHistoryMapper;

    public Long getTodayDau() {
        LocalDate today =LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        System.out.println(start);
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        return loginHistoryMapper.countDistinctUsersBetween(start, end);
    }


    public List<DailyDauDto> rangeUser(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();
        return loginHistoryMapper.countDailyDistinctUsersBetween(start, end);

    }
}
