package com.kh.spring_jpa.service;

import com.kh.spring_jpa.dto.MemberReqDto;
import com.kh.spring_jpa.dto.MemberResDto;
import com.kh.spring_jpa.entity.Member;
import com.kh.spring_jpa.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor // 생성자를 통한 의존성 주입을 받기 위해 LOMBOK을 통한 생성자 생성

public class MemberService {


    // 의존성 주입받기
    // 해당하는 리포지토리를 사용하려면 리포티토리 객체를 만들어줘야 하는데 스프링 컨테이너에 등록이 되어 있는 걸 쓰려고 의존성 주입받음
    private final MemberRepository memberRepository;


    // 회원 전체 조회
    // 전체 조회기 때문에 매개변수가 필요없음
    public List<MemberResDto> getAllMembers() {
        // 제네릭의 <T> 타입 변수 자리에 원하는 타입으로 반환 받겠다고 선언함
        // DB로부터 모든 회원 정보를 Member Entity 객체로 읽어 옴
        List<Member> members = memberRepository.findAll();
        // 프론트엔드에 정보를 전달하기 위해 DTO(Data Transfer Object) List를 생성
        List<MemberResDto> memberResDtoList = new ArrayList<>();
        // Member Entity로 구성된 List를 처음부터 끝까지 순회
        for (Member member : members) {
            // members만큼 순회하면서 Member List의 요소값인 member를 꺼냄
            // 꺼낸 요소들을 memberResDtoList에 추가해줌
            memberResDtoList.add(convertEntityToDto(member));
        }
        return memberResDtoList;
    }


    // 회원 정보 조회
    public MemberResDto getMemberResDetail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));
        return convertEntityToDto(member);
    }


    // 회원정보 수정
    public boolean modifyMember(MemberReqDto memberReqDto) {
        try {
            // 회원정보 수정하려면 유일한 키인 이메일이 있어야 함
            Member member = memberRepository.findByEmail(memberReqDto.getEmail())
                    .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));
            member.setName(memberReqDto.getName());
            member.setImgPath(memberReqDto.getImgPath());
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            log.error("회원 정보 수정 실패 : {}", e.getMessage());
            return false;
        }
    }


    // 회원 삭제
    public boolean deleteMember(String email) {
        try {
            // 회원 정보 조회 선행
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));
            memberRepository.delete(member);
            return true;
        } catch (Exception e) {
            log.error("회원 삭제 실패 : {}", e.getMessage());
            return false;
        }
    }


    // 공통 함수 만들기
    // Member Entity > MemberResDto로 변환
    private MemberResDto convertEntityToDto(Member member) {
        MemberResDto memberResDto = new MemberResDto();
        memberResDto.setEmail(member.getEmail());
        memberResDto.setName(member.getName());
        memberResDto.setImagePath(member.getImgPath());
        memberResDto.setRegDate(member.getRegDate());
        return memberResDto;
    }

}
