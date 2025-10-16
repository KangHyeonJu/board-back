package com.hyeonju.boardback.config;

import com.hyeonju.boardback.auth.AuthEntryPoint;
import com.hyeonju.boardback.auth.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//정책, 필터, 예외, 상태관리 정의
public class SecurityConfig {
    private final AuthenticationFilter authenticationFilter;
    private final AuthEntryPoint exceptionHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable()) // CSRF 비활성화 (JWT 사용 시 필요 없음)
                .cors(withDefaults()) // CORS 설정 허용 -> @Bean CorsConfigurationSource를 자동으로 찾아 사용, 빈이 없으면 디폴트
                .formLogin(form -> form.disable())   // ✅ 기본 /login 처리 필터 끄기
                // 세션 사용 안함 (서버 세션을 안 쓰고, 요청마다 JWT 이용)
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/login", "/member/new").permitAll()
                        .anyRequest().authenticated())
//                        .anyRequest().permitAll())

                // 커스텀 JWT 필터 등록
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 인증 실패 시 처리 핸들러 등록
                .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(exceptionHandler));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        //허용할 요청 Origin(출처, 도메인) 지정
        config.setAllowedOrigins(List.of("http://localhost:5173"));

        //허용할 HTTP 메서드 지정, CORS preflight(사전 검증 요청) 시 OPTIONS가 필수이므로 꼭 포함해야 함.
        //REST API에서 자주 쓰는 메서드 전부 허용
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));

        //클라이언트가 요청에 보낼 수 있는 헤더 지정.
        //JWT 토큰용 Authorization, JSON 요청용 Content-Type, Ajax 요청에서 흔히 쓰는 X-Requested-With
        config.setAllowedHeaders(List.of("Authorization","Content-Type","X-Requested-With"));

        // 서버 응답 헤더 중 브라우저 JavaScript에서 접근할 수 있는 헤더 지정.
        //기본적으로 Authorization은 노출되지 않음. 여기서 노출 설정 해줘야 React에서 로그인 응답 헤더에 담긴 토큰을 읽을 수 있음.
        config.setExposedHeaders(List.of("Authorization"));

        //true면 쿠키, 세션, 자격증명(Authorization 헤더 포함)을 함께 보낼 수 있음.
        //하지만 true인 경우 setAllowedOrigins("*") 같이 와일드카드는 허용 불가.
        //지금은 JWT 기반이라 쿠키를 쓰지 않고 헤더만 사용 -> false 유지해도 문제 없음
        config.setAllowCredentials(false);

        //Spring이 제공하는 기본 CORS설정을 추가 적용(직접 지정하지 않은 항목만 기본값으로 채워줌)
        config.applyPermitDefaultValues();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        //애플리케이션 모든 경로에 대해 위 설정 적용
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
