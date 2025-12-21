package com.lapoit.api.controller.docs;

import com.lapoit.api.dto.user.CreateStoreRequestDto;
import com.lapoit.api.dto.user.PasswordCheckRequestDto;
import com.lapoit.api.dto.user.UpdatePasswordRequestDto;
import com.lapoit.api.dto.user.UpdateProfileRequestDto;
import com.lapoit.api.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@Tag(name = "User", description = "User self-service APIs")
public interface UserControllerDocs {

    @Operation(
            summary = "Get my profile",
            description = "Return the authenticated user's profile, store, and point information.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> getMyInfo(@Parameter(hidden = true) CustomUserDetails principal);

    @Operation(
            summary = "Create score table",
            description = "Create a score record for a store for the authenticated user.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Score table created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> createScoreTable(
            @Parameter(hidden = true) CustomUserDetails principal,
            CreateStoreRequestDto dto
    );

    @Operation(
            summary = "List my scores",
            description = "Return score list for the authenticated user.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Scores returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> getMyScoreList(@Parameter(hidden = true) CustomUserDetails principal);

    @Operation(
            summary = "Check password",
            description = "Compare the provided password with the authenticated user's current password.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password match result returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> checkPassword(@Parameter(hidden = true) CustomUserDetails principal, PasswordCheckRequestDto dto);

    @Operation(
        summary = "Change password",
        description = "Update the authenticated user's password after verifying the current password.",
        security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated"),
            @ApiResponse(responseCode = "400", description = "Current password mismatch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> changePassword(@Parameter(hidden = true) CustomUserDetails principal, UpdatePasswordRequestDto dto);

    @Operation(
            summary = "Update profile",
            description = "Update nickname and store ID for the authenticated user.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated"),
            @ApiResponse(responseCode = "409", description = "Nickname or phone number already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> updateProfile(@Parameter(hidden = true) CustomUserDetails principal, UpdateProfileRequestDto dto);

    @Operation(
            summary = "Get my history",
            description = "Return score history for the authenticated user.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "History returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> getUserHistory(
            @Parameter(hidden = true) CustomUserDetails principal,
            @Parameter(description = "Filter by store ID") Long storeId,
            @Parameter(description = "Start date (yyyy-MM-dd)") LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") LocalDate endDate,
            @Parameter(description = "Page number (0-based)") int page,
            @Parameter(description = "Page size") int size
    );

    @Operation(
            summary = "Delete my account",
            description = "Delete the authenticated user and all related records.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> deleteMe(@Parameter(hidden = true) CustomUserDetails principal);
}
