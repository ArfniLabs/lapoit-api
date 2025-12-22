package com.lapoit.api.service;


import com.lapoit.api.domain.TempUser;
import com.lapoit.api.domain.User;
import com.lapoit.api.dto.auth.*;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.jwt.JwtTokenProvider;
import com.lapoit.api.mapper.TempUserMapper;
import com.lapoit.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final StringRedisTemplate redisTemplate;
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
            throw new CustomException(ErrorCode.ID_ALREADY_EXISTS);
        }
        TempUser existingTemp = tempUserMapper.findByUserId(requestDto.getUserId());
        if (existingTemp != null) throw new CustomException(ErrorCode.ID_ALREADY_EXISTS);

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
        long refreshTtlMillis = jwtTokenProvider.getRefreshTokenValidityInMillis();
        String key = "RT:" + user.getUserId();
        //레디스를 통해 저장
        redisTemplate.opsForValue().set(
                key,
                refreshToken,
                refreshTtlMillis,
                TimeUnit.MILLISECONDS
        );

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

    public void checkPhoneNumber(String phoneNumber) {
        List<User> existingUsers = userMapper.findUserByPhoneNumber(phoneNumber);
        TempUser existingTemp = tempUserMapper.findByPhoneNumber(phoneNumber);
        if ((existingUsers != null && !existingUsers.isEmpty()) || existingTemp != null) {
            throw new CustomException(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    //토큰 재발급 과정
    public TokenResponseDto refresh(RefreshTokenRequestDto request) {

        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String userId= jwtTokenProvider.getUserId(request.getRefreshToken());

        User user=userMapper.findByUserId(userId);
        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
        }String key = "RT:" + userId;
        String saved = redisTemplate.opsForValue().get(key);
        if (saved == null || !saved.equals(request.getRefreshToken())) {
            // 탈취/중복로그인/만료 등
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String newAccess = jwtTokenProvider.createAccessToken(user.getUserId(), user.getRole());

        String newRefresh = jwtTokenProvider.createRefreshToken(userId);

        long refreshTtlMillis = jwtTokenProvider.getRefreshTokenValidityInMillis();
        redisTemplate.opsForValue().set( "RT:" + userId, newRefresh, refreshTtlMillis, TimeUnit.MILLISECONDS);

        long accessTtl = jwtTokenProvider.getAccessTokenValidityInMillis();

        return TokenResponseDto.of(newAccess, newRefresh, accessTtl);


    }

    public void logout(String userId) {
        //refresh  제거
        redisTemplate.delete(userId);
        //lastLogoutAt
        long now = System.currentTimeMillis();
        long accessTtl = jwtTokenProvider.getAccessTokenValidityInMillis();
        redisTemplate.opsForValue().set("LO:" + userId, String.valueOf(now), accessTtl, TimeUnit.MILLISECONDS);

    }

    public FindIdResponseDto findId(FindIdRequestDto findIdRequestDto) {
        User user = userMapper.findByUserNameAndPhoneNumber(findIdRequestDto.getUserName(), findIdRequestDto.getPhoneNumber());
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        FindIdResponseDto id= FindIdResponseDto.builder()
                .userId(user.getUserId())
                .build();
        return id;



    }
}
