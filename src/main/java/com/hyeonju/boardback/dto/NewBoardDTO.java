package com.hyeonju.boardback.dto;

import com.hyeonju.boardback.entity.Board;
import com.hyeonju.boardback.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter
@Setter
public class NewBoardDTO {
    //제목
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Length(min = 2, max = 50, message = "제목은 2자 이상, 50자 이하로 입력해주세요")
    private String title;

    //본문
    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Length(min = 2, max = 1500, message = "내용은 2자 이상, 1500자 이하로 입력해주세요")
    private String content;

    //작성자
    private Member member;

    //등록일
    private LocalDate regDate = LocalDate.now();

    public static Board to(NewBoardDTO dto) {
        return new Board(
                dto.getTitle(),
                dto.getContent(),
                dto.getMember(),
                dto.getRegDate()
        );
    }
}
