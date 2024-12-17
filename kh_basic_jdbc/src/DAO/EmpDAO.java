package DAO;
// DAO(Database Acceess Object) : 데이터베이스에 접근하여 데이터를 조회하거나 수정하는데 사용
// DAO는 VO 객체와 데이터베이스 간의 매핑을 담당

import Common.Common;
import VO.EmpVO;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmpDAO {

    // 기본적인 것들을 인스턴스 필드로 넣기
    Connection conn = null;
    Statement stmt = null;
    // 주로 사용할 PreparedStatement
    PreparedStatement pstmt = null;
    // 결과값을 받는 ResultSet
    ResultSet rs = null;
    Scanner sc = null;

    // 빈 생성자 만들기, 불려지면 sc 호출
    public EmpDAO() {
        sc = new Scanner(System.in);
    }

    // SELECT(조회) 기능 구현하기
    // ArrayList는 참조 타입, 객체가 들어가야 함
    // EmpVO는 EMP테이블과 똑같이 만들어둔 클래스, 로우데이터를 받아내기 위해 ArrayList를 만듦
    // 리스트의 크기는 데이터베이스(EMP 테이블)의 행 개수가 결정함
    public List<EmpVO> empSelect() {
        List<EmpVO> list = new ArrayList<>();

        try {

            // 연결 정보로 드라이버 로딩 및 연결까지 진행
            // 오라클 DB 연결
            conn = Common.getConnection();

            // Statement 객체에 대한 참조 변수
            // 연결된 상태에서 Statement 생성
            stmt = conn.createStatement();

            // 전체 조회하는 쿼리문 만들기
            // SELECT * FROM EMP를 직접 때려넣는 하드코딩
            String query = "SELECT * FROM EMP";

            // 실행 결과 받기(ResultSet : 여러 행의 결과값을 받아서 반복자 Iterator 제공)
            // executeQuery : SELECT문과 같이 결과값이 여러 개의 레코드로 반환되는 경우 사용
            rs = stmt.executeQuery(query);

            // rs가 제공하는 Iterator 문법 몇가지 알아보기
            // 1) next() : 현재 행에서 한 행 앞으로 이동
            // 2) previous() : 현재 행에서 한 행 뒤로 이동
            // 3) first() : 첫번째 행으로 이동
            // 4) last() : 마지막 행으로 이동
            // 아래 while문에서 읽을 게 있으면 true가 됨
            // DB에 있는 내용을 rs가 받아 옴, columnLabel은 정확하게 입력할 것
            while (rs.next()) {
                int empno = rs.getInt("EMPNO");
                String name = rs.getString("ENAME");
                String job = rs.getString("JOB");
                int mgr = rs.getInt("MGR");
                Date date = rs.getDate("HIREDATE");
                BigDecimal sal = rs.getBigDecimal("SAL");
                BigDecimal comm = rs.getBigDecimal("COMM");
                int deptno = rs.getInt("DEPTNO");

                // EmpVO라는 객체를 만들어서 생성자를 불러오기
                // 하나로 모아서 리스트에 담아주기
                // 있으면 객체 만들고 집어넣어주기를 데이터베이스의 행 개수만큼 반복
                EmpVO vo = new EmpVO(empno, name, job, mgr, date, sal, comm, deptno);
                list.add(vo);
            }

            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);

        } catch (Exception e) {
            System.out.println("EMP 조회 실패");
        }
        return list;
    }

    // INSERT 기능 구현
    // VALUES 뒤 물음표 구간이 PreparedStatement를 넣는 방식, 각 변수에 해당됨
    public boolean empInsert(EmpVO vo) {
        String sql = "INSERT INTO EMP(EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO) VALUES (?,?,?,?,?,?,?,?)";
        try {
            conn = Common.getConnection();

            // 지금 물음표 자리가 결정이 안돼있어서 동적인 결합관계로 만듦 : Statement 대신 PreparedStatement
            pstmt = conn.prepareStatement(sql);

            // 1번 자리(물음표 첫번째 자리)는 Main에서 Scanner로 입력받은 int empno값
            pstmt.setInt(1, vo.getEmpno());
            pstmt.setString(2, vo.getName());
            pstmt.setString(3, vo.getJob());
            pstmt.setInt(4, vo.getMgr());
            pstmt.setDate(5, vo.getDate());
            pstmt.setBigDecimal(6, vo.getSal());
            pstmt.setBigDecimal(7, vo.getComm());
            pstmt.setInt(8, vo.getDeptno());

            // Insert, Update, Delete에 해당하는 함수
            pstmt.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println("INSERT 실패");
            return false;

            // finally는 성공/실패에 관계 없이 무조건 불려짐
        } finally {

            Common.close(pstmt);
            Common.close(conn);

        }

    }

    // UPDATE 기능 구현
    public boolean empUpdate(EmpVO vo) {
        String sql = "UPDATE EMP SET ENAME = ?, JOB = ?, MGR = ?, HIREDATE = ?, SAL = ?, COMM = ?, DEPTNO = ? WHERE EMPNO = ?";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, vo.getName());
            pstmt.setString(2, vo.getJob());
            pstmt.setInt(3, vo.getMgr());
            pstmt.setDate(4, vo.getDate());
            pstmt.setBigDecimal(5, vo.getSal());
            pstmt.setBigDecimal(6, vo.getComm());
            pstmt.setInt(7, vo.getDeptno());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("UPDATE 실패");
            return false;
        } finally {
            Common.close(pstmt);
            Common.close(conn);
        }

    }

    // DELETE 기능 구현
    public boolean empDelete (String name) {
        String sql = "DELETE FROM EMP WHERE ENAME = ?";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("DELETE 실패");
            return false;
        } finally {
            Common.close(pstmt);
            Common.close(conn);
        }

    }


    // 매개변수로 리스트를 집어넣음
    public void empSelectResult(List<EmpVO> list) {

        System.out.println("---------------------------------------------------");
        System.out.println("                   사원 정보");
        System.out.println("---------------------------------------------------");

        // EmpVO 타입의 리스트를 받아서
        for (EmpVO e : list) {
            System.out.print(e.getEmpno() + " ");
            System.out.print(e.getName() + " ");
            System.out.print(e.getJob() + " ");
            System.out.print(e.getMgr() + " ");
            System.out.print(e.getDate() + " ");
            System.out.print(e.getSal() + " ");
            System.out.print(e.getComm() + " ");
            System.out.print(e.getDeptno());
            System.out.println();
        }
        System.out.println("---------------------------------------------------");
    }

}
