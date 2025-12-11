package com.lapoit.api.mapper;

import com.lapoit.api.domain.TempUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TempUserMapper {

    void save(TempUser user);

    TempUser findByUserId(@Param("userId") String userId);


}
