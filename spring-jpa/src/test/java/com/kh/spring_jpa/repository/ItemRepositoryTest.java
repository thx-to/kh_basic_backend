package com.kh.spring_jpa.repository;

import com.kh.spring_jpa.constant.ItemSellStatus;
import com.kh.spring_jpa.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.time.LocalDateTime;
import java.util.List;

import static com.kh.spring_jpa.constant.ItemSellStatus.SELL;

@Slf4j
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 테스트 실행 순서 지정

class ItemRepositoryTest {

    // 어노테이션 안먹을 경우에 필드로 생성자 주입
    @Autowired
    ItemRepository itemRepository;

    @Test
    @Order(1) // 테스트 실행 순서 지정
    @DisplayName("상품 저장 테스트") // 테스트 이름 지정
    public void createItemTest() {

        // for문으로 10개의 상품 생성
        for(int i = 1; i <= 10; i++) {
            Item item = new Item(); // Item 만들기(빈 객체)
            item.setItemNum("테스트 상품" + i);
            item.setPrice(1000 * i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            if (i % 2 == 0) item.setItemSellStatus(SELL);
            else item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now()); // 현재 시간
            item.setUpdateTime(LocalDateTime.now());

            Item savedItem = itemRepository.save(item);

            log.info("Item 저장 : {}", savedItem);
        }
    }


    @Test
    @Order(2)
    @DisplayName("상품 조회 테스트")
    public void findByItemNumTest() {

        // 메소드 개별 실행시 각 메소드에 상품 생성해주기
        // 위 createItemTest 메소드를 불러와 for문으로 10개의 상품 생성
        // this.createItemTest();

        List<Item> itemList = itemRepository.findByItemNum("테스트 상품5");

        for(Item item : itemList) {
            log.info("상품 조회 테스트 : {}", item);
        }

    }


    @Test
    @Order(3)
    @DisplayName("상품명이나 상세 설명으로 조회하기 (OR)")
    public void findByItemNumOrItemDetailTest() {

        // this.createItemTest();

        List<Item> itemList = itemRepository.findByItemNumOrItemDetail("테스트 상품3", "테스트 상품 상세 설명3");

        for(Item item : itemList) {
            log.info("상품명이나 상세 설명으로 조회하기 (OR) : {}", item);
        }

    }


    @Test
    @Order(4)
    @DisplayName("설정한 가격 : 5000 미만의 상품 조회하기")
    public void findByPriceLessThan() {

        // this.createItemTest();

        List<Item> itemList = itemRepository.findByPriceLessThan(5000);

        for(Item item : itemList) {
            log.info("설정한 가격 : 5000 미만의 상품 조회하기 : {}", item);
        }

    }


    @Test
    @Order(5)
    @DisplayName("설정한 가격 : 5000 이하의 상품 조회하기")
    public void findByPriceLessThanEqual() {

        // this.createItemTest();

        List<Item> itemList = itemRepository.findByPriceLessThanEqual(5000);

        for(Item item : itemList) {
            log.info("설정한 가격 : 5000 이하의 상품 조회하기 : {}", item);
        }

    }


    @Test
    @Order(6)
    @DisplayName("가격이 5000 이상이고 판매 중인 상품만 조회하기")
    public void findByPriceGreaterThanEqualAndItemSellStatus() {

        // this.createItemTest();

        int price = 5000;
        ItemSellStatus itemSellStatus = SELL;

        List<Item> itemList = itemRepository.findByPriceGreaterThanEqualAndItemSellStatus(price, itemSellStatus);

        for(Item item : itemList) {
            log.info("가격이 5000 이상이고 판매 중인 상품만 조회하기 : {}", item);
        }

    }


    @Test
    @Order(7)
    @DisplayName("상품 가격에 대한 내림차순 정렬")
    public void findAllByOrderByPriceDesc() {

        // this.createItemTest();

        List<Item> itemList = itemRepository.findAllByOrderByPriceDesc();

        for(Item item : itemList) {
            log.info("상품 가격에 대한 내림차순 정렬 : {}", item);
        }

    }


    @Test
    @Order(8)
    @DisplayName("상품 이름에 특정 키워드가 포함된 상품 검색")
    public void findByItemNumContaining() {

        // this.createItemTest();

        List<Item> itemList = itemRepository.findByItemNumContaining("7");

        for(Item item : itemList) {
            log.info("상품 이름에 특정 키워드가 포함된 상품 검색 : {}", item);
        }

    }


    @Test
    @Order(9)
    @DisplayName("상품명과 가격이 일치하는 상품 검색 (AND)")
    public void findByItemNumAndPrice() {

        // this.createItemTest();

        List<Item> itemList = itemRepository.findByItemNumAndPrice("테스트 상품8", 8000);

        for(Item item : itemList) {
            log.info("상품명과 가격이 일치하는 상품 검색 (AND) : {}", item);
        }

    }


    @Test
    @Order(10)
    @DisplayName("가격 범위에 해당하는 조건 검색 (3000 ~ 7000)")
    public void findByPriceBetween() {

        // this.createItemTest();

        List<Item> itemList = itemRepository.findByPriceBetween(3000, 7000);

        for(Item item : itemList) {
            log.info("가격 범위에 해당하는 조건 검색 (3000 ~ 7000) : {}", item);
        }
    }

    @Test
    @Order(11)
    @DisplayName("✨ JPQL TEST")
    public void findByItemDetail() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetail("설명1");
        for(Item item : itemList) {
            log.info("👉 JPQL TEST RESULT : {}", item);
        }
    }

    @Test
    @Order(12)
    @DisplayName("✨ nativeQuery TEST")
    public void findByItemDetailByNative() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetailByNative("설명1");
        for(Item item : itemList) {
            log.info("👉 nativeQuery TEST RESULT : {}", item);
        }
    }

}