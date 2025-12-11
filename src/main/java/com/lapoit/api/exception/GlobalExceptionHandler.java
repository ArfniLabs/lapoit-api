package com.lapoit.api.exception;

import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.exception.custom.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDto<?>> handleBusinessException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponseDto.fail(errorCode.getCode(), errorCode.getMessage()));
    }

    // 예상하지 못한 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<?>> handleException(Exception ex) {
        ErrorCode error = ErrorCode.INTERNAL_ERROR;

        return ResponseEntity
                .status(error.getStatus())
                .body(ApiResponseDto.fail(error.getCode(), error.getMessage()));
    }
}
