package com.lapoit.api.dto.store;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreCreateRequestDto {
    private String storeName;
    private String storePhone;
    private String storeStreet;
}
