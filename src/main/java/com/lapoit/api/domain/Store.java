package com.lapoit.api.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    private Long storeId;       // PK
    private String storeName;   // 지점명
    private String storePhone;  // 전화번호
    private String storeAddress; // 주소
}

