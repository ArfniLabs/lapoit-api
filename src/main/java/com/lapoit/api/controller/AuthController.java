package com.lapoit.api.controller;


import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.auth.LoginRequestDto;
import com.lapoit.api.dto.auth.SignupRequestDto;
import com.lapoit.api.dto.auth.TokenResponseDto;
import com.lapoit.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        TokenResponseDto tokenResponse = authService.login(request);

        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "로그인 성공", tokenResponse)
        );
    }

}

