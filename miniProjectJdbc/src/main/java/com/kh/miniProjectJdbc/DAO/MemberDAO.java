package com.kh.miniProjectJdbc.DAO;

import com.kh.miniProjectJdbc.VO.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository // MemberDAO를 싱글톤 객체로 등록, 다른 곳에서 객체 생성 없이 사용할 수 있게 함 (의존성 주입 제어 역전 관계)
@RequiredArgsConstructor // 생성자를 통해 의존성 주입을 받음
@Slf4j
public class MemberDAO {

    // 내부적으로 스프링부트에서 Jdbc 템플릿이 만들어져 있음
    // Jdbc 템플릿 의존성 주입받기 (생성자를 통해)
    // 연결 요청 해제 등 얘가 다 해줌
    private final JdbcTemplate jdbcTemplate;


    // 아래 List에서 쿼리문 밖으로 빼기 (찾기 쉽게, 오류 검증 등의 용도)

    // 전체 회원 조회 쿼리
    private static final String SELECT_ALL_MEMBERS = "SELECT EMAIL, NAME, REG_DATE FROM MINI_MEMBER";

    // 개별 회원 조회 쿼리
    private static final String SELECT_EMAIL = "SELECT EMAIL, NAME, REG_DATE, PASSWORD FROM MINI_MEMBER WHERE EMAIL = ?";

    // 로그인 쿼리
    // 조건에 해당하는 행의 숫자 (count)
    private static final String LOGIN_QUERY = "SELECT COUNT(*) FROM MINI_MEMBER WHERE EMAIL = ? AND PASSWORD = ?";

    // 회원가입 쿼리
    private static final String SIGNUP_QUERY = "INSERT INTO MINI_MEMBER (EMAIL, PASSWORD, NAME) VALUES(?, ?, ?)";

    // 회원가입 여부 확인 쿼리
    private static final String CHECK_EMAIL = "SELECT COUNT(*) FROM MINI_MEMBER WHERE EMAIL = ?";

    // 회원 정보 수정 쿼리
    private static final String UPDATE_MEMBER = "UPDATE mini_member SET email = ?, password = ?, name = ? WHERE email = ?";

    // 회원 삭제 쿼리
    private static final String DELETE_MEMBER = "DELETE FROM mini_member WHERE email = ?";


    // 전체 회원 조회
    public List<MemberVO> memberList() {
        try {
            return jdbcTemplate.query(SELECT_ALL_MEMBERS, new MemberRowMapper());
        } catch (DataAccessException e) {
            log.error("회원 목록 조회 중 예외 발생", e);
            throw e; // 예외처리를 안하겠다 (밖으로 던지겠다)
        }
    }

    // 개별 회원 조회
    public List<MemberVO> findMemberByEmail(String email) {
        try {
            return jdbcTemplate.query((SELECT_EMAIL), new Object[]{email}, new MemberRowMapper());
        } catch (DataAccessException e) {
            log.error("회원 목록 조회 중 예외 발생", e);
            throw e;
        }
    }


    // 이에밀 및 이름으로 회원 검색 하기
    public List<MemberVO> findMemberByEmailOrName(String email, String name) {
        StringBuilder sql = new StringBuilder(SELECT_ALL_MEMBERS);
        boolean hasEmail = (email != null && !email.isEmpty());
        boolean hasName = (name != null && !name.isEmpty());

        if (hasEmail || hasName) {
            sql.append(" WHERE ");
            if (hasEmail) {
                sql.append("email = ?");
            }
            if (hasName) {
                if (hasEmail) {
                    sql.append(" OR ");
                }
                sql.append("name = ?");
            }
        }

        try {
            Object[] params = hasEmail && hasName
                    ? new Object[]{email, name}
                    : hasEmail
                    ? new Object[]{email}
                    : new Object[]{name};

            return jdbcTemplate.query(sql.toString(), params, new MemberRowMapper());
        } catch (DataAccessException e) {
            log.error("회원 목록 조회 중 예외 발생 ", e);
            throw e;
        }
    }

    // 로그인
    public boolean login(String email, String password) {
        try {
            int count = jdbcTemplate.queryForObject(LOGIN_QUERY, new Object[]{email, password}, Integer.class);
            return count > 0; // 0보다 크면 true (회원이 있음)
        } catch (DataAccessException e) {
            log.error("로그인 중 예외 발생", e);
            return false;
        }
    }

    // 회원가입
    // 회원정보를 다 받아야 돼서 개별 매개변수로 받는 것보다 MemberVO의 객체로 받는게 더 편함
    public boolean signup(MemberVO VO) {
        try {
            // INSERT는 영향을 받는 행이 1개, UPDATE는 1개 이상 (0보다 큰 경우를 참으로 봐야 함)
            int result = jdbcTemplate.update(SIGNUP_QUERY, VO.getEmail(), VO.getPassword(), VO.getName());
            return result > 0;
        } catch (DataAccessException e) {
            log.error("회원가입 중 예외 발생", e);
            return false;
        }
    }

    // 회원가입 여부 확인
    public boolean isEmailExist (String email) {
        try {
            // 대상 쿼리 / 넣을 값 / 반환받을 값
            int count = jdbcTemplate.queryForObject(CHECK_EMAIL, new Object[]{email}, Integer.class);
            return count > 0;
        } catch (DataAccessException e) {
            log.error("이메일 존재 여부 확인 중 예외 발생", e);
            return false;
        }
    }

    // 회원 정보 수정
    public boolean updateMember(String email, MemberVO vo) {
        int rowsAffected = jdbcTemplate.update(UPDATE_MEMBER, vo.getEmail(), vo.getPassword(), vo.getName(), email);
        return rowsAffected > 0;
    }

    // 회원 삭제
    public boolean deleteMember(String email) {
        int rowAffected = jdbcTemplate.update(DELETE_MEMBER, email);
        return rowAffected > 0;
    }


    private static class MemberRowMapper implements RowMapper<MemberVO> {

        @Override
        public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberVO(
                    rs.getString("email"),
                    null,
                    rs.getString("name"),
                    rs.getDate("reg_date")
            );

        }
    }
}
