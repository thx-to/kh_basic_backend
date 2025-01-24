import axios from "axios";
import moment from "moment"; // 시간을 경과 시간 형태로 표시
import "moment/locale/ko";

moment.locale("ko"); // 한국 시간 적용

const Commons = {

  KH_DOMAIN: "http://localhost:8111",
  KH_SOCKET_URL: "wf://localhost:8111/ws/chat",

  timeFromNow: (timestamp) => {
    return moment(timestamp).fromNow();
  },

  formatDate: (dateString) => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const day = ("0" + date.getDate()).slice(-2);
    const hour = ("0" + date.getHours()).slice(-2);
    const minute = ("0" + date.getMinutes()).slice(-2);
    return `${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분`;
  },

  // AccessToken 키의 값을 로컬스토리지에서 읽어 반환하는 함수
  getAccessToken: () => {
    return localStorage.getItem("accessToken");
  },

  // 주어진 AccessToken 정보를 accessToken 키에 저장
  setAccessToken: (token) => {
    localStorage.setItem("accessToken", token);
  },

  getRefreshToken: () => {
    return localStorage.getItem("refreshToken");
  },

  setRefreshToken: (token) => {
    localStorage.setItem("refreshToken", token);
  },

  // 401 오류 처리 함수
  // 401(토큰 만료 오류) 오류 발생 시 이 함수만 불러주면 처리
  // 서버에 refresh 토큰을 던져서 access 토큰을 재발행하게 함 (갱신)
  handleUnauthorized: async () => {
    console.log("handleUnauthorized");
    const accessToken = Commons.getAccessToken();
    const refreshToken = Commons.getRefreshToken();
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    try {
      const rsp = await axios.post(
        `${Commons.KH_DOMAIN}/auth/refresh`,
        refreshToken,
        config
      );
      console.log(rsp.data); // 데이터 로그 찍어보기
      Commons.setAccessToken(rsp.data);
      return true;
    } catch (e) {
      console.log(e);
      return false;
    }
  },

};

export default Commons;
