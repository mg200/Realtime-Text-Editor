import React from "react";
import { Modal, Button } from "react-bootstrap";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";

function Share({ show, handleClose, HandleSubmit }) {
  const initialValues = {
    username: "",
    permission: "",
  };

  const validationSchema = Yup.object({
    username: Yup.string().required("Username is required"),
    permission: Yup.string().required("Permission is required"),
  });

  const handleSubmit = (values, { resetForm }) => {
    HandleSubmit(values.username, values.permission);
    resetForm();
    handleClose();
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Share :</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Formik
          initialValues={initialValues}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
        >
          <Form>
            <div className="mb-3">
              <label htmlFor="username" className="form-label">
                Username
              </label>
              <Field type="text" name="username" className="form-control" />
              <ErrorMessage
                name="username"
                component="div"
                className="text-danger"
              />
            </div>
            <div className="mb-3">
              <label htmlFor="permission" className="form-label">
                Permission
              </label>
              <Field as="select" name="permission" className="form-control">
                <option value="">Select permission</option>
                <option value="viewer">Viewer</option>
                <option value="editor">Editor</option>
              </Field>
              <ErrorMessage
                name="permission"
                component="div"
                className="text-danger"
              />
            </div>
            <Button type="submit" variant="primary">
              Share
            </Button>
          </Form>
        </Formik>
      </Modal.Body>
    </Modal>
  );
}

export default Share;