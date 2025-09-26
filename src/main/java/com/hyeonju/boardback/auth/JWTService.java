package com.hyeonju.boardback.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTService {
    //토큰 만료 시간(밀리초) - 1일(86,400,000ms). 실제 운영 시에는 더 짧아야 함
    static final long EXPIRATIONTIME = 86400000;
    static final String PREFIX = "Bearer";  //JWT 앞에 붙는 문자열

    //비밀키 생성. 시연 목적으로만 이용
    //운영 환경에서는 애플리케이션 구성에서 읽어들여와야 함 (application.yml 같은 설정파일에서)
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //JWT 서명 키

    //서명된 JWT 토큰 생성
    public String getToken(String username) {
        String token = Jwts.builder() //JWT 토큰을 만드는 빌더 객체 생성
                .setSubject(username) //토큰에 사용자 이름 저장
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME)) //토큰 만료시간 설정
                .signWith(key) //위에서 정의한 비밀키로 서명
                .compact(); //JWT 문자열로 직렬화
        return token;
    }

    //요청의 Authorization 헤더에서 토큰을 가져온 뒤 토큰을 확인하고 사용자 이름을 가져옴
    public String getAuthUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION); //Authorization 헤더 가져오기
        if (token != null) {
            String user = Jwts.parserBuilder()
                    .setSigningKey(key) //검증할 때 동일한 키 필요
                    .build()
                    .parseClaimsJws(token.replace(PREFIX, "")) //"Bearer" 제거 후 파싱
                    .getBody()
                    .getSubject(); //payLoad의 subject(username) 꺼내기
            if (user != null)
                return user;
        }
        return null;
    }
}
