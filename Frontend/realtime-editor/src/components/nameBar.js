import axios from 'axios';
import { useEffect, useState } from 'react';
import { FormControl } from 'react-bootstrap';
let Username;
const NameBar = () => {
  // const [username, setUsername] = useState('');
  let username;
  const token = localStorage.getItem('token');

  useEffect(() => {
    console.log("@nameBar, token is ", token);
    const getUsernameFromToken = async () => {
      try {
        const res = await axios.get(
          `${process.env.REACT_APP_API_URL}/dc/getUsernameFromToken`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log("@nameBar, res.data is ", res.data); // 'res.data' is an object with a key 'username' and a value 'username
        // setUsername(res.data.username);
        username = res.data;
        Username = res.data;
        console.log("@nameBar, username has been set ", username);
      } catch (error) {
        console.log("@nameBar, error is ", error);
        console.error(error);
      }
    };

    getUsernameFromToken();
  }, [token]);

  return (
    // <div className="container border mt-4 vh-100 w-25">
    //   <input
    //     type="text"
    //     value={Username}
    //     readOnly
    //     className="ml-3"
    //   />
    // </div>
    <div className="container border mt-4 vh-100 w-25 position-relative">
      <input
        type="text"
        className="form-control position-absolute top-50 start-50 translate-middle"
        value={username} // Display username or allow editing
        // onChange={handleUsernameChange} // Handle input change
      />
      {/* Optional button for alternative display */}
      {/* <button className="btn btn-primary position-absolute top-50 start-50 translate-middle">
      {username}
    </button> */}
    </div>
  );
};

export default NameBar;



// import React, { useState, useEffect } from 'react';
// import axios from 'axios';

// const NameBar = () => {
//   const [username, setUsername] = useState(''); // State to store username
//   const token = localStorage.getItem('token');

//   useEffect(() => {
//     const getUsernameFromToken = async () => {
//       try {
//         const response = await axios.get(
//           `${process.env.REACT_APP_API_URL}/dc/getUsernameFromToken`,
//           {
//             headers: {
//               Authorization: `Bearer ${token}`,
//             },
//           }
//         );
//         setUsername(response.data.username); // Set username from API response
//       } catch (error) {
//         console.error("Error fetching username:", error);
//       }
//     };

//     getUsernameFromToken();
//   }, [token]); // Update only when token changes

//   const handleUsernameChange = (event) => {
//     setUsername(event.target.value); // Update username on input change
//   };

//   return (
//     <div className="container border mt-4 vh-100 w-25 position-relative">
//       <input
//         type="text"
//         className="form-control position-absolute top-50 start-50 translate-middle"
//         value={username} // Display username or allow editing
//         onChange={handleUsernameChange} // Handle input change
//       />
//       {/* Optional button for alternative display */}
//       {/* <button className="btn btn-primary position-absolute top-50 start-50 translate-middle">
//         {username}
//       </button> */}
//     </div>
//   );
// };

// export default NameBar;
