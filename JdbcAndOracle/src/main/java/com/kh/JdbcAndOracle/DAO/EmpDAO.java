package com.kh.JdbcAndOracle.DAO;

import com.kh.JdbcAndOracle.VO.EmpVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// bin에 등록, 생성 없이 아무데서나 갖다 쓸 수 있음
@Repository
public class EmpDAO {
    private final JdbcTemplate jdbcTemplate;

    // 템플릿이 JDBC 연결을 관리해줌, 기능만 구현해주면 됨
    // 생성자 만들기
    public EmpDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<EmpVO> empSelect() {
        String sql = "SELECT * FROM EMP";
        return jdbcTemplate.query(sql, new EmpRowMapper());
    }

    public boolean empInsert(EmpVO vo) {
        int result = 0;
        String sql = "INSERT INTO EMP (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            result = jdbcTemplate.update(sql, vo.getEmpno(), vo.getName(), vo.getJob(), vo.getMgr(), vo.getDate(), vo.getSal(), vo.getComm(), vo.getDeptno());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // 0보다 크면 참
        // 실패하거나 영향받는 행이 없으면 0
        return result > 0;
    }

    private static class EmpRowMapper implements RowMapper<EmpVO> {
        @Override
        public EmpVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new EmpVO(
                    rs.getInt("EMPNO"),
                    rs.getString("ENAME"),
                    rs.getString("JOB"),
                    rs.getInt("MGR"),
                    rs.getDate("HIREDATE"),
                    rs.getBigDecimal("SAL"),
                    rs.getBigDecimal("COMM"),
                    rs.getInt("DEPTNO")
            );
        }
    }

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
