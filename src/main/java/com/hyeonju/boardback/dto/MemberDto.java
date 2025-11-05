package com.hyeonju.boardback.dto;

import com.hyeonju.boardback.constant.Role;
import com.hyeonju.boardback.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter
@Setter
public class MemberDto {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Length(max = 50, message = "이메일은 50자 이하로 입력해주세요")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하로 입력해주세요")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Length(min = 2, max = 10, message = "닉네임은 2자 이상, 10자 이하로 입력해주세요")
    private String nickname;

    @NotNull(message = "생일은 필수 입력 값입니다.")
    @Past(message = "생일은 오늘보다 이전 날짜여야 합니다.")
    private LocalDate birthday;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Length(max = 30, message = "전화번호는 30자 이하로 입력해주세요")
    private String phoneNumber;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    @Length(max = 50, message = "주소는 50자 이하로 입력해주세요")
    private String address;

    @NotBlank(message = "상세주소는 필수 입력 값입니다.")
    @Length(max = 50, message = "상세주소는 50자 이하로 입력해주세요")
    private String detailAddress;

    public static Member to(MemberDto memberDto, String encodedPassword, Role role) {
        return new Member(
                memberDto.getEmail(),
                encodedPassword,
                memberDto.getNickname(),
                memberDto.getBirthday(),
                memberDto.getPhoneNumber(),
                memberDto.getAddress(),
                memberDto.getDetailAddress(),
                role
        );
    }
}
