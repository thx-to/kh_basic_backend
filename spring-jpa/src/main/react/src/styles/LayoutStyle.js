import styled from "styled-components";
import { Link } from "react-router-dom";

const defaultBackgroundColor = "#f9aa06";
const sideMenuBackgroundColor = "#eee";
const topbarHeight = "54px";

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  width: 100%;
  margin: auto;
  background-color: ${(props) => props.color || defaultBackgroundColor};

  .head {
    display: flex;
    height: ${topbarHeight};
    width: 100%;
    justify-content: space-between;
    position: fixed;
    z-index: 1;
    top: 0;
    left: 0;
    padding: 10px 30px;
  }

  .body {
    margin-top: ${topbarHeight};
    height: calc(100vh - ${topbarHeight} - 50px);
  }

  .footer {
    text-align: center;
  }
`;

export const UserContainer = styled.div`
  display: flex;
  margin: 40px 20px;
`;

export const UserImage = styled.img`
  width: 50px;
  height: 50px;
  border-radius: 10px;
`;

export const UserIdAndName = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: start;
  margin: 2px 10px;
  line-height: 1.5;
`;

export const StyledSideMenu = styled.div`
  position: fixed;
  left: 0;
  top: ${topbarHeight};
  width: 250px;
  height: calc(100vh - ${topbarHeight});
  background-color: ${sideMenuBackgroundColor};
  box-shadow: 2px 0px 5px rgba(0, 0, 0, .5);
  z-index: 1000;
  border-top-right-radius: 10px;
  // props값을 받아서 isOpen이 참이면 현재 위치 그대로, 아니면 X축에서 -100%(사라짐)
  transform: ${(props) => props.isOpen ? "translateX(0)" : "translateX(-100%)"};
  transition: transform 0.3s ease;
`;

export const StyledMenuList = styled.ul`
  list-style: none;
  padding: 0;
`;

export const StyledMenuItem = styled.li`
  padding: 10px 20px;
  border-bottom: 1px solic #ccc;
  display: flex;
  align-items: center;
`;

export const MenuIcon = styled.span`
  margin-right: 10px;
`;

// Link는 원래 HTML 태그에 없고 리액트에서 만든거라 소괄호()로 감쌈
// HTML에서 link는 a태그
// Material-UI때도 소괄호로 감싼 스타일링 봄..
export const StyledLink = styled(Link)`
  text-decoration: none;
  color: inherit;

  &:hover {
    color: #3498db;
  }
`;
