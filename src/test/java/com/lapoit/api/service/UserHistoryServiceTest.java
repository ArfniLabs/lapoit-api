package com.lapoit.api.service;

import com.lapoit.api.mapper.LoginHistoryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserHistoryServiceTest {

    @Mock
    LoginHistoryMapper loginHistoryMapper;

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
}
