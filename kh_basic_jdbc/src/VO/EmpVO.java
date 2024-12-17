package VO;
// VO (Value Object) : 데이터베이스에서 가져온 레코드(튜플)를 자바 객체로 매핑(Mapping)하는 데 사용
// VO 객체는 데이터베이스 테이블의 각 컬럼에 해당하는 멤버 변수(인스턴스 필드)를 만듦
// 일반적으로 데이터베이스 테이블의 레코드 한 행을 객체로 매핑하기 위해 사용
// 특수한 목적으로는 데이터 테이블을 모아서 하나의 VO로 만들 수도 있음
// 테이블 정보가 민감정보라서 화면에 표현하기 곤란하면 빼고 넘겨주는 등의 작업도 필요
// 일반적으로는 1:1이나, 빼야 하는 경우도, 결합해서 넘겨야 하는 경우도 있음
// VO 객체는 로직을 가지지 않고 순수하게 데이터를 전달하는 용도로 사용 (매핑만 수행)

import java.math.BigDecimal;
import java.sql.Date;


public class EmpVO {

    private int empno;
    private String name;
    private String job;
    private int mgr;
    private Date date;
    // 부동소수점방식 대신 보다 정확한 근사치계산법 사용을 위해 BigDecimal
    private BigDecimal sal;
    private BigDecimal comm;
    private int deptno;

    // 모든 변수를 매개변수로 받는 생성자 생성
    // EmpVO를 여러개 받아 ArrayList로 만들기
    public EmpVO(int empno, String name, String job, int mgr, Date date, BigDecimal sal, BigDecimal comm, int deptno) {
        this.empno = empno;
        this.name = name;
        this.job = job;
        this.mgr = mgr;
        this.date = date;
        this.sal = sal;
        this.comm = comm;
        this.deptno = deptno;
    }

    // 아무것도 받지 않는 빈 생성자 생성
    // VO 레코드를 받아 내기 위함
    public EmpVO() {
    }

    public int getEmpno() {
        return empno;
    }

    public void setEmpno(int empno) {
        this.empno = empno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getMgr() {
        return mgr;
    }

    public void setMgr(int mgr) {
        this.mgr = mgr;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getSal() {
        return sal;
    }

    public void setSal(BigDecimal sal) {
        this.sal = sal;
    }

    public BigDecimal getComm() {
        return comm;
    }

    public void setComm(BigDecimal comm) {
        this.comm = comm;
    }

    public int getDeptno() {
        return deptno;
    }

    public void setDeptno(int deptno) {
        this.deptno = deptno;
    }

}
