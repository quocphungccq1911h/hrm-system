package com.hrm.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String employeeId; // ID nhân viên để FE biết ai đang đăng nhập
    private String tokenType = "Bearer";
    private Long expiresIn; // Thời gian token hết hạn
}
