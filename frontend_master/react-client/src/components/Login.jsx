import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const saveSession = (token, user) => {
  try {
    if (token) localStorage.setItem("authToken", token);
    if (user)  localStorage.setItem("authUser", JSON.stringify(user));
  } catch {}
};

const getToken = (data) => data?.token || data?.access_token || data?.jwt || null;
const getUser  = (data) => data?.user  || data?.profile || null;

export default function Login() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [status, setStatus]     = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus("Submitting...");

    try {
      const res = await fetch("/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: username, password })
      });

      const data = await res.json().catch(() => ({}));

      if (!res.ok) {
        setStatus(data.message || `Login failed (${res.status})`);
        return;
      }

      const tokenFromApi = getToken(data);
      const userFromApi  = getUser(data);

      const token = tokenFromApi || "dummy-token";
      const user  =
        userFromApi ||
        data ||
        { email: username };

      saveSession(token, user);

      setStatus("Login successful!");
      navigate("/home");
    } catch (err) {
      setStatus("Network error: " + (err?.message || "Unknown error"));
    }
  };

  return (
    <div className="auth-root">
      <style>{`
        .auth-root {
          min-height: 100vh;
          display: flex;
          align-items: center;
          justify-content: center;
          position: relative;
        }
        .auth-root .ring {
          position: relative; /* keep in normal flow */
          display: flex;
          align-items: center;
          justify-content: center;
          width: 100%;
          max-width: 520px;   /* limit width so it doesn't hug the right edge */
          margin: 0 auto;
        }
        .auth-root .login {
          position: static;   /* disable any absolute rules from theme */
          display: block;
          width: 100%;
        }
        .auth-root .login-box {
          margin: 0 auto;     /* center the card itself */
          width: 100%;
          max-width: 520px;
        }
      `}</style>

      <div className="ring" style={{ "--clr": "#ff6ec7" }}>
        <i></i><i></i><i></i>

        <div className="login">
          <div className="login-box">
            <h2>StudentNest Login</h2>

            <form onSubmit={handleSubmit}>
              <input
                type="text"
                placeholder="Email"
                required
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
              <input
                type="password"
                placeholder="Password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />

              <button type="submit" className="login-btn">Login</button>

              <div className="signup-row">
                Don’t have an account?
                <a
                  href="/register"
                  className="signup-link"
                  onClick={(e) => { e.preventDefault(); navigate("/register"); }}
                >
                  Create One!
                </a>
              </div>
            </form>

            {status && <div className="note" style={{ marginTop: 8 }}>{status}</div>}
          </div>
        </div>
      </div>
    </div>
  );
}
