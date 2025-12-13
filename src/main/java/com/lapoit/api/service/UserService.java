package com.lapoit.api.service;

import com.lapoit.api.domain.User;
import com.lapoit.api.dto.user.UserResponseDto;
import com.lapoit.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public UserResponseDto getMyInfo(String userId) {
        User user= userMapper.findByUserId(userId);

        return UserResponseDto.builder()
                .id(user.getId())
                .storeId(user.getStoreId())
                .userId(user.getUserId())
                .createAt(user.getCreateAt())
                .code(user.getCode())
                .phoneNumber(user.getPhoneNumber())
                .userNickname(user.getUserNickname())
                .userName(user.getUserName())
                .build();


    }
}
