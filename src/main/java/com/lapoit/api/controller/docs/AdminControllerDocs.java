package com.lapoit.api.controller.docs;

import com.lapoit.api.dto.admin.ResetPasswordRequestDto;
import com.lapoit.api.dto.admin.UpdateUserPointRequest;
import com.lapoit.api.dto.admin.UpdateUserScoreRequest;
import com.lapoit.api.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@Tag(name = "Admin", description = "Admin-facing user management APIs")
public interface AdminControllerDocs {

    @Operation(
            summary = "List pending users",
            description = "Retrieve pending sign-up requests for the admin's store.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pending users returned")
    })
    ResponseEntity<?> getPendingUsers(@Parameter(hidden = true) CustomUserDetails principal);

    @Operation(
            summary = "Approve pending user",
            description = "Approve a pending user and move them to active status.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User approved"),
            @ApiResponse(responseCode = "404", description = "Pending user not found")
    })
    ResponseEntity<?> approveUser(@Parameter(description = "User ID to approve") String userId);

    @Operation(
            summary = "Reject pending user",
            description = "Reject a pending user sign-up request.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User rejected")
    })
    ResponseEntity<?> rejectUser(@Parameter(description = "User ID to reject") String userId);

    @Operation(
            summary = "List users",
            description = "List all users registered in the service.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users returned")
    })
    ResponseEntity<?> getUsers();

    @Operation(
            summary = "Search users by name",
            description = "Find users whose names match the query.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users returned")
    })
    ResponseEntity<?> findUserByName(@Parameter(description = "Exact user name") String userName);

    @Operation(
            summary = "Search users by store",
            description = "Find users linked to a specific store.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users returned")
    })
    ResponseEntity<?> findUserByStoreId(@Parameter(description = "Store identifier") String storeId);

    @Operation(
            summary = "Search users by phone number",
            description = "Find users by their phone number.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users returned")
    })
    ResponseEntity<?> findUserByPhoneNumber(@Parameter(description = "Phone number without formatting") String phoneNumber);

    @Operation(
            summary = "Search users by nickname",
            description = "Find users by their nickname.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users returned")
    })
    ResponseEntity<?> findUserByNickname(@Parameter(description = "Nickname to search") String userNickname);

    @Operation(
            summary = "Reset user password",
            description = "Reset a user's password using identifying details.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<?> resetPassword(ResetPasswordRequestDto dto);

    @Operation(
            summary = "Update user point",
            description = "Update a user's point balance and deduct the same amount from the admin's point.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User point updated"),
            @ApiResponse(responseCode = "400", description = "Admin point not enough"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<?> updateUserPoint(
            @Parameter(hidden = true) CustomUserDetails principal,
            @Parameter(description = "User ID") String userId,
            UpdateUserPointRequest request
    );

    @Operation(
            summary = "Update user score",
            description = "Update a user's score for a store.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User score updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<?> updateUserScore(
            @Parameter(hidden = true) CustomUserDetails principal,
            @Parameter(description = "User ID") String userId,
            UpdateUserScoreRequest request
    );

    @Operation(
            summary = "Get user history",
            description = "Return score history for a user, including actor user identifiers.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "History returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<?> getUserHistory(
            @Parameter(description = "User ID") String userId,
            @Parameter(description = "Filter by store ID") Long storeId,
            @Parameter(description = "Start date (yyyy-MM-dd)") LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") LocalDate endDate,
            @Parameter(description = "Page number (0-based)") int page,
            @Parameter(description = "Page size") int size
    );

    @Operation(
            summary = "Deactivate user",
            description = "Update a user's status to INACTIVE.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deactivated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<?> deactivateUser(@Parameter(description = "User ID") String userId);

    @Operation(
            summary = "Activate user",
            description = "Update a user's status to ACTIVE.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User activated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<?> activateUser(@Parameter(description = "User ID") String userId);
}
