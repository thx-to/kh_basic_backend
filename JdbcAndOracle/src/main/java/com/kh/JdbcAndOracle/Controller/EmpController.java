package com.kh.JdbcAndOracle.Controller;

import com.kh.JdbcAndOracle.DAO.EmpDAO;
import com.kh.JdbcAndOracle.VO.EmpVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// 어노테이션 전까지는 이름만 Controller인 클래스
// @Controller를 붙이면서 스프링 컨테이너에 등록되고 싱글톤 객체가 됨
// 웹페이지에서 진입할 수 있는 지점을 만드는 것 (URL을 치는 것처럼, 진입지점 만들기)
// 여기서는 일반 Controller, 리액트할때는 Rest Controller 사용 예정
@Controller

// http://localhost:8112/emp 이 지점으로 들어옴
// 만약 local host가 아니라면 IP가 나오고, 이 IP를 도메인명에 넣으면 URL이 됨
// 접속 순서) IP를 전송, 포트번호가 IP 내부에, URL 경로에 따라 메소드가 불려옴
@RequestMapping("/emp")
public class EmpController {

    private final EmpDAO empDAO;

    // 위 private final에 뜨는 빨간색 경고 add controller parameter 클릭해서 자동 생성된 영역
    // 아래 생성자를 통해 의존성을 주입함
    // DAO(DB와 연결, VO에 매핑하는 역할, Data Access Object)를 사용할 수 있는 상태가 됨
    public EmpController(EmpDAO empDAO) {
        this.empDAO = empDAO;
    }

    // SELECT 조회
    // http://localhost:8112/emp/select 이 지점으로 들어옴
    @GetMapping("/select")
    // 요 위치로 들어왔을 때 불러줘야 할 메소드 만들기
    public String selectViewEmp(Model model) {
        // DB 정보를 매핑해서 emps 리스트에 다 담음 (DB에 다녀옴)
        List<EmpVO> emps = empDAO.empSelect();
        // addAttribute로 모델에 넘기기 위해 ui에 담음
        model.addAttribute ("employees", emps);
        // (main/resourcex/templates/)thymeleaf 디렉토리의 empSelect.html 반환
        return "thymeleaf/empSelect";
    }

    // INSERT 삽입(회원가입)
    // Model : 넣어서 전달해줌, 스프링부트에서 쓰는 UI 관련, 타임리프에서만 씀
    @GetMapping("/insert")
    public String insertViewEmp(Model model) {
        // 아직 정보가 없는 상태에서 EmpVO(사원정보를 담는) 빈 객체를 하나 만들고 입력을 받기 위해 (빈 통을) addAttribute에 넘김(타임리프로)
        // 터미널 모드에서는 사원정보 입력 창 뜨고 하나하나 입력
        model.addAttribute("employees", new EmpVO());
        return "thymeleaf/empInsert";
    }

    // empInsert에서 post 메소드 실행, PostmMapping이 불려져서 여기서 찾음
    // <form th:action="@{/emp/insert}" th:object="${employees}" method="post">
    // 실제 DB에 넣는 동작
    // employees 정보를 empVO에 받아옴
    @PostMapping("/insert")
    public String insertDBEmp(@ModelAttribute("employees") EmpVO empVO, Model model) {
        // insert할 때 VO를 불러옴
        boolean isSuccess = empDAO.empInsert(empVO);
        // DB에 갔다 와서 성공/실패만 넘겨줌
        model.addAttribute("isSuccess", isSuccess);
        return "thymeleaf/empResult";
    }

}
