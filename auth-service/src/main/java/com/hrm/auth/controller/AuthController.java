package com.hrm.auth.controller;

import com.hrm.auth.dto.AuthResponse;
import com.hrm.auth.dto.LoginRequest;
import com.hrm.auth.model.User;
import com.hrm.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        // Lưu ý: Trong thực tế, nên dùng RegisterRequest DTO để ẩn các trường không cần thiết
        User createdUser = authService.registerNewUser(user);

        // Loại bỏ mật khẩu trước khi trả về
        createdUser.setPassword(null);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }
    // TODO: Triển khai endpoint /api/v1/auth/refresh (dùng refresh token)
}
