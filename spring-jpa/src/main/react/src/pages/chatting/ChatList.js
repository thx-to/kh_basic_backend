import React, { useEffect, useState } from "react";
import AxiosApi from "../../api/AxiosApi";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import Commons from "../../utils/Common";

const ChatListContainer = styled.div`
  padding: 30px;
  position: relative;
  margin: 40px;
  background-color: #f3f3f3;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const ChatUl = styled.ul`
  list-style-type: none;
  padding: 0;
`;

const ChatRoom = styled.li`
  background-color: #fff;
  border: 1px solid #ddd;
  margin-bottom: 10px;
  padding: 15px;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.2s ease-in-out;

  &:hover {
    background-color: #e9e9e9;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  }
`;
const Header = styled.h1`
  color: #333;
  text-align: center;
  margin-bottom: 20px;
`;

const ChatName = styled.p`
  font-size: 1.5em;
  margin: 0 0 10px 0;
  color: #444;
`;
const ChatDate = styled.p`
  font-size: 1em;
  color: #666;
  margin: 0;
  text-align: right;
`;
const CircleFixedButton = styled.button`
  position: fixed; // 버튼을 부모 컨테이너에 대해 절대적 위치로 설정
  bottom: 24px;
  right: 30px;
  z-index: 10;

  width: 60px; // 버튼의 크기를 정사각형으로 설정
  height: 60px; // 버튼의 크기를 정사각형으로 설정
  border-radius: 50%; // 동그란 모양으로 만들기 위해 반지름을 50%로 설정

  display: flex; // Flexbox 레이아웃 사용
  justify-content: center; // 가로 중앙 정렬
  align-items: center; // 세로 중앙 정렬

  background-color: #1da1f2; // 트위터 색상
  color: white;
  font-size: 30px; // 플러스 기호 크기
  line-height: 1; // 기본 라인 높이 제거
  // 그림자 효과
  box-shadow: 1px 4px 8px rgba(0, 0, 0, 0.4);

  border: none; // 기본 테두리 제거
  cursor: pointer;
  outline: none; // 클릭 시 테두리 제거

  &:hover {
    background-color: #1991db; // 호버 시 배경색 변경
  }

  &:before {
    // 가상 요소로 플러스 기호 생성
    content: "+";
  }
`;

const ChatList = () => {
  const [chatRooms, setChatRooms] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    // 서버로부터 채팅방 목록을 가져오는 API 호출
    const getChatRoom = async () => {
      try {
        // rsp는 서버로부터 가져온 데이터(채팅방 목록)
        const rsp = await AxiosApi.chatList();

        // 서버에서 가져온 채팅방 목록 확인하는 로그 찍기
        console.log(rsp.data);

        // chatRooms에 채팅방 목록 담기
        setChatRooms(rsp.data);
      } catch (error) {
        // 서버와의 통신 실패에 대한 예외처리
        console.log(error);
      }
    };

    // 1초마다 채팅방 목록을 갱신하기 위한 Interval 함수 생성
    // getChatRoom이라는 함수를 불러줌
    const intervalID = setInterval(getChatRoom, 1000);
    return () => {
      // 언마운트 시점에 인터벌 함수(타이머)를 지워줌
      clearInterval(intervalID);
    };

    // 마운트 시점(처음 화면이 나타나는 시점)에 서버로부터 정보를 가져옴
    // 의존성 배열을 빈 배열로 비워둠
  }, []);

  // 채팅방 접속
  const enterChatRoom = (roomId) => {
    // 방 번호를 가지고 채팅방으로 이동
    navigate(`/chatting/${roomId}`);
  };

  // 채팅방 생성
  const createChatRoom = () => {
    // 새 채팅창을 만드는 링크로 이동
    navigate("/chat-create");
  };

  return (
    <>
      <ChatListContainer>
        <Header>채팅방 목록</Header>
        <ChatUl>
          {chatRooms.map((room) => (
            <ChatRoom
              key={room.roomId}
              onClick={() => enterChatRoom(room.roomId)}
            >
              <ChatName>{room.name}</ChatName>
              <ChatDate>{Commons.formatDate(room.regDate)}</ChatDate>
            </ChatRoom>
          ))}
        </ChatUl>
        <CircleFixedButton onClick={createChatRoom}></CircleFixedButton>
      </ChatListContainer>
    </>
  );
};

export default ChatList;
