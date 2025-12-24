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
    PHONE_NUMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-4093", "이미 사용 중인 전화번호입니다."),
    USER_INFO_MISMATCH_NAME(HttpStatus.BAD_REQUEST, "USER-407", "이름 정보가 일치하지 않습니다."),
    USER_INFO_MISMATCH_NUMBER(HttpStatus.BAD_REQUEST, "USER-408", "전화번호 정보가 일치하지 않습니다."),
    ACCESS_DENIED(HttpStatus.BAD_REQUEST, "USER-409", "관리자 계정 생성에 접근할수없습니다."),
    ADMIN_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER-410", "해당 관리자가 존재하지 않습니다."),
    ADMIN_ALREADY_ACTIVE(HttpStatus.BAD_REQUEST, "USER-411", "이미 활성화된 관리자입니다."),
    INVALID_POINT_AMOUNT(HttpStatus.BAD_REQUEST, "USER-412", "포인트 생성 금액이 음수입니다."),

    // 인증/인가
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH-403", "접근 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN,"AUTH-407","유효하지 않은 토큰입니다." ),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"AUTH-408" ,"유효하지 않은 리프레시 토큰입니다." ),
    INVALID_STACK(HttpStatus.BAD_REQUEST, "GAME-400", "유효하지않은 리바인 스택입니다."),
    REBUYIN_COUNT_ZERO(HttpStatus.BAD_REQUEST, "GAME-400", "리바인 스택이 0이하 입니다."),
    REBUYIN_COUNT_FULL(HttpStatus.BAD_REQUEST, "GAME-400", "리바인 스택을 전부사용했습니다."),

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
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-500", "서버 오류가 발생했습니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "REQ-400", "요청 바디(JSON)가 없거나 형식이 올바르지 않습니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "REQ-400-1", "필수 파라미터가 누락되었습니다."),
    INVALID_PARAMETER_TYPE(HttpStatus.BAD_REQUEST, "REQ-400-2", "파라미터 타입이 올바르지 않습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "REQ-400-4", "입력값이 올바르지 않습니다."),

    // 점수 관련
    SCORE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCORE-404", "해당 스코어 테이블이 존재하지 않습니다."),
    SCORE_ALREADY_EXISTS(HttpStatus.CONFLICT, "SCORE-409", "해당 스코어 테이블이 이미 존재합니다."),
    POINT_NOT_ENOUGH(HttpStatus.BAD_REQUEST,"POINT-400","포인트가 부족합니다." ),
    SCORE_NOT_ENOUGH(HttpStatus.BAD_REQUEST,"SCORE-400","승점이 부족합니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
