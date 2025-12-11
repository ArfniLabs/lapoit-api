package com.lapoit.api.dto.auth;

import lombok.Data;

@Data
public class LoginRequestDto {


    private String userId;


    private String password;
}