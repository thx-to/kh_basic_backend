package com.kh.spring_jpa.dto;

// BoardResDto는 게시글 상세보기용

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class BoardResDto {

    private Long boardId;
    private String title;
    private String content;
    private String imgPath;
    private LocalDateTime regDate;
    private String email;

    // 댓글 목록 출력
    private List<CommentResDto> comments;

}
