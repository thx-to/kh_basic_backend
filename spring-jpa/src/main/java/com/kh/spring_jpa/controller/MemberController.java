package com.kh.spring_jpa.controller;

import com.kh.spring_jpa.dto.MemberReqDto;
import com.kh.spring_jpa.dto.MemberResDto;
import com.kh.spring_jpa.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor // 생성자 자동 생성

public class MemberController {


    private final MemberService memberService;


    // 회원 전체 조회
    @GetMapping("/memberlist")
    public ResponseEntity<List<MemberResDto>> allMembers() {
        List<MemberResDto> memberResDtoList = memberService.getAllMembers();
        return ResponseEntity.ok(memberResDtoList);
    }


    // 회원 정보 조회
    @GetMapping("/{email}")
    public ResponseEntity<MemberResDto> memberResDetail(@PathVariable String email) {
        MemberResDto memberResDto = memberService.getMemberResDetail(email);
        return ResponseEntity.ok(memberResDto);
    }


    // 회원 수정
    // Put도 Post와 같은 방식이라 @RequestBody 사용
    @PutMapping("/modify")
    public ResponseEntity<Boolean> memberModify(@RequestBody MemberReqDto memberReqDto) {
        boolean isSuccess = memberService.modifyMember(memberReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 회원 삭제
    @DeleteMapping("/{email}")
    public ResponseEntity<Boolean> memberDelete(@PathVariable String email) {
        boolean isSuccess = memberService.deleteMember(email);
        return ResponseEntity.ok(isSuccess);
    }

}
