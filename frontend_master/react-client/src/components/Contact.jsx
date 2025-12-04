import React, { useState } from "react";

export default function Contact() {
  const [status, setStatus] = useState("");

  const onSubmit = (e) => {
    e.preventDefault();
    // TODO: hook up to backend or email service
    setStatus("Thanks! Your message has been recorded (demo).");
  };

  return (
    <div className="home-page">
      <header className="home-topbar">
        <div className="home-left">
          <img src="/lb_logo.png" alt="CSULB Logo" className="home-logo" />
          <div className="home-title">
            <div className="home-title-top">StudentNest</div>
            <div className="home-title-sub">Contact Us</div>
          </div>
        </div>
      </header>

      <main className="page-wrap">
        <section className="page-card">
          <div className="page-card-header">Get in Touch</div>
          <div className="page-card-body">
            <form className="page-form" onSubmit={onSubmit}>
              <input type="text" placeholder="Your name" required />
              <input type="email" placeholder="Your email" required />
              <textarea rows="5" placeholder="How can we help?" required />
              <button type="submit">Send Message</button>
            </form>

            <p style={{ marginTop: 12, color: "#6b6b6b" }}>
              Or email us at{" "}
              <a className="signup-link" href="mailto:support@studentnest.example">
                support@studentnest.com
              </a>
            </p>

            {status && <p style={{ marginTop: 12 }}>{status}</p>}
          </div>
        </section>
      </main>
    </div>
  );
}
