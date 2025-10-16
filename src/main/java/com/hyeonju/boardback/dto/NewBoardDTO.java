package com.hyeonju.boardback.dto;

import com.hyeonju.boardback.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

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
//    private String writer;

    //등록일
    private LocalDate regDate = LocalDate.now();

    //조회수
    private Long viewCnt = 0L;

    private static ModelMapper modelMapper = new ModelMapper();

    public Board newBoard() {
        return modelMapper.map(this, Board.class);
    }
}
