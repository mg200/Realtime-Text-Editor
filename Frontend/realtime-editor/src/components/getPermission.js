// async function getUserAccessLevel(token, id) {
//     const response = await fetch(`http://your-api-url.com/getUserAccessLevel/${id}`, {
//       method: 'GET',
//       headers: {
//         'Authorization': `Bearer ${token}`
//       }
//     });
  
//     if (!response.ok) {
//       throw new Error(`HTTP error! status: ${response.status}`);
//     }
  
//     return await response.text();
//   }

import axios from 'axios';

async function getUserAccessLevel(token, id) {
  try {
    const response = await axios.get(
    process.env.REACT_APP_API_URL + `/dc/getUserAccessLevel/${id}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    return response.data;
  } catch (error) {
    console.error(`HTTP error! status: ${error.response.status}`);
    throw error;
  }
}

export default getUserAccessLevel;