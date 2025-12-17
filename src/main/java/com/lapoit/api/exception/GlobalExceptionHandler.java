package com.lapoit.api.exception;

import com.lapoit.api.dto.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDto<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ErrorCode error = ErrorCode.INVALID_REQUEST_BODY; // 새로 만들거나 기존 코드 사용

        return ResponseEntity
                .status(error.getStatus())
                .body(ApiResponseDto.fail(error.getCode(), error.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseDto<?>> handleMissingRequestParam(MissingServletRequestParameterException ex) {
        ErrorCode error = ErrorCode.MISSING_PARAMETER; // 400
        return ResponseEntity.status(error.getStatus())
                .body(ApiResponseDto.fail(error.getCode(), error.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDto<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorCode error = ErrorCode.INVALID_PARAMETER_TYPE; // 400
        return ResponseEntity.status(error.getStatus())
                .body(ApiResponseDto.fail(error.getCode(), error.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<?>> handleValidation(MethodArgumentNotValidException ex) {
        ErrorCode error = ErrorCode.INVALID_INPUT; // 400
        return ResponseEntity.status(error.getStatus())
                .body(ApiResponseDto.fail(error.getCode(), error.getMessage()));
    }
}
