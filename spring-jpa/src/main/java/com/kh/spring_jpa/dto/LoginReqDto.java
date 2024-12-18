package com.kh.spring_jpa.dto;

// LoginReqDto는 로그인용

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class LoginReqDto {

    private String email;
    private String pwd;

}
