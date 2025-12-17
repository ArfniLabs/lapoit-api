package com.lapoit.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 유저 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-404", "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER-400", "비밀번호가 올바르지 않습니다."),
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "USER-4031", "비활성화된 계정입니다."),
    ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-4091", "이미 존재하는 아이디입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-4092", "이미 존재하는 닉네임 입니다."),
    USER_INFO_MISMATCH_NAME(HttpStatus.BAD_REQUEST,"USER-407" ,"이름을 잘못입력했습니다."),
    USER_INFO_MISMATCH_NUMBER(HttpStatus.BAD_REQUEST,"USER-408" ,"휴대폰 번호을 잘못입력했습니다."),
    // 인증/인가
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH-403", "접근 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN,"AUTH-407","유효하지 않은 토큰입니다." ),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"AUTH-408" ,"유효하지 않은 리프레시 토큰입니다." ),

    // 매장 관련
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE-404", "해당 지점이 존재하지 않습니다."),


    // 게임 관련
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "GAME-404", "해당 게임 템플릿이 존재하지 않습니다."),
    GAME_NOT_STARTED(HttpStatus.BAD_REQUEST, "GAME-400", "해당 게임이 아직 시작하지 않았습니다."),
    GAME_ALREADY_START(HttpStatus.BAD_REQUEST, "GAME-400", "해당 게임이 이미 진행중입니다."),
    GAME_ALREADY_PAUSE(HttpStatus.BAD_REQUEST, "GAME-400", "해당 게임이 이미 정지중입니다."),
    GAME_ALREADY_RESUME(HttpStatus.BAD_REQUEST, "GAME-400", "해당 게임이 이미 재시작중입니다."),
    GAME_ALREADY_FINISHED(HttpStatus.BAD_REQUEST, "GAME-400", "해당 게임이 이미 종료되었습니다."),
    GAME_ALREADY_JOINED(HttpStatus.BAD_REQUEST, "GAME-400", "해당 게임에 이미 참가한 유저입니다."),
    USER_NOT_IN_GAME(HttpStatus.BAD_REQUEST, "GAME-400", "해당 게임에 참가하지 않은 유저입니다."),
    USER_ALREADY_OUT(HttpStatus.BAD_REQUEST, "GAME-400", "이미 게임에서 아웃된 유저입니다."),


    // 서버/기타
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-500", "서버 내부 오류가 발생했습니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "REQ-400", "요청 바디(JSON)가 없거나 형식이 올바르지 않습니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "REQ-400-1", "필수 파라미터가 누락되었습니다."),

    INVALID_PARAMETER_TYPE(HttpStatus.BAD_REQUEST, "REQ-400-2", "파라미터 타입이 올바르지 않습니다."),

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "REQ-400-4", "입력값이 올바르지 않습니다."),

    //점수 관련
    SCORE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCORE-404", "해당 스코어 테이블이 존재하지 않습니다."),
    SCORE_ALREADY_EXISTS(HttpStatus.CONFLICT, "SCORE-409", "해당 스코어 테이블이 이미 존재합니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
