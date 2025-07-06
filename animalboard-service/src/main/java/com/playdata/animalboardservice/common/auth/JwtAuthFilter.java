package com.playdata.animalboardservice.common.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

            authorityList.add(new SimpleGrantedAuthority("ROLE_" + userRole));

            nickname = JwtTokenProvider.urlDecode(nickname);
            log.info("decoded nickname:{}", nickname);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    new TokenUserInfo(userEmail, userRole, nickname, Long.valueOf(userId)),
                    "",
                    authorityList // 인가 정보 (권한)
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

        }
        filterChain.doFilter(request, response);

    }
}
