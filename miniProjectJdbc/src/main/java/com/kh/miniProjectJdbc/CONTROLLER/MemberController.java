package com.kh.miniProjectJdbc.CONTROLLER;


import com.kh.miniProjectJdbc.DAO.MemberDAO;
import com.kh.miniProjectJdbc.VO.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberDAO memberDAO;
    // 전체 회원 조회
    @GetMapping
    public ResponseEntity<List<MemberVO>> memberList() {
        List<MemberVO> list = memberDAO.memberList();
        return ResponseEntity.ok(list);
    }
    // 이메일 또는 이름으로 멤버 조회
    @GetMapping("/search")
    public ResponseEntity<List<MemberVO>> getMemberByEmailOrName(@RequestParam(required = false) String email,
                                                                 @RequestParam(required = false) String name) {
        log.info("이메일 : {}, 이름 {}", email, name);
        List<MemberVO> list = memberDAO.findMemberByEmailOrName(email, name);
        return ResponseEntity.ok(list);
    }

    // 회원 정보 수정
    @PutMapping("/{email}")
    public ResponseEntity<Boolean> updateMember(@PathVariable String email, @RequestBody MemberVO VO) {
        log.error("이메일 : {}", email);
        log.error("멤버 : {}", VO);
        boolean isSuccess = memberDAO.updateMember(email, VO);
        return ResponseEntity.ok(isSuccess);
    }

    // 회원 삭제
    @DeleteMapping("/{email}")
    public ResponseEntity<Boolean> deleteMember(@PathVariable String email) {
        boolean isSuccess = memberDAO.deleteMember(email);
        return ResponseEntity.ok(isSuccess);
    }

}

