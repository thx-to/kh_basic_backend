package com.kh.spring_jpa.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity // 해당 클래스가 엔티티임을 나타내는 엔티티 매핑 어노테이션

// Java는 Camel Case, DB는 대소문자 구분하지 않음
// JPA가 Member 클래스를 자동으로 member로 변경해줌
// regData의 경우에는 reg_data로 자동 변환
@Table(name="member") // 테이블 이름 설정
@ToString // 자동 오버라이딩, 이 내용에 대해 출력할 때 오버라이딩해줌


public class Member {

    @Id // 기본 키 필드 지정
    @Column(name="member_id")
    // Oracle은 생성 전략이 없음(시퀀스는 별도로 만들어줘야 하는 등)
    // MySQL에서는 JPA가 자동으로 만들어줌(auto)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Primary Key

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String pwd;

    private String name;
    private LocalDateTime regDate;
    private String imgPath;

}
