import React, { useEffect } from "react";
import { useEditor, EditorContent, EditorProvider } from "@tiptap/react";
import { Button } from "react-bootstrap";
import { BiHeading, BiBold, BiItalic, BiStrikethrough } from "react-icons/bi";
import { AiOutlineOrderedList } from "react-icons/ai";

import StarterKit from "@tiptap/starter-kit";
import Heading from "@tiptap/extension-heading";
import Superscript from "@tiptap/extension-superscript";
import Link from "@tiptap/extension-link";
import Blockquote from "@tiptap/extension-blockquote";
import Code from "@tiptap/extension-code";
import CodeBlock from "@tiptap/extension-code-block";
import BulletList from "@tiptap/extension-bullet-list";
import OrderedList from "@tiptap/extension-ordered-list";
import ListItem from "@tiptap/extension-list-item";
import TextAlign from "@tiptap/extension-text-align";

const content = `

`;

const extensions = [
  StarterKit,
  Heading,
  Superscript,
  Link,
  Blockquote,
  Code,
  CodeBlock.configure({
    HTMLAttributes: {
      class: "rounded-sm bg-neutral-200 p-2",
    },
  }),
  BulletList,
  OrderedList,
  ListItem,
  TextAlign,
];

const TextEditor = () => {
  const editor = useEditor({
    extensions: extensions,
    content: content,
    onUpdate: ({ editor }) => {
      content = editor.getJSON();
      console.log(content);
      sendContentToServer(content);
    },
  });
  useEffect(() => {
    const unsubscribe = RecieveFromServer((content) => {
      editor.commands.setContent(content);
    });
    // Clean up the listener when the component is unmounted/deleted
    return unsubscribe;
  }, [editor]);
  return (
    <div className="container border mt-4 vh-100 w-50">
      <MenuBar editor={editor} />
      <EditorContent editor={editor} className="border-none" />
    </div>
  );
};
let ws = new WebSocket('ws://localhost:8000/ws'); 
function sendContentToServer(content) {
  const message = {
    type: 'edit',
    content: content,
    // userId: userId, //// TODO: Add the userId here
  };
  ws.send(JSON.stringify(message));
}

function RecieveFromServer(callback) {
  // Set up a listener for updates from the server
  ws.onopen = () => {
  ws.onmessage = (event) => {
    const message = JSON.parse(event.data);
    if (message.type === 'edit') {
      callback(message.content);
    }
  };
  };
  // Return a function that removes the listener
  return () => {
    ws.onmessage = null;
  };
}
export default TextEditor;

const MenuBar = ({ editor }) => {
  if (!editor) {
    return null;
  }
  return (
    <div className="d-flex justify-content-around align-items-center container mb-4 mt-4">
      <Button
        variant="outline-dark"
        size="sm"
        onClick={() => editor.chain().focus().toggleHeading({ level: 1 }).run()}
        className={
          editor.isActive("heading", { level: 1 }) ? "btn-primary" : ""
        }
      >
        <BiHeading size={20} />
      </Button>
      <Button
        variant="outline-dark"
        size="sm"
        onClick={() => editor.chain().focus().toggleBold().run()}
        className={editor.isActive("bold") ? "btn-primary" : ""}
      >
        <BiBold size={20} />
      </Button>
      <Button
        variant="outline-dark"
        size="sm"
        onClick={() => editor.chain().focus().toggleItalic().run()}
        className={editor.isActive("italic") ? "btn-primary" : ""}
      >
        <BiItalic size={20} />
      </Button>
      <Button
        variant="outline-dark"
        size="sm"
        onClick={() => editor.chain().focus().toggleStrike().run()}
        className={editor.isActive("strike") ? "btn-primary" : ""}
      >
        <BiStrikethrough size={20} />
      </Button>
      <Button
        variant="outline-dark"
        size="sm"
        onClick={() => editor.chain().focus().toggleOrderedList().run()}
        className={editor.isActive("orderedList") ? "btn-primary" : ""}
      >
        <AiOutlineOrderedList size={20} />
      </Button>
      {/* Add more buttons for other functionality as needed */}
    </div>
  );
};
