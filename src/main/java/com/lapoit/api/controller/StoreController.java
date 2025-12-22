package com.lapoit.api.controller;

import com.lapoit.api.controller.docs.StoreControllerDocs;
import com.lapoit.api.dto.ApiResponseDto;
import com.lapoit.api.dto.store.StoreCreateRequestDto;
import com.lapoit.api.dto.store.StoreResponseDto;
import com.lapoit.api.dto.store.StoreUpdateRequestDto;
import com.lapoit.api.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
public class StoreController implements StoreControllerDocs {

    private final StoreService storeService;

    /** 지점 단건 조회 */
    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStore(@PathVariable("storeId") Long storeId) {
        StoreResponseDto response = storeService.getStore(storeId);
        return ResponseEntity.ok(
                ApiResponseDto.success("Store-200", "지점 단건 조회 성공", response)
        );
    }

    /** 전체 지점 목록 조회 */
    @GetMapping
    public ResponseEntity<?> getStoreList() {
        List<StoreResponseDto> response = storeService.getStoreList();
        return ResponseEntity.ok(
                ApiResponseDto.success("Store-200", "지점 목록 조회 성공", response)
        );
    }

    /** 지점 생성 */
    @PostMapping
    public ResponseEntity<?> createStore(
            @RequestBody StoreCreateRequestDto request
    ) {
        StoreResponseDto response = storeService.createStore(request);
        return ResponseEntity.ok(
                ApiResponseDto.success("Store-201", "지점 생성 성공", response)
        );
    }

    /** 지점 수정 */
    @PutMapping("/{storeId}")
    public ResponseEntity<?> updateStore(
            @PathVariable("storeId") Long storeId,
            @RequestBody StoreUpdateRequestDto request
    ) {
        StoreResponseDto response = storeService.updateStore(storeId, request);
        return ResponseEntity.ok(
                ApiResponseDto.success("Store-200", "지점 수정 성공", response)
        );
    }

    /** 지점 삭제 */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable("storeId") Long storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.ok(
                ApiResponseDto.success("Store-200", "지점 삭제 성공", null)
        );
    }
}

