package com.lapoit.api.service;


import com.lapoit.api.domain.User;
import com.lapoit.api.dto.LoginRequestDto;
import com.lapoit.api.dto.SignupRequestDto;
import com.lapoit.api.dto.TokenResponseDto;
import com.lapoit.api.jwt.JwtTokenProvider;
import com.lapoit.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequestDto requestDto) {

        // 1) 아이디 중복 체크
        User existing = userMapper.findByUserId(requestDto.getUserId());
        if (existing != null) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 2) 새 유저 생성
        User user = User.builder()
                .userId(requestDto.getUserId())
                .userPw(passwordEncoder.encode(requestDto.getPassword()))
                .userName(requestDto.getUserName())
                .userNickname(requestDto.getUserNickname())
                .storeId(requestDto.getStoreId())
                .phoneNumber(requestDto.getPhoneNumber())
                .role("USER")          // 기본 권한
                .status("ACTIVE")      // 기본 상태
                .build();

        userMapper.save(user);
    }

    public TokenResponseDto login(LoginRequestDto requestDto){
        //유저조회
        User user=userMapper.findByUserId(requestDto.getUserId());

        if(user ==null){
            throw new UsernameNotFoundException("존재하지 않는 아이디");

        }


        //비번 검증
        if(!passwordEncoder.matches(requestDto.getPassword(),user.getUserPw())){
            throw new BadCredentialsException("비번이 일치하지 않습니다.");

        }

        //계정상태 확인
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new IllegalStateException("비활성화된 계정입니다.");
        }


        // 4) JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        long accessTokenValidity = jwtTokenProvider.getAccessTokenValidityInMillis();

        // 5) Redis를 나중에 붙일 거면 여기에서 refreshToken 저장하면 됨

        return TokenResponseDto.of(accessToken, refreshToken, accessTokenValidity);
    }
}
