package com.lapoit.api.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindIdResponseDto {
    String userId;
}
