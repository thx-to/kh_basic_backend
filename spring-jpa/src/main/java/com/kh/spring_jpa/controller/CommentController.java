package com.kh.spring_jpa.controller;

import com.kh.spring_jpa.dto.CommentReqDto;
import com.kh.spring_jpa.dto.CommentResDto;
import com.kh.spring_jpa.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor

public class CommentController {

    // 의존성 주입받기
    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/new")
    public ResponseEntity<Boolean> newComment(@RequestBody CommentReqDto commentReqDto) {
        boolean isSuccess = commentService.commentRegister(commentReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 댓글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Boolean> commentDelete(@PathVariable Long id,
                                                 @PathVariable String email) {
        boolean isSuccess = commentService.deleteComment(id, email);
        return ResponseEntity.ok(isSuccess);
    }

//    // 댓글 목록 조회 (게시글 id로 조회)
//    @GetMapping("")
//    public ResponseEntity<List<CommentResDto>> commentList(@PathVariable Long boardId) {
//        List<CommentResDto> commentResDtoList = commentService.findByBoardId(boardId);
//            return ResponseEntity.ok(commentResDtoList);
//    }

}
