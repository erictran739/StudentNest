import React, { useState } from "react";

export default function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [busy, setBusy] = useState(false);
  const [status, setStatus] = useState("");

  const onSubmit = async (e) => {
    e.preventDefault();
    setStatus("");
    setBusy(true);

    try {
      const res = await fetch(`/auth/forgot-password`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: email.trim() }),
      });

      // Some backends return empty body on 200; try to parse but don’t require it
      let data = {};
      try { data = await res.json(); } catch {}

      if (!res.ok) {
        const msg = data?.message || data?.error || `Request failed (${res.status})`;
        setStatus(msg);
        return;
      }

      setStatus(
        data?.message ||
        "If an account exists for that email, a reset link has been sent."
      );
    } catch (err) {
      setStatus("Network error: " + (err?.message || "Please try again"));
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="login-box">
        <h2>Reset your password</h2>
        <form onSubmit={onSubmit} noValidate>
          <input
            type="email"
            placeholder="Your email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <button type="submit" className="login-btn" disabled={busy}>
            {busy ? "Sending…" : "Send reset link"}
          </button>
        </form>

        {status && (
          <div className="note" style={{ marginTop: 8 }}>
            {status}
          </div>
        )}

        <div className="signup-row" style={{ marginTop: 14 }}>
          <a
            href="/login"
            className="signup-link"
            onClick={(e) => { e.preventDefault(); window.history.back(); }}
            title="Back"
          >
            ← Back
          </a>
        </div>
      </div>
    </div>
  );
}
