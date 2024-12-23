package com.kh.spring_jpa.service;

import com.kh.spring_jpa.dto.CommentReqDto;
import com.kh.spring_jpa.dto.CommentResDto;
import com.kh.spring_jpa.entity.Board;
import com.kh.spring_jpa.entity.Comment;
import com.kh.spring_jpa.entity.Member;
import com.kh.spring_jpa.repository.BoardRepository;
import com.kh.spring_jpa.repository.CommentRepository;
import com.kh.spring_jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 로그 메시지 출력
@Slf4j

// 스프링 컨테이너(Bean)에 등록
@Service

// 생성자 자동 생성
@RequiredArgsConstructor

public class CommentService {


    // Member 객체의 정보를 가져오기 위해서 필요 (Member 객체에 대한 DB 접근)
    private final MemberRepository memberRepository;

    // Board 객체의 정보 가져오기 (게시글 정보, Board 객체에 대한 DB 접근)
    private final BoardRepository boardRepository;

    // 댓글 작성을 위해서 (댓글을 DB에 쓰기)
    private final CommentRepository commentRepository;

    // 댓글 등록
    @Transactional
    public boolean commentRegister(CommentReqDto commentReqDto) {
        try {

            // 멤버 객체를 만들어줌
            Member member = memberRepository.findByEmail(commentReqDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

            // 보드 객체를 만들어줌
            Board board = boardRepository.findById(commentReqDto.getBoardId())
                    .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

            Comment comment = new Comment();
            comment.setContent(commentReqDto.getContent());
            comment.setMember(member);
            comment.setBoard(board);

            // commentRepository에 comment 객체를 넣고 save해줌
            commentRepository.save(comment);

            return true;
        } catch (Exception e) {
            log.error("댓글 등록 실패 : {}", e.getMessage());
            return false;
        }
    }


    // 댓글 삭제 (id로)
    public boolean deleteComment(Long commentId, String email) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
            if (comment.getMember().getEmail().equals(email)) {
                commentRepository.delete(comment);
                log.info("댓글이 성공적으로 삭제되었습니다.");
                return true;
            } else {
                log.error("댓글은 작성자만 삭제할 수 있습니다.");
                return false;
            }
        } catch (Exception e) {
            log.error("댓글 삭제 실패 : {}", e.getMessage());
            return false;
        }
    }


    // 댓글 목록 조회 (게시글 id로)
    public List<CommentResDto> findByBoardId(Long boardId) {
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

            // Comment의 입장에서 해당 Board를 가져옴
            // 해당 Board에 대한 댓글 목록 가져오기
            List<Comment> commentList = commentRepository.findByBoard(board);
            List<CommentResDto> commentResDtoList = new ArrayList<>();
            for (Comment comment : commentList) {
                CommentResDto commentResDto = new CommentResDto();
                commentResDto.setEmail(comment.getMember().getEmail());
                commentResDto.setBoardId(comment.getBoard().getId());
                commentResDto.setCommentId(comment.getCommentId());
                commentResDto.setContent(comment.getContent());
                commentResDto.setRegDate(comment.getRegDate());
                commentResDtoList.add(commentResDto);
            }
            return commentResDtoList;
        } catch (Exception e) {
            log.error("댓글 목록 조회 실패 : {}", e.getMessage());
            return null;
        }

    }


}