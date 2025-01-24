import React, { useState, useEffect, useRef } from "react";
import AxiosApi from "../../api/AxiosApi";
import styled from "styled-components";
import { useNavigate, useParams } from "react-router-dom";
import Commons from "../../utils/Common";

const ChatContainer = styled.div`
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const ChatHeader = styled.div`
  font-size: 1.5em;
  color: #333;
  text-align: center;
  margin-bottom: 20px;
`;

const MessagesContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 400px;
  overflow-y: auto;
  border-top: 1px solid #ddd;
  border-bottom: 1px solid #ddd;
  padding: 10px;
  margin-bottom: 20px;
`;

const Message = styled.div`
  max-width: 60%;
  padding: 10px;
  margin: 10px;
  border-radius: 20px;
  background-color: ${(props) => (props.isSender ? "#DCF8C6" : "#E0E0E0")};
  align-self: ${(props) => (props.isSender ? "flex-end" : "flex-start")};
  border: ${(props) =>
    props.isSender ? "1px solid #DCF8C6" : "1px solid #E0E0E0"};
`;

const Input = styled.input`
  padding: 10px;
  width: 70%;
  border-radius: 4px;
  border: 1px solid #ddd;
`;

const SendButton = styled.button`
  padding: 10px 15px;
  border: none;
  background-color: #4caf50;
  color: white;
  border-radius: 4px;
  margin-left: 10px;
  cursor: pointer;

  &:hover {
    background-color: #45a049;
  }
`;
const CloseButton = styled.button`
  padding: 10px 15px;
  border: none;
  background-color: #f44336;
  color: white;
  border-radius: 4px;
  margin-top: 10px;
  cursor: pointer;

  &:hover {
    background-color: #d32f2f;
  }
`;

