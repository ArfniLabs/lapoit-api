package com.lapoit.api.controller;

import com.lapoit.api.controller.docs.SseControllerDocs;
import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.sse.SsePublishRequest;
import com.lapoit.api.jwt.CustomUserDetails;
import com.lapoit.api.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sse")
@RequiredArgsConstructor
public class SseController implements SseControllerDocs {

    private final SseService sseService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails principal,
                                @RequestParam("playGameId") String playGameId) {
        String userId = principal.getUsername();
        return sseService.subscribe(playGameId, userId);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @PostMapping("/publish")
    public ResponseEntity<?> publish(@RequestBody SsePublishRequest request) {
        if (request.getPlayGameId() == null || request.getPlayGameId().isBlank()) {
            return ResponseEntity.badRequest().body(
                    ApiResponseDto.fail("SSE-400", "playGameId is required")
            );
        }

        String event = request.getEvent();
        if (event == null || event.isBlank()) {
            event = "message";
        }

        int delivered = sseService.sendToPlayGame(request.getPlayGameId(), event, request.getData());

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "SSE-200",
                        "SSE message sent",
                        Map.of("delivered", delivered)
                )
        );
    }
}
