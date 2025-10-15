import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const navigate = useNavigate();

  const [studentId, setStudentId] = useState("");
  const [name, setName]           = useState("");
  const [email, setEmail]         = useState("");
  const [password, setPassword]   = useState("");
  const [confirm, setConfirm]     = useState("");
  const [role, setRole]           = useState("student");
  const [status, setStatus]       = useState("");
  const [busy, setBusy]           = useState(false);

  const validEmail = (v) => /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(v);

  const validate = () => {
    if (!studentId.trim()) return "Student ID is required.";
    if (!name.trim()) return "Name is required.";
    if (!validEmail(email)) return "Please enter a valid email.";
    if (password.length < 8) return "Password must be at least 8 characters.";
    if (password !== confirm) return "Passwords do not match.";
    return null;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const err = validate();
    if (err) { setStatus(err); return; }

    setBusy(true);
    setStatus("Creating your account...");

    try {
      const res = await fetch(`https://puggu.dev/auth/register/${role}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          student_id: studentId.trim(),
          name: name.trim(),
          email: email.trim(),
          password
        })
      });

      let data = {};
      try { data = await res.json(); } catch {}

      if (!res.ok || data.ok === false) {
        const s = res.status || "";
        const m = (data && (data.message || data.error)) || "Registration failed";
        navigate(`/account-failure?status=${encodeURIComponent(String(s))}&msg=${encodeURIComponent(m)}`);
        return;
      }

      // optional: if your API returns a token and you want to store it:
      if (data.token) localStorage.setItem("authToken", data.token);

      navigate("/account-success?next=/login");
    } catch (err) {
      navigate(`/account-failure?status=network&msg=${encodeURIComponent(err.message || "Network error")}`);
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="login-box">
      <h2>Create Your StudentNest Account</h2>

      <form onSubmit={handleSubmit} noValidate>
        <input
          type="text"
          placeholder="Student ID"
          required
          value={studentId}
          onChange={(e) => setStudentId(e.target.value)}
        />
        <input
          type="text"
          placeholder="Full Name"
          required
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <input
          type="email"
          placeholder="Email"
          required
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password (min 8 chars)"
          required
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          type="password"
          placeholder="Confirm Password"
          required
          value={confirm}
          onChange={(e) => setConfirm(e.target.value)}
        />

        <div className="options-row" style={{ justifyContent: "center", gap: 12 }}>
          <label className="remember-me">
            <input
              type="radio"
              name="role"
              value="student"
              checked={role === "student"}
              onChange={() => setRole("student")}
            />
            Student
          </label>
          <label className="remember-me">
            <input
              type="radio"
              name="role"
              value="teacher"
              checked={role === "teacher"}
              onChange={() => setRole("teacher")}
            />
            Teacher
          </label>
          <label className="remember-me">
            <input
              type="radio"
              name="role"
              value="admin"
              checked={role === "admin"}
              onChange={() => setRole("admin")}
            />
            Admin
          </label>
        </div>

        <button type="submit" className="login-btn" disabled={busy}>
          {busy ? "Creating…" : "Create Account"}
        </button>

        <div className="signup-row">
          Already have an account?
          <a href="/login" className="signup-link" onClick={(e) => { e.preventDefault(); navigate("/login"); }}>
            Log In
          </a>
        </div>
      </form>

      {status && <div className="note" style={{ marginTop: 8 }}>{status}</div>}
    </div>
  );
}
