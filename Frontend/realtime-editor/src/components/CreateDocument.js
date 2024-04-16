import React from "react";
import { Modal, Button } from "react-bootstrap";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";

function CreateDocumentModal({ show, handleClose, onCreate }) {
  const initialValues = {
    documentName: "",
  };

  const validationSchema = Yup.object({
    documentName: Yup.string().required("Document name is required"),
  });

  const handleSubmit = (values, { resetForm }) => {
    onCreate(values.documentName);
    resetForm();
    handleClose();
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Create New Document</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Formik
          initialValues={initialValues}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
        >
          <Form>
            <div className="mb-3">
              <label htmlFor="documentName" className="form-label">
                Document Name
              </label>
              <Field type="text" name="documentName" className="form-control" />
              <ErrorMessage
                name="documentName"
                component="div"
                className="text-danger"
              />
            </div>
            <Button type="submit" variant="primary">
              Create Document
            </Button>
          </Form>
        </Formik>
      </Modal.Body>
    </Modal>
  );
}

export default CreateDocumentModal;
