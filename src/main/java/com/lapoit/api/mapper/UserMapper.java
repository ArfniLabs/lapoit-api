package com.lapoit.api.mapper;

import com.lapoit.api.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface  UserMapper {
    User findByUserId(@Param("userId") String userId);

    void save(User user);

    User findById(@Param("id") Long id);

    User findByNickname(@Param("userNickname") String userNickname);

    User findByUserNameAndPhoneNumber(
            @Param("userName") String userName,
            @Param("phoneNumber") String phoneNumber
    );

    List<User> findAll();

    List<User> findByUserName(@Param("userName") String userName);

    List<User> findUserByStoreId(@Param("storeId") String storeId);

    List<User> findUserByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    List<User> findUserByNickname(@Param("userNickname") String userNickname);

    void updatePassword(@Param("id") Long id, @Param("encode") String encode);

    void updateProfile(
            @Param("id") Long id,
            @Param("userNickname") String userNickname,
            @Param("storeId") Integer storeId
    );



    int updateUserPointDelta(@Param("userId") String userId, @Param("delta") Long delta);
}