const Chatting = () => {
  // 웹소켓 연결 여부
  const [socketConnected, setSocketConnected] = useState(false);

  // 입력 메시지
  const [inputMsg, setInputMsg] = useState("");

  // 채팅 글 목록
  const [chatList, setChatList] = useState([]);

  // 채팅방 번호 / useParams로부터 구조분해해서 가져옴
  // 새로운 방, 기존 방 모두 해당됨
  const { roomId } = useParams();

  // 메시지를 보낸 사람
  const [sender, setSender] = useState("");

  // 채팅방 이름
  const [roomName, setRoomName] = useState("");

  // 웹소켓 객체 생성
  // useRef : 실제 돔에 접근해야 할 때 사용, 값을 유지하지만 렌더링을 일으키지 않음
  // 소켓 연결 정보를 유지, 렌더링과는 무관
  const ws = useRef(null);

  // 페이지 이동
  const navigate = useNavigate();

  // 로컬 스토리지로부터 이메일을 가져옴
  const email = window.localStorage.getItem("email");

  // 메시지 입력값이 변경될 때 상태 업데이트
  const onChangeMsg = (e) => {
    setInputMsg(e.target.value);
  };

  // 엔터키 입력시 공백 제거 후 비어 있지 않으면 메시지 전송
  const onEnterKey = (e) => {
    if (e.key === "Enter" && inputMsg.trim() !== "") {
      // 기존 이벤트 무시하고 사용자가 만든 이벤트만 사용
      e.preventDefault();
      // 엔터키가 눌렸을 때도 아래 OnclickMsgSend 실행
      onClickMsgSend(e);
    }
  };

  // 전송 버튼 클릭시 웹소켓을 통해 메시지 전송
  const onClickMsgSend = (e) => {
    // useRef를 쓰면 항상 current 안에 넣어야 함, 현재 값을 유지하기 위해
    ws.current.send(
      // JSON 객체로 바꿔줌
      // Axios로 보내는게 아니기 때문에 필요
      JSON.stringify({
        type: "TALK",
        roomId: roomId,
        sender: sender,
        message: inputMsg,
      })
    );
    // 전송 이후 입력창을 비워줌
    setInputMsg("");
  };

  // 채팅 종료 버튼 클릭시 웹소켓으로 종료 메시지 전송 및 연결 닫기
  const onClickMsgClose = () => {
    ws.current.send(
      JSON.stringify({
        type: "CLOSE",
        roomId: roomId,
        sender: sender,
        message: "종료 합니다.",
      })
    );
    ws.current.close();
    // 채팅 목록으로 이동
    navigate("/Chat");
  };

  // 이메일로 회원 정보 가져오기
  useEffect(() => {
    const getMember = async () => {
      try {
        const response = await AxiosApi.memberInfo(email);
        console.log(response.data.name);
        setSender(response.data.name);
      } catch (e) {
        console.log(e);
      }
    };
    getMember();

    // 의존성 배열을 넣어주지 않으면 렌더링이 될때마다 계속 돌려짐
    // 의존성 배열을 넣어 처음 마운팅 시점에만 렌더링될 수 있도록 함
  }, []);

  // 채팅방 정보 가져오기
  useEffect(() => {
    const getChatRoom = async () => {
      try {
        const response = await AxiosApi.chatDetail(roomId);
        console.log(response.data.name);
        setRoomName(response.data.name);
      } catch (e) {
        console.log(e);
      }
    };
    getChatRoom();
  }, []);

  // 웹소켓 연결하기
  useEffect(() => {
    console.log("방번호 : " + roomId);

    // current에 정보가 없으면 (웹소켓 연결 정보가 없으면)
    if (!ws.current) {
      // 연결 위치는 하드코딩으로 일단 넣어줌
      // 웹소켓 프로토콜은 ws, http 대신 ws으로 입력하면 웹소켓 구간
      ws.current = new WebSocket("ws://localhost:8111/ws/chat");
      ws.current.onopen = () => {
        setSocketConnected(true);
      };
    }
    if (socketConnected) {
      ws.current.send(
        JSON.stringify({
          type: "ENTER",
          roomId: roomId,
          sender: sender,
          message: "✨입장✨",
        })
      );
    }
    // useEffect에 타이머 넣으면 클리어하기 전까지 계속 움직이는 것처럼
    // onMessage로 메시지가 들어올때마다 자동으로 감지됨
    ws.current.onmessage = (msg) => {
      // parse : 메시지의 데이터를 바꿔줌 (제이슨에서 읽을 수 있는 타입으로)
      const data = JSON.parse(msg.data);

      // 전개연산자로 이전 아이템은 그냥 두고 새로운 아이템(메시지)을 뒤에 붙여줌
      // concat과 같은 구조
      setChatList((prevItems) => [...prevItems, data]);
    };
  }, [socketConnected]);

  // 화면 하단으로 자동 스크롤 (메시지가 쌓이면)
  // 스크롤 이동시에 돔 요소 추적
  const chatContainerRef = useRef(null);
  useEffect(() => {
    // current에 할당이 되면 (정보가 있으면)
    // 비동기가 아니기 때문에 함수 없이 useEffect 사용
    if (chatContainerRef.current) {
      // 새로운 메시지 추가시 scrollHeight를 기준으로 스크롤 위치(scrollTop)를 업데이트하여 화면을 가장 아래로 이동
      // chatContainerRef.current는 동기적으로 DOM에 접근하므로 별도의 비동기 처리가 필요 없음
      chatContainerRef.current.scrollTop =
        chatContainerRef.current.scrollHeight;
    }
  }, [chatList]);

  return (
    <>
      <ChatContainer>
        <ChatHeader>채팅방 {roomName}</ChatHeader>
        <MessagesContainer ref={chatContainerRef}>
          {chatList.map((chat, index) => (
            <Message key={index} isSender={chat.sender === sender}>
              {`${chat.sender} > ${chat.message}`}
            </Message>
          ))}
        </MessagesContainer>
        <div>
          <Input
            placeholder="문자 전송"
            valud={inputMsg}
            onChange={onChangeMsg}
            onKeyUp={onEnterKey}
          />
          <SendButton onClick={onClickMsgSend}>전송</SendButton>
        </div>
        <CloseButton onClick={onClickMsgClose}>채팅 종료 하기</CloseButton>
      </ChatContainer>
    </>
  );
};

export default Chatting;
