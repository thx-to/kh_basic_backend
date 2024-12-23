package com.kh.spring_jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter

// 문자열로 변환할 때 객체가 들어오면 객체 부분을 제외 (순환 참조 방지)
// ToString을 걸다보면 순환 참조가 자주 발생함
// comment에서 board 객체를 바라보는데 board객체에서 다시 comment를 바라보면 무한 루프에 빠질 수 있음
@ToString(exclude = {"board", "member"})
@NoArgsConstructor

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId; // comment_id, 컬럼 이름을 따로 지정해주지 않으려면 @Column 어노테이션을 쓰지 않아도 됨

    // many-to-one 관계 걸기, 여러개의 댓글을 한개의 게시물에 매핑
    // board의 기본키인 board_id로 연관 관계 매핑
    // 게시글에 대한 정보가 없는데 댓글이 써지면 안되니까, 게시글 정보를 불러옴
    // board 객체에서 바라볼 때는 OneToMany의 관계
    // board 객체에서 OneToMany를 걸어주지 않는 이유는 게시글 작성시에는 댓글이 꼭 필요하지 않기 때문
    // 댓글에 대한 정보가 들어오긴 하지만, 있는 정보만 끌어와야 함
    // OneToMany로 한다면 ArrayList로 댓글을 당겨와야 하지만 지금은 비어 있는 상태
    // 이러한 이유 등으로 실제로 많은 경우에서 OneToMany의 관계는 걸 필요가 없음 (ManyToOne 관계만 있으면 됨)
    // 게시글에서 가져오는게 유리하냐, 댓글쪽에서 가져오는게 유리하냐의 문제
    @ManyToOne
    @JoinColumn(name = "board_id") // 참조 키 : 해당 객체의 기본키여야 함
    private Board board;

    // 댓글을 쓰려면 댓글을 쓰는 사람이 있어야 함
    // 한명당 여러개의 댓글을 쓸 수 있음
    // 댓글을 쓸 때 들어와야 할 참조 객체는 두개 (board와 member)
    @ManyToOne
    @JoinColumn(name = "member_id") // 참조 키 : 해당 객체의 기본키여야 함
    private Member member;

    // DB 확인해보니 그냥 length=1000을 지정해주면 varchar(1000)으로 출력
    // @Lob 어노테이션 붙이니 longtext로 출력
    @Column(length = 1000)
    private String content;

    @Column(nullable = false, updatable = false) // 댓글 수정시에도 regDate는 업데이트되지 않게
    private LocalDateTime regDate;

    // JPA의 콜백(등록해두면 자동으로 호출) 메소드, 엔티티가 저장되기 전에 실행
    // DB에 삽입되기 전에 자동 설정
    // 데이터 삽입시 onCreate가 자동으로 불려짐 (현재 시간을 regDate에 자동으로 넣어줌)
    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }

}
