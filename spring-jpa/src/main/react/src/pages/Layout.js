import { useContext, useEffect, useState } from "react";
import { useNavigate, Outlet } from "react-router-dom";
import { GiHamburgerMenu, GiCancel } from "react-icons/gi";
import { FiSettings } from "react-icons/fi";
import { FaHome, FaClipboardList, FaRegNewspaper } from "react-icons/fa";
import { BiCameraMovie } from "react-icons/bi";
import { CgProfile } from "react-icons/cg";
import { LuListTodo } from "react-icons/lu";
import { Container, UserContainer, UserImage, UserIdAndName, StyledSideMenu, StyledMenuList, StyledMenuItem, MenuIcon, StyledLink } from "../styles/LayoutStyle"
import { UserContext } from "../context/UserStore";
import AxiosApi from "../api/AxiosApi";

const Layout = () => {

  // 사이드바 메뉴 열고 닫기
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  // 회원정보 업데이트
  const [member, setMember] = useState("");

  // 전역 상태 관리
  // useContext 훅을 통해 사용자가 만든 UserContext 사용
  const {color, name, imgUrl} = useContext(UserContext);

  const navigate = useNavigate();

  // Login.js에서 로그인시 저장한 로컬스토리지의 이메일 가져오기
  const email = localStorage.getItem("email");

  const toggleMenu = () => {
    // 토글, 기존 menuopen과 반대값을 넣어줌 (열려있으면 닫고, 닫혀있으면 열고)
    setIsMenuOpen(!isMenuOpen);
  };

  const goToSetting = () => {
    navigate("/setting");
  }

  useEffect(() => {

    // 비동기함수 만들기
    const getMemberInfo = async() => {
      try {
        const rsp = await AxiosApi.memberInfo(email);
        console.log("Layout: ", rsp.data);
        setMember(rsp.data);
      } catch (e) {
        alert("서버가 응답하지 않습니다.");
      }
    }
    getMemberInfo();
    
  }, []);

  return (
    // 전역상태관리에서 컬러값 받아오기
    <Container color={color}>
      <header className="head">
        <div onClick={toggleMenu}>
          {isMenuOpen ? (
            <GiCancel size={32} color="white" />
          ) : (
            <GiHamburgerMenu size={32} color="white" />
          )}
        </div>
        <div onClick={goToSetting}>
          <FiSettings size={32} color="white" />
        </div>
      </header>
      <StyledSideMenu isOpen={isMenuOpen} onClick={toggleMenu}>
        <StyledMenuList>
          <UserContainer>
            <UserImage
              src={member.image || "http://via.placeholder.com/160"}
              alt="User"
            />
            <UserIdAndName>
              <span>{member.name}</span>
              <span>{member.email}</span>
            </UserIdAndName>
          </UserContainer>
          {[
            { icon: <FaHome />, label: "Home", to: "/home" },
            { icon: <FaClipboardList />, label: "Boards", to: "/boards" },
            { icon: <FaRegNewspaper />, label: "News", to: "/news" },
            { icon: <CgProfile />, label: "Members", to: "/members" },
            { icon: <BiCameraMovie />, label: "Movies", to: "/movies" },
            { icon: <LuListTodo />, label: "ChatList", to: "/chat" },
          ].map((item, index) => (
            <StyledMenuItem key={index}>
              <MenuIcon>{item.icon}</MenuIcon>
              <StyledLink to={item.to}>{item.label}</StyledLink>
            </StyledMenuItem>
          ))}
        </StyledMenuList>
      </StyledSideMenu>
      <main className="body">
        <Outlet />
      </main>
    </Container>
  );

};

export default Layout;