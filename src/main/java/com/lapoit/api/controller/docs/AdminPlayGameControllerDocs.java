package com.lapoit.api.controller.docs;

import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.playgame.AdminJoinGameRequest;
import com.lapoit.api.dto.playgame.AdminPlayGameCreateRequest;
import com.lapoit.api.dto.playgame.RebuyCancelRequest;
import com.lapoit.api.dto.playgame.UserGamePaymentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin PlayGame", description = "Admin play game control APIs")
public interface AdminPlayGameControllerDocs {

    @Operation(
            summary = "Create play game",
            description = "Create a new live game based on a template.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    ResponseEntity<ApiResponseDto<?>> createPlayGame(AdminPlayGameCreateRequest request);

    @Operation(
            summary = "Start play game",
            description = "Start a live game.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    ResponseEntity<ApiResponseDto<?>> startPlayGame(
            @Parameter(description = "Play game ID") Long playGameId
    );

    @Operation(
            summary = "Pause play game",
            description = "Pause a live game.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    ResponseEntity<ApiResponseDto<?>> pausePlayGame(
            @Parameter(description = "Play game ID") Long playGameId
    );

    @Operation(
            summary = "Resume play game",
            description = "Resume a paused live game.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    ResponseEntity<ApiResponseDto<?>> resumePlayGame(
            @Parameter(description = "Play game ID") Long playGameId
    );

    @Operation(
            summary = "Finish play game",
            description = "Finish a live game.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    ResponseEntity<ApiResponseDto<?>> finishPlayGame(
            @Parameter(description = "Play game ID") Long playGameId
    );

    // =========================
    // ✅ 플레이어 조작 (userGameId 기준)
    // =========================

    @Operation(
            summary = "Mark player out",
            description = """
                Mark a player as OUT in a play game.
                This API uses userGameId, not userId.
                It supports both registered users and guest players.
                """,
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    ResponseEntity<ApiResponseDto<?>> outPlayer(
            @Parameter(description = "Play game ID") Long playGameId,
            @Parameter(description = "UserGame ID (player identifier)") Long userGameId
    );

    @Operation(
            summary = "Rebuy player",
            description = """
                Apply a rebuy for a player.
                This API uses userGameId, not userId.
                It supports both registered users and guest players.
                """,
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    ResponseEntity<?> rebuy(
            @Parameter(description = "Play game ID") Long playGameId,
            @Parameter(description = "UserGame ID (player identifier)") Long userGameId
    );

    @Operation(
            summary = "Cancel rebuy",
            description = """
                Cancel the latest rebuy for a player.
                This API uses userGameId, not userId.
                """,
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    ResponseEntity<ApiResponseDto<?>> cancelRebuy(
            @Parameter(description = "Play game ID") Long playGameId,
            @Parameter(description = "UserGame ID (player identifier)") Long userGameId
    );

    // =========================
    // 참가
    // =========================

    @Operation(
            summary = "Join registered user",
            description = "Add a registered user to a play game.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    ResponseEntity<ApiResponseDto<?>> joinUser(
            @Parameter(description = "Play game ID") Long playGameId,
            AdminJoinGameRequest request
    );

    // =========================
// 결제 상태 변경
// =========================

    @Operation(
            summary = "Update player payment status",
            description = """
            Update payment status for a player in a play game.
            This API uses userGameId, not playGameId.
            """,
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    ResponseEntity<ApiResponseDto<?>> updatePayment(
            @Parameter(description = "UserGame ID (player identifier)", required = true)
            Long userGameId,
            UserGamePaymentRequest request
    );


}
