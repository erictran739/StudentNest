import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";


export default function Login() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [remember, setRemember] = useState(false);
  const [status, setStatus] = useState("");

  const handleSubmit = async (e) => {
  e.preventDefault();
  setStatus("Submitting...");

  try {
    const res = await fetch("https://puggu.dev/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email: username, password: password }),
    });

    const data = await res.json().catch(() => ({}));

    if (!res.ok) {
      setStatus(data.message || `Login failed (${res.status})`);
      return;
    }

    setStatus("Login successful!");
    navigate("/home");

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


        <div className="options-row">
          <label className="remember-me">
            <input
              type="checkbox"
              checked={remember}
              onChange={(e) => setRemember(e.target.checked)}
            />
            Remember Me
          </label>

          {/* Route to the page instead of no-op */}
          <Link to="/forgot-password" className="forgot-link">
            Forgot Password?
          </Link>
        </div>

        <button type="submit" className="login-btn">Login</button>

        <div className="signup-row">
          Don’t have an account?
          <Link to="/register" className="signup-link">
            Create One!
          </Link>
        </div>
      </form>

      {status && <div className="note" style={{ marginTop: 10 }}>{status}</div>}
    </div>
  );
}
