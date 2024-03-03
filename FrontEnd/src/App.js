import { useState } from "react";
import axios from "axios";
import TextEditor from "../src/components/textEditor";
import Login from "./components/login";
import Signup from "./components/signup";
import Navbar from "../src/components/navbar";

function App() {
  return (
    <>
      <Navbar />
      <TextEditor />
    </>
  );
}

export default App;
