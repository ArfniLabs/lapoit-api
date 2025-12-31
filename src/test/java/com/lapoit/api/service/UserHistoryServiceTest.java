package com.lapoit.api.service;

import com.lapoit.api.dto.history.DailyDauDto;
import com.lapoit.api.dto.history.DailyGameAndDauDto;
import com.lapoit.api.dto.history.DailyJoinCountDto;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.LoginHistoryMapper;
import com.lapoit.api.mapper.PlayGameMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserHistoryServiceTest {

    @Mock
    LoginHistoryMapper loginHistoryMapper;

    @Mock
    PlayGameMapper playGameMapper;

    @InjectMocks
    UserHistoryService userHistoryService;

    @Test
    public void getTodayDau() {
        // given
        when(loginHistoryMapper.countDistinctUsersBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(12L);

        // when
        Long result = userHistoryService.getTodayDau();

        // then (결과 검증)
        assertThat(result).isEqualTo(12L);

        // then (파라미터 검증)
        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        verify(loginHistoryMapper).countDistinctUsersBetween(startCaptor.capture(), endCaptor.capture());

        LocalDate today = LocalDate.now();
        LocalDateTime expectedStart = today.atStartOfDay();
        LocalDateTime expectedEnd = today.plusDays(1).atStartOfDay();

        assertThat(startCaptor.getValue()).isEqualTo(expectedStart);
        assertThat(endCaptor.getValue()).isEqualTo(expectedEnd);
    }

    @Test
    public void rangeUser() {
        List<DailyDauDto> dau = List.of(
                new DailyDauDto(LocalDate.of(2024, 12, 20), 10l),
                new DailyDauDto(LocalDate.of(2024, 12, 21), 12l),
                new DailyDauDto(LocalDate.of(2024, 12, 22), 14l)
        );

        List<DailyJoinCountDto> join=List.of(
                new DailyJoinCountDto(LocalDate.of(2024, 12, 20), 10l),
                new DailyJoinCountDto(LocalDate.of(2024, 12, 21), 12l),
                new DailyJoinCountDto(LocalDate.of(2024, 12, 22), 14l)
        );

        // given
        when(loginHistoryMapper.countDailyDistinctUsersBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(dau);

        when(playGameMapper.selectDailyJoinCount(
                any(LocalDate.class),
                any(LocalDate.class)))
                .thenReturn(join);


        //입력할 날짜
        LocalDate startDate = LocalDate.of(2024, 12, 20);
        LocalDate endDate = LocalDate.of(2024, 12, 22);

        // when
        List<DailyGameAndDauDto> result = userHistoryService.rangeUser(startDate,endDate);


        System.out.println(result);
        // then (결과 검증)
        assertThat(result)
                .extracting(
                        DailyGameAndDauDto::getDate,
                        DailyGameAndDauDto::getDau,
                        DailyGameAndDauDto::getJoinCount
                )
                .containsExactly(
                        tuple(LocalDate.of(2024, 12, 20), 10L, 10L),
                        tuple(LocalDate.of(2024, 12, 21), 12L, 12L),
                        tuple(LocalDate.of(2024, 12, 22), 14L, 14L)
                );

    }


    @Test
    void rangeUser_시작날짜가_종료날짜보다_늦으면_INVALID_DATE_RANGE() {
        // given
        LocalDate startDate = LocalDate.of(2024, 12, 22);
        LocalDate endDate   = LocalDate.of(2024, 12, 20);

        // when & then
        assertThatThrownBy(() -> userHistoryService.rangeUser(startDate, endDate))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_DATE_RANGE);
    }

    @Test
    void rangeUser_종료날짜가_미래면_END_DATE_IN_FUTURE() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate   = LocalDate.now().plusDays(1); // 항상 미래

        // when & then
        assertThatThrownBy(() -> userHistoryService.rangeUser(startDate, endDate))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.END_DATE_IN_FUTURE);
    }



}
