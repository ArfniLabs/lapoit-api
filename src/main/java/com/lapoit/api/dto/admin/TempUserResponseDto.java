package com.lapoit.api.dto.admin;

import lombok.*;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempUserResponseDto {
    private Long id;
    private String userId;
    private String userName;
    private String userNickname;
    private Integer storeId;
    private String phoneNumber;
    private String role;    // USER / ADMIN / SUPERADMIN
    private String code;
    private String status;       // ACTIVE / INACTIVE
    private String createAt;
    private String updateAt;
}

