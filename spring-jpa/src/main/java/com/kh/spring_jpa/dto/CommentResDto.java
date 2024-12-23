package com.kh.spring_jpa.dto;

// CommentResDto는 댓글 상세보기용

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @ToString
public class CommentResDto {

    private String email;
    private Long boardId;
    private Long commentId;
    private String content;
    private LocalDateTime regDate;

}
