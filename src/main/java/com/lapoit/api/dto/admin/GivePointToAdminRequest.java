package com.lapoit.api.dto.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GivePointToAdminRequest {
    private Long adminId;
    private Long amount;
}

