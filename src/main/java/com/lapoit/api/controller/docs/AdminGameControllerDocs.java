package com.lapoit.api.controller.docs;

import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.game.AdminGameCreateRequestDto;
import com.lapoit.api.dto.game.GameBlindDto;
import com.lapoit.api.dto.game.GamePatchRequest;
import com.lapoit.api.dto.game.GameReEntryPatchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin Game", description = "Admin game configuration APIs")
public interface AdminGameControllerDocs {

    @Operation(
            summary = "Create game template",
            description = "Create a game configuration template.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Game created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> createGame(AdminGameCreateRequestDto request);

    @Operation(
            summary = "Delete game template",
            description = "Delete a game configuration by ID.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    ResponseEntity<?> deleteGame(@Parameter(description = "Game ID") Long gameId);

    @Operation(
            summary = "Patch game template",
            description = "Update game template fields.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Game updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    ResponseEntity<ApiResponseDto<?>> patchGame(
            @Parameter(description = "Game ID") Long gameId,
            GamePatchRequest request
    );

    @Operation(
            summary = "Patch game blinds",
            description = "Update blind level data for a game template.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Blind updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Game or level not found")
    })
    ResponseEntity<ApiResponseDto<?>> patchGameBlind(
            @Parameter(description = "Game ID") Long gameId,
            @Parameter(description = "Blind level") Integer level,
            GameBlindDto request
    );

    @Operation(
            summary = "Patch game re-entry",
            description = "Update re-entry data for a game template.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Re-entry updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Game or re-entry not found")
    })
    ResponseEntity<ApiResponseDto<?>> patchGameReEntry(
            @Parameter(description = "Game ID") Long gameId,
            @Parameter(description = "Re-entry count") Integer count,
            GameReEntryPatchRequest request
    );

    @Operation(
            summary = "List game templates",
            description = "Return all game templates.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Games returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> getGameList();
}
