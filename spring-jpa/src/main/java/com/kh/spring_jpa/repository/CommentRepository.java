package com.kh.spring_jpa.repository;

import com.kh.spring_jpa.entity.Board;
import com.kh.spring_jpa.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository <Comment, Long> {

    // Comment 타입의 List로 반환받기
    // Comment 엔티티에 Board가 참조 객체로 들어가 있어서 이렇게 쓸 수 있음
    List<Comment> findByBoard(Board board);

}
