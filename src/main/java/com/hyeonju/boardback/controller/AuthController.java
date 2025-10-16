package com.hyeonju.boardback.controller;

import com.hyeonju.boardback.auth.JWTService;
import com.hyeonju.boardback.dto.AuthRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            // 1) 인증 위임 (아이디/비번 검증)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            // 2) Authentication에서 username/roles 꺼내기
            String username = authentication.getName(); // loadUserByUsername의 username
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            // 3) JWT 생성
            String token = jwtService.createToken(username, roles);
            System.out.println("token: " + token);

            // 4) 헤더로 반환 (+ CORS 노출)
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                    .build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }
}