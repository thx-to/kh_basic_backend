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

    // 제약 조건 걸어주기
    // 길이 default는 255 (지정해주지 않았을 때)
    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 15)
    private String pwd;

    @Column(length = 50)
    private String name;

    @Column(name="register_date") // 지정해주고 싶은 특정 컬럼명이 있으면 지정
    private LocalDateTime regDate; // DB로 갈 때 자동 REG_DATE로 감 (Camel > Snake)

    @Column(name = "image_path")
    private String imgPath;

    // JPA의 콜백(등록해두면 자동으로 호출) 메소드, 엔티티가 저장되기 전에 실행
    // DB에 삽입되기 전에 자동 설정
    // 데이터 삽입시 onCreate가 자동으로 불려짐 (현재 시간을 regDate에 자동으로 넣어줌)
    @PrePersist
    private void onCreate() {
        this.regDate = LocalDateTime.now();
    }

}
