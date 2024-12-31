package com.kh.spring_jpa.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TokenDto {

    // 인증 방식
    private String grantType;

    // 액세스 토큰
    private String accessToken;

    // 액세스 토큰 만료 시간
    private Long accessTokenExpiresIn;

    // 리프레쉬 토큰
    private String refreshToken;

    // 리프레쉬 토큰 만료 시간
    private Long refreshTokenExpiresIn;

}
