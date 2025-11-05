package com.hyeonju.boardback.service;

import com.hyeonju.boardback.constant.Role;
import com.hyeonju.boardback.dto.MemberDto;
import com.hyeonju.boardback.entity.Member;
import com.hyeonju.boardback.handler.DuplicateEmailException;
import com.hyeonju.boardback.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long newMember(MemberDto memberDto) {
        if(memberRepository.existsByEmail(memberDto.getEmail())){
            throw new DuplicateEmailException(memberDto.getEmail());
        }
        String encodedPassword = passwordEncoder.encode(memberDto.getPassword());
        Role defaultRole = Role.USER;

        Member newMember = MemberDto.to(memberDto, encodedPassword, defaultRole);

        memberRepository.save(newMember);
        return newMember.getId();
    }
}
