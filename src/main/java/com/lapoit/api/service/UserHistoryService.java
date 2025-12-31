package com.lapoit.api.service;

import com.lapoit.api.dto.history.*;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.LoginHistoryMapper;
import com.lapoit.api.mapper.PlayGameMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserHistoryService {

    private final LoginHistoryMapper loginHistoryMapper;
    private final PlayGameMapper playGameMapper;

    public Long getTodayDau() {
        LocalDate today =LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        System.out.println(start);
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        return loginHistoryMapper.countDistinctUsersBetween(start, end);
    }


    public List<DailyGameAndDauDto> rangeUser(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();

        if(start.isAfter(end)){
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }

        if(endDate.isAfter(LocalDate.now())){
            throw new CustomException(ErrorCode.END_DATE_IN_FUTURE);
        }


        List<DailyDauDto>  dau=  loginHistoryMapper.countDailyDistinctUsersBetween(start, end);
        List<DailyJoinCountDto> joinCountDtos=playGameMapper.selectDailyJoinCount(startDate, endDate);
        Map<LocalDate, DailyGameAndDauDto> map = new HashMap<>();
        for(DailyDauDto d :dau){
            map.put(d.getDate(), new DailyGameAndDauDto(d.getDate(), d.getDau(), 0l));

        }

        for (DailyJoinCountDto j : joinCountDtos) {
            map.merge(
                    j.getDate(),
                    new DailyGameAndDauDto(
                            j.getDate(),
                            0L,
                            j.getJoinCount()
                    ),
                    (oldVal, newVal) -> {
                        oldVal.setJoinCount(newVal.getJoinCount());
                        return oldVal;
                    }
            );
        }

        List<DailyGameAndDauDto> rs=map.values().stream()
                .sorted(Comparator.comparing(DailyGameAndDauDto::getDate))
                .toList();
        return rs;
    }

    public DailyGameDto getTodayGame() {
        LocalDate date=LocalDate.now();
        List<GameDto> gameList=playGameMapper.selectJoinCountByStoreAndDate(date);
        Long sum=0l;
        for(GameDto game :gameList){
            sum+=game.getJoinCount();

        }
        return DailyGameDto.builder()
                .joinCount(sum)
                .gameList(gameList)
                .build();



    }
}
