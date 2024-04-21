// import React, { useState } from "react";
// import { EditorState, convertToRaw, convertFromRaw } from "draft-js";
// import { Editor } from "react-draft-wysiwyg";
// import "react-draft-wysiwyg/dist/react-draft-wysiwyg.css";

// const TextEditor = () => {
//   const [editorState, setEditorState] = useState(() =>
//     EditorState.createEmpty()
//   );

//   const onEditorStateChange = (editorState) => {
//     setEditorState(editorState);
//   };

//   return (
//     <div>
//       <Editor
//         editorState={editorState}
//         onEditorStateChange={onEditorStateChange}
//       />
//       <button
//         onClick={() =>
//           console.log(convertToRaw(editorState.getCurrentContent()))
//         }
//       >
//         Save
//       </button>
//     </div>
//   );
// };

// export default TextEditor;
