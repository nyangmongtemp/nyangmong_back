package com.playdata.adminservice.common.auth;


import com.playdata.adminservice.admin.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secretAdminKey}")
    private String secretKey;

    @Value("${jwt.expirationAdmin}")
    private int expiration;

    public String createToken(String email, Role role, Long adminId) {


        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", String.valueOf(role));
        claims.put("adminId", adminId.toString());

        // 토큰 만료기간 생성을 위해 선언
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                // 현재 시간 밀리초에 30분을 더한 시간만큼을 만료시간으로 세팅
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 토큰에서 이메일(subject)을 추출
     * @param token JWT 토큰
     * @return 이메일
     */
    public String extractEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }


}