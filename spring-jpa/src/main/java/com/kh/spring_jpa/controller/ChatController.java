package com.kh.spring_jpa.controller;

import com.kh.spring_jpa.dto.ChatRoomReqDto;
import com.kh.spring_jpa.dto.ChatRoomResDto;
import com.kh.spring_jpa.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping("/new")
    public ResponseEntity<String> createRoom(@RequestBody ChatRoomReqDto chatRoomReqDto) {

        // 로그 찍어보기 (개설자 이메일, 채팅방 제목)
        // 로그를 위해 ChatRoomReqDto에 @ToString 어노테이션 넣어줌
        log.info("chatRoomReqDto : {}", chatRoomReqDto);

        // 방 생성
        ChatRoomResDto chatRoomResDto = chatService.createRoom(chatRoomReqDto.getName());

        // 응답으로 채팅방 ID 반환
        return ResponseEntity.ok(chatRoomResDto.getRoomId());
    }


    // 전체 채팅방 리스트 가져오기
    @GetMapping("/list")
    public List<ChatRoomResDto> findAllRoom() {
        return chatService.findAllRoom();
    }


    // 방 정보 가져오기
    @GetMapping("/room/{roomId}")
    public ChatRoomResDto findRoomById(@PathVariable String roomId) {
        return chatService.findRoomById(roomId);
    }
}
