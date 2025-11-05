package com.hyeonju.boardback.service;

import com.hyeonju.boardback.dto.BoardDTO;
import com.hyeonju.boardback.dto.NewBoardDTO;
import com.hyeonju.boardback.dto.UpdateBoardDTO;
import com.hyeonju.boardback.entity.Board;
import com.hyeonju.boardback.entity.Member;
import com.hyeonju.boardback.repository.BoardRepository;
import com.hyeonju.boardback.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    //게시물 등록
    @Transactional
    public Long newBoard(NewBoardDTO boardDto) {
        Member member = memberRepository.findById(boardDto.getMemberId()).orElseThrow(EntityNotFoundException::new);
        Board newBoard = NewBoardDTO.toEntity(boardDto,  member);

        Board board = boardRepository.save(newBoard);
        return board.getId();
    }

    //게시물 목록조회
    public List<BoardDTO> boardList() {
        List<Board> boardList = boardRepository.findAllByOrderByIdDesc();

        return boardList.stream().map(BoardDTO::from).toList();
    }

    //게시물 목록조회 페이징
    public Map<String, Object> boardListPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boardList = boardRepository.findAllByOrderByIdDesc(pageable);

        List<BoardDTO> boardDTOList = boardList.stream().map(BoardDTO::from).toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", boardDTOList);
        response.put("page", page);
        response.put("size", size);
        response.put("totalPages", boardList.getTotalPages());
        response.put("totalElements", boardList.getTotalElements());

        return response;
    }

    //게시물 상세조회
    public BoardDTO boardDtl(boolean isSameAsLastRequest, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

        //조회수 증가
        if (!isSameAsLastRequest) {
            board.setViewCnt(board.getViewCnt() + 1);
            boardRepository.save(board);
        }

        return BoardDTO.from(board);
    }

    //게시물 수정
    @Transactional
    public void updateBoard(Long boardId, UpdateBoardDTO updateBoardDTO) {
        Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

        board.setTitle(updateBoardDTO.getTitle());
        board.setContent(updateBoardDTO.getContent());
        board.setModDate(LocalDate.now());

        boardRepository.save(board);
    }

    //게시물 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

        boardRepository.delete(board);
    }
}
