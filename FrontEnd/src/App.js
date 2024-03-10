import { useState } from "react";
import axios from "axios";
import Signup from "./components/signup";
import Navbar from "../src/components/navbar";
import Document from "./components/Document"; 
import TextEditor from './components/textEditor'; 

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