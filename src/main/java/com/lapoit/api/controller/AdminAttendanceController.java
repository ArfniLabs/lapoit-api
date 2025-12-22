package com.lapoit.api.controller;

import com.lapoit.api.controller.docs.AdminAttendanceControllerDocs;
import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminAttendanceController implements AdminAttendanceControllerDocs {

    private final AttendanceService attendanceService;

    @GetMapping("/store/{storeId}/attendance/ranking")
    public ResponseEntity<ApiResponseDto<?>> getStoreAttendanceRanking(
            @PathVariable("storeId") long storeId,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "ATTEND-201",
                        "지점별 출석 랭킹을 조회했습니다.",
                        attendanceService.getStoreAttendanceRanking(
                                storeId, year, month
                        )
                )
        );
    }
}

