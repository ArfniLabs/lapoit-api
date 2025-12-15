package com.lapoit.api.domain;

public enum PlayGameStatus {

    WAIT,       // 생성됨 (아직 시작 안 함)
    STARTED,    // 진행 중
    PAUSED,     // 일시정지
    FINISHED;   // 종료
}
