import React, { useEffect, useState, useContext } from "react";
import { SiMdnwebdocs } from "react-icons/si";
import { FaThList } from "react-icons/fa";
import { IoMdLogIn } from "react-icons/io";
import { FaSignInAlt } from "react-icons/fa";
import { RiFileEditFill } from "react-icons/ri";
import { AuthContext } from "./AuthProvider";
import { Link } from "react-router-dom";
import { useLocation } from "react-router-dom";
import NameBar from "./nameBar";
// const [docName, setDocName] = useState('');

export default function Navbar() {
  const { isAuthenticated, logout } = useContext(AuthContext);
  const userName = localStorage.getItem("username");
  const location = useLocation();
  console.log("userName in navbar", userName);
  // async function getDocName(id) {
  //   if (!isAuthenticated || !location.pathname.includes('dc/view')) {
  //     return;
  //   }

  //   const response = await fetch(process.env.REACT_APP_API_URL+`getDocName/${id}`);

  //   if (!response.ok) {
  //     throw new Error(`HTTP error! status: ${response.status}`);
  //   }

  //   const doc = await response.json();

  //   return doc.name;
  // }

  // const getDocName = async (id) => {
  //   if (!isAuthenticated || !location.pathname.includes('dc/view')) {
  //     return;
  //   }

  //   const response = await fetch( `/getDocName/${id}`);

  //   if (!response.ok) {
  //     throw new Error(`HTTP error! status: ${response.status}`);
  //   }

  //   const doc = await response.json();

  //   return doc.name;
  // }

  

  // useEffect(() => {
  //   getDocName(documentId).then(setDocName);
  // }, [documentId, isAuthenticated, location]);



  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container-fluid">
        <Link className="navbar-brand text-primary ms-5" to="/">
          <SiMdnwebdocs size={50} />
          <span> HMAM </span>
        </Link>

        {!isAuthenticated ? (
          // ... your code for the not authenticated state
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
          <div className="d-flex align-items-center">
            {isAuthenticated && location.pathname.includes('dc/view') && (
              <div style={{ marginRight: '20px' }}>
                <input type="text" value="Hello World" readOnly />
              </div>
            )}
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
            }} title={userName}>
              {userName ? userName.charAt(0).toUpperCase() : ''}
            </div>
            {userName}

            <div className="nav-link" onClick={logout}>
              <div>
                <i className="fas fa-bell fa-lg mb-1"></i>
                <span className="badge rounded-pill badge-notification bg-info" style={{ height: '40px', display: 'inline-block', lineHeight: '30px', marginLeft: '-10px', marginTop: '10px' }}>
                  Logout
                </span>
              </div>
              <IoMdLogIn />
            </div>

          </div>
        )}
      </div>
    </nav>
  );
}
