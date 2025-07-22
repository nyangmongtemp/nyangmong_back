package com.playdata.adminservice.common.auth;

import com.playdata.adminservice.admin.entity.Role;
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
        String adminEmail = request.getHeader("X-Admin-Email");
        String adminRole = request.getHeader("X-Admin-Role");
        String adminId = request.getHeader("X-Admin-Id");
        log.info("Email:{}", adminEmail);
        log.info("Role:{}", adminRole);
        log.info("adminId:{}", adminId);

        if (adminEmail != null  && adminRole != null && adminId != null) {

            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

            authorityList.add(new SimpleGrantedAuthority("ROLE_" + adminRole));

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    new TokenUserInfo(adminEmail, Role.valueOf(adminRole) ,Long.valueOf(adminId)),
                    "",
                    authorityList // 인가 정보 (권한)
            );

            // 위에 만든 토큰 정보를 던져준다.
            SecurityContextHolder.getContext().setAuthentication(auth);

        }
        // 필터 정리
        filterChain.doFilter(request, response);

    }
}
