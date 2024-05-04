import React, { useState, useContext } from "react";
import DocumentList from "./DocumentList";
import CreateDocumentForm from "./CreateDocument";
import { Button } from "react-bootstrap";
import TextEditor from "./textEditor";
import { AuthContext } from "./AuthProvider";
import Login from "./login";
import { useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function App() {
  const navigate = useNavigate();
  useEffect(() => {
    // Fetch owned documents when component mounts
    fetchOwnedDocuments();
    fetchSharedDocuments();
  }, []);
  async function fetchSharedDocuments() {
    const token = localStorage.getItem("token");
    try {
      const res = await axios.get(`http://localhost:8000/dc/viewShared`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log("Fetched shared documents:", res.data);
      // Update your state with the fetched documents here
      setSharedDocuments(
        res.data.map((doc) => ({
          id: doc.id,
          title: doc.title,
          content: doc.content,
        }))
      );
    } catch (error) {
      console.error("Error fetching shared documents:", error);
    }
  }
  async function fetchOwnedDocuments() {
    const token = localStorage.getItem("token");
    try {
      const res = await axios.get(`http://localhost:8000/dc/viewAll`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log("Fetched owned documents:", res.data);
      // Update your state with the fetched documents here
      setOwnedDocuments(
        res.data.map((doc) => ({
          id: doc.id,
          title: doc.title,
          content: doc.content,
        }))
      );
    } catch (error) {
      if (error.response.status === 403) {
        alert("Your Session expired. Logging out...");
        // console.error("Token expired. Logging out...");
        logout();
      } else {
        console.error("Error fetching owned documents:", error);
      }
    }
  }
  const [ownedDocuments, setOwnedDocuments] = useState([]);
  const [sharedDocuments, setSharedDocuments] = useState([]);
  const { isAuthenticated, logout } = useContext(AuthContext);
  const [modalShow, setModalShow] = useState(false);

  const handleOpenModal = () => setModalShow(true);
  const handleCloseModal = () => setModalShow(false);

  const handleCreate = (documentTitle) => {
    const newDocument = { id: Date.now(), title: documentTitle };
    setOwnedDocuments([...ownedDocuments, newDocument]);
    fetchOwnedDocuments();
  };
  async function handleDelete(documentId) {
    const token = localStorage.getItem("token");

    try {
      const res = await axios.delete(
        `http://localhost:8000/dc/delete/${documentId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      console.log("Document deleted:", res.data);
      // Update your state with the deleted document here
      setOwnedDocuments(ownedDocuments.filter((doc) => doc.id !== documentId));
    } catch (error) {
      console.error("Error deleting document:", error);
    }
  }

  async function handleRename(documentId, newName) {
    const token = localStorage.getItem("token");

    try {
      const res = await axios.put(
        `http://localhost:8000/dc/rename/${documentId}`,
        { title: newName },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      console.log("Document renamed:", res.data);
      // Update your state with the renamed document here
      setOwnedDocuments((docs) =>
        docs.map((doc) =>
          doc.id === documentId ? { ...doc, title: newName } : doc
        )
      );
    } catch (error) {
      console.error("Error renaming document:", error);
    }
  }

  async function handleShare(documentId, username, permission) {
    const token = localStorage.getItem("token");

    try {
      const res = await axios.post(
        `http://localhost:8000/dc/share/${documentId}`,
        { username: username, permission: permission },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      console.log("Document shared:", res.data);
      // Update your state with the shared document here
      // setSharedDocuments([...sharedDocuments, res.data]);
    } catch (error) {
      console.error("Error sharing document:", error);
    }
  }

  const handleOpen = async (documentId) => {
    navigate(`/dc/view/${documentId}`);
  };

  return (
    <>
      {!isAuthenticated ? (
        <Login />
      ) : (
        <div className="container">
          <div className="container mt-4">
            <h1 className="border-bottom border-dark mb-4 mt-2 pb-2">
              Dashboard
            </h1>
            <Button variant="primary" onClick={handleOpenModal}>
              Create New Document
            </Button>
            <CreateDocumentForm
              show={modalShow}
              handleClose={handleCloseModal}
              onCreate={handleCreate}
            />
          </div>
          <h2>Owned Documents</h2>
          <DocumentList
            documents={ownedDocuments}
            onDelete={handleDelete}
            onRename={handleRename}
            onShare={handleShare}
            onOpen={handleOpen}
          />

          <h2>Shared With You</h2>
          <DocumentList
            documents={sharedDocuments}
            onDelete={handleDelete}
            onRename={handleRename}
            onShare={handleShare}
            onOpen={handleOpen}
          />
        </div>
      )}
    </>
  );
}

export default App;
