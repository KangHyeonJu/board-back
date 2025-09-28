package com.hyeonju.boardback.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


//로그인 이후 사용자가 매 요청하다 비밀번호를 다시 입력하지 않고 JWT 토큰만 들고옴
//따라서 매 요청마다 UserDetailsService를 호출하는 대신, JWT 필터가 토큰을 검증하고
//-> Authentication 객체를 만들어 SecurityContext에 넣어줌
//이 Authentication 안에는 username + roles 정보만 담기고, 비밀번호는 필요 없음
@Component
@RequiredArgsConstructor
//매 요청마다 JWT를 검증해 SecurityContext에 인증정보 등록
public class AuthenticationFilter extends OncePerRequestFilter {
    //OncePerRequestFilter: Spring Security 필터 체인에서 요청 당 한 번만 실행되는 필터(모든 요청마다 JWT 검증 역할을 맡음)
    private final JWTService jwtService;
    private final AuthEntryPoint authEntryPoint;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    //FilterChain을 꼭 호출해야 다음 보안/서블릿 단계로 진행됨
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            //클라이언트가 보낸 Authorization 헤더를 꺼냄
            //JWT를 사용하는 표준 규격은 Authorization: Bearer <토큰> 형식(스킴 구분 목적)
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (header != null && header.startsWith(BEARER_PREFIX)) {
                String token = header.substring(BEARER_PREFIX.length()).trim();
                try{
                    // 토큰 1회 파싱 → subject / roles 동시 활용
                    Claims claims = jwtService.parseClaims(token);
                    String username = jwtService.getAuthUser(claims);
                    if (username == null || username.isBlank()) {
                        SecurityContextHolder.clearContext();
                        authEntryPoint.commence(request, response, new AuthenticationException("Invalid token subject") {});
                        return;
                    }
                    List<GrantedAuthority> authorities = jwtService.toAuthorities(jwtService.getRoles(claims));
                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }catch (JwtException ex){
                    //만료, 변조, 형식 오류 등 -> 명시적으로 401
                    SecurityContextHolder.clearContext();
                    authEntryPoint.commence(request, response, new AuthenticationException(ex.getMessage(), ex){});
                    return;
                }
            }
        }
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}