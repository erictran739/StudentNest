import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function AccountSuccess() {
  const navigate = useNavigate();
  const { search } = useLocation();
  const params = new URLSearchParams(search);
  const next = params.get("next") || "/login";

  const [count, setCount] = useState(4);

  useEffect(() => {
    const t = setInterval(() => {
      setCount((c) => {
        const n = c - 1;
        if (n <= 0) {
          clearInterval(t);
          navigate(next);
        }
        return n;
      });
    }, 1000);
    return () => clearInterval(t);
  }, [navigate, next]);

  return (
    <div className="login-box" style={{ textAlign: "center" }}>
      <h2>Account successfully created 🎉</h2>
      <p className="note" style={{ margin: "8px 0 16px" }}>
        Redirecting to login in <strong>{count}s</strong>…
      </p>
      <div style={{ display: "flex", gap: 12, justifyContent: "center" }}>
        <button className="login-btn" onClick={() => navigate(next)}>Go to Login</button>
        <a className="signup-link" href="/" onClick={(e) => { e.preventDefault(); navigate("/"); }}>
          Back to Home
        </a>
      </div>
    </div>
  );
}
