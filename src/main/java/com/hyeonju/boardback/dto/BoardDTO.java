package com.hyeonju.boardback.dto;

import com.hyeonju.boardback.entity.Board;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Getter
@Setter
public class BoardDTO {
    //게시물 id
    private Long boardId;

    //제목
    private String title;

    //본문
    private String content;

    //작성자
    private String writer;

    //등록일
    private LocalDate regDate;

    //수정일
    private LocalDate modDate;

    //조회수
    private Long views;

    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardDTO boardToDTO(Board board) {
        return modelMapper.map(board, BoardDTO.class);
    }
}
