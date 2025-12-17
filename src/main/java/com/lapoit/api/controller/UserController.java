package com.lapoit.api.controller;

import com.lapoit.api.controller.docs.UserControllerDocs;
import com.lapoit.api.domain.UserScore;
import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.user.CreateStoreRequestDto;
import com.lapoit.api.dto.user.UserResponseDto;
import com.lapoit.api.jwt.CustomUserDetails;
import com.lapoit.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    //유저 정보 수정
    //비밀번호 변경
    //회원 탈퇴



}
