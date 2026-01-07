package com.lapoit.api.controller.docs;

import com.lapoit.api.dto.sse.SsePublishRequest;
import com.lapoit.api.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE", description = "Server-Sent Events APIs")
public interface SseControllerDocs {

    @Operation(
            summary = "Subscribe SSE",
            description = "Open an SSE stream for a playGameId group.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SSE stream opened"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    SseEmitter subscribe(@Parameter(hidden = true) CustomUserDetails principal,
                         @Parameter(description = "Target playGameId to join") String playGameId);

    @Operation(
            summary = "Publish SSE message",
            description = "Send an SSE message to a playGameId group (admin only).",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message sent"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    ResponseEntity<?> publish(SsePublishRequest request);
}
