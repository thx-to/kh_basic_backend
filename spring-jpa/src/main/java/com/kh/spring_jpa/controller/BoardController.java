package com.kh.spring_jpa.controller;

import com.kh.spring_jpa.dto.BoardReqDto;
import com.kh.spring_jpa.dto.BoardResDto;
import com.kh.spring_jpa.repository.BoardRepository;
import com.kh.spring_jpa.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor

public class BoardController {

    // 의존성 주입받기
    private final BoardService boardService;

    // 게시글 등록
    @PostMapping("/new")
    public ResponseEntity<Boolean> newBoard(@RequestBody BoardReqDto boardReqDto) {
        boolean isSuccess = boardService.saveBoard(boardReqDto);
        return ResponseEntity.ok(isSuccess);
    }


    // 게시글 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<BoardResDto> findByBoardId(@PathVariable Long id) {
        BoardResDto boardResDto = boardService.findByBoardId(id);
        return ResponseEntity.ok(boardResDto);
    }


    // 게시글 전체 조회
    @GetMapping("")
    public ResponseEntity<List<BoardResDto>> boardList() {
        List<BoardResDto> boardResDtoList = boardService.findAllBoard();
        return ResponseEntity.ok(boardResDtoList);
    }


    // 게시글 검색
    @GetMapping("/search-title")
    public ResponseEntity<List<BoardResDto>> searchBoardTitle(@RequestBody String keyword) {
        List<BoardResDto> boardResDtoList = boardService.searchBoard(keyword);
        return ResponseEntity.ok(boardResDtoList);
    }

    // 게시글 페이징을 위한 페이지 수 조회 (선행 작업)
    @GetMapping("/count")
    public ResponseEntity<Integer> boardPageCount(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Integer pageCount = boardService.getBoardPageCount(page, size);
        return ResponseEntity.ok(pageCount);
    }


    // 게시글 페이징 (페이지 단위로 나누기)
    // 리액트에서 key와 value 형태로 page와 size를 url에 넣게 구성(?)
    @GetMapping("/list/page")
    public ResponseEntity<List<BoardResDto>> pagingBoardList(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        List<BoardResDto> boardResDtoList = boardService.pagingBoardList(page, size);
        return ResponseEntity.ok(boardResDtoList);
    }


    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> boardDelete(@PathVariable Long id,
                                               @RequestParam String email) {
        boolean isSuccess = boardService.deleteBoard(id, email);
        return ResponseEntity.ok(isSuccess);
    }


    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> boardModify(@PathVariable Long id, @RequestBody BoardReqDto boardReqDto) {
        boolean isSuccess = boardService.modifyBoard(id, boardReqDto);
        return ResponseEntity.ok(isSuccess);
    }


    // 게시글 검색 (제목 OR 내용)
    @GetMapping("/search-title-content")
    public ResponseEntity<List<BoardResDto>> boardSearchTitleOrContent(@RequestParam String title, @RequestParam String content) {
        List<BoardResDto> boardResDtoList = boardService.searchSpecificBoard(title, content);
        return ResponseEntity.ok(boardResDtoList);
    }

}
