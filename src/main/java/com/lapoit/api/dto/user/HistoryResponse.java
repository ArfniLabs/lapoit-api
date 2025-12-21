package com.lapoit.api.dto.user;

import com.lapoit.api.domain.UserHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResponse {
    private Long historyId;
    private Long userId;
    private Long storeId;

    private Long scoreDelta;
    private Long pointDelta;

    private LocalDateTime createdAt;

    public static HistoryResponse from(UserHistory history) {
        return HistoryResponse.builder()
                .historyId(history.getHistoryId())
                .userId(history.getUserId())
                .storeId(history.getStoreId())
                .scoreDelta(history.getScoreDelta())
                .pointDelta(history.getPointDelta())
                .createdAt(history.getCreatedAt())
                .build();
    }

}