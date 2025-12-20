package com.lapoit.api.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserScoreRequest {
    private int score;
    private Long storeId;;
}
