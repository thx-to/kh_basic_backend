package com.kh.miniProjectJdbc.CONTROLLER;

import com.kh.miniProjectJdbc.DAO.MemberDAO;
import com.kh.miniProjectJdbc.VO.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor

// 동일 출처 에러 잡기 (같은 IP인데 다른 포트 경로해서 오는 경우 발생하는 오류 풀어주기)
// 리액트가 3000번 포트로 설정되어 있음
// 지금 IntelliJ는 8011 포트
@CrossOrigin(origins = "http://localhost:3000")

public class AuthController {

    private final MemberDAO memberDAO;

    // 로그인
    @PostMapping("/login")
    // 성공/실패를 보내려고 Boolean 타입으로 응답을 줌
    // 이 함수를 불러달라고 매핑, 함수 이름은 아무거나 해도 상관없음
    // 리액트에서 바디 영역에 객체를 받았으니, 바디로 받음 RequestBody
    public ResponseEntity<Boolean> login(@RequestBody MemberVO VO) {
        log.error("이메일 : {} ", VO.getEmail());
        log.error("패스워드 : {}", VO.getPassword());
        boolean inSuccess = memberDAO.login(VO.getEmail(), VO.getPassword());
        // 반환값 돌려주기
        return ResponseEntity.ok(inSuccess);
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Boolean> signup(@RequestBody MemberVO VO) {
        log.error("member : {}", VO);
        boolean isSuccess = memberDAO.signup(VO);
        return ResponseEntity.ok(isSuccess);
    }

    // 회원가입 여부 확인
    @GetMapping ("/exists/{email}")
    public ResponseEntity<Boolean> exists(@PathVariable String email) {
        log.error("email : {}", email);  // AxiosApi.js의 regCheck에서 ${email} 변수로 받은 중괄호에서 email을 빼냄
        boolean isExist = memberDAO.isEmailExist(email);
        return ResponseEntity.ok(!isExist); // 존재하지 않으면 가입 가능
    }

}
