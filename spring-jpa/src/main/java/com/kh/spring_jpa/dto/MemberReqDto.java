package com.kh.spring_jpa.dto;

// DTO : 다른 레이어 간의 데이터를 교환할 때 사용
// 주로 프론트엔드와 백엔드 사이에서 데이터를 주고받는 용도
// MemberReqDto에는 프론트엔드에서 요청하는 인스턴스 필드만 포함해야 함 (필요한 필드만 보낼 수 있게끔)
// VO처럼 모든 필드를 다 써버리면 어떤 필드를 넣어줘야할지 따로 설명이 필요함

// MemberReqDto는 회원가입용
// entity/Member.java에서 회원가입에 필요한 필드는 이메일, 패스워드, 네임

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class MemberReqDto {

    private String email;
    private String pwd;
    private String name;
    private String imgPath;

}
