package com.kh.spring_jpa.dto;

// MemberResDto는 회원정보 조회용

import com.kh.spring_jpa.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder
public class MemberResDto {

    private String email;
    private String name;
    private String imagePath;
    private LocalDateTime regDate;

    public static MemberResDto of(Member member) {
        return MemberResDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .imagePath(member.getImgPath())
                .regDate(member.getRegDate())
                .build();
    }

}
