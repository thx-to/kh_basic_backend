package com.kh.spring_jpa.dto;

// 채팅 메시지를 주고 받는 DTO

import lombok.Getter;
import lombok.Setter;

@Getter @Setter

public class ChatMessageDto {

    public enum MessageType {
        ENTER, TALK, CLOSE
    }

    // 방 진입, 메시지 종료
    private MessageType type;

    // 채팅방 번호
    private String roomId;

    // 보내는 사람
    private String sender;

    // 메시지 내용
    private String message;

}
