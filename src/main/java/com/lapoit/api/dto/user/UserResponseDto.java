package com.lapoit.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String userId;
    private String userName;
    private String userNickname;
    private Integer storeId;
    private String phoneNumber;// USER / ADMIN / SUPERADMIN
    private String code;// ACTIVE / INACTIVE
    private String createAt;
    private Long point;
}
