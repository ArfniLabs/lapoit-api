package com.lapoit.api.service;

import com.lapoit.api.domain.TempUser;
import com.lapoit.api.domain.User;
import com.lapoit.api.dto.admin.TempUserResponseDto;
import com.lapoit.api.dto.admin.UserListResponseDto;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.TempUserMapper;
import com.lapoit.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserMapper userMapper;
    private final TempUserMapper tempUserMapper;


    @Transactional
    public List<TempUserResponseDto> getPendingUsers(String userId) {

        // 유저 아이디 조회 하여 스토어 아이디 불러오기
        Integer storeId = userMapper.findByUserId(userId).getStoreId();

        //스토어 아이디 기반으로 가입 대기 유저 조회
        List<TempUserResponseDto> temps=tempUserMapper.finTempUsersById(storeId);

        return temps;




    }
    @Transactional
    public void approveUser(String userId) {
        TempUser temp=tempUserMapper.findByUserId(userId);


        if(temp==null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        //
        User user = User.builder()
                .userId(temp.getUserId())
                .userPw(temp.getUserPw())
                .userName(temp.getUserName())
                .userNickname(temp.getUserNickname())
                .storeId(temp.getStoreId())
                .phoneNumber(temp.getPhoneNumber())
                .role(temp.getRole())          // 보통 USER
                .code(temp.getCode())
                .status("ACTIVE")
                .point(0L)
                .build();

        userMapper.save(user);


        tempUserMapper.deleteByUserId(userId);

    }

    @Transactional
    public void rejectUser(String userId) {
        tempUserMapper.deleteByUserId(userId);
    }

    public List<UserListResponseDto> getUsers() {
        List<User> users = userMapper.findAll();

        return users.stream()
                .map(UserListResponseDto::from)
                .toList();
    }

    public List<UserListResponseDto> findUserByName(String userName) {
        List<User> users = userMapper.findByUserName(userName);

        return users.stream()
                .map(UserListResponseDto::from)
                .toList();
    }

    public List<UserListResponseDto> findUserByStoreId(String storeId) {
        List<User> users = userMapper.findUserByStoreId(storeId);

        return users.stream()
                .map(UserListResponseDto::from)
                .toList();
    }

    public List<UserListResponseDto> findUserByPhoneNumber(String phoneNumber) {
        List<User> users = userMapper.findUserByPhoneNumber(phoneNumber);

        return users.stream()
                .map(UserListResponseDto::from)
                .toList();
    }

    public List<UserListResponseDto> findUserByNickname(String userNickname) {
        List<User> users = userMapper.findUserByNickname(userNickname);

        return users.stream()
                .map(UserListResponseDto::from)
                .toList();
    }
}
