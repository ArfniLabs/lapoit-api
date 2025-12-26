package com.lapoit.api.controller;

import com.lapoit.api.controller.docs.UserControllerDocs;
import com.lapoit.api.domain.UserScore;
import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.rank.StoreRankingResponse;
import com.lapoit.api.dto.user.*;
import com.lapoit.api.jwt.CustomUserDetails;
import com.lapoit.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserService userService;

    //유저 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal CustomUserDetails principal) {
        String userId=principal.getUsername();
        UserResponseDto myInfo = userService.getMyInfo(userId);

        return ResponseEntity.ok(
                ApiResponseDto.success("USER-200", "개인정보 조회 성공", myInfo)
        );
    }

    //지역 별 점수 테이블 생성

    @PostMapping("/score")
    public ResponseEntity<?> createScoreTable(@AuthenticationPrincipal CustomUserDetails principal, @RequestBody CreateStoreRequestDto dto) {

        String user=principal.getUsername();
        userService.createScoreTable(user, dto);

        return ResponseEntity.ok(
                ApiResponseDto.success("USER-200", "지역 점수 테이블 생성 성공",null)
        );

    }

    //내 점수 전체 목록 조회
    @GetMapping("/scores/stores")
    public ResponseEntity<?> getMyScoreList(@AuthenticationPrincipal CustomUserDetails principal) {
        String user=principal.getUsername();
        List<UserScore> scores=userService.getMyScoreList(user);

        return ResponseEntity.ok(
                ApiResponseDto.success("USER-200", "내 스코어 전체 목록 조회 성공",scores)
        );
    }

    //비번 체크
    @PostMapping("/password/check")
    public ResponseEntity<?> checkPassword(@AuthenticationPrincipal CustomUserDetails principal, @RequestBody PasswordCheckRequestDto dto) {
        String userId = principal.getUsername();
        boolean matches = userService.checkPassword(userId, dto.getPassword());

        return ResponseEntity.ok(
                ApiResponseDto.success("USER-200", "비밀번호 확인 결과", matches)
        );
    }

    //비번 변경
    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal CustomUserDetails principal, @RequestBody UpdatePasswordRequestDto dto) {
        String userId = principal.getUsername();
        userService.changePassword(userId, dto.getCurrentPassword(), dto.getNewPassword());

        return ResponseEntity.ok(
                ApiResponseDto.success("USER-200", "비밀번호 변경 완료", null)
        );
    }

    //개인정보 수정 (닉네임,휴대폰 번호, 상점아이디)
    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUserDetails principal, @RequestBody UpdateProfileRequestDto dto) {
        String userId = principal.getUsername();
        userService.updateProfile(userId, dto);

        return ResponseEntity.ok(
                ApiResponseDto.success("USER-200", "개인정보 수정 완료", null)
        );
    }

    //포인트,승점 히스토리 조회
    //포인트인 경우 상점아이디를 0으로 고정한다.
    //그외의 각 상점에 따른 승점은 상점 아이디 기반으로 조회한다.
    @GetMapping("/history")
    public ResponseEntity<?> getUserHistory(@AuthenticationPrincipal CustomUserDetails principal,
                                            @RequestParam(required = false) Long storeId,
                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                LocalDate startDate,
                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                LocalDate endDate,

                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        String userId = principal.getUsername();
        List<HistoryResponse> list=userService.getUserHistory(userId,storeId,startDate, endDate, page, size);


        return ResponseEntity.ok(
                ApiResponseDto.success("USER-200", "포인트 조회 완료", list)
        );

    }


    //유저 회원가입 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe(@AuthenticationPrincipal CustomUserDetails principal) {
        String userId = principal.getUsername();
        userService.deleteUser(userId);

        return ResponseEntity.ok(
                ApiResponseDto.success("USER-200", "탈퇴 성공", null)
        );
    }



    // 지점별 월간 랭킹 조회
    @GetMapping("/store/{storeId}/ranking")
    public ResponseEntity<?> getStoreRanking(
            @PathVariable("storeId") Long storeId
    ) {
        StoreRankingResponse response =
                userService.getStoreRanking(storeId);

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "RANK-200",
                        "승점 랭킹을 조회했습니다.",
                        response
                )
        );
    }

}
