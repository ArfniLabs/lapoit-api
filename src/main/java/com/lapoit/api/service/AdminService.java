package com.lapoit.api.service;

import com.lapoit.api.domain.TempUser;
import com.lapoit.api.domain.User;
import com.lapoit.api.domain.UserHistory;
import com.lapoit.api.domain.UserScore;
import com.lapoit.api.dto.admin.*;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.jwt.CustomUserDetails;
import com.lapoit.api.mapper.TempUserMapper;
import com.lapoit.api.mapper.UserHistoryMapper;
import com.lapoit.api.mapper.UserMapper;
import com.lapoit.api.mapper.UserScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserMapper userMapper;
    private final TempUserMapper tempUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserHistoryMapper userHistoryMapper;
    private final UserScoreMapper  userScoreMapper;

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

    @Transactional
    public void resetPassword(ResetPasswordRequestDto dto) {
        User user = userMapper.findByUserId(dto.getUserId());
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        if (!user.getUserName().equals(dto.getUserName())) {
            throw new CustomException(ErrorCode.USER_INFO_MISMATCH_NAME);
        }

        if(!user.getPhoneNumber().equals(dto.getPhoneNumber())){
            throw new CustomException(ErrorCode.USER_INFO_MISMATCH_NUMBER);

        }
        String tempPassword = "123456";
        userMapper.updatePassword(user.getId(), passwordEncoder.encode(tempPassword));




    }

    @Transactional
    public void updateUserPoint(String adminId,String userId, UpdateUserPointRequest request) {
        User user = userMapper.findByUserId(userId);
        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        User admin=userMapper.findByUserId(adminId);
        if (admin == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        long delta = request.getPoint(); // +면 적립, -면 차감

        int adminUpdated = userMapper.updateUserPointDelta(adminId, -delta);
        if (adminUpdated == 0) {
            throw new CustomException(ErrorCode.POINT_NOT_ENOUGH);
        }

        int updated = userMapper.updateUserPointDelta(userId, delta); // 성공하면 1
        if (updated == 0) {

            throw new CustomException(ErrorCode.POINT_NOT_ENOUGH);
        }



        // 히스토리 기록 (store_id는 user에 있으니 가져와서 넣기) ( 유저쪽)
        userHistoryMapper.insert(UserHistory.builder()
                .userId(user.getId())          // PK id
                .storeId(0L) //점수는 0으로 고정 하여 조회 할수 있도록 한다.
                .scoreDelta(0L)
                .pointDelta(delta)
                .actorUserId(admin.getId())
                .build());

        //어드민쪽
        userHistoryMapper.insert(UserHistory.builder()
                .userId(admin.getId())          // PK id
                .storeId(0L) //점수는 0으로 고정 하여 조회 할수 있도록 한다.
                .scoreDelta(0L)
                .pointDelta(-delta)
                .actorUserId(user.getId())
                .build());
    }

    @Transactional
    public void updateUserScore(String adminId,String userId, UpdateUserScoreRequest request) {
        User user = userMapper.findByUserId(userId);
        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        User admin=userMapper.findByUserId(adminId);

        //지점 확인
        UserScore score =userScoreMapper.findByUserIdAndStoreId(user.getId(),request.getStoreId());

        // ✅ row 존재 보장 (동시성 안전)
        try {
            userScoreMapper.upsertUserScore(user.getId(), request.getStoreId());
        } catch (DataIntegrityViolationException e) {
            // store FK 없으면 여기서 터질 수 있음
            throw new CustomException(ErrorCode.STORE_NOT_FOUND);
        }

        int updated = userScoreMapper.updateUserScore(
                user.getId(),
                request.getStoreId(),
                request.getScore()
        );

        if (updated == 0) {
            throw new CustomException(ErrorCode.SCORE_NOT_ENOUGH);
        }

        Long delta = (long) request.getScore();


        userHistoryMapper.insert(UserHistory.builder()
                .userId(user.getId())
                .storeId(request.getStoreId())
                .scoreDelta(delta)
                .pointDelta(0L)
                .actorUserId(admin.getId())
                .build());
    }

    public List<UserHistoryResponse> getUserHistory(
            String userId,
            Long storeId,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size
    ) {
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
                .map(UserHistoryResponse::from)
                .toList();
    }

    @Transactional
    public void deactivateUser(String userId) {
        User user = userMapper.findByUserId(userId);
        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        userMapper.updateStatusByUserId(userId, "INACTIVE");
    }
    @Transactional
    public void activateUser(String userId) {
        User user = userMapper.findByUserId(userId);
        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        userMapper.updateStatusByUserId(userId, "ACTIVE");
    }


    /** 포인트 생성코드이므로 가장 중요하게 관리해야함 */
    @Transactional
    public void mintPoint(Long amount, CustomUserDetails principal) {

        // 1. SUPERADMIN 권한 확인
        if (!"SUPERADMIN".equals(principal.getUser().getRole())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        if (amount <= 0) {
            throw new CustomException(ErrorCode.INVALID_POINT_AMOUNT);
        }

        // 2. SUPERADMIN 포인트 증가 (무에서 유)
        userMapper.addPoint(principal.getUser().getId(), amount);

        // 3. 히스토리 기록 (발행 로그)
        userHistoryMapper.insert(UserHistory.builder()
                .userId(principal.getUser().getId())     // 중앙은행 자신
                .storeId(0L)
                .pointDelta(amount)
                .scoreDelta(0L)
                .actorUserId(principal.getUser().getId())
                .build());
    }



    // 슈퍼관리자 -> 관리자 포인트 지급
    @Transactional
    public void givePointToAdmin(Long adminUserId,
                                 Long amount,
                                 CustomUserDetails principal) {

        // 1️⃣ SUPERADMIN 권한 확인
        if (!"SUPERADMIN".equals(principal.getUser().getRole())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        if (amount <= 0) {
            throw new CustomException(ErrorCode.INVALID_POINT_AMOUNT);
        }

        User admin = userMapper.findById(adminUserId);
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            throw new CustomException(ErrorCode.ADMIN_NOT_FOUND);
        }

        Long superAdminId = principal.getUser().getId();

        // 2️⃣ SUPERADMIN 포인트 차감
        int updated = userMapper.subtractPoint(superAdminId, amount);
        if (updated == 0) {
            throw new CustomException(ErrorCode.POINT_NOT_ENOUGH);
        }

        // 3️⃣ ADMIN 포인트 증가
        userMapper.addPoint(adminUserId, amount);

        // 4️⃣ 히스토리 기록 (중앙은행 → 시중은행)
        userHistoryMapper.insert(UserHistory.builder()
                .userId(adminUserId)          // 포인트 받은 주체
                .storeId(0L)                  // 슈퍼관리자 포인트 지급 store_id
                .actorUserId(superAdminId)    // 지급한 주체
                .pointDelta(amount)
                .scoreDelta(0L)
                .build());
    }


}
