package com.lapoit.api.dto.admin;

import com.lapoit.api.domain.User;
import com.lapoit.api.domain.UserHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserHistoryResponse {
    private Long historyId;
    private Long userId;
    private Long storeId;

    private Long scoreDelta;
    private Long pointDelta;
    private Long actorUserId;
    private LocalDateTime createdAt;

    public static UserHistoryResponse from(UserHistory history) {
        return UserHistoryResponse.builder()
                .historyId(history.getHistoryId())
                .userId(history.getUserId())
                .storeId(history.getStoreId())
                .scoreDelta(history.getScoreDelta())
                .pointDelta(history.getPointDelta())
                .createdAt(history.getCreatedAt())
                .actorUserId(history.getActorUserId())
                .build();
    }

}
