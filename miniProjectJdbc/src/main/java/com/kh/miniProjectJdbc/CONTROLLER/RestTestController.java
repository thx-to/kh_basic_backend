package com.kh.miniProjectJdbc.CONTROLLER;

import com.kh.miniProjectJdbc.VO.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j // 디버깅 메시지나 시스템 정보 출력시 등급별로 로그 메시지 출력, 쌓기 가능
@RequestMapping("/api/get-api")
public class RestTestController {

    // 매개변수가 없는 GET 메소드 구현
    @GetMapping("/hello")
    public String getHello() {
        return "안녕하세요. Rest API의 기본 동작입니다.";
    }

    // URL 경로에 값을 포함하여 전송하는 방식
    @GetMapping("/board/{number}")
    public String getPathVariable(@PathVariable String number) {
        return "요청받은 게시글 번호 : " + number;
    }

    // 쿼리형식으로 값을 전달하는 방식 (RequestParam)
    @GetMapping("/search")
    public String getRequestParam(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password) {
        return "이름은 " + name + ", 메일은 " + email + ", 비밀번호는 " + password;
    }

    // 쿼리형식으로 값을 전달하는데 객체로 받는 방식
    @GetMapping("/search2")
    public String getRequestParam2(MemberVO VO) {
        return VO.toString();
    }
}
