package com.lapoit.api.controller;

import com.lapoit.api.domain.TempUser;
import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.admin.TempUserResponseDto;
import com.lapoit.api.dto.admin.UserListResponseDto;
import com.lapoit.api.jwt.CustomUserDetails;
import com.lapoit.api.jwt.JwtTokenProvider;
import com.lapoit.api.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;


    //승인대기 목록 조회
    @GetMapping("/users/pending")
    public ResponseEntity<?> getPendingUsers(@AuthenticationPrincipal CustomUserDetails principal) {
        String userId=principal.getUsername();
        List<TempUserResponseDto> pendingUsers = adminService.getPendingUsers(userId);


        return ResponseEntity.ok(
                ApiResponseDto.success("Admin-200", "회원가입 대기 목록 조회 성공", pendingUsers)
        );
    }

    //회원 승인
    @PatchMapping("/users/{userId}/approve")
    public ResponseEntity<?> approveUser(@PathVariable String userId) {

        adminService.approveUser(userId);
        return ResponseEntity.ok(
                ApiResponseDto.success("Admin-200", "회원 승인 성공", null)
        );

    }
    //회원 거절
    @DeleteMapping("/users/{userId}/reject")
    public ResponseEntity<?> rejectUser(@PathVariable String userId) {

        adminService.rejectUser(userId);
        return ResponseEntity.ok(
                ApiResponseDto.success("Admin-200", "회원 승인 거절 성공", null)
        );
    }

    //비번찾기 (아이디와 사람이름 휴대폰 번호를 받아서 비번 찾기 가능)

    //전체회원조회
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<UserListResponseDto> users = adminService.getUsers();


        return ResponseEntity.ok(
                ApiResponseDto.success("Admin-200", "전체 회원 조회 성공", users)
        );
    }

    //이름 기반 조회
    @GetMapping("/users/find/name/{userName}")
    public ResponseEntity<?> findUserByName(@PathVariable String userName) {
        List<UserListResponseDto> users = adminService.findUserByName(userName);


        return ResponseEntity.ok(
                ApiResponseDto.success("Admin-200", "이름 기반으로 회원 조회 성공", users)
        );
    }

    //지역 기반 조회
    @GetMapping("/users/find/store/{storeId}")
    public ResponseEntity<?> findUserByStoreId(@PathVariable String storeId) {
        List<UserListResponseDto> users = adminService.findUserByStoreId(storeId);


        return ResponseEntity.ok(
                ApiResponseDto.success("Admin-200", "지역 기반으로 회원 조회 성공", users)
        );
    }

    @GetMapping("/users/find/number/{phoneNumber}")
    public ResponseEntity<?> findUserByPhoneNumber(@PathVariable String phoneNumber) {
        List<UserListResponseDto> users = adminService.findUserByPhoneNumber(phoneNumber);


        return ResponseEntity.ok(
                ApiResponseDto.success("Admin-200", "휴대폰 번호로 회원 조회 성공", users)
        );
    }

    @GetMapping("/users/find/nickname/{userNickname}")
    public ResponseEntity<?> findUserByNickname(@PathVariable String userNickname) {
        List<UserListResponseDto> users = adminService.findUserByNickname(userNickname);


        return ResponseEntity.ok(
                ApiResponseDto.success("Admin-200", "닉네임기반 회원 조회 성공", users)
        );
    }



}
