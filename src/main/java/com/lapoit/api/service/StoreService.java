package com.lapoit.api.service;

import com.lapoit.api.domain.Store;
import com.lapoit.api.dto.store.StoreCreateRequestDto;
import com.lapoit.api.dto.store.StoreResponseDto;
import com.lapoit.api.dto.store.StoreUpdateRequestDto;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreMapper storeMapper;

    public StoreResponseDto getStore(Long storeId) {
        Store store = storeMapper.findById(storeId);

        if (store == null) {
            //throw new CustomException(ErrorCode.STORE_NOT_FOUND);
        }

        return StoreResponseDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storePhone(store.getStorePhone())
                .storeAddress(store.getStoreAddress())
                .build();
    }

    public List<StoreResponseDto> getStoreList() {
        List<Store> stores = storeMapper.findAll();

        return stores.stream()
                .map(s -> StoreResponseDto.builder()
                        .storeId(s.getStoreId())
                        .storeName(s.getStoreName())
                        .storePhone(s.getStorePhone())
                        .storeAddress(s.getStoreAddress())
                        .build())
                .collect(Collectors.toList());
    }

    public StoreResponseDto createStore(StoreCreateRequestDto dto) {

        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .storePhone(dto.getStorePhone())
                .storeAddress(dto.getStoreStreet())
                .build();

        storeMapper.insertStore(store); // storeId 자동 생성됨

        return StoreResponseDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storePhone(store.getStorePhone())
                .storeAddress(store.getStoreAddress())
                .build();
    }


    public StoreResponseDto updateStore(Long storeId, StoreUpdateRequestDto dto) {

        Store store = storeMapper.findById(storeId);
        if (store == null) {
           // throw new CustomException(ErrorCode.STORE_NOT_FOUND);
        }

        store.setStoreName(dto.getStoreName());
        store.setStorePhone(dto.getStorePhone());
        store.setStoreAddress(dto.getStoreAddress());

        storeMapper.updateStore(store);

        return getStore(storeId);
    }

    public void deleteStore(Long storeId) {
        Store store = storeMapper.findById(storeId);
        if (store == null) {
           // throw new CustomException(ErrorCode.STORE_NOT_FOUND);
        }

        storeMapper.deleteStore(storeId);
    }
}

