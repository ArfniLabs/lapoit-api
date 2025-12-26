package com.lapoit.api.controller;


import com.lapoit.api.controller.docs.AuthControllerDocs;
import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.auth.*;
import com.lapoit.api.jwt.CustomUserDetails;
import com.lapoit.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 로그인/ 회원가입/ 토큰발급/ 로그아웃

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto request) {
        authService.signup(request);
        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "회원가입 성공", null)
        );

    }

    /** 슈퍼관리자 -> 관리자 계정 생성 */
    @PostMapping("/admin/signup")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> createAdmin(
            @RequestBody AdminCreateRequestDto request,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        authService.createAdmin(request, principal);
        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-201", "관리자 계정 생성 완료", request)
        );
    }

    /** 슈퍼관리자 -> 관리자 계정 조회 */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> getAdmins(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "ADMIN-200",
                        "관리자 목록 조회 성공",
                        authService.getAdmins(principal)
                )
        );
    }

    /** 슈퍼관리자 -> 관리자 계정 비활성화 */
    @PatchMapping("/admin/{adminId}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> deleteAdmin(
            @PathVariable("adminId") Long adminId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        authService.deleteAdmin(adminId, principal);
        return ResponseEntity.ok(
                ApiResponseDto.success("ADMIN-204", "관리자 비활성화 완료", null)
        );
    }

    /** 슈퍼관리자 -> 관리자 계정 재활성화 */
    @PatchMapping("/admin/{adminId}/activate")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> activateAdmin(
            @PathVariable("adminId") Long adminId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        authService.activateAdmin(adminId, principal);
        return ResponseEntity.ok(
                ApiResponseDto.success("ADMIN-203", "관리자 재활성화 완료", null)
        );
    }


    //아이디중복 (temp 와 user 에서 모두 중복처리된거거처야함
    @GetMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestParam String userId) {
        authService.checkId(userId);
        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "아이디중복 확인 성공", null)
        );
    }
    //닉네임중복
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickName(@RequestParam String userNickname) {
        authService.checkNickName(userNickname);
        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "닉네임중복 확인 성공", null)
        );
    }

    //전화번호 중복
    @GetMapping("/check-phone")
    public ResponseEntity<?> checkPhoneNumber(@RequestParam String phoneNumber) {
        authService.checkPhoneNumber(phoneNumber);
        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "전화번호 중복 확인 성공", null)
        );
    }
    //지금은 인증 없음. 나중에 인증 코드 관리 혹은 DB에서 관리해야함




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
    //이쪽은 인증 권한요
    //로그아웃 ( lastLogoutAt 방식이라면  캐시로 저장한 토큰 관리하고?
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal CustomUserDetails principal) {
        String userId=principal.getUsername();
        authService.logout(userId);


        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "로그아웃 성공", null)
        );
    }

    //아이디찾기
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody FindIdRequestDto findIdRequestDto){
        FindIdResponseDto id= authService.findId(findIdRequestDto);

        return ResponseEntity.ok(
                ApiResponseDto.success("Auth-200", "아이디찾기 성공", id)
        );

    }


}
