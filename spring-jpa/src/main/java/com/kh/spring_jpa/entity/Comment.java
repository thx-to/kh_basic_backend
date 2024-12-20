package com.kh.spring_jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId; // comment_id, 컬럼 이름을 따로 지정해주지 않으려면 @Column 어노테이션을 쓰지 않아도 됨

    // many-to-one 관계 걸기, 여러개의 댓글을 한개의 게시물에 매핑
    // board의 기본키인 board_id로 연관 관계 매핑
    // 게시글에 대한 정보가 없는데 댓글이 써지면 안되니까, 게시글 정보를 불러옴
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    // 댓글을 쓰려면 댓글을 쓰는 사람이 있어야 함
    // 한명당 여러개의 댓글을 쓸 수 있음
    // 댓글을 쓸 때 들어와야 할 참조 객체는 두개 (board와 member)
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // DB 확인해보니 그냥 length=1000을 지정해주면 varchar(1000)으로 출력
    // @Lob 어노테이션 붙이니 longtext로 출력
    @Column(length = 1000)
    private String content;

    private LocalDateTime regDate;

    // JPA의 콜백(등록해두면 자동으로 호출) 메소드, 엔티티가 저장되기 전에 실행
    // DB에 삽입되기 전에 자동 설정
    // 데이터 삽입시 onCreate가 자동으로 불려짐 (현재 시간을 regDate에 자동으로 넣어줌)
    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }

}
