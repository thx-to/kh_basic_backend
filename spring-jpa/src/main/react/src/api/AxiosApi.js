import axios from "axios";
import AxiosInstance from "./AxiosInstance";

const KH_DOMAIN = "http://localhost:8111";

const AxiosApi = {
  // 로그인
  login: async (email, pw) => {
    const login = {
      email: email,
      pwd: pw,
    };
    return await axios.post(KH_DOMAIN + "/auth/login", login);
  },

  // 회원가입 여부 확인
  regCheck: async (email) => {
    return await axios.get(KH_DOMAIN + `/auth/exists/${email}`);
  },

  // 회원가입
  signup: async (email, pw, name) => {
    console.log("이메일 : ", email);
    console.log("패스워드 : ", pw);
    console.log("이름 : ", name);
    const member = {
      email: email,
      pwd: pw,
      name: name,
    };
    return await axios.post(KH_DOMAIN + `/auth/signup`, member);
  },

  // 전체 회원 조회
  memberList: async () => {
    // return await axios.get(KH_DOMAIN + `/members/memberlist`);
    return await AxiosInstance.get("/members/memberlist");
  },

  // 개별 회원 조회 (수정)
  memberInfo: async (email) => {
    return await AxiosInstance.get(`/members/${email}`);
  },

  // // 개별 회원 조회
  // memberInfo: async (email) => {
  //   return await axios.get(KH_DOMAIN + `/members/${email}`);
  // },

  // 채팅방 목록 가져오기
  chatList: async () => {
    return await axios.get(KH_DOMAIN + "/chat/list");
  },

  // 채팅방 생성하기
  // ChatRoomReqDto와 맞추기
  chatCreate: async (email, name) => {
    console.log(email, name);
    const chat = {
      email: email,
      name: name,
    };
    return await axios.post(KH_DOMAIN + "/chat/new", chat);
  },

  // 개별 채팅방 정보 가져오기
  chatDetail: async (roomId) => {
    return await axios.get(KH_DOMAIN + "/chat/room/${roomId}");
  },
};

export default AxiosApi;
