package com.lapoit.api.controller;

import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.history.DailyDauDto;
import com.lapoit.api.dto.history.DailyGameAndDauDto;
import com.lapoit.api.dto.history.DailyGameDto;
import com.lapoit.api.service.UserHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class UserHistoryController {

    private final UserHistoryService userHistoryService;

    //오늘 방문자 조회
    @GetMapping("/today")
    public ResponseEntity<?> todayDau(){
        Long today=userHistoryService.getTodayDau();


        return ResponseEntity.ok(
                ApiResponseDto.success("USER_HISTORY-200", "오늘방문자 조회 성공", today)
        );
    }



    //시작 날짜 끝나는 날짜 기준 하루에 몇몇 방문 했는지 조회
    @GetMapping("/range/user")
    public ResponseEntity<?> rangeUser(                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                        LocalDate startDate,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                        LocalDate endDate){
        List<DailyGameAndDauDto> day=userHistoryService.rangeUser(startDate,endDate);

        return ResponseEntity.ok(
                ApiResponseDto.success("USER_HISTORY-200", "특정기간 방문 유저 조회 성공", day)
        );
    }

    //오늘 게임참가자 조회 (이건 각 지점별 게임 참가자수와 함께 조회 하도록 하면 최종 총 몇몇인지도 함께 출력하도록 한다)
    @GetMapping("/today/game")
    public ResponseEntity<?> todayGame() {
        DailyGameDto today = userHistoryService.getTodayGame();

        return ResponseEntity.ok(
                ApiResponseDto.success("USER_HISTORY-200", "오늘 게임 참가자 조회 성공", today)
        );

    }


}
