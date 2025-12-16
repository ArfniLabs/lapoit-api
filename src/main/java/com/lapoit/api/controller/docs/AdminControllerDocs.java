package com.lapoit.api.controller.docs;

import com.lapoit.api.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

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
}
