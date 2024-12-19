package com.kh.spring_jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

// 게시글에 관한 엔티티

@Entity
@Table(name="board")
@Getter @Setter @ToString
@NoArgsConstructor

public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title; // 글 제목

    @Lob
    @Column(length=1000)
    private String content; // 글 내용

    private String imgPath; // 게시글 이미지 경로
    private LocalDateTime regDate; // 게시글 등록 일자

    // JPA의 콜백(등록해두면 자동으로 호출) 메소드, 엔티티가 저장되기 전에 실행
    // DB에 삽입되기 전에 자동 설정
    // 데이터 삽입시 onCreate가 자동으로 불려짐 (현재 시간을 regDate에 자동으로 넣어줌)
    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }

    // 하나의 회원은 여러개 개시글을 쓰고, 하나의 게시글은 하나의 회원만 가짐
    // Join : 객체에서 다른 객체를 참조 (게시글에서 Member 객체를 참조하는게 Join, 있는 객체를 바라봐야 함
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


}
