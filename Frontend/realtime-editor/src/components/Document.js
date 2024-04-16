import React, { useState, useEffect } from "react";
import TextEditor from "./textEditor";
import { EditorState, convertToRaw, convertFromRaw } from "draft-js";
import addPageIcon from "./images/plus.png";

const Document = () => {
  const [pages, setPages] = useState([""]);
  const [activePage, setActivePage] = useState(0);
  const [editorState, setEditorState] = useState(() => {
    const storedState = localStorage.getItem("PageContent");
    if (storedState) {
      return EditorState.createWithContent(
        convertFromRaw(JSON.parse(storedState))
      );
    }
    return EditorState.createEmpty();
  });

  useEffect(() => {
    const contentState = editorState.getCurrentContent();
    const rawContentState = convertToRaw(contentState);
    const key = String(activePage);
    localStorage.setItem("PageContent", JSON.stringify(rawContentState));
  }, [editorState, activePage]);

  useEffect(() => {
    const storedState = localStorage.getItem("PageContent");
    setEditorState(
      EditorState.createWithContent(convertFromRaw(JSON.parse(storedState)))
    );
  }, [activePage]);

  const addPage = () => {
    setPages((prevPages) => [...prevPages, ""]);
    setActivePage(pages.length);
  };

  const goToPage = (index) => {
    setActivePage(index);
  };

  const handleTextChange = (pageIndex, newText) => {
    setPages(
      pages.map((text, index) => (index === pageIndex ? newText : text))
    );
  };

  return (
    <div className="Document">
      <TextEditor
        text={pages[activePage]}
        onTextChange={(newText) => handleTextChange(activePage, newText)}
        setEditorState={setEditorState}
        editorState={editorState}
      />
      <footer className="footer btn btn-primary">
        {pages.map((pageText, index) => (
          <button
            key={index + 1000}
            onClick={() => goToPage(index + 1000)}
            className="page-button btn  ms-2"
          >
            Page {index + 1}
          </button>
        ))}
        <button onClick={addPage} className="add-page-button">
          <img src={addPageIcon} alt="Add page" className="add-page-icon" />
        </button>
      </footer>
    </div>
  );
};

export default Document;
