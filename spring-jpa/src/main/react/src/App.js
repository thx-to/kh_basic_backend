import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import GlobalStyles from "./styles/GlobalStyles";
import UserStore from "./context/UserStore";
import Login from "./pages/signup/Login";
import Signup from "./pages/signup/Signup";
import Home from "./pages/Home";
import Layout from "./pages/Layout";
import ChatList from "./pages/chatting/ChatList";
import ChatRoomCreate from "./pages/chatting/ChatRoomCreate";
import Chatting from "./pages/chatting/Chatting";

function App() {
  return (
    <>
      <GlobalStyles />
      <UserStore>
        <Router>
          <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route element={<Layout />}>
              <Route path="/home" element={<Home />} />
              <Route path="/chat" element={<ChatList />} />
              <Route path="/chat-create" element={<ChatRoomCreate />} />
              <Route path="/chatting/:roomId" element={<Chatting />} />
            </Route>
          </Routes>
        </Router>
      </UserStore>
    </>
  );
}

export default App;
