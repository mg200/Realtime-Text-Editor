import React, { useEffect, useState, useContext } from "react";
import { SiMdnwebdocs } from "react-icons/si";
import { FaThList } from "react-icons/fa";
import { IoMdLogIn } from "react-icons/io";
import { FaSignInAlt } from "react-icons/fa";
import { RiFileEditFill } from "react-icons/ri";
import { AuthContext } from "./AuthProvider";
import { Link } from "react-router-dom";
import NameBar from "./nameBar";
export default function Navbar() {
  // check is loged in or not ?
  // const [isLogedIn, setIslogedIn] = useState();
  // const [loged, setLoged] = useState(false);
  // useEffect(() => {
  //   localStorage.getItem("token");
  // }, []);
  // const { isAuthenticated, logout } = useState(true);
  const { isAuthenticated, logout } = useContext(AuthContext);
  const userName = localStorage.getItem("username");
  console.log("userName in navbar", userName);
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container-fluid">
        <Link className="navbar-brand text-primary ms-5" to="/">
          <SiMdnwebdocs size={50} />
          <span> HMAM </span>
        </Link>

        {!isAuthenticated ? (
          <>
            <button
              className="navbar-toggler text-primary me-5"
              type="button"
              data-mdb-toggle="collapse"
              data-mdb-target="#navbarSupportedContent"
              aria-controls="navbarSupportedContent"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              {/* <OptionList /> */}
            </button>
            <div
              className="collapse navbar-collapse"
              id="navbarSupportedContent"
            >
              <ul className="navbar-nav ms-auto d-flex flex-row mt-3 mt-lg-0">
                <li className="nav-item text-center mx-2 mx-lg-1">
                  <Link to="/login" className="nav-link">
                    <div>
                      <i className="fas fa-bell fa-lg mb-1"></i>
                      <span className="badge rounded-pill badge-notification bg-info">
                        Login
                      </span>
                    </div>
                    <IoMdLogIn />
                  </Link>
                </li>
                <li className="nav-item text-center mx-2 mx-lg-1">
                  <Link className="nav-link" to="/signup">
                    <div>
                      <i className="fas fa-bell fa-lg mb-1"></i>
                      <span className="badge rounded-pill badge-notification bg-info">
                        Sign up
                      </span>
                    </div>
                    <FaSignInAlt />
                  </Link>
                </li>
              </ul>
            </div>
          </>
        ) : (
          <div className="nav-link" onClick={logout}>
            <div>
              <i className="fas fa-bell fa-lg mb-1"></i>
              <span className="badge rounded-pill badge-notification bg-info">
                Logout
              </span>
            </div>
            <IoMdLogIn />
            {/* <div className="ms-2">
              {userName}
            </div> */}
            <div className="ms-2 d-flex align-items-center">
              <div style={{
                width: '40px',
                height: '40px',
                borderRadius: '50%',
                backgroundColor: '#ccc',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                marginRight: '5px',
                fontSize: '20px',
                fontWeight: 'bold',
                backgroundColor: 'white',
              }}>
                {userName.charAt(0).toUpperCase()}
              </div>
              {userName}
            </div>
          </div>
        )}
      </div>
    </nav>
  );
}
