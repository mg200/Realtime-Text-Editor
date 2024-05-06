import { io } from "socket.io-client";

// "undefined" means the URL will be computed from the `window.location` object
const URL = "http://hmamdocs.me/api/topic";

export const socket = io(URL);
