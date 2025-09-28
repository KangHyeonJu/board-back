package com.hyeonju.boardback.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
//토큰 발급, 검증
public class JWTService {
    //토큰 만료 시간(밀리초) - 1일(86,400,000ms). 실제 운영 시에는 더 짧아야 함
    static final long EXPIRATION_TIME = 86400000;

    //운영 환경에서는 애플리케이션 구성에서 읽어들여와야 함 (application.yml 같은 설정파일에서)
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //JWT 서명 키

    //서명된 JWT 토큰 생성
    public String createToken(String username, List<String> roles) {
        String token = Jwts.builder() //JWT 토큰을 만드는 빌더 객체 생성
                .setSubject(username) //토큰에 사용자 이름 저장
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //토큰 만료시간 설정
                .signWith(key) //위에서 정의한 비밀키로 서명
                .compact(); //JWT 문자열로 직렬화
        return token;
    }

    // 토큰 → Claims (시계 오차 허용)
    public Claims parseClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(60)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //요청의 Authorization 헤더에서 토큰을 가져온 뒤 토큰을 확인하고 사용자 이름을 가져옴
    public String getAuthUser(Claims claims) {
        return claims.getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(Claims claims) {
        List<String> roles = claims.get("roles", List.class);
        return roles == null ? Collections.emptyList() : roles;
    }

    public List<GrantedAuthority> toAuthorities(List<String> roles) {
        if (roles == null) return Collections.emptyList();
        return roles.stream()
                .filter(Objects::nonNull)   //null값 제거
                .map(String::trim)          //문자열 앞뒤 공백 제거
                .filter(s -> !s.isEmpty())  //빈 문자열("") 제거
                .distinct()    //중복 제거
                .map(SimpleGrantedAuthority::new)   //문자열을 SimpleGrantedAuthority 객체로 감쌈
                .map(a -> (GrantedAuthority) a) //SimpleGrantedAuthority는 GrantedAuthority 인터페이스를 구현하므로 캐스팅
                .toList();  //최종적으로 List<GrantedAuthority>로 수집
    }

}
