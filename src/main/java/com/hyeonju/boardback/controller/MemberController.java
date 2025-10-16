package com.hyeonju.boardback.controller;

import com.hyeonju.boardback.dto.MemberDto;
import com.hyeonju.boardback.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/new")
    public ResponseEntity<?> newMember(@Valid @RequestBody MemberDto memberDto) {
        Long memberId = memberService.newMember(memberDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("memberId", memberId, "message", "회원이 성공적으로 등록되었습니다."));
    }
}
