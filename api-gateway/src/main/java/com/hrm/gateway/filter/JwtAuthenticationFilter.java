package com.hrm.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 💡 1. Định nghĩa các URL KHÔNG cần xác thực (ví dụ: login, register, refresh)
        final Predicate<ServerHttpRequest> isApiSecured = r ->
                !r.getURI().getPath().contains("/api/v1/auth/");

        // Nếu API cần bảo mật
        if (isApiSecured.test(request)) {
            // 💡 2. Lấy token từ Header
            if (!request.getHeaders().containsKey("Authorization")) {
                return this.onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
            String token = authHeader.replace("Bearer ", "");

            // 💡 3. Xác thực và Giải mã token
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                // 💡 4. Thêm thông tin User vào Header để gửi đến Backend Services
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-Auth-User-Id", claims.getSubject()) // Subject là User ID
                        .header("X-Auth-Employee-Id", claims.get("employeeId", String.class))
                        // Thêm Role/Authorities nếu có
                        .build();
                // Chuyển tiếp request đến Backend Service
                return chain.filter(exchange.mutate().request(modifiedRequest).build());

            } catch (Exception e) {
                return this.onError(exchange, "Invalid or expired token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
