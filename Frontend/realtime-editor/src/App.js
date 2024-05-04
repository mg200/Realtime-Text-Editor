import { useState } from "react";
import Signup from "./components/signup";
import Navbar from "../src/components/navbar";
import Login from "./components/login";
import MainFeed from "./components/mainfeed";
import TextEditor from "./components/textEditor";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "react-query";

const queryClient = new QueryClient();
function App() {
  const [option, setOption] = useState("");
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <Navbar />

        <Routes>
          {/* <Route path="/" element={<Navigate to="/home" />} />
        <Route path="/home" element={option === "signup" ? <Signup /> : null} />
// <Route path="/home" element={option === "login" ? <Login /> : null} /> */}
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/" element={<MainFeed />} />
          <Route path="dc/view/:documentId" element={<TextEditor />} />
        </Routes>
      </Router>
    </QueryClientProvider>
  );
}

export default App;
