package com.kh.spring_jpa.service;

// 비즈니스 로직 만들기

import com.kh.spring_jpa.dto.LoginReqDto;
import com.kh.spring_jpa.dto.MemberReqDto;
import com.kh.spring_jpa.entity.Member;
import com.kh.spring_jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


// 로그 출력용
@Slf4j

// 스프링 컨테이너에 해당하는 빈(객체, 서비스)를 등록
// 누군가가 의존성 주입만 받아 사용할 수 있게
// 스프링 컨테이너가 생명주기 관리
@Service

// 생성자 자동 생성
@RequiredArgsConstructor

// 트랜잭션 : 여러 개의 작업을 하나의 논리적인 단위로 묶어줌
// 특히 동시에 여러가지 일들이 일어나고 모든게 성공했을때 응답을 줘야 하는 경우에 사용
@Transactional


// 인증과 인가 때문에 로그인/회원가입만을 위한 서비스 메소드를 따로 구성
public class AuthService {


    // 생성자를 통한 의존성 주입
    // 생성자를 통해 의존성 주입을 받는 경우에는 @Autowired 생략
    private final MemberRepository memberRepository;


    // 회원가입 여부 확인
    // isMember를 컨트롤러가 불러주면 됨
    public boolean isMember(String email) {
        // 해당 이메일이 존재하면 true, 없으면 false 반환
        return memberRepository.existsByEmail(email);
    }


    // 회원가입
    public boolean signUp(MemberReqDto memberReqDto) {
        // 서버에 다녀 와야 하니까 오류 발생 가능성이 있어 try ~ catch구문으로 넣어줌
        try {
            // member 엔티티(DB 테이블과 같음) 만들기
            Member member = convertDtoToEntity(memberReqDto);
            // save()는 insert와 update 역할을 하는 메소드로 JpaRepository 안에 있음
            // 기본적인 CRUD는 상속 관계를 통해 이미 다 만들어져 있음
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            log.error("회원 가입 실패 : {}", e.getMessage());
            return false;
        }
    }


    // 로그인
    public boolean login(LoginReqDto loginReqDto) {
        try {
            // null을 방지하기 위해 Member 객체에 껍데기(?)를 하나 매핑함
            Optional<Member> member = memberRepository
                    .findByEmailAndPwd(loginReqDto.getEmail(), loginReqDto.getPwd());
            return member.isPresent(); // 해당 객체가 존재함을 의미, 있으면 true 없으면 false
        } catch (Exception e) {
            log.error("로그인 실패 : {}", e.getMessage());
            return false;
        }
    }


    // 회원가입 DTO를 ENTITY(VO 역할)로 변환
    private Member convertDtoToEntity(MemberReqDto memberReqDto) {
        Member member = new Member();
        member.setEmail(memberReqDto.getEmail());
        member.setName(memberReqDto.getName());
        member.setPwd(memberReqDto.getPwd());
        return member;
    }


}
