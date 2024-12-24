package com.kh.spring_jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // OneToMany로 걸어주기 TEST
    // mappedBy를 쓰면 실제 DB에는 만들어지지 않음, 주인이 아님을 의미함
    // 실제로 만들어지는건 ManyToOne, 한쪽에서만 만들면 되고 만들어진걸 참조(객체를 참조만 함)
    // 영속성 전이 : 부모 엔티티의 상태 변화가 자식 엔티티에도 전이되는 것
    // 고아 객체 제거 : 부모와의 연관 관계가 끊어진 자식 엔티티를 자동으로 데이터베이스에서 제거하는 것
    // 부모가 관리하는 List에서 해당 객체를 삭제하는 경우 실제 DB에서 삭제됨
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setBoard(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setBoard(null);
    }



}
