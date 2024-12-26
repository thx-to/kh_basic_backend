package com.kh.spring_jpa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.spring_jpa.dto.ChatMessageDto;
import com.kh.spring_jpa.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor // 롬복 어노테이션으로 생성자
@Slf4j // 롬복 어노테이션으로 log 메시지 사용
@Component // 스프링 컨테이너에 빈 등록

public class WebSocketHandler extends TextWebSocketHandler {

    // REST Controller는 데이터를 자동으로 Mapping하여
    // 자동으로 객체를 JSON으로 바꿔주거나 JSON을 객체로 바꿈
    // 여기는 REST Controller 구간이 아니기 때문에 JSON 데이터를 직접 못만들어 ObjectMapper 사용
    private final ObjectMapper objectMapper;

    private final ChatService chatService;

    // 세션과 채팅방을 매핑하는데 사용
    // 사용자가 어떤 채팅방에 속해있는지
    // throws Exception으로 예외 처리를 특별히 하지 않고 외부로 던짐
    private Map<WebSocketSession, String> sessionRoomIdMap = new ConcurrentHashMap<>();

    // 오버라이딩이 필요한 것 최대 3가지정도
    // 1. 메시지가 왔을 때 원하는 형태로 가공 (handleTextMessage)
    // 2. 연결됐을 때 동작 (connection 관련, 필요 없어서 안넣음)
    // 3. 웹소켓이 해제되었을 때 동작 (afterConnectionClosed) 방폭 등


    // 메시지를 원하는 형태로 가공
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        // 클라이언트가 전송한 메시지
        String payload = message.getPayload();

        // 클라이언트 메시지를 제대로 받았는지 로그 찍어보기
        log.info("{}", payload);

        // JSON 문자열을 ChatMessageDto 객체로 변환
        ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);

        // 채팅방 ID
        String roomId = chatMessage.getRoomId();

        // 메시지 타입이 ENTER이면
        if (chatMessage.getType() == ChatMessageDto.MessageType.ENTER) {

            // 세션과 채팅방 ID를 매핑
            sessionRoomIdMap.put(session, chatMessage.getRoomId());

            // 채팅방에 입장한 세션 추가
            chatService.addSessionAndHandleEnter(roomId, session, chatMessage);

        // 메시지 타입이 CLOSE이면
        } else if (chatMessage.getType() == ChatMessageDto.MessageType.CLOSE) {

            // 채팅방에서 퇴장한 세션 제거
            chatService.removeSessionAndHandleExit(roomId, session, chatMessage);

        // 그 외 (메시지 타입이 TALK면)
        } else {

            // 전체 메시지 전송
            chatService.sendMessageToAll(roomId, chatMessage);
        }

    }


    // 웹소켓 해제 후 동작
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        // 세션과 매핑된 채팅방 ID 로그 찍어보기
        // 메시지가 왔을 때(해제됐을 때)
        log.info("연결 해제 이후 동작 : {}", session);

        // 제거된 세션을 가져옴
        String roomId = sessionRoomIdMap.remove(session);

        if (roomId != null) {
            ChatMessageDto chatMessage = new ChatMessageDto();
            chatMessage.setType(ChatMessageDto.MessageType.CLOSE);
            chatService.removeSessionAndHandleExit(roomId, session, chatMessage);

        }
    }
}
