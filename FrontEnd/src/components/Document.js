import React, { useState } from 'react';
import TextEditor from './textEditor'; 
import addPageIcon from './images/plus.png';
import './Document.css';

const Document = () => {
  const [pages, setPages] = useState(['']); // Initialization with empty txt
  const [activePage, setActivePage] = useState(0);

  const addPage = () => {
    setPages(prevPages => [...prevPages, '']); // add paeg
    setActivePage(pages.length); // the new is the active
  };

  const goToPage = (index) => {
    setActivePage(index); //clicked is active
  };

  const handleTextChange = (pageIndex, newText) => {
    setPages(pages.map((text, index) => index === pageIndex ? newText : text));
  };

  return (
    <div className="Document">
     <TextEditor
  text={pages[activePage]}
  onTextChange={(newText) => handleTextChange(activePage, newText)}
/>
      <footer className="footer">
        {pages.map((pageText, index) => (
          <button key={index} onClick={() => goToPage(index)} className="page-button">
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