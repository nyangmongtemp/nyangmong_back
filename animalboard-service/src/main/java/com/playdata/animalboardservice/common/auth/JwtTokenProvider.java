package com.playdata.animalboardservice.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰 생성 및 파싱을 담당하는 클래스
 */
@Component
@Slf4j
public class JwtTokenProvider {

    // 엑세스 토큰 서명용 비밀키
    @Value("${jwt.secretKey}")
    private String secretKey;

    // 엑세스 토큰 만료시간 (분 단위)
    @Value("${jwt.expiration}")
    private int expiration;

    // 리프레시 토큰 서명용 비밀키
    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    // 리프레시 토큰 만료시간 (분 단위)
    @Value("${jwt.expirationRt}")
    private int expirationRt;

    /**
     * 엑세스 토큰 생성
     * @param email 사용자 이메일 (subject)
     * @param role 사용자 권한
     * @param nickname 사용자 닉네임
     * @param userId 사용자 ID
     * @return JWT 토큰 문자열
     */
    public String createToken(String email, String role, String nickname, Long userId) {

        // 닉네임은 한글 등 특수문자 포함 가능 → URL 인코딩 처리
        String encodedNickname = urlEncode(nickname);

        log.info(nickname);
        log.info(encodedNickname);

        // 토큰의 Payload(Claim)에 담을 정보 설정
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        claims.put("userId", userId.toString());
        claims.put("nickname", encodedNickname);

        Date now = new Date();

        // 토큰 생성
        return Jwts.builder()
                .setClaims(claims) // 사용자 정보
                .setIssuedAt(now)  // 발급 시간
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000)) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey) // 서명 알고리즘 및 키
                .compact();
    }

    /**
     * 리프레시 토큰 생성
     * @param email 사용자 이메일
     * @param role 권한
     * @param userId 사용자 ID
     * @return 리프레시 토큰 문자열
     */
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

    /**
     * 토큰을 검증하고 내부 사용자 정보를 추출하여 객체로 반환
     * @param token JWT 토큰
     * @return 파싱된 사용자 정보 (TokenUserInfo)
     * @throws Exception 파싱 실패 시 예외 발생
     */
    public TokenUserInfo validateAndGetTokenUserInfo(String token) throws Exception {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 닉네임은 디코딩 처리
        String s = claims.get("nickname", String.class);
        String decodedNickname = urlDecode(s);

        log.info(s);
        log.info(decodedNickname);

        return TokenUserInfo.builder()
                .email(claims.getSubject())
                .role(claims.get("role", String.class))
                .userId(Long.valueOf(claims.get("userId", String.class)))
                .nickname(decodedNickname)
                .build();
    }

    /**
     * URL 인코딩 유틸
     */
    public static String urlEncode(String input) {
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }

    /**
     * URL 디코딩 유틸
     */
    public static String urlDecode(String input) {
        return URLDecoder.decode(input, StandardCharsets.UTF_8);
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
