package com.lapoit.api.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreateRequestDto {
    private String userId;
    private String password;
    private String userName;
    private String userNickname;
    private Integer storeId;
    private String phoneNumber;
    private Long point;
}

