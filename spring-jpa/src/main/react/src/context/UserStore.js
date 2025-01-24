import { createContext, useEffect, useState } from "react";
export const UserContext = createContext(null);

const UserStore = (props) => {
  // 전역상태관리로 배경색 컬러값 관리(배경색에 대한 전역 상태 관리)
  // localStorage에서 getItem으로 가져온 bgcolor가 있으면 그대로, 아니면 오렌지
  // 값을 바꿔줄 때 localStorage도 같이 바꿔주면 새로고침시에도 색상 유지됨
  const [color, setColor] = useState(
    localStorage.getItem("bgcolor") || "orange"
  );

  // 로그인한 사용자 이름을 여러군데에서 사용해야 할 때
  const [name, setName] = useState(
    localStorage.getItem("name") || "이름을 입력해주세요."
  );

  // 의존성 배열에 color를 넣어줘 컬러값을 바꾸는 순간에 useEffect에 컬러값 감지
  // 그 때 localStorage에 컬러값을 넣고 그 값을 위에서 읽어서 세팅하게 함
  useEffect(() => {
    localStorage.setItem("bgcolor", color);
  }, [color]);

  // name이 바뀔 때 집어넣음
  useEffect(() => {
    localStorage.setItem("name", name);
  }, [name]);

  return (
    // 구조분해로 value 넣어주기
    <UserContext.Provider
      value={{
        color,
        setColor,
        name,
        setName,
      }}
    >
      {props.children}
    </UserContext.Provider>
  );
};

export default UserStore;
