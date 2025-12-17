package com.lapoit.api.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserScore {
    private Long userScoreId;

    private Long userId;
    private Long storeId;

    private int score;
}
