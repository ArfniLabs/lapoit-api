package com.lapoit.api.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGame {
    private Long userGameId;
    private Long id;           // user.id
    private Long playGameId;
    private Long gameId;
    private Long storeId;

    private String attendanceDate; // DATE (yyyy-MM-dd)
}

