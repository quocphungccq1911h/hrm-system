package com.hrm.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Khai báo Bean mã hóa mật khẩu
    // Rất quan trọng! Dùng BCrypt là tiêu chuẩn hiện nay.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    // 2. Cấu hình chuỗi lọc bảo mật (Security Filter Chain)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 🔒 Tắt CSRF (Thường dùng cho Web, không cần thiết cho Mobile App/REST API)
                .csrf(AbstractHttpConfigurer::disable)
                // 🚪 Cho phép truy cập công khai (permitAll) các đường dẫn của Authen
                .authorizeHttpRequests(authorize -> authorize
                        // Đường dẫn /api/v1/auth/login và /api/v1/auth/register được phép truy cập mà không cần Token
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // Tất cả các request khác (đường dẫn khác) phải được xác thực
                        .anyRequest().authenticated()
                )
                // 🚫 Tắt form login mặc định của Spring
                .httpBasic(Customizer.withDefaults());
        // (Sẽ cấu hình SessionManagement và Filter JWT ở bước sau)

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
