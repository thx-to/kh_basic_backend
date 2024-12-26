package com.kh.spring_jpa.service;

// 연결된 DB가 없음
// 추후 사용시 DB 연결부 별도 구현 필요

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.spring_jpa.dto.ChatMessageDto;
import com.kh.spring_jpa.dto.ChatRoomResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service

public class ChatService {

    // JSON 문자열로 반환하기 위한 객체
    private final ObjectMapper objectMapper;

    // 채팅방 정보를 담을 맵
    private Map<String, ChatRoomResDto> chatRooms;


    // 의존성 주입 이후 초기화를 수행하는 메소드를 지정하는 어노테이션
    // 빈 생성 > 의존성 주입 > @PostConstruct가 붙은 메소드 실행 > 어플리케이션 로직 실행
    @PostConstruct

    // 채팅방 정보를 담을 맵을 초기화
    private void init() {

        // 채팅방 정보를 담을 맵
        chatRooms = new LinkedHashMap<>();
    }


    // 전체 채팅방 리스트를 반환
    public List<ChatRoomResDto> findAllRoom() {

        // 위 chatRooms Map에서 key는 String, value는 ChatRoomResDto
        // chatRoomResDto 반환
        return new ArrayList<>(chatRooms.values());
    }


    // 방 번호로 채팅방 찾기
    public ChatRoomResDto findRoomById(String roomId) {

        // 해당 방 번호를 가진 chatRooms 반환
        return chatRooms.get(roomId);
    }


    // 방 개설하기
    public ChatRoomResDto createRoom (String name) {

        // UUID(Universally Unique Identifier) : 범용 고유 식별자, 전 세계적으로 고유한 값을 생성하기 위한 표준
        // randomId로 랜덤한 값을 반환
        // 자바에서는 UUID.randomUUID()로 간단히 생성 가능
        String randomId = UUID.randomUUID().toString();

        // randomId 로그 찍어보기
        log.info("UUID : " + randomId);

        // chatRoom을 만들어 채팅방 정보 넣어주기
        ChatRoomResDto chatRoom = ChatRoomResDto.builder()
                .roomId(randomId)
                .name(name)
                .regDate(LocalDateTime.now())
                .build();

        // chatRooms에 해당 채팅방 추가하기
        // Map의 key는 randomId, value는 chatRoom(방 정보)
        chatRooms.put(randomId, chatRoom);

        // 채팅방 정보 반환
        return chatRoom;
    }


    // 방 삭제하기
    public void removeRoom(String roomId) {

        // 채팅방 정보 가져오기
        ChatRoomResDto room = chatRooms.get(roomId);

        // 방이 존재하면 (not null이면)
        if (room != null) {

            // 방에 세션이 없으면
            if (room.isSessionEmpty()) {

                // chatRooms에서 해당 roomId를 가진 방 삭제
                chatRooms.remove(roomId);
            }
        }
    }


    // 채팅방에 입장한 세션 추가
    public void addSessionAndHandleEnter(String roomId, WebSocketSession session, ChatMessageDto chatMessage) {

        // 채팅방 번호로 채팅방 정보 가져오기
        ChatRoomResDto room = findRoomById(roomId);

        // 방이 존재하면 (not null이면)
        if (room != null) {

            // 채팅방에 입장한 세션 추가
            room.getSessions().add(session);

            // 채팅방에 입장한 사용자가 있으면
            if (chatMessage.getSender() != null) {

                // 입장 메시지 생성
                chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");

                // 채팅방에 입장 메시지 전송
                sendMessageToAll(roomId, chatMessage);
            }

            // 디버깅용 세션 로그 추가
            log.debug("새로운 세션 추가 : " + session);
        }
    }


    // 채팅방에서 퇴장한 세션 제거
    public void removeSessionAndHandleExit(String roomId, WebSocketSession session, ChatMessageDto chatMessage) {

        // 채팅방 번호로 채팅방 정보 가져오기
        ChatRoomResDto room = findRoomById(roomId);

        // 방이 존재하면 (not null이면)
        if (room != null) {

            // 채팅방에서 퇴장한 세션 제거
            room.getSessions().remove(session);

            // 채팅방에서 퇴장한 사용자가 있으면
            if (chatMessage.getSender() != null) {

                // 퇴장 메시지 생성
                chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장했습니다.");

                // 채팅방에 퇴장 메시지 전송
                sendMessageToAll(roomId, chatMessage);
            }

            // 디버깅용 세션 로그 추가
            log.debug("세션 삭제 : " + session);

            // 세션이 비어 있으면
            if (room.isSessionEmpty()) {

                // 해당 채팅방 번호의 채팅방 지우기
                removeRoom(roomId);
            }
        }
    }


    // 전체 메시지 전송
    public void sendMessageToAll(String roomId, ChatMessageDto message) {

        // 채팅방 번호로 채팅방 정보 가져오기
        ChatRoomResDto room = findRoomById(roomId);

        // 방이 존재하면 (not null이면)
        if (room != null) {
            for (WebSocketSession session : room.getSessions()) {
                sendMessage(session, message);
            }
        }
    }


    // 개별 메시지 전송
    // <T> 제네릭 타입 변수, 반환 타입이 객체이기만 하면 됨
    // 제네릭 : 자바에서 클래스, 메소드, 인터페이스에서 타입을 매개변수로 사용할 수 있게 하는 기능
    // 메소드 선언부에 <T>를 명시하여 message의 타입이 호출시 결정됨
    // 제네릭을 사용하지 않으면 특정 타입으로 고정되므로 다양한 타입의 메시지를 처리하기 어려움
    // T message : message는 호출 시점에 전달된 값의 타입에 따라 결정됨 (예 : String, Integer, 사용자 정의 객체 등 다양한 타입 가능)
    public <T> void sendMessage(WebSocketSession session, T message) {

        try {

            // objectMapper.writeValueAsString(message) : Jackson 라이브러리를 사용해 message 객체를 JSON 문자열로 변환
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));

        } catch(IOException e) {

            // 채팅 메시지 전송 실패 오류 로그
            log.error("메시지 전송 실패 : {}", e.getMessage());
        }
    }


}
