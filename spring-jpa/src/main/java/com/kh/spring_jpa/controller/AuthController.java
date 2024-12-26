package com.kh.spring_jpa.controller;

import com.kh.spring_jpa.dto.LoginReqDto;
import com.kh.spring_jpa.dto.MemberReqDto;
import com.kh.spring_jpa.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor // 생성자 자동 생성

public class AuthController {


    // 의존성 주입받기
    private final AuthService authService;


    // 회원가입 여부 확인
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> isMember(@PathVariable String email) {
        boolean isTrue = authService.isMember(email);
        return ResponseEntity.ok(!isTrue); // 존재하면 가입하면 안되니까 not(!)을 붙여줌
    }


    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Boolean> signUp(@RequestBody MemberReqDto memberReqDto) {
        boolean isSuccess = authService.signUp(memberReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody LoginReqDto loginReqDto) {
        boolean isSucceess = authService.login(loginReqDto);
        return ResponseEntity.ok(isSucceess);
    }


}
