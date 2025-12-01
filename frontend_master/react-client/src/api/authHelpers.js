// src/api/authHelpers.js

export function getAuthUser() {
  try {
    const token = localStorage.getItem("token");
    if (!token) return null;

    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload; // contains userId, role, email, etc.
  } catch (err) {
    console.error("Invalid token:", err);
    return null;
  }
}
