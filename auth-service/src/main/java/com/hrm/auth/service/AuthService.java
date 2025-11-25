package com.hrm.auth.service;

import com.hrm.auth.dto.AuthResponse;
import com.hrm.auth.dto.LoginRequest;
import com.hrm.auth.mapper.UserMapper;
import com.hrm.auth.model.User;
import com.hrm.auth.security.AuthUserDetails;
import com.hrm.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public User registerNewUser(User user) {
        // 🔒 Mã hóa mật khẩu trước khi lưu vào DB
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userMapper.insert(user);
        return user;
    }

    public AuthResponse authenticate(LoginRequest request) {
        // 🔑 Bước 1: Xác thực bằng Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        // 🔑 Bước 2: Lấy thông tin User đã xác thực
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();

        // 🔑 Bước 3: Tạo JWT Access Token
        String accessToken =
    }


}
