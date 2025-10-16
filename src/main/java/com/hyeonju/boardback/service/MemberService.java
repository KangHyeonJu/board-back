package com.hyeonju.boardback.service;

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
        Member newMember = memberDto.newMember();

        String encodedPassword = passwordEncoder.encode(memberDto.getPassword());
        newMember.setPassword(encodedPassword);

        memberRepository.save(newMember);
        return newMember.getId();
    }
}
