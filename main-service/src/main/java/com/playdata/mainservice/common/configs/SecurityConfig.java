package com.playdata.mainservice.common.configs;


import com.playdata.mainservice.common.auth.JwtAuthFilter;
import com.playdata.mainservice.common.exception.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 권한 검사를 컨트롤러의 메서드에서 전역적으로 수행하기 위한 설정.

public class SecurityConfig {

    private  final JwtAuthFilter jwtAuthFilter;
    private  final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }
    @Bean // 이 메서드가 리턴하는 시큐리티 설정을 빈으로 등록하겠다.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

//        http.cors(Customizer.withDefaults()); // 직접 커스텀한 CORS 설정을 적용.

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 요청 권한 설정 (어떤 url이냐에 따라 검사를 할 지 말지를 결정)
        http.authorizeHttpRequests(auth -> {
            auth
//
                    .requestMatchers(
                            "/user/login", "/user/create", "/user/templogin", "/user/temp",
                            "/user/verify-email", "/user/verify-code"
                    ).permitAll()   // 추후에 token이 필요하지 않은 요청 url 들은 여기에 추가해야함.
                    .anyRequest().authenticated();
        });

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // 설정한 HttpSecurity 객체를 기반으로 시큐리티 설정 구축 및 반환.
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
