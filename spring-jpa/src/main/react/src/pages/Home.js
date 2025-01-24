import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import AxiosApi from "../api/AxiosApi";


const Home = () => {

  const [members, setMembers] = useState("");

  useEffect (() => {
    const getMembers = async () => {
      
      // 통신 구간이니까 try ~ catch 구문 걸어주기
      try {
      const rsp = await AxiosApi.memberList(); // 매개변수가 없기 때문에 이렇게만 입력
      console.log("Home: ", rsp.data);
      setMembers(rsp.data);
      } catch (e) {
        alert("서버가 응답하지 않습니다.", e);
      };
    };
    getMembers();
  }, []);

  return (
    <>
      <h1>회원 정보 조회</h1>
      <table>
        <tr>
          <th>이름</th>
          <th>이메일</th>
          <th>가입일</th>
        </tr>
        {members && members.map((member) => (
          <tr key={member.email}>
            <td>{member.name}</td>
            <td>{member.email}</td>
            <td>{member.reg_date}</td>
          </tr>
        ))}
      </table>
    </>
  );
};

export default Home;