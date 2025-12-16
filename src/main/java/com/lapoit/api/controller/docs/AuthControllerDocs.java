package com.lapoit.api.controller.docs;

import com.lapoit.api.dto.auth.FindIdRequestDto;
import com.lapoit.api.dto.auth.LoginRequestDto;
import com.lapoit.api.dto.auth.RefreshTokenRequestDto;
import com.lapoit.api.dto.auth.SignupRequestDto;
import com.lapoit.api.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "Authentication and token APIs")
public interface AuthControllerDocs {

    @Operation(
            summary = "Sign up",
            description = "Submit a new user sign-up. The account stays pending until an admin approves it."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sign-up request accepted")
    })
    ResponseEntity<?> signup(SignupRequestDto request);

    @Operation(
            summary = "Check duplicate ID",
            description = "Validate that the given userId is not already registered."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User ID is available"),
            @ApiResponse(responseCode = "409", description = "User ID already exists")
    })
    ResponseEntity<?> checkId(String userId);

    @Operation(
            summary = "Check duplicate nickname",
            description = "Validate that the given nickname is not already registered."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nickname is available"),
            @ApiResponse(responseCode = "409", description = "Nickname already exists")
    })
    ResponseEntity<?> checkNickName(String userNickname);

    @Operation(
            summary = "Log in",
            description = "Authenticate with credentials and issue access/refresh tokens."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens issued"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    ResponseEntity<?> login(LoginRequestDto request);

    @Operation(
            summary = "Refresh token",
            description = "Issue a new access token (and refresh token) using a valid refresh token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens refreshed"),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    ResponseEntity<?> refresh(RefreshTokenRequestDto request);

    @Operation(
            summary = "Log out",
            description = "Invalidate the current user's refresh token and record logout time.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> logout(@Parameter(hidden = true) CustomUserDetails principal);

    @Operation(
            summary = "Find user ID",
            description = "Find a user's ID using their name and phone number."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User ID returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<?> findId(FindIdRequestDto findIdRequestDto);
}
