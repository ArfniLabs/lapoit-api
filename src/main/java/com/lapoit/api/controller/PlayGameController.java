package com.lapoit.api.controller;

import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.service.PlayGameQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/play-game")
@RequiredArgsConstructor
public class PlayGameController {

    private final PlayGameQueryService playGameQueryService;

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponseDto<?>> getGamesByStore(
            @PathVariable("storeId") Long storeId
    ) {
        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "GAME-200",
                        "지점별 오늘 게임 조회 성공",
                        playGameQueryService.getGamesByStore(storeId)
                )
        );
    }
}