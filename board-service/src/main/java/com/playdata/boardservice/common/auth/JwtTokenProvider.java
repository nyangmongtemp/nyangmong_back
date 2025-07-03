package com.playdata.boardservice.common.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    @Value("${jwt.expirationRt}")
    private int expirationRt;

    public String createToken(String email, String role, String nickname, Long userId) {


        String encodedNickname = urlEncode(nickname);

        log.info(nickname);
        log.info(encodedNickname);

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        claims.put("userId", userId.toString());
        claims.put("nickname", encodedNickname);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                // 현재 시간 밀리초에 30분을 더한 시간만큼을 만료시간으로 세팅
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(String email, String role, Long userId) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        claims.put("userId", userId);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationRt * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKeyRt)
                .compact();
    }

    public TokenUserInfo validateAndGetTokenUserInfo(String token)
            throws Exception {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String s = claims.get("nickname", String.class);
        String decodedNickname = urlDecode(s);

        log.info(s);
        log.info(decodedNickname);

        return TokenUserInfo.builder()
                .email(claims.getSubject())
                .role(claims.get("role", String.class))
                .userId(Long.valueOf(claims.get("userId", String.class)))
                //.nickname(claims.get("nickname", String.class))
                .nickname(decodedNickname)
                .build();
    }

    public static String urlEncode(String input) {
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }

    public static String urlDecode(String input) {
        return URLDecoder.decode(input, StandardCharsets.UTF_8);
    }

}