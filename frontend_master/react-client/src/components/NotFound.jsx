import React from "react";
import { useNavigate } from "react-router-dom";

export default function NotFound() {
  const nav = useNavigate();
  return (
    <div className="home-page">
      <main className="system-center">
        <div className="system-box">
          <h1 style={{ marginBottom: 8 }}>404 — Page Not Found</h1>
          <p style={{ color: "#6b6b6b" }}>
            The page you’re looking for doesn’t exist or has moved.
          </p>
          <button className="login-btn" style={{ marginTop: 16 }} onClick={() => nav("/login")}>
            Go back to Login
          </button>
        </div>
      </main>
    </div>
  );
}
