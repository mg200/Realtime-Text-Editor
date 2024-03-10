import { useState } from "react";
import axios from "axios";
import AlertBox from "./alretBox";

export default function Signup() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();

    try {
      if (!email || !password) {
        return;
      }

      const user = {
        email: email,
        password: password,
      };
      const res = await axios.post("http://localhost:3001/signup", user);
      console.log(res.data);

      if (!res.ok) {
        console.log("error");
      } else {
        console.log(user);
      }
    } catch (error) {
      console.error("Error:", error);
    }
  }

  return (
    <section
      className="container"
      style={{ backgroundColor: "hsl(0, 0%, 96%)" }}
    >
      <div className="px-4 py-5 px-md-5 text-center text-lg-start">
        <div className="container">
          <div className="row gx-lg-5 align-items-center">
            <div className="col-lg-6 mb-5 mb-lg-0">
              <h1 className="my-5 display-3 fw-bold ls-tight">
                Sign up <br />
                <span className="text-primary">HMAM DOCS</span>
              </h1>
              <p style={{ color: "hsl(217, 10%, 50.8%)" }}></p>
            </div>

            <div className="col-lg-6 mb-5 mb-lg-0">
              <div className="card">
                <div className="card-body py-5 px-md-5">
                  <form onSubmit={handleSubmit}>
                    <div className="row">
                      <div className="col-md-6 mb-4"></div>
                    </div>

                    <div className="form-outline mb-4">
                      <label className="form-label" htmlFor="form3Example3">
                        First Name
                      </label>
                      <input
                        type="email"
                        className="form-control"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                      />
                      <div className="text-danger">
                        {!firstName ? (
                          <AlertBox>Please fill First Name!</AlertBox>
                        ) : null}
                      </div>
                    </div>

                    <div className="form-outline mb-4">
                      <label className="form-label" htmlFor="form3Example3">
                        Last Name
                      </label>
                      <input
                        type="email"
                        className="form-control"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                      />
                      <div className="text-danger">
                        {!firstName ? (
                          <AlertBox>Please fill Last Name!</AlertBox>
                        ) : null}
                      </div>
                    </div>

                    <div className="form-outline mb-4">
                      <label className="form-label" htmlFor="form3Example3">
                        Email address
                      </label>
                      <input
                        type="email"
                        className="form-control"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                      />
                      <div className="text-danger">
                        {!email ? (
                          <AlertBox>Please fill Email!</AlertBox>
                        ) : null}
                      </div>
                    </div>
                    <div className="form-outline mb-4">
                      <label className="form-label" htmlFor="form3Example4">
                        Password
                      </label>
                      <input
                        type="password"
                        value={password}
                        className="form-control"
                        onChange={(e) => setPassword(e.target.value)}
                      />

                      <div className="text-danger">
                        {!password ? (
                          <AlertBox>Please fill Password!</AlertBox>
                        ) : null}
                      </div>
                    </div>
                    <button
                      type="submit"
                      className="btn btn-primary btn-block mb-4"
                      onClick={handleSubmit}
                    >
                      Submit
                    </button>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
