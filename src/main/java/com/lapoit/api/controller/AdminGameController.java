package com.lapoit.api.controller;

import com.lapoit.api.controller.docs.AdminGameControllerDocs;
import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.game.*;
import com.lapoit.api.dto.playgame.PlayGamePrizeCreateRequest;
import com.lapoit.api.dto.playgame.PlayGamePrizeDto;
import com.lapoit.api.service.AdminGameService;
import com.lapoit.api.service.PlayGamePrizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/game")
@RequiredArgsConstructor
public class AdminGameController implements AdminGameControllerDocs {

    private final AdminGameService adminGameService;
    private final PlayGamePrizeService prizeService;

    // 게임 생성
    @PostMapping
    public ResponseEntity<?> createGame(
            @RequestBody AdminGameCreateRequestDto request
    ) {
        Long gameId = adminGameService.createGame(request);
        return ResponseEntity.ok(
                ApiResponseDto.success("GAME-201", "게임 템플릿 생성 완료", request)
        );
    }


    // 게임 삭제
    @DeleteMapping("/{gameId}")
    public ResponseEntity<?> deleteGame(@PathVariable("gameId") Long gameId) {
        adminGameService.deleteGame(gameId);
        return ResponseEntity.ok(
                ApiResponseDto.success("GAME-200", "게임 템플릿 삭제 완료", null)
        );
    }

    // 게임 정보 수정
    @PatchMapping("/{gameId}")
    public ResponseEntity<ApiResponseDto<?>> patchGame(
            @PathVariable("gameId") Long gameId,
            @RequestBody GamePatchRequest request
    ) {
        adminGameService.patchGame(gameId, request);

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "GAME-204",
                        "게임 정보 수정 완료",
                        request
                )
        );
    }

    // 게임 블라인드 수정
    @PatchMapping("/{gameId}/blinds/{level}")
    public ResponseEntity<ApiResponseDto<?>> patchGameBlind(
            @PathVariable("gameId") Long gameId,
            @PathVariable("level") Integer level,
            @RequestBody GameBlindDto request
    ) {
        // path level 우선 적용
        request.setLevel(level);

        adminGameService.patchGameBlind(gameId, request);

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "GAME-204",
                        "게임 블라인드 수정 완료",
                        request
                )
        );
    }

    // 게임 리앤트리 수정
    @PatchMapping("/{gameId}/re-entries/{count}")
    public ResponseEntity<ApiResponseDto<?>> patchGameReEntry(
            @PathVariable("gameId") Long gameId,
            @PathVariable("count") Integer count,
            @RequestBody GameReEntryPatchRequest request
    ) {
        adminGameService.patchGameReEntry(gameId, count, request);

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "GAME-204",
                        "게임 리엔트리 수정 완료",
                        request
                )
        );
    }



    // 게임 목록 조회
    @GetMapping
    public ResponseEntity<?> getGameList() {
        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "GAME-200",
                        "게임 템플릿 목록 조회 성공",
                        adminGameService.getGameList()
                )
        );
    }

    // 게임 상금 입력
    @PostMapping("/{playGameId}/prizes")
    public ResponseEntity<ApiResponseDto<?>> registerPrizes(
            @PathVariable("playGameId") Long playGameId,
            @RequestBody PlayGamePrizeCreateRequest request
    ) {
        List<PlayGamePrizeDto> result =
                prizeService.registerPrizes(playGameId, request.getPrizes());

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "GAME-201",
                        "게임 상금 입력 완료",
                        result
                )
        );
    }


}

