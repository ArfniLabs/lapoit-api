package com.lapoit.api.controller;

import com.lapoit.api.controller.docs.UserControllerDocs;
import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.user.UserResponseDto;
import com.lapoit.api.jwt.CustomUserDetails;
import com.lapoit.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    //유저 정보 수정
    //비밀번호 변경
    //회원 탈퇴



}
