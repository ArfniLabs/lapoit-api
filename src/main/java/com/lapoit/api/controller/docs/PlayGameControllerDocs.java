package com.lapoit.api.controller.docs;

import com.lapoit.api.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "PlayGame", description = "Play game query APIs")
public interface PlayGameControllerDocs {

    @Operation(
            summary = "List games by store",
            description = "Return active games for a store.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Games returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    ResponseEntity<ApiResponseDto<?>> getGamesByStore(@Parameter(description = "Store ID") Long storeId);

    @Operation(
            summary = "Get game detail",
            description = "Return play game detail by playGameId.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game detail returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Play game not found")
    })
    ResponseEntity<ApiResponseDto<?>> getGameDetail(@Parameter(description = "Play game ID") Long playGameId);
}
