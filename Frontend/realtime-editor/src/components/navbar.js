import React, { useEffect, useState, useContext } from "react";
import { SiMdnwebdocs } from "react-icons/si";
import { FaThList } from "react-icons/fa";
import { IoMdLogIn } from "react-icons/io";
import { FaSignInAlt } from "react-icons/fa";
import { RiFileEditFill } from "react-icons/ri";
import { AuthContext } from "./AuthProvider";
import { Link } from "react-router-dom";

export default function Navbar() {
  // check is loged in or not ?
  // const [isLogedIn, setIslogedIn] = useState();
  // const [loged, setLoged] = useState(false);
  // useEffect(() => {
  //   localStorage.getItem("token");
  // }, []);
  // const { isAuthenticated, logout } = useState(true);
  const { isAuthenticated, logout } = useContext(AuthContext);

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
          </div>
        )}
      </div>
    </nav>
  );
}

// function OptionList({ setOption }) {
//   const [isOpen, setIsOpen] = useState(false);

//   const toggleDropdown = () => {
//     setIsOpen(!isOpen);
//   };

//   return (
//     <div className="dropdown" onClick={toggleDropdown}>
//       <button
//         className="btn  dropdown-toggle text-primary"
//         aria-expanded={isOpen ? "true" : "false"}
//       >
//         <FaThList size={25} />
//       </button>
//       <ul className={`dropdown-menu${isOpen ? " show" : ""}`}>
//         <ul className="navbar-nav mt-3">
//           <li
//             className=" text-center mx-2 my-2 "
//             onClick={() => setOption("login")}
//           >
//             <div>
//               <i className="fas fa-bell fa-lg mb-1"></i>
//               <span className="badge rounded-pill badge-notification bg-info">
//                 Login
//               </span>
//             </div>
//           </li>
//           <li
//             className="nav-item text-center mx-2 my-2 mx-lg-1"
//             onClick={() => setOption("signup")}
//           >
//             <div>
//               <i className="fas fa-bell fa-lg mb-1"></i>
//               <span className="badge rounded-pill badge-notification bg-info">
//                 Sign up
//               </span>
//             </div>
//           </li>
//           <li
//             className="nav-item text-center my-2 mx-2 mx-lg-1"
//             onClick={() => setOption("texteditor")}
//           >
//             <div>
//               <i className="fas fa-bell fa-lg mb-1"></i>
//               <span className="badge rounded-pill badge-notification bg-info">
//                 Text Editor
//               </span>
//             </div>
//           </li>
//         </ul>
//       </ul>
//     </div>
//   );
// }
