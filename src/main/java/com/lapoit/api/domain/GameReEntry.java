package com.lapoit.api.domain;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameReEntry {
    private Long reEntryId;
    private Long gameId;

    private Integer reEntryCount; // 리엔트리 가능 횟수
    private Integer reEntryPrice; // 리엔트리 금액
    private Integer reEntryStack; // 리엔트리 스택
}

