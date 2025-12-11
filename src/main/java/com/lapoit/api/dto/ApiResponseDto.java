package com.lapoit.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor

/** 성공시 어느 컨트롤러에서 성공햇다고 표현하면됨**/
public class ApiResponseDto<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> ApiResponseDto<T> success(String code, String message, T data) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDto<T> fail(String code, String message) {
        return ApiResponseDto.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}
