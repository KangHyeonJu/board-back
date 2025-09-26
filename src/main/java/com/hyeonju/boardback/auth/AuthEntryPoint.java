package com.hyeonju.boardback.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
//인증 실패 처리기
//AuthenticationEntryPoint: Spring Security에서 인증 실패시 호출되는 핸들러
public class AuthEntryPoint implements AuthenticationEntryPoint {
    /*
    동작 흐름
    1. 인증되지 않은 사용자가 보안 리소스에 접근 시 → AuthEntryPoint.commence() 실행.
    2. 응답을 401 Unauthorized로 세팅.
    3. JSON 응답 본문에 "Error: <에러메시지>" 출력.
     */

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.println("Error: " + authException.getMessage());
    }
}