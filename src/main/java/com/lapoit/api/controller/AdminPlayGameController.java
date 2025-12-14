package com.lapoit.api.controller;

import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.playgame.AdminPlayGameCreateRequest;
import com.lapoit.api.dto.playgame.AdminPlayGameResponse;
import com.lapoit.api.dto.playgame.PlayGameResponse;
import com.lapoit.api.service.AdminPlayGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/play-game")
@RequiredArgsConstructor
public class AdminPlayGameController {

    private final AdminPlayGameService adminPlayGameService;


    /** 실제 게임 생성 */
    @PostMapping
    public ResponseEntity<ApiResponseDto<?>> createPlayGame(
            @RequestBody AdminPlayGameCreateRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "GAME-201",
                        "실제 게임 생성 완료",
                        adminPlayGameService.createPlayGame(request)
                )
        );
    }

    /** 게임 시작 */
    @PatchMapping("/{playGameId}/start")
    public ResponseEntity<ApiResponseDto<?>> startPlayGame(
            @PathVariable("playGameId") Long playGameId
    ) {

        return ResponseEntity.ok(
                ApiResponseDto.success("GAME-204", "게임 시작", adminPlayGameService.startPlayGame(playGameId))
        );
    }

    /** 게임 중지 */
    @PatchMapping("/{playGameId}/pause")
    public ResponseEntity<ApiResponseDto<?>> pausePlayGame(
            @PathVariable("playGameId") Long playGameId
    ) {
        return ResponseEntity.ok(
                ApiResponseDto.success("GAME-204", "게임 중지", adminPlayGameService.pausePlayGame(playGameId))
        );
    }

    /** 게임 재개 */
    @PatchMapping("/{playGameId}/resume")
    public ResponseEntity<ApiResponseDto<?>> resumePlayGame(
            @PathVariable("playGameId") Long playGameId
    ) {
        return ResponseEntity.ok(
                ApiResponseDto.success("GAME-204", "게임 재개", adminPlayGameService.resumePlayGame(playGameId))
        );
    }



}

