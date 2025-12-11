package com.lapoit.api.dto.store;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreUpdateRequestDto {
    private String storeName;
    private String storePhone;
    private String storeAddress;
}

