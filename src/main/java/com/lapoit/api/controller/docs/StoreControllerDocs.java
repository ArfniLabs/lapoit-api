package com.lapoit.api.controller.docs;

import com.lapoit.api.dto.store.StoreCreateRequestDto;
import com.lapoit.api.dto.store.StoreUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Store", description = "Store management APIs")
public interface StoreControllerDocs {

    @Operation(
            summary = "Get store",
            description = "Return a single store by ID.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Store returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    ResponseEntity<?> getStore(@Parameter(description = "Store ID") Long storeId);

    @Operation(
            summary = "List stores",
            description = "Return all stores.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stores returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> getStoreList();

    @Operation(
            summary = "Create store",
            description = "Create a new store.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Store created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> createStore(StoreCreateRequestDto request);

    @Operation(
            summary = "Update store",
            description = "Update store information by ID.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Store updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    ResponseEntity<?> updateStore(
            @Parameter(description = "Store ID") Long storeId,
            StoreUpdateRequestDto request
    );

    @Operation(
            summary = "Delete store",
            description = "Delete a store by ID.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Store deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    ResponseEntity<?> deleteStore(@Parameter(description = "Store ID") Long storeId);
}
