package com.kh.spring_jpa.dto;

// BoardReqDto는 글쓰기용

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor

public class BoardReqDto {

    private String title;
    private String content;
    private String imgPath;
    private String email;

}
