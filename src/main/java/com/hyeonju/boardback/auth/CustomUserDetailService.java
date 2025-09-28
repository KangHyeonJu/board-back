package com.hyeonju.boardback.auth;

import com.hyeonju.boardback.entity.Member;
import com.hyeonju.boardback.reposiroty.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
//로그인 과정에서 사용
//사용자가 /login 같은 엔드포인트에서 아이디+비밀번호를 입력하면 스프링 시큐리티는 UserDetailsService를 호출해서 DB에 사용자가 존해하는지, 비밀번호가 맞는지 확인
//이때 반환하는 UserDetails는 DB에 저장된 사용자 정보를 담고 있음
//즉, 최초 로그인+토큰 발급 시점에 반드시 필요
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //1. DB에서 email로 Member 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // 2. Role → GrantedAuthority 변환
        // GrantedAuthority: Spring Security에서 권한을 나타내는 인터페이스(권한 객체)
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + member.getRole().name());

        // 3. UserDetails 반환
        // Collections.singletonList(authority) -> 권한 1개짜리 리스트(반환 타입 맞추기용)
        return new User(member.getEmail(), member.getPassword(), Collections.singletonList(authority));
    }
}
