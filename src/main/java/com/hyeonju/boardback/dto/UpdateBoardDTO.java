package com.hyeonju.boardback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UpdateBoardDTO {
    //제목
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Length(min = 2, max = 50, message = "제목은 2자 이상, 50자 이하로 입력해주세요")
    private String title;

    //본문
    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Length(min = 2, max = 1500, message = "내용은 2자 이상, 1500자 이하로 입력해주세요")
    private String content;
}
