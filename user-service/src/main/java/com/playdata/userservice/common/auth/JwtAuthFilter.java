package com.playdata.userservice.common.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // 게이트웨이가 토큰 내에 클레임을 헤더에 담아서 보내준다.
        String userEmail = request.getHeader("X-User-Email");
        String userRole = request.getHeader("X-User-Role");
        String userId = request.getHeader("X-User-Id");
        String nickname = request.getHeader("X-User-Nickname");
        log.info("userEmail:{}", userEmail);
        log.info("userRole:{}", userRole);
        log.info("userId:{}", userId);
        log.info("nickname:{}", nickname);

        if (userEmail != null  && userRole != null && userId != null && nickname != null) {

            // spring security에게 전달할 인가 정보 리스트를 생성. (권한 정보)
            // 권한이 여러 개 존재할 경우 리스트로 권한 체크에 사용할 필드를 add. (권한 여러개면 여러번 add 가능)
            // 나중에 컨트롤러의 요청 메서드마다 권한을 파악하게 하기 위해 미리 저장을 해 놓는 것.
            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

            authorityList.add(new SimpleGrantedAuthority("ROLE_" + userRole));

            // 인증 완료 처리
            // 위에서 준비한 여러가지 사용자 정보, 인가정보 리스트를 하나의 객체로 포장
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    new TokenUserInfo(userEmail, userRole, nickname, Long.valueOf(userId)), // 컨트롤러 등에서 활용할 유저 정보
                    "", // 인증된 사용자의 비밀번호: 보통 null 혹은 빈 문자열로 선언.
                    authorityList // 인가 정보 (권한)
            );

            // 시큐리티 컨테이너에 인증 정보 객체를 등록.
            // 인증 정보를 전역적으로 어느 컨테이너, 어느 서비스에서나 활용할 수 있도록 미리 저장.
            SecurityContextHolder.getContext().setAuthentication(auth);

        }
        // 필터를 통과하는 메서드 (doFilter를 호출하지 않으면 필터 통과가 안됩니다!)
        // 일단 토큰이 있든 없든 필터를 통과해서 시큐리티한테 검사는 받아야 하니깐...
        filterChain.doFilter(request, response);

    }
}
