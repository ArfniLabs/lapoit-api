package com.lapoit.api.dto.auth;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    private String userId;

    private String password;


    private String userName;

    private String userNickname;


    // 지점 ID
    private Integer storeId;


    private String phoneNumber;



}

