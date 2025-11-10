import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState("");
  const [lastName,  setLastName]  = useState("");
  const [email,     setEmail]     = useState("");
  const [password,  setPassword]  = useState("");
  const [confirm,   setConfirm]   = useState("");
  const [role,      setRole]      = useState("student"); // student | professor | department chair
  const [status,    setStatus]    = useState("");
  const [busy,      setBusy]      = useState(false);

  // Simple, reliable email check
  const validEmail = (v) => /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(v);

  const validate = () => {
    if (!firstName.trim()) return "First name is required.";
    if (!lastName.trim())  return "Last name is required.";
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
      // ---- IMPORTANT: correct endpoint and payload ----
      const res = await fetch("/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          firstName: firstName.trim(),
          lastName:  lastName.trim(),
          email:     email.trim(),
          password,                         // do not trim password unless you want to
          role                                // "student" | "professor" | "department chair"
        })
      });

      // Never allow res.json() to crash the app → prevents the “black screen”
      const data = await res.json().catch(() => ({}));

      if (!res.ok) {
        const msg = data.message || data.error || "Registration failed";
        // If your router doesn't have /account-failure mounted yet, we still show an error
        try {
          navigate(`/account-failure?status=${encodeURIComponent(String(res.status))}&msg=${encodeURIComponent(msg)}`);
        } catch {
          setStatus(`Registration failed (${res.status}): ${msg}`);
        }
        return;
      }

      if (data && data.token) localStorage.setItem("authToken", data.token);

      try {
        navigate("/account-success?next=/login");
      } catch {
        setStatus("Account created! You can now log in.");
      }
    } catch (e2) {
      try {
        navigate(`/account-failure?status=network&msg=${encodeURIComponent(e2.message || "Network error")}`);
      } catch {
        setStatus(`Network error: ${e2.message || "Unknown error"}`);
      }
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
          key="password" // keeps your remount behavior if needed
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

        {/* Role selector: student | professor | department chair */}
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
