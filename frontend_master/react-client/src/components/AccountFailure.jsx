import React from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function AccountFailure() {
  const { search } = useLocation();
  const navigate = useNavigate();
  const params = new URLSearchParams(search);
  const status = params.get("status");
  const msg = params.get("msg");

  const details = [status ? `Status: ${status}` : null, msg || null].filter(Boolean).join(" • ");

  return (
    <div className="login-box">
      <h2>Account creation failed</h2>
      <p className="note" style={{ margin: "8px 0 16px" }}>
        Something went wrong while creating your account. This could be due to a network issue,
        duplicate email/ID, or a server error.
      </p>

      {details && (
        <div className="note" style={{ wordBreak: "break-word", marginBottom: 12 }}>
          {details}
        </div>
      )}

      <div style={{ display: "flex", gap: 12, justifyContent: "center", flexWrap: "wrap" }}>
        <button className="login-btn" onClick={() => navigate("/register")}>Try Again</button>
        <a className="signup-link" href="/login" onClick={(e) => { e.preventDefault(); navigate("/login"); }}>
          Back to Login
        </a>
      </div>

      <details style={{ marginTop: 12, opacity: 0.9, color: "#fff" }}>
        <summary style={{ color: "#fff" }}>Troubleshooting tips</summary>
        <ul style={{ marginTop: 8, textAlign: "left", lineHeight: 1.4, color: "#fff" }}>
          <li>Use a valid email address and a password with at least 8 characters.</li>
          <li>Make sure your Student ID isn't already registered.</li>
          <li>If the server is down, try again later or contact support.</li>
        </ul>
      </details>
    </div>
  );
}
