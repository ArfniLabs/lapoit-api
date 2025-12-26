package com.lapoit.api.mapper;

import com.lapoit.api.domain.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreMapper {

    Store findById(Long storeId);

    List<Store> findAll();

    void insertStore(Store store);

    int updateStore(Store store);

    int deleteStore(Long storeId);

    /** 지점 존재 여부 확인 */
    boolean existsById(@Param("storeId") Long storeId);
}

