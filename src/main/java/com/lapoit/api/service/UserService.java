package com.lapoit.api.service;

import com.lapoit.api.domain.User;
import com.lapoit.api.domain.UserHistory;
import com.lapoit.api.domain.UserScore;
import com.lapoit.api.dto.rank.StoreRankingItem;
import com.lapoit.api.dto.rank.StoreRankingResponse;
import com.lapoit.api.dto.user.CreateStoreRequestDto;
import com.lapoit.api.dto.user.HistoryResponse;
import com.lapoit.api.dto.user.UpdateProfileRequestDto;
import com.lapoit.api.dto.user.UserResponseDto;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserScoreMapper userScoreMapper;
    private final TempUserMapper tempUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserHistoryMapper userHistoryMapper;
    private final UserGameMapper userGameMapper;
    private final RankingMapper rankingMapper;
    private final StoreMapper storeMapper;

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
                .role(user.getRole())
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

    public boolean checkPassword(String userId, String password) {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return passwordEncoder.matches(password, user.getUserPw());
    }

    @Transactional
    public void changePassword(String userId, String currentPassword, String newPassword) {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(currentPassword, user.getUserPw())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        userMapper.updatePassword(user.getId(), passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void updateProfile(String userId, UpdateProfileRequestDto dto) {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        String newNickname = dto.getUserNickname();
        Integer newStoreId = dto.getStoreId();

        if (newNickname != null && !newNickname.equals(user.getUserNickname())) {
            User existingUserNick = userMapper.findByNickname(newNickname);
            if (existingUserNick != null && !existingUserNick.getId().equals(user.getId())) {
                throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
            }
            if (tempUserMapper.findByNickname(newNickname) != null) {
                throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
            }
        }

        userMapper.updateProfile(user.getId(), newNickname, newStoreId);
    }


    public List<HistoryResponse> getUserHistory(String userId, Long storeId,LocalDate startDate, LocalDate endDate, int page, int size) {
        User user = userMapper.findByUserId(userId);
        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        int offset = page * size;

        LocalDateTime from = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime to   = endDate != null ? endDate.atTime(23, 59, 59) : null;

        List<UserHistory> histories =
                userHistoryMapper.findUserHistory(
                        user.getId() , storeId, from, to, size, offset
                );

        return histories.stream()
                .map(HistoryResponse::from)
                .toList();


    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Long id = user.getId();
        userGameMapper.deleteByUserId(id);
        userHistoryMapper.deleteByUserId(id);
        userScoreMapper.deleteByUserId(id);
        tempUserMapper.deleteByUserId(userId);
        userMapper.deleteById(id);
    }



    @Transactional(readOnly = true)
    public StoreRankingResponse getStoreRanking(Long storeId) {

        if (!storeMapper.existsById(storeId)) {
            throw new CustomException(ErrorCode.STORE_NOT_FOUND);
        }

        List<StoreRankingItem> rankings =
                rankingMapper.findStoreRanking(storeId);

        if (rankings.isEmpty()) {
            throw new CustomException(ErrorCode.RANKING_NOT_FOUND);
        }

        return new StoreRankingResponse(storeId, rankings);
    }

}

