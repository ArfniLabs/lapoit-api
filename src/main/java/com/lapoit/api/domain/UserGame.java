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

    /** 유저 상태: ALIVE / DIE */
    private String status;

    /** 리바인 횟수 */
    private Integer rebuyinCount;
}

