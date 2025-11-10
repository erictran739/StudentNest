import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState("");
  const [lastName,  setLastName]  = useState("");
  const [email,     setEmail]     = useState("");
  const [password,  setPassword]  = useState("");
  const [confirm,   setConfirm]   = useState("");
  const [studentId, setStudentId] = useState("");
  const [role,      setRole]      = useState("student"); // student | professor | department chair
  const [status,    setStatus]    = useState("");
  const [busy,      setBusy]      = useState(false);

  // map UI labels to backend values
  const roleMap = {
    student: "student",
    professor: "professor",
    "department chair": "department_chair",
  };
  const roleValue = roleMap[role] || "student";

  // email validation
  const validEmail = (v) => /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(v);

  const validate = () => {
    if (!firstName.trim()) return "First name is required.";
    if (!lastName.trim())  return "Last name is required.";
    if (!validEmail(email)) return "Please enter a valid email.";
    if (password.length < 8) return "Password must be at least 8 characters.";
    if (password !== confirm) return "Passwords do not match.";
    if (roleValue === "student" && !studentId.trim())
      return "Student ID is required for students.";
    return null;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const err = validate();
    if (err) { setStatus(err); return; }

    setBusy(true);
    setStatus("Creating your account...");

    try {
      const body = {
        firstName: firstName.trim(),
        lastName:  lastName.trim(),
        email:     email.trim(),
        password,                  // don't trim password
        role: roleValue,
      };
      if (roleValue === "student") body.student_id = studentId.trim();

      const res = await fetch("/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      const data = await res.json().catch(() => ({}));

      if (!res.ok) {
        const msg = data.message || data.error || "Registration failed";
        navigate(
          `/account-failure?status=${encodeURIComponent(String(res.status))}&msg=${encodeURIComponent(msg)}`
        );
        return;
      }

      if (data?.token) localStorage.setItem("authToken", data.token);
      navigate("/account-success?next=/login");
    } catch (e2) {
      navigate(
        `/account-failure?status=network&msg=${encodeURIComponent(e2.message || "Network error")}`
      );
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
          placeholder="First Name"
          required
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        <input
          type="text"
          placeholder="Last Name"
          required
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
        <input
          type="email"
          placeholder="Email"
          required
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          key="password"
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

        {/* Student ID field appears only for students */}
        {role === "student" && (
          <input
            type="text"
            placeholder="Student ID"
            required
            value={studentId}
            onChange={(e) => setStudentId(e.target.value)}
          />
        )}

        {/* Role selector */}
        <fieldset className="role-group" disabled={busy}>
          <legend className="role-legend">I am a…</legend>
          <div className="role-options">
            {["student", "professor", "department chair"].map((opt) => (
              <label key={opt} className={`role-chip ${role === opt ? "selected" : ""}`}>
                <input
                  type="radio"
                  name="role"
                  value={opt}
                  checked={role === opt}
                  onChange={(e) => setRole(e.target.value)}
                />
                <span className="role-label">
                  {opt.split(" ").map(s => s[0].toUpperCase()+s.slice(1)).join(" ")}
                </span>
              </label>
            ))}
          </div>
        </fieldset>

        <button type="submit" className="login-btn" disabled={busy}>
          {busy ? "Creating…" : "Create Account"}
        </button>

        <div className="signup-row">
          Already have an account?
          <a
            href="/login"
            className="signup-link"
            onClick={(e) => { e.preventDefault(); navigate("/login"); }}
          >
            Log In
          </a>
        </div>
      </form>

      {status && <div className="note" style={{ marginTop: 8 }}>{status}</div>}
    </div>
  );
}
