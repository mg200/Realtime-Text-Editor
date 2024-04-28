import React, { useState, useContext } from "react";
import DocumentList from "./DocumentList";
import CreateDocumentForm from "./CreateDocument";
import { Button } from "react-bootstrap";
import TextEditor from "./textEditor";
import { AuthContext } from "./AuthProvider";
import Login from "./login";

function App() {
  const [ownedDocuments, setOwnedDocuments] = useState([]);
  const [sharedDocuments, setSharedDocuments] = useState([]);
  const { isAuthenticated, logout } = useContext(AuthContext);
  const [modalShow, setModalShow] = useState(false);

  const handleOpenModal = () => setModalShow(true);
  const handleCloseModal = () => setModalShow(false);

  const handleCreate = (documentName) => {
    const newDocument = { id: Date.now(), name: documentName };
    setOwnedDocuments([...ownedDocuments, newDocument]);
  };

  const handleDelete = (documentId) => {
    setOwnedDocuments(ownedDocuments.filter((doc) => doc.id !== documentId));
  };

  const handleRename = (documentId, newName) => {
    if (newName) {
      setOwnedDocuments((docs) =>
        docs.map((doc) =>
          doc.id === documentId ? { ...doc, name: newName } : doc
        )
      );
    }
  };

  const handleShare = (documentId) => {
    // Handle sharing logic here
    alert(`Document with ID ${documentId} shared!`);
  };

  const handleOpen = (documentId) => {
    // Handle document opening logic here
    alert(`Opening document with ID ${documentId}`);
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

          <h2>Shared Documents</h2>
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
