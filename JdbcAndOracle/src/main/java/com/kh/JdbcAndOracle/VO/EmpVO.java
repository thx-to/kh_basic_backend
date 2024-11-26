package com.kh.JdbcAndOracle.VO;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmpVO {
    private int empno;
    private String name;
    private String job;
    private int mgr;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private BigDecimal sal;
    private BigDecimal comm;
    private int deptno;
}
