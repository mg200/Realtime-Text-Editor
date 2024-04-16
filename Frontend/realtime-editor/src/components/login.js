import React from "react";
import axios from "axios";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import AlertBox from "./alretBox";

export default function Login() {
  const validationSchema = Yup.object().shape({
    email: Yup.string()
      .email("Invalid email format")
      .required("Email is required"),
    password: Yup.string()
      .min(6, "Password must be at least 6 characters")
      .required("Password is required"),
  });

  async function handleSubmit(values, { setSubmitting }) {
    try {
      const res = await axios.post("http://localhost:3001/login", values);
      console.log(res.data);
    } catch (error) {
      console.error("Error:", error);
    }
    setSubmitting(false);
  }

  return (
    <section style={{ backgroundColor: "hsl(0, 0%, 96%)" }}>
      <div className="container w-full">
        <div className="row gx-lg-5 align-items-center  vh-100">
          <div className="col-lg-6 mb-5 mb-lg-0">
            <h1 className="my-5 display-3 fw-bold ls-tight">
              Login To <br />
              <span className="text-primary">HMAM DOCS</span>
            </h1>
          </div>

          <div className="col-lg-6 mb-5 mb-lg-0">
            <div className="card">
              <div className="card-body py-5 px-md-5">
                <Formik
                  initialValues={{
                    email: "",
                    password: "",
                  }}
                  validationSchema={validationSchema}
                  onSubmit={handleSubmit}
                >
                  {({ isSubmitting }) => (
                    <Form>
                      <div className="form-outline mb-4">
                        <label className="form-label" htmlFor="email">
                          Email address
                        </label>
                        <Field
                          type="email"
                          name="email"
                          className="form-control"
                        />
                        <ErrorMessage name="email" component={AlertBox} />
                      </div>
                      <div className="form-outline mb-4">
                        <label className="form-label" htmlFor="password">
                          Password
                        </label>
                        <Field
                          type="password"
                          name="password"
                          className="form-control"
                        />
                        <ErrorMessage name="password" component={AlertBox} />
                      </div>
                      <button
                        type="submit"
                        className="btn btn-primary btn-block mb-4"
                        disabled={isSubmitting}
                      >
                        Submit
                      </button>
                    </Form>
                  )}
                </Formik>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
