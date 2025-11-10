import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const navigate = useNavigate();

  // Base fields
  const [firstName, setFirstName] = useState("");
  const [lastName,  setLastName]  = useState("");
  const [email,     setEmail]     = useState("");
  const [password,  setPassword]  = useState("");
  const [confirm,   setConfirm]   = useState("");
  const [role,      setRole]      = useState("student"); // "student" | "professor" | "department chair"

  // Student extras
  const [major,          setMajor]          = useState("");
  const [enrollmentYear, setEnrollmentYear] = useState("");

  // Department Chair extras
  const [building,     setBuilding]     = useState("");
  const [roomNumber,   setRoomNumber]   = useState("");
  const [contactEmail, setContactEmail] = useState("");
  const [phone,        setPhone]        = useState("");

  // UI state
  const [status, setStatus] = useState("");
  const [busy,   setBusy]   = useState(false);

  // Map UI labels -> backend values
  const roleMap = {
    student: "student",
    professor: "professor",
    "department chair": "chair", // <-- backend expects "chair"
  };
  const roleValue = roleMap[role] || "student";

  // Validators
  const isEmail = (v) => /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(v);
  const isYear  = (v) => /^\d{4}$/.test(v) && +v >= 1900 && +v <= 2100;
  const isPhone = (v) => {
    const digits = (v || "").replace(/\D/g, "");
    return digits.length >= 7;
  };

  const validate = () => {
    if (!firstName.trim()) return "First name is required.";
    if (!lastName.trim())  return "Last name is required.";
    if (!isEmail(email))   return "Please enter a valid email.";
    if (password.length < 8) return "Password must be at least 8 characters.";
    if (password !== confirm) return "Passwords do not match.";

    if (roleValue === "student") {
      if (!major.trim()) return "Major is required for students.";
      if (!isYear(enrollmentYear)) return "Enter a valid enrollment year (e.g., 2025).";
    }

    if (roleValue === "chair") {
      if (!building.trim()) return "Building is required for Department Chair.";
      if (!roomNumber.trim()) return "Room Number is required for Department Chair.";
      if (!isEmail(contactEmail)) return "Enter a valid contact email for Department Chair.";
      if (!isPhone(phone)) return "Enter a valid contact phone number for Department Chair.";
    }
    return null;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const err = validate();
    if (err) { setStatus(err); return; }

    setBusy(true);
    setStatus("Creating your account...");

    // Build request body
    const body = {
      firstName: firstName.trim(),
      lastName:  lastName.trim(),
      email:     email.trim(),
      password,          // keep as typed
      role: roleValue,   // "student" | "professor" | "chair"
    };

    if (roleValue === "student") {
      body.major = major.trim();
      body.enrollmentYear = Number(enrollmentYear);
    } else if (roleValue === "chair") {
      body.building     = building.trim();
      body.roomNumber   = roomNumber.trim();
      body.contactEmail = contactEmail.trim();
      body.phone        = phone.trim();
    }

    try {
      const res = await fetch("/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      const data = await res.json().catch(() => ({})); // crash-proof

      if (!res.ok) {
        const msg = data.message || data.error || "Registration failed";
        navigate(`/account-failure?status=${encodeURIComponent(String(res.status))}&msg=${encodeURIComponent(msg)}`);
        return;
      }

      if (data?.token) localStorage.setItem("authToken", data.token);
      navigate("/account-success?next=/login");
    } catch (e2) {
      navigate(`/account-failure?status=network&msg=${encodeURIComponent(e2.message || "Network error")}`);
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

        {/* Role selector */}
        <fieldset className="role-group" disabled={busy} style={{ marginTop: 10 }}>
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

        {/* Student fields */}
        {role === "student" && (
          <>
            <input
              type="text"
              placeholder="Major"
              required
              value={major}
              onChange={(e) => setMajor(e.target.value)}
            />
            <input
              type="text" // styled to match theme
              placeholder="Enrollment Year (e.g., 2025)"
              required
              value={enrollmentYear}
              onChange={(e) => setEnrollmentYear(e.target.value)}
              pattern="\d{4}"
              inputMode="numeric"
              style={{ appearance: "none", WebkitAppearance: "none" }}
            />
          </>
        )}

        {/* Department Chair fields */}
        {role === "department chair" && (
          <>
            <input
              type="text"
              placeholder="Building"
              required
              value={building}
              onChange={(e) => setBuilding(e.target.value)}
            />
            <input
              type="text"
              placeholder="Room Number"
              required
              value={roomNumber}
              onChange={(e) => setRoomNumber(e.target.value)}
            />
            <input
              type="email"
              placeholder="Contact Email"
              required
              value={contactEmail}
              onChange={(e) => setContactEmail(e.target.value)}
            />
            <input
              type="text" // styled to match theme
              placeholder="Phone Number"
              required
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
              inputMode="tel"
              style={{ appearance: "none", WebkitAppearance: "none" }}
            />
          </>
        )}

        <button type="submit" className="login-btn" disabled={busy} style={{ marginTop: 8 }}>
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
