package com.lapoit.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 유저 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-404", "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER-400", "비밀번호가 올바르지 않습니다."),

    // 인증/인가
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH-403", "접근 권한이 없습니다."),

    // 서버/기타
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-500", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
