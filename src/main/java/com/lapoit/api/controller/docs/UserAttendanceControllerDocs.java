package com.lapoit.api.controller.docs;

import com.lapoit.api.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Attendance", description = "User attendance APIs")
public interface UserAttendanceControllerDocs {

    @Operation(
            summary = "Get user attendance",
            description = "Return a user's attendance for the given month.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attendance returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<ApiResponseDto<?>> getUserAttendance(
            @Parameter(description = "User ID") Long userId,
            @Parameter(description = "Year (yyyy)") int year,
            @Parameter(description = "Month (1-12)") int month
    );
}
