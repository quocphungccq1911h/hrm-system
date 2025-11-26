package com.hrm.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {
        "com.hrm.auth.mapper",        // Chứa các Mapper tự sinh (Generated)
        "com.hrm.auth.mapper.ext"     // Chứa các Mapper tùy chỉnh (Custom/Extended)
})
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
