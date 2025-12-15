package com.lapoit.api.mapper;

import com.lapoit.api.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
