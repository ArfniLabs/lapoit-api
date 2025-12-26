package com.lapoit.api.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateRequestDto {
    private String userName;
    private String userNickname;
    private Integer storeId;
    private String phoneNumber;
    private String status; // ACTIVE / INACTIVE
}

