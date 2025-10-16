package com.hyeonju.boardback.controller;

import com.hyeonju.boardback.dto.NewBoardDTO;
import com.hyeonju.boardback.dto.UpdateBoardDTO;
import com.hyeonju.boardback.service.BoardService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    private static String lastRequest = null;

    public void requestURL(String currentRequest){
        lastRequest = currentRequest;
    }

    //게시물 등록
    @PostMapping("/new")
    public ResponseEntity<?> newBoard(@Valid @RequestBody NewBoardDTO boardDto, HttpServletRequest httpServletRequest) {
        try {
            requestURL(httpServletRequest.getRequestURI());

            Long boardId = boardService.newBoard(boardDto);

            Map<String, Object> response = new HashMap<>();
            response.put("boardId", boardId);
            response.put("message", "게시물이 성공적으로 등록되었습니다.");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "게시물 등록 중 오류가 발생했습니다."));
        }
    }

    //게시물 목록조회
    @GetMapping()
    public ResponseEntity<?> boardList(@RequestParam(required = false) Integer page, @RequestParam(required = false, defaultValue = "10") int size, HttpServletRequest httpServletRequest) {
        try {
            requestURL(httpServletRequest.getRequestURI());

            if (page == null) {
                return new ResponseEntity<>(boardService.boardList(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(boardService.boardListPaging(page, size), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "게시물 조회 중 오류가 발생했습니다."));
        }
    }

    //게시물 상세조회
    @GetMapping("/{boardId}")
    public ResponseEntity<?> boardDtl(@PathVariable Long boardId, HttpServletRequest httpServletRequest) {
        try {
            boolean isSameAsLastRequest = httpServletRequest.getRequestURI().equals(lastRequest);

            requestURL(httpServletRequest.getRequestURI());

            return new ResponseEntity<>(boardService.boardDtl(isSameAsLastRequest, boardId), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "해당 게시물을 찾을 수 없습니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "게시물 상세조회 중 오류가 발생했습니다."));
        }
    }


    //게시물 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<?> boardUpdate(@PathVariable Long boardId, @Valid @RequestBody UpdateBoardDTO boardDto, HttpServletRequest httpServletRequest) {
        try {
            requestURL(httpServletRequest.getRequestURI());

            boardService.updateBoard(boardId, boardDto);

            Map<String, Object> response = new HashMap<>();
            response.put("boardId", boardId);
            response.put("message", "게시물이 성공적으로 수정되었습니다.");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "해당 게시물을 찾을 수 없습니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "게시물 수정 중 오류가 발생했습니다."));
        }
    }

    //게시물 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> boardDelete(@PathVariable Long boardId, HttpServletRequest httpServletRequest) {
        try {
            requestURL(httpServletRequest.getRequestURI());

            boardService.deleteBoard(boardId);

            Map<String, Object> response = new HashMap<>();
            response.put("boardId", boardId);
            response.put("message", "게시물이 성공적으로 삭제되었습니다.");

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "해당 게시물을 찾을 수 없습니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "게시물 삭제 중 오류가 발생했습니다."));
        }
    }
}