import React, { useState } from "react";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [remember, setRemember] = useState(false);
  const [status, setStatus] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus("Submitting...");

    try {
      const res = await fetch("/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: username, password })
      });

      // Some backends return empty body on 401/204 → guard json()
      const ct = res.headers.get("content-type") || "";
      let data = null;
      if (ct.includes("application/json")) {
        data = await res.json();
      } else {
        const text = await res.text();
        data = text ? { message: text } : {};
      }

      if (!res.ok || (data && data.ok === false)) {
        setStatus((data && data.message) || `Login failed (${res.status})`);
        return;
      }

      if (data && data.token) localStorage.setItem("authToken", data.token);
      window.location.href = "/Profile.html"; // (swap to React Router later)
    } catch (err) {
      setStatus("Network error: " + err.message);
    }
  };

  return (
    <div className="login-box">
      <h2>StudentNest Login</h2>

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        {/* Remember + Forgot row (matches your CSS) */}
        <div className="options-row">
          <label className="remember-me">
            <input
              type="checkbox"
              checked={remember}
              onChange={(e) => setRemember(e.target.checked)}
            />
            Remember Me
          </label>

          <a
            href="#"
            className="forgot-link"
            onClick={(e) => { e.preventDefault(); setStatus("Forgot Password clicked (no-op)"); }}
          >
            Forgot Password?
          </a>
        </div>

        <button type="submit" className="login-btn">Login</button>

        <div className="signup-row">
          Don’t have an account?
          <a href="#" className="signup-link" onClick={(e) => e.preventDefault()}>
            Create One!
          </a>
        </div>
      </form>

      {status && <div className="note" style={{ marginTop: 10 }}>{status}</div>}
    </div>
  );
}
