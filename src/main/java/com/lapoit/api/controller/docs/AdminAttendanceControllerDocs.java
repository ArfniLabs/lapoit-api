package com.lapoit.api.controller.docs;

import com.lapoit.api.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin Attendance", description = "Admin attendance ranking APIs")
public interface AdminAttendanceControllerDocs {

    @Operation(
            summary = "Get store attendance ranking",
            description = "Return attendance ranking for a store and month.",
            security = { @SecurityRequirement(name = "bearer-jwt") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranking returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    ResponseEntity<ApiResponseDto<?>> getStoreAttendanceRanking(
            @Parameter(description = "Store ID") long storeId,
            @Parameter(description = "Year (yyyy)") int year,
            @Parameter(description = "Month (1-12)") int month
    );
}
