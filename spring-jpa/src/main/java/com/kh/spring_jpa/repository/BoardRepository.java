package com.kh.spring_jpa.repository;


import com.kh.spring_jpa.entity.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitleContaining(String keyword); // 제목 검색
    List<Board> findByTitleContainingOrContentContaining(String title, String content);

    // 목록을 한번에 읽어오게 하는 어노테이션
    // JPA Repository의 메소드와 함께 사용됨, 엔티티를 조회할 때 특정 연관 관계를 명시적으로 설정
    // attributePaths : 즉시 로딩할 연관 속성(필드) 지정
    @EntityGraph(attributePaths = "comments")
    Optional<Board> findById(Long id);
}
