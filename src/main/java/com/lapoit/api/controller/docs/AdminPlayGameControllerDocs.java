package com.lapoit.api.controller.docs;

import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.playgame.AdminJoinGameRequest;
import com.lapoit.api.dto.playgame.AdminPlayGameCreateRequest;
import com.lapoit.api.dto.playgame.RebuyCancelRequest;
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
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Play game created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<ApiResponseDto<?>> createPlayGame(AdminPlayGameCreateRequest request);

    @Operation(
            summary = "Start play game",
            description = "Start a live game.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Play game started"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Play game not found")
    })
    ResponseEntity<ApiResponseDto<?>> startPlayGame(@Parameter(description = "Play game ID") Long playGameId);

    @Operation(
            summary = "Pause play game",
            description = "Pause a live game.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Play game paused"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Play game not found")
    })
    ResponseEntity<ApiResponseDto<?>> pausePlayGame(@Parameter(description = "Play game ID") Long playGameId);

    @Operation(
            summary = "Resume play game",
            description = "Resume a paused live game.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Play game resumed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Play game not found")
    })
    ResponseEntity<ApiResponseDto<?>> resumePlayGame(@Parameter(description = "Play game ID") Long playGameId);

    @Operation(
            summary = "Finish play game",
            description = "Finish a live game.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "205", description = "Play game finished"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Play game not found")
    })
    ResponseEntity<ApiResponseDto<?>> finishPlayGame(@Parameter(description = "Play game ID") Long playGameId);

    @Operation(
            summary = "Mark player out",
            description = "Set a player as out for a play game.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "206", description = "Player marked out"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Play game or user not found")
    })
    ResponseEntity<ApiResponseDto<?>> outPlayer(
            @Parameter(description = "Play game ID") Long playGameId,
            @Parameter(description = "User ID") Long userId
    );

    @Operation(
            summary = "Rebuy",
            description = "Apply a rebuy for a player.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "210", description = "Rebuy applied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Play game or user not found")
    })
    ResponseEntity<?> rebuy(
            @Parameter(description = "Play game ID") Long playGameId,
            @Parameter(description = "User ID") Long userId
    );

    @Operation(
            summary = "Cancel rebuy",
            description = "Cancel a rebuy for a player.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Rebuy canceled"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Play game or user not found")
    })
    ResponseEntity<ApiResponseDto<?>> cancelRebuy(
            @Parameter(description = "Play game ID") Long playGameId,
            RebuyCancelRequest request
    );

    @Operation(
            summary = "Join player",
            description = "Add a player to a play game.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Player joined"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Play game or user not found")
    })
    ResponseEntity<ApiResponseDto<?>> joinUser(
            @Parameter(description = "Play game ID") Long playGameId,
            AdminJoinGameRequest request
    );
}
