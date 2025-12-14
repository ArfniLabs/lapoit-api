package com.lapoit.api.controller;


import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.admin.TempUserResponseDto;
import com.lapoit.api.dto.auth.LoginRequestDto;
import com.lapoit.api.dto.auth.RefreshTokenRequestDto;
import com.lapoit.api.dto.auth.SignupRequestDto;
import com.lapoit.api.dto.auth.TokenResponseDto;
import com.lapoit.api.jwt.CustomUserDetails;
import com.lapoit.api.service.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 로그인 / 회원가입/ 재발급 / 로그아웃

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto request) {
        authService.signup(request);
        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "회원가입 성공", null)
        );

    }

    //아이디 중복 (temp 와 user 둘다 모두다 중복확인 거처야한다)
    @GetMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestParam String userId) {
        authService.checkId(userId);
        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "아이디 중복 확인 성공", null)
        );
    }
    //닉네임 중복
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickName(@RequestParam String userNickname) {
        authService.checkNickName(userNickname);
        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "닉네임 중복 확인 성공", null)
        );
    }
    //지점 불러오기




    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        TokenResponseDto tokenResponse = authService.login(request);

        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "로그인 성공", tokenResponse)
        );
    }

    //Refresh API
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequestDto request) {
        TokenResponseDto tokenResponse = authService.refresh(request);

        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "로그인 성공", tokenResponse)
        );
    }
    //이쪽은 인증 권한필요
    //로그아웃 ( lastLogoutAt 방식이면  으로 액세스 토큰 관리하자)
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal CustomUserDetails principal) {
        String userId=principal.getUsername();
        authService.logout(userId);


        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "로그아웃 성공", null)
        );
    }
}

