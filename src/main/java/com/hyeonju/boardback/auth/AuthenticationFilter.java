package com.hyeonju.boardback.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
//JWT가 있으면 인증 객체 생성해서 SecurityContext에 등록하는 필터
public class AuthenticationFilter extends OncePerRequestFilter {
    //OncePerRequestFilter: Spring Security 필터 체인에서 요청 당 한 번만 실행되는 필터(모든 요청마다 JWT 검증 역할을 맡음)
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Authorization 헤더에서 토큰 가져오기
        String jws = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jws != null) {
            // 2. JwtService로 토큰 검증 후 username 추출
            String user = jwtService.getAuthUser(request);

            // 3. 인증 객체 생성(스프링 시큐리티에서 인증정보를 담는 표준 객체)
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

            // 4. SecurityContext에 인증 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}