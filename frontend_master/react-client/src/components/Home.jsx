import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getCurrentUser, getCurrentUserId } from "../api/session";

const MOCK_SCHEDULE = [
  { id: "CECS 428-02", name: "Analysis of Algorithms",                   room: "ECS 416", time: "9:30 AM – 10:45 AM", days: "Mon/Wed" },
  { id: "CECS 470-01", name: "Web Programming & Accesibility",           room: "ECS 412", time: "3:30 PM – 4:45 PM",  days: "Mon/Wed" },
  { id: "CECS 475-01", name: "Software Development with Frameworks",     room: "VEC 518", time: "11:00 AM – 12:15 PM", days: "Mon/Wed" },
  { id: "CECS 491B-01",name: "Computer Science Senior Project II",       room: "ECS 308", time: "2:00 PM – 3:15 PM",  days: "Mon/Wed" },
];

export default function Home() {
  const nav = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);
  const [drawerOpen, setDrawerOpen] = useState(false);

  const logout = () => {
    try {
      localStorage.removeItem("authToken");
      localStorage.removeItem("authUser");
    } catch {}
    nav("/");
  };

  useEffect(() => {
    const user = getCurrentUser();
    const userId = getCurrentUserId();
    console.log("DEBUG currentUser:", user);
    console.log("DEBUG currentUserId:", userId);
  }, []);

  return (
    <div className="home-page">
      {/* Top bar */}
      <header className="home-topbar">
        {/* Left: menu + logo + title */}
        <div className="home-left">
          <button
            className="home-menu-btn"
            onClick={() => setDrawerOpen(true)}
            aria-label="Open navigation"
          >
            ☰
          </button>

          {/* Logo image you added earlier */}
          <img src="/lb_logo.png" alt="LB" className="home-logo" />

          {/* Title simplified per request */}
          <div className="home-title-top">Simon's Student Center</div>
        </div>

        {/* Right: icons */}
      <div className="home-right">
        {/* Profile icon first */}
        <button className="icon-btn" aria-label="Profile">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
            <path d="M12 12a5 5 0 1 0-5-5 5 5 0 0 0 5 5Zm0 2c-5 0-9 2.5-9 5.5V22h18v-2.5C21 16.5 17 14 12 14Z"/>
          </svg>
        </button>

        {/* Email icon second */}
        <button className="icon-btn" aria-label="Email">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
            <path d="M20 4H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2Zm0 4-8 5-8-5V6l8 5 8-5v2Z"/>
          </svg>
        </button>
      </div>
      </header>

      {/* Slide-out drawer + overlay */}
      <div
        className={`home-drawer-overlay ${drawerOpen ? "show" : ""}`}
        onClick={() => setDrawerOpen(false)}
      />
      <aside className={`home-drawer ${drawerOpen ? "open" : ""}`}>
        <div className="drawer-header">Menu</div>
        <button onClick={() => { setDrawerOpen(false); nav("/home"); }}>Home</button>
        <button onClick={logout}>Logout</button>
      </aside>

      {/* Main content */}
      <main className="home-content">
        {/* Weekly Schedule card */}
        <section className="home-card">
          <div className="home-card-header">
            <h2>Weekly Schedule</h2>
          </div>

          <div className="home-table-wrap">
            <table className="home-table">
              <thead>
                <tr>
                  <th>Class ID</th>
                  <th>Class name</th>
                  <th>Room location</th>
                  <th>Class time</th>
                  <th>Days</th>
                </tr>
              </thead>
              <tbody>
                {MOCK_SCHEDULE.map((c) => (
                  <tr key={c.id}>
                    <td>{c.id}</td>
                    <td>{c.name}</td>
                    <td>{c.room}</td>
                    <td>{c.time}</td>
                    <td>{c.days}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>

        {/* Shortcuts */}
        <section className="home-shortcuts">
          <h3>Shortcuts</h3>
          <div className="home-shortcut-grid">
            <button className="home-shortcut" onClick={() => nav("/courses")} aria-label="Courses">
              <div className="home-shortcut-icon">📚</div>
              <div className="home-shortcut-text">Courses</div>
            </button>

            <button className="home-shortcut" onClick={() => nav("/transcript")} aria-label="Transcript">
              <div className="home-shortcut-icon">📄</div>
              <div className="home-shortcut-text">Transcript</div>
            </button>

            <button className="home-shortcut" onClick={() => nav("/colleges")} aria-label="Colleges">
              <div className="home-shortcut-icon">🏫</div>
              <div className="home-shortcut-text">Colleges</div>
            </button>


            <button className="home-shortcut" onClick={() => nav("/services")} aria-label="Services">
              <div className="home-shortcut-icon">🧰</div>
              <div className="home-shortcut-text">Services</div>
            </button>
          </div>
        </section>
      </main>
    </div>
  );
}
