package com.playdata.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.secretAdminKey}")
    private String adminKey;

    private final List<String> allowUrl = Arrays.asList(
            "/user/login", "/scheduler/crawler", "/scheduler/api", "/stray-animal-board/**",
            "/animal-board/list", "/animal-board/public/{postId}", "/user/create", "/user/temp", "/user/verify-code",
            "/user/verify-email", "/user/forget/*", "/user/forget/auth", "/user/verify-code", "/user/refresh",
            "/main/list", "/main/comment/list", "/main/detail", "/main/introduction", "/board/popular/children",
            "/board/information/list", "/board/introduction/list", "/board/introduction/main", "/board/information/main",
            "/board/information/popular", "/board/detail/**", "/api/festivals/**", "/festival-service/api/festivals/**",
            "/festival-service/api/festivals","/main/reply/list/*", "/user/kakao"
    );

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            log.info("요청 path: {}", path);

            AntPathMatcher antPathMatcher = new AntPathMatcher();

            // ✅ 허용 경로와 현재 요청 path가 일치하는지 확인
            boolean isAllowed = allowUrl.stream()
                    .anyMatch(url -> antPathMatcher.match(url, path));

            log.info("isAllowed:{}", isAllowed);

            if (isAllowed || path.startsWith("/actuator")) {
                return chain.filter(exchange);
            }

            // 🔐 인증이 필요한 요청 처리
            String authorizationHeader = exchange.getRequest()
                    .getHeaders().getFirst("Authorization");
            log.info("authorizationHeader: {}", authorizationHeader);

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return onError(exchange, "Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
            }

            String token = authorizationHeader.replace("Bearer ", "");
            log.info("token: {}", token);

            Claims claims;
            String roleHeader = "X-User-Role";

            try {
                if (path.startsWith("/admin")) {
                    claims = validateJwt(token, adminKey);
                } else {
                    claims = validateJwt(token, secretKey);
                }
            } catch (RuntimeException e) {
                if (e.getMessage().equals("EXPIRED_TOKEN")) {
                    return onError(exchange, "EXPIRED_TOKEN", HttpStatus.UNAUTHORIZED);
                } else if (e.getMessage().equals("INVALID_TOKEN")) {
                    return onError(exchange, "INVALID_TOKEN", HttpStatus.UNAUTHORIZED);
                }
                return onError(exchange, "인증 오류 발생", HttpStatus.UNAUTHORIZED);
            }

            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header("X-User-Email", claims.getSubject())
                    .header(roleHeader, claims.get("role", String.class))
                    .header("X-User-Id", claims.get("userId", String.class))
                    .header("X-User-Nickname", claims.get("nickname", String.class))
                    .build();

            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String msg, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(msg);

        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    private Claims validateJwt(String token, String secretKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("JWT 만료됨: {}", e.getMessage());
            throw new RuntimeException("EXPIRED_TOKEN"); // 사용자 정의 예외 메시지
        } catch (Exception e) {
            log.error("JWT 파싱 실패: {}", e.getMessage());
            throw new RuntimeException("INVALID_TOKEN"); // 다른 예외는 따로
        }
    }
}