import { useState } from "react";
import axios from "axios";
import Signup from "./components/signup";
import Navbar from "../src/components/navbar";
import Document from "./components/Document"; 
import TextEditor from './components/textEditor'; 

function App() {
  return (
    <>
      <Navbar />
      <Document />
      {/* <TextEditor /> */}
    </>
  );
}

export default App;