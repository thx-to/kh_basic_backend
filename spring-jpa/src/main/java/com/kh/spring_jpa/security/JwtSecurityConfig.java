package com.kh.spring_jpa.security;

// HTTP 요청에서 JWT를 확인하고 인증 객체를 설정하는 역할

import com.kh.spring_jpa.jwt.JwtFilter;
import com.kh.spring_jpa.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    // TokenProvider 의존성 주입받기
    private final TokenProvider tokenProvider;

    // HttpSecurity 내부에 있는 메소드들
    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}