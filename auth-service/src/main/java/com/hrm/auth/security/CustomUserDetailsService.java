package com.hrm.auth.security;

import com.hrm.auth.mapper.ext.ExtUserMapper;
import com.hrm.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ExtUserMapper userMapperExt;
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user;

        // 💡 BƯỚC 1: Kiểm tra xem tham số có phải là UUID hợp lệ không
        try {
            UUID userId = UUID.fromString(identifier);
            // Nếu là UUID hợp lệ, tìm kiếm theo ID
            user = userMapperExt.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + identifier));
        } catch (IllegalArgumentException e) {
            // 💡 BƯỚC 2: Nếu không phải UUID (ví dụ: là username), tìm kiếm theo Username
            user = userMapperExt.findByUsername(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + identifier));
        }

        // 💡 BƯỚC 3: Trả về AuthUserDetails đã gói (wrap) User
        return new AuthUserDetails(user);
    }
}
