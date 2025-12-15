package com.lapoit.api.dto.admin;

import com.lapoit.api.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponseDto {
    private Long id;
    private String userId;
    private String userName;
    private String userNickname;
    private Integer storeId;
    private String phoneNumber;
    private String status;

    private String code;
    private String createAt;
    private String updateAt;

    public static UserListResponseDto from(User user) {
        return new UserListResponseDto(
                user.getId(),
                user.getUserId(),
                user.getUserName(),
                user.getUserNickname(),
                user.getStoreId(),
                user.getPhoneNumber(),
                user.getStatus(),
                user.getCode(),
                user.getCreateAt().toString(),
                user.getUpdateAt().toString()
        );
    }
}
