package com.lapoit.api.dto.store;

import lombok.*;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {
    private Long storeId;
    private String storeName;
    private String storePhone;
    private String storeAddress;
}
