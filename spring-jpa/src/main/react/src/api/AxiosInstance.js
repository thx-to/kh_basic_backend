import axios from "axios";
import Commons from "../utils/Common";

// axios의 인스턴스를 생성하여 공통 설정(예: baseURL)을 재사용할 수 있게 함
const AxiosInstance = axios.create({

  // 모든 요청의 기본 URL을 Commons.KH_DOMAIN으로 설정
  // 예를 들어 AxiosInstance.get("/path")는 Commons.KH_DOMAIN/path에 요청을 보냄
  baseURL: Commons.KH_DOMAIN,


});

// 요청 인터셉터 추가
AxiosInstance.interceptors.request.use(

  // 요청 성공 콜백
  // config요청 정보가 들어오면
  // config : 요청 설정 객체, 요청의 메소드, 헤더, URL 등이 포함
  async(config) => {

    // Commons 유틸리티에서 액세스 토큰을 읽어들이고
    const accessToken = await Commons.getAccessToken();

    // 요청의 Authorization 헤더에 Barer 토큰을 추가하여 인증 정보를 포함시킴
    config.headers.Authorization = `Bearer ${accessToken}`;
    return config;

  },

  // 요청 실패 콜백
  (error) => {

    // Promise.reject를 반환하여 오류 처리를 위임
    return Promise.reject(error);
  }

);


// 응답 인터셉터 추가
AxiosInstance.interceptors.response.use(
  
  (response) => {

    // 문제가 없으면 바로 response를 날려줌
    return response;

  },

  async (error) => {

    // 오류이고, 오류 코드가 401이면 (토큰 만료)
    if (error.response && error.response.status === 401) {

      // Commons에서 만들어진 토큰 재발행 함수를 불러옴
      const newToken = await Commons.handleUnauthorized();

      // 새로운 토큰이 발행됐다면
      if (newToken) {

        // 원래 하고자 했던 요청을 다시 시도
        error.config.headers.Authorization = `Bearer ${Commons.getAccessToken()}`;
        return AxiosInstance.request(error.config);
      }
    }
    return Promise.reject(error);
  }
);

export default AxiosInstance;