package com.kh.miniProjectJdbc.VO;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor // 매개변수가 전부 다 있는 생성자
@NoArgsConstructor // 매개변수가 없는 생성자 (기본 생성자)
@ToString

// memberVO : value object, 값을 주고받기 위해 사용
public class MemberVO {

    // React의 const AxiosApi의 key값과 Java에서의 변수 이름이 같아야 함
    private String email;
    private String password;
    private String name;
    private Date date;

}
