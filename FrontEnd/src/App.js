import { useState } from "react";
import axios from "axios";
import TextEditor from "../src/components/textEditor";
import Login from "./components/login";
import Signup from "./components/signup";
import Navbar from "../src/components/navbar";

function App() {
  const [option, setOption] = useState("");
  return (
    <>
      <Navbar setOption={setOption} />
      {option === "signup" ? <Signup /> : null}
      {option === "login" ? <Login /> : null}
      {option === "texteditor" ? <TextEditor /> : null}
    </>
  );
}

export default App;
