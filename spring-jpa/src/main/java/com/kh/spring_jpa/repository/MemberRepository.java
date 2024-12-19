package com.kh.spring_jpa.repository;

import com.kh.spring_jpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // Spring Data JPA는 JpaRepository 인터페이스를 통해 기본적인 CRUD 및 페이징 기능 제공
    // 기본적인 CRUD는 이미 만들어져 있음

    // SELECT * FROM MEMBER WHERE EMAIL = ""와 같은 쿼리문을 자동으로 만들어줌
    // 이와 같은 Spring Data JPA의 메소드 작명 규칙을 배워야 함
    // 여기서 컬럼명을 잘못쓰면(Mail 등) 코드가 죽음 (Application run failed)
    boolean existsByEmail(String email);

    // Optional<T> : 값이 있을 수도 있고 없을 수도 있는 상황을 명시적으로 처리하기 위해 만들어진 코드
    // NullPointException(NPE) 방지
    // JPA의 findBy 메소드는 일반적으로 특정 조건을 만족하는 데이터 조회
    // 데이터가 없을 경우 null을 반환하는 대신, JPA는 Optional을 반환하는 이점을 가짐
    Optional<Member> findByEmail(String email);
    Optional<Member> findByPwd(String pwd);
    Optional<Member> findByEmailAndPwd(String email, String pwd);
}
