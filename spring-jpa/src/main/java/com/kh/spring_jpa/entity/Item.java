package com.kh.spring_jpa.entity;

import com.kh.spring_jpa.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // 해당 클래스가 엔티티임을 나타냄 (데이터베이스 테이블을 의미)
@Table(name = "item") // 테이블 이름은 넣어주지 않아도 되지만, 명시적으로 처리
@Getter @Setter @ToString

public class Item {

    @Id // 기본 키 필드 지정
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNum; // 상품명

    @Column(nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고 수량

    @Lob // 대용량 데이터 매핑, 오라클에서는 사용 불가
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    // Enum 타입 매핑
    // 사전 지정해준(constant/ItemSellStatus.java) SELL이나 SOLD_OUT 둘 중 하나의 값만 올 수 있음
    // Enum Type 열거형 특징 1) 타입 제한, 제한된 상수값 사용
    // Enum Type 열거형 특징 2) 순서, 몇번째 순서인지
    // STRING : 문자열 그대로 DB에 저장
    // ORDINARY : 순서적인 의미를 가짐 (몇번째 순서인지)
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    private LocalDateTime regTime; // 등록 시간
    private LocalDateTime updateTime; // 수정 시간

}
