package com.kh.spring_jpa.dto;

// CommentReqDto는 댓글쓰기용

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class CommentReqDto {

    // 댓글을 쓰려면 필요한 것들
    // 나중에 토큰을 사용하게 되면 email 등은 뺄 수 있음

    private String email;
    private Long boardId;
    private String content;

}
