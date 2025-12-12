package com.lapoit.api.service;


import com.lapoit.api.domain.TempUser;
import com.lapoit.api.domain.User;
import com.lapoit.api.dto.auth.LoginRequestDto;
import com.lapoit.api.dto.auth.SignupRequestDto;
import com.lapoit.api.dto.auth.TokenResponseDto;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.jwt.JwtTokenProvider;
import com.lapoit.api.mapper.TempUserMapper;
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
    private final TempUserMapper tempUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private void checkExistenceAndThrow(Object existing, Object existingTemp, ErrorCode errorCode) {
        if (existing != null || existingTemp != null) {
            throw new CustomException(errorCode);
        }
    }

    @Transactional
    public void signup(SignupRequestDto requestDto) {

        // 1) 아이디 중복 체크
        User existing = userMapper.findByUserId(requestDto.getUserId());
        if (existing != null) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 2) 새 유저 생성 (임시 유저 테이블에 저장)
        TempUser user = TempUser.builder()
                .userId(requestDto.getUserId())
                .userPw(passwordEncoder.encode(requestDto.getPassword()))
                .userName(requestDto.getUserName())
                .userNickname(requestDto.getUserNickname())
                .storeId(requestDto.getStoreId())
                .phoneNumber(requestDto.getPhoneNumber())
                .code(requestDto.getCode())
                .role("USER")          // 기본 권한
                .status("PENDING")      // 기본 상태 임시 대기임으로                 .status("ACTIVE")
                .build();

        tempUserMapper.save(user);
    }

    public TokenResponseDto login(LoginRequestDto requestDto){
        //유저조회
        User user=userMapper.findByUserId(requestDto.getUserId());

        if(user ==null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);

        }


        //비번 검증
        if(!passwordEncoder.matches(requestDto.getPassword(),user.getUserPw())){
            throw new CustomException(ErrorCode.INVALID_PASSWORD);

        }

        //계정상태 확인
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
        }


        // 4) JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        long accessTokenValidity = jwtTokenProvider.getAccessTokenValidityInMillis();

        // 5) Redis를 나중에 붙일 거면 여기에서 refreshToken 저장하면 됨

        return TokenResponseDto.of(accessToken, refreshToken, accessTokenValidity);
    }


    public void checkId(String userId) {
        User existing = userMapper.findByUserId(userId);
        TempUser existingTemp = tempUserMapper.findByUserId(userId);

        checkExistenceAndThrow(existing, existingTemp, ErrorCode.ID_ALREADY_EXISTS);
    }

    public void checkNickName(String userNickname) {
        User existing = userMapper.findByNickname(userNickname);
        TempUser existingTemp = tempUserMapper.findByNickname(userNickname);

        checkExistenceAndThrow(existing, existingTemp, ErrorCode.NICKNAME_ALREADY_EXISTS);
    }
}
