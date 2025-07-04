package com.playdata.gatewayservice.filter;

import io.jsonwebtoken.Claims;
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
            "/user/login", "/scheduler/crawler", "/scheduler/api"
            ,"/user/create", "/user/temp", "/user/templogin", "/user/verify-email",
            "/user/verify-code", "/user/refresh", "/main/list", "/main/comment/list", "/main/detail"
    );

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            log.info("요청 path: {}", path); // 추가
            AntPathMatcher antPathMatcher = new AntPathMatcher();

            // 허용 url 리스트를 순회하면서 지금 들어온 요청 url과 하나라도 일치하면 true 리턴
            boolean isAllowed
                    = allowUrl.stream()
                    .anyMatch(url -> antPathMatcher.match(url, path));
            log.info("isAllowed:{}", isAllowed);

            if (isAllowed || path.startsWith("/actuator")) {
                return chain.filter(exchange);
            }

            // 토큰이 필요한 요청은 Header에 Authorization 이라는 이름으로 Bearer ~~~가 전달됨.
            String authorizationHeader
                    = exchange.getRequest()
                    .getHeaders().getFirst("Authorization");
            log.info("authorizationHeader: {}", authorizationHeader);

            if (authorizationHeader == null
                    || !authorizationHeader.startsWith("Bearer ")) {
                // 토큰이 존재하지 않거나, Bearer로 시작하지 않는다면
                return onError(exchange, "Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
            }

            // Bearer 떼기
            String token
                    = authorizationHeader.replace("Bearer ", "");
            log.info("token: {}", token);

            Claims claims;
            String roleHeader = "X-User-Role";

            if (path.startsWith("/admin")) {
                // 관리자 경로는 adminSecretKey 사용
                claims = validateJwt(token, adminKey);
                roleHeader = "X-Admin-Role"; // 필요 시 다르게
            } else {
                // 사용자 경로는 userSecretKey 사용
                claims = validateJwt(token, secretKey);
            }

            if (claims == null) {
                return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
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


    private Mono<Void> onError(ServerWebExchange exchange,
                               String msg, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(msg);

        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        // 데이터를 알맞은 형태로 변경
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        // 나중에 하나의 데이터를 준비해서 보내겠다. just(): 준비된 데이터를 Mono로 감싸는 메서드
        return response.writeWith(Mono.just(buffer));
    }

    private Claims validateJwt(String token, String secretKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return null;
        }

    }
}