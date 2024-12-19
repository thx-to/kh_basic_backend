package com.kh.spring_jpa.repository;

import com.kh.spring_jpa.constant.ItemSellStatus;
import com.kh.spring_jpa.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 상품명으로 조회하기
    List<Item> findByItemNum(String itemNum);

    // 상품명이나 상품 상세 설명으로 조회하기 (OR)
    List<Item> findByItemNumOrItemDetail(String itemNum, String itemDetail);

    // 설정한 가격 : 5000 미만의 상품 조회하기
    List<Item> findByPriceLessThan(int price);

    // 설정한 가격 : 5000 이하의 상품 조회하기
    List<Item> findByPriceLessThanEqual(int price);

    // 상품 판매 상태를 짝수는 SELL, 홀수는 SOLD_OUT으로 변경
    // 가격이 5000 이상이고 판매 중인 상품만 조회하기
    List<Item> findByPriceGreaterThanEqualAndItemSellStatus(int price, ItemSellStatus itemSellStatus);

    // 상품 가격에 대한 내림차순 정렬
    List<Item> findAllByOrderByPriceDesc();

    // 상품 이름에 특정 키워드가 포함된 상품 검색
    List<Item> findByItemNumContaining(String itemNum);

    // 상품명과 가격이 일치하는 상품 검색 (AND)
    List<Item> findByItemNumAndPrice(String itemNum, int price);

    // 가격 범위에 해당하는 조건 검색 (3000 ~ 7000)
    List<Item> findByPriceBetween(int minprice, int maxprice);

    // JPQL(Java Persistence Query Language) : 객체 지향 쿼리 언어, 테이블이 아닌 엔티티 속성에 대해 작동
    // FROM 테이블은 클래스명, WHERE 등 기준은 컬럼명이 아닌 엔티티에 있는 필드 이름(itemDetail 등)
    @Query("SELECT i FROM Item i WHERE i.itemDetail LIKE %:itemDetail% ORDER BY i.price DESC")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    // nativeQuery 사용하기
    // FROM 테이블은 DB 테이블명
    // @Param은 Java 코드를 직접 잡아 주는 부분이라 그 부분만(LIKE %:itemDetail%) 자바 코드로 작성
    @Query(value = "SELECT * FROM ITEM i WHERE i.ITEM_DETAIL LIKE %:itemDetail% ORDER BY i.PRICE DESC", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}


