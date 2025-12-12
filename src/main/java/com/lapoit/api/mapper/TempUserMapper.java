package com.lapoit.api.mapper;

import com.lapoit.api.domain.TempUser;
import com.lapoit.api.dto.admin.TempUserResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TempUserMapper {

    void save(TempUser user);

    TempUser findByUserId(@Param("userId") String userId);


    TempUser findByNickname(@Param("userNickname") String userNickname);

    List<TempUserResponseDto> finTempUsersById(@Param("storeId") Integer storeId);

    void deleteByUserId(String userId);
}
