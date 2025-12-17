package com.lapoit.api.service;

import com.lapoit.api.domain.User;
import com.lapoit.api.domain.UserScore;
import com.lapoit.api.dto.user.CreateStoreRequestDto;
import com.lapoit.api.dto.user.UserResponseDto;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.UserMapper;
import com.lapoit.api.mapper.UserScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserScoreMapper userScoreMapper;

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
                .point(user.getPoint())
                .build();


    }

    @Transactional
    public void createScoreTable(String user, CreateStoreRequestDto dto) {
        User userInfo= userMapper.findByUserId(user);

        Long userId= userInfo.getId();
        Long storeId=dto.getStoreId();

        //현재 유저가 그지역 테이블이 이미 있는지 확인 절차
       UserScore score =userScoreMapper.findByUserIdAndStoreId(userId,storeId);

       if(score!=null){
           throw new CustomException(ErrorCode.SCORE_ALREADY_EXISTS);
       }
       UserScore newScore = UserScore.builder()
                .userId(userId)
                .storeId(storeId)
                .score(0)
                .build();

        try {
            userScoreMapper.save(newScore);
        } catch (DuplicateKeyException e) {
            throw new CustomException(ErrorCode.SCORE_ALREADY_EXISTS);
        }catch (DataIntegrityViolationException e){
            throw new CustomException(ErrorCode.STORE_NOT_FOUND);
        }


    }


    public List<UserScore> getMyScoreList(String user) {
        User userInfo= userMapper.findByUserId(user);

        List<UserScore> scores=userScoreMapper.findByUserId(userInfo.getId());

        return scores;
    }
}

