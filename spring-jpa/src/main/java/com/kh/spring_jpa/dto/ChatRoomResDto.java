package com.kh.spring_jpa.dto;

// 채팅방을 관리하는 DTO

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
@Slf4j

public class ChatRoomResDto {

    // 채팅방 ID
    private String roomId;

    // 채팅방 이름
    private String name;

    // 채팅방 생성 시간
    private LocalDateTime regDate;

    // WebSocketSession의 직렬화를 방지하는 어노테이션
    // 누가 이 방에 들어왔는지 등에 대한 정보를 관리를 하는데 클라이언트에 알려줄 필요는 없음
    @JsonIgnore

    // 채팅방에 입장한 세션 정보를 담을 Set
    private Set<WebSocketSession> sessions;

    // 채팅방에 포함된 세션이 비어 있는지 확인
    // 비어 있다면 방 삭제를 하기 위함
    public boolean isSessionEmpty() {
        return this.sessions.isEmpty();
    }

    // 빌더 패턴으로 새로운 값을 받아서 적용
    @Builder
    public ChatRoomResDto(String roomId, String name, LocalDateTime regDate) {

        // Setter와 다른 방식으로 객체를 생성함 (가독성이 좋음)
        this.roomId = roomId;
        this.name = name;
        this.regDate = regDate;

        // 멀티스레드 환경에서 동시성 문제를 해결하기 위해 사용
        this.sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    }


}
