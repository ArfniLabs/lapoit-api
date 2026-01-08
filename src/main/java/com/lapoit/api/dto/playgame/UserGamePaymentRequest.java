package com.lapoit.api.dto.playgame;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGamePaymentRequest {

    private boolean isPaid;              // O / X
    private PaymentMethod paymentMethod; // CARD, CASH, TRANSFER, POINT, MIXED
    private String paymentMemo;
}

