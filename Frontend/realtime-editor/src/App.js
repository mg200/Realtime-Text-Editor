import { useState } from "react";
import Signup from "./components/signup";
import Navbar from "../src/components/navbar";
import Login from "./components/login";
import MainFeed from "./components/mainfeed";
import TextEditor from "./components/textEditor";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

function App() {
  const [option, setOption] = useState("");
  return (
    <Router>
      <Navbar />

      <Routes>
        {/* <Route path="/" element={<Navigate to="/home" />} />
        <Route path="/home" element={option === "signup" ? <Signup /> : null} />
// <Route path="/home" element={option === "login" ? <Login /> : null} /> */}
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/" element={<MainFeed />} />
        <Route path="/textEditor" element={<TextEditor />} />
      </Routes>
    </Router>
  );
}

export default App;
