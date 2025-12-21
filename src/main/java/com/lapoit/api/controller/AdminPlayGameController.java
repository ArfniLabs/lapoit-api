package com.lapoit.api.controller;

import com.lapoit.api.controller.docs.AdminPlayGameControllerDocs;
import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.playgame.*;
import com.lapoit.api.service.AdminPlayGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/play-game")
@RequiredArgsConstructor
public class AdminPlayGameController implements AdminPlayGameControllerDocs {

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

    /** 게임 종료 */
    @PatchMapping("/{playGameId}/finish")
    public ResponseEntity<ApiResponseDto<?>> finishPlayGame(
            @PathVariable("playGameId") Long playGameId
    ) {
        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "GAME-205",
                        "게임 종료",
                        adminPlayGameService.finishPlayGame(playGameId)
                )
        );
    }

    /** 게임 탈락 처리 */
    @PatchMapping("/{playGameId}/out/{userId}")
    public ResponseEntity<ApiResponseDto<?>> outPlayer(
            @PathVariable("playGameId") Long playGameId,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "GAME-206",
                        "플레이어 탈락 처리",
                        adminPlayGameService.outPlayer(playGameId, userId)
                )
        );
    }


    /** 리바인 증가 */
    @PatchMapping("/{playGameId}/rebuy/{userId}")
    public ResponseEntity<?> rebuy(
            @PathVariable("playGameId") Long playGameId,
            @PathVariable("userId") Long userId
    ) {
        adminPlayGameService.rebuy(playGameId, userId);
        return ResponseEntity.ok(
                ApiResponseDto.success("GAME-210", "리바인 완료", null)
        );
    }


    /** 리바인 감소(관리자 실수 복구용) */
    @PatchMapping("/{playGameId}/rebuy/cancel")
    public ResponseEntity<ApiResponseDto<?>> cancelRebuy(
            @PathVariable("playGameId") Long playGameId,
            @RequestBody RebuyCancelRequest request
    ) {
        adminPlayGameService.cancelRebuy(playGameId, request.getUserId());
        return ResponseEntity.ok(
                ApiResponseDto.success("REBUY-204", "리바인 취소 완료", null)
        );
    }





    /** 유저 게임 참가 */
    @PostMapping("/{playGameId}/join")
    public ResponseEntity<ApiResponseDto<?>> joinUser(
            @PathVariable("playGameId") Long playGameId,
            @RequestBody AdminJoinGameRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "GAME-201",
                        "유저가 게임에 참가했습니다.",
                        adminPlayGameService.joinUser(
                                playGameId,
                                request.getUserId()
                        )
                )
        );
    }


}

