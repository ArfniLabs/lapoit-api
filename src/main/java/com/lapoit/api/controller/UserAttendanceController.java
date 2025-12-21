package com.lapoit.api.controller;

import com.lapoit.api.controller.docs.UserAttendanceControllerDocs;
import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserAttendanceController implements UserAttendanceControllerDocs {

    private final AttendanceService attendanceService;

    @GetMapping("/{userId}/attendance")
    public ResponseEntity<ApiResponseDto<?>> getUserAttendance(
            @PathVariable("userId") Long userId,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "ATTEND-200",
                        "사용자의 월간 출석 정보를 조회했습니다.",
                        attendanceService.getUserAttendance(userId, year, month)
                )
        );
    }

}

