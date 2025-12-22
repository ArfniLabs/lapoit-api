package com.lapoit.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHistory {

    private Long historyId;     // history_id (PK)
    private Long userId;        // user_id (FK -> user.id)
    private Long storeId;       // store_id (FK -> store.store_id)

    private Long scoreDelta; // score_delta (변동량)
    private Long pointDelta;    // point_delta (변동량)

    private LocalDateTime createdAt; // created_at
}