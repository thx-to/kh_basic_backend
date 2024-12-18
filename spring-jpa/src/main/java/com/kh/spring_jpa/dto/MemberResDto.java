package com.kh.spring_jpa.dto;

// MemberResDto는 회원정보 조회용

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class MemberResDto {

    private String email;
    private String name;
    private String imagePath;
    private LocalDateTime regDate;

}
