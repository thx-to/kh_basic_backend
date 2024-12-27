package com.kh.spring_jpa.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TokenDto {

    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;

}
