import DAO.EmpDAO;
import VO.EmpVO;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class JdbcMain {
    public static void main(String[] args) {
        menuSelect();
    }

    public static void menuSelect() {

        Scanner sc = new Scanner(System.in);

        // EmpDAO라는 객체를 만들기
        // EmpDAO에서 가져오니까 import, EmpDAO의 빈 생성자에는 public 넣어주기
        EmpDAO dao = new EmpDAO();

        while (true) {
            System.out.println("==================== EMP TABLE ====================");
            System.out.println("메뉴를 선택하세요.");
            System.out.print("[1]SELECT [2]INSERT [3]UPDATE [4]DELETE [5]EXIT : ");
            int sel = sc.nextInt();
            boolean isSuccess = false;
            switch (sel) {
                case 1 :
                    List<EmpVO> list = dao.empSelect();
                    dao.empSelectResult(list);
                    break;
                case 2 :
                    // Input을 넣고 결과를 받아냄
                    isSuccess = dao.empInsert(empInput());
                    if (isSuccess) System.out.println("사원 등록 성공");
                    else System.out.println("사원 등록 실패");
                    break;
                case 3 :
                    isSuccess = dao.empUpdate(empUpdateInput());
                    if (isSuccess) System.out.println("사원 정보 수정 성공");
                    else System.out.println("사원 정보 수정 실패");
                    break;
                case 4 :
                    isSuccess = dao.empDelete(empDeleteInput());
                    if (isSuccess) System.out.println("사원 삭제 성공");
                    else System.out.println("사원 삭제 실패");
                    break;
                case 5 :
                    System.out.println("프로그램을 종료합니다.");
                    return;
            }
        }

    }


    // 사원 등록용 정보 입력받기
    public static EmpVO empInput() {

        Scanner sc = new Scanner(System.in);

        System.out.println("사원 정보를 입력하세요.");

        System.out.print("사원 번호 : ");
        int empno = sc.nextInt();

        System.out.print("이름 : ");
        String name = sc.next();

        System.out.print("직책 : ");
        String job = sc.next();

        System.out.print("상관 사원 번호 : ");
        int mgr = sc.nextInt();

        // 문자열로 입력받아도 날짜 형식에만 맞으면 입력 가능
        // 데이터 포맷에만 맞으면 들어가니까 String으로 입력받기
        // 2024-05-24 형식으로
        System.out.print("입사일 : ");
        String date = sc.next();

        System.out.print("급여 : ");
        BigDecimal sal = sc.nextBigDecimal();

        System.out.print("성과급 : ");
        BigDecimal comm = sc.nextBigDecimal();

        System.out.print("부서번호 : ");
        int deptno = sc.nextInt();

        // EmpVO 생성자를 만들어서 다 때려박기
        return new EmpVO(empno, name, job, mgr, Date.valueOf(date), sal, comm, deptno);
    }

    // 사원 정보 수정용 정보 입력받기
    public static EmpVO empUpdateInput() {

        Scanner sc = new Scanner(System.in);

        System.out.print("정보 수정을 원하는 사원의 사원번호를 입력하세요 : ");
        int empno = sc.nextInt();

        System.out.print("수정할 이름을 입력하세요 : ");
        String name = sc.next();

        System.out.print("수정할 직책을 입력하세요 : ");
        String job = sc.next();

        System.out.print("수정할 상관 사원 번호를 입력하세요 : ");
        int mgr = sc.nextInt();

        System.out.print("수정할 입사일을 입력하세요 : ");
        String date = sc.next();

        System.out.print("수정할 급여를 입력하세요 : ");
        BigDecimal sal = sc.nextBigDecimal();

        System.out.print("수정할 성과급을 입력하세요 : ");
        BigDecimal comm = sc.nextBigDecimal();

        System.out.print("수정할 부서번호를 입력하세요 : ");
        int deptno = sc.nextInt();

        return new EmpVO(empno, name, job, mgr, Date.valueOf(date), sal, comm, deptno);
    }

    public static String empDeleteInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print("삭제를 원하는 사원의 사원번호를 입력하세요 : ");
        return sc.next();
    }

}