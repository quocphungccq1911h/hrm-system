package com.hrm.auth.util;

import com.hrm.auth.security.AuthUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration; // Thời gian sống của Access Token (ms)

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(AuthUserDetails userDetails) {
        Map<String, Object> claims= new HashMap<>();

        // Thêm các thông tin cần thiết vào Claims (Payload của JWT)
        claims.put("user_id", userDetails.getUserId());
        claims.put("emp_id", userDetails.getEmployeeId());
        // claims.put("roles", userDetails.getAuthorities());    // Sẽ thêm Roles sau
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // tên đăng nhập
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
