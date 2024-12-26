package com.kh.spring_jpa.dto;

// 채팅방 생성 요청에 대한 DTO

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString

public class ChatRoomReqDto {

    // 채팅방 개설자의 이메일
    private String email;

    // 채팅방 제목
    private String name;

}
