import React from "react";

const QA = [
  { q: "How do I reset my password?", a: "Use the 'Forgot Password?' link on the Login page." },
  { q: "Where can I see my classes?", a: "Go to Home → Weekly Schedule. More detail pages will be added soon." },
  { q: "Who do I contact for account issues?", a: "Use the Contact page or email support@studentnest.com." },
];

export default function FAQ() {
  return (
    <div className="home-page">
      <header className="home-topbar">
        <div className="home-left">
          <img src="/lb_logo.png" alt="CSULB Logo" className="home-logo" />
          <div className="home-title">
            <div className="home-title-top">StudentNest</div>
            <div className="home-title-sub">FAQ</div>
          </div>
        </div>
      </header>

      <main className="page-wrap">
        <section className="page-card">
          <div className="page-card-header">Frequently Asked Questions</div>
          <div className="page-card-body">
            {QA.map(({ q, a }) => (
              <details key={q} style={{ marginBottom: 10 }}>
                <summary style={{ fontWeight: 700 }}>{q}</summary>
                <p style={{ marginTop: 6 }}>{a}</p>
              </details>
            ))}
          </div>
        </section>
      </main>
    </div>
  );
}
