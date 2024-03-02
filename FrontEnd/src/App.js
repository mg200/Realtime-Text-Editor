import { useState } from "react";
import axios from 'axios';

function App() {
  return <Login />;
}

export default App;

function Login() {
  const [email, setEmail] = useState();
  const [password, setPassword] = useState();

  function handleSubmit() {
    if (!email || !password) {
      return;
    }

    const user = {
      email: email,
      password: password,
    };

    const res = fetch("");
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
                Login To <br />
                <span className="text-primary">HMAM DOCS</span>
              </h1>
              <p style={{ color: "hsl(217, 10%, 50.8%)" }}></p>
            </div>

            <div className="col-lg-6 mb-5 mb-lg-0">
              <div className="card">
                <div className="card-body py-5 px-md-5">
                  <form>
                    <div className="row">
                      <div className="col-md-6 mb-4"></div>
                    </div>
                    <div className="form-outline mb-4">
                      <input
                        type="email"
                        className="form-control"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                      />
                      <label className="form-label" htmlFor="form3Example3">
                        Email address
                      </label>
                      <div className="text-danger">
                        {!email ? (
                          <emptyGaps>Please fill Email!</emptyGaps>
                        ) : null}
                      </div>
                    </div>
                    <div className="form-outline mb-4">
                      <input
                        type="password"
                        value={password}
                        className="form-control"
                        onChange={(e) => setPassword(e.target.value)}
                      />
                      <label className="form-label" htmlFor="form3Example4">
                        Password
                      </label>
                      <div className="text-danger">
                        {!password ? (
                          <emptyGaps>Please fill Password!</emptyGaps>
                        ) : null}
                      </div>
                    </div>
                    <button
                      type="submit"
                      className="btn btn-primary btn-block mb-4"
                      onClick={handleSubmit()}
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

function emptyGaps({ childern }) {
  return <div>{childern}</div>;
}
