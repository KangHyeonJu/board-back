package com.hyeonju.boardback.dto;

import com.hyeonju.boardback.entity.Board;
import com.hyeonju.boardback.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class BoardDTO {
    //게시물 id
    private Long boardId;

    //제목
    private String title;

    //본문
    private String content;

    //작성자
    private Member member;

    //등록일
    private LocalDate regDate;

    //수정일
    private LocalDate modDate;

    //조회수
    private Long viewCnt;

    public static BoardDTO from(Board board) {
        return new BoardDTO(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getMember(),
                board.getRegDate(),
                board.getModDate(),
                board.getViewCnt()
        );
    }
}
