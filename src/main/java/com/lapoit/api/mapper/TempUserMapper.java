package com.lapoit.api.mapper;

import com.lapoit.api.domain.TempUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TempUserMapper {

    void save(TempUser user);
}
