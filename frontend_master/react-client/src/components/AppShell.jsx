// src/components/AppShell.jsx
import React, { useState, useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import { getCurrentUser, getCurrentUserId, clearSession } from "../api/session";

export default function AppShell() {
  const nav = useNavigate();
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [firstName, setFirstName] = useState("");

  useEffect(() => {
    const user = getCurrentUser();
    const userId = getCurrentUserId();
    console.log("DEBUG shell currentUser:", user);
    console.log("DEBUG shell currentUserId:", userId);

    if (user) {
      const fn =
        user.first_name ||
        user.firstname ||
        user.firstName ||
        (user.name ? user.name.split(" ")[0] : "");
      setFirstName(fn || "");
    }
  }, []);

  const logout = () => {
    clearSession();
    nav("/");
  };

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

          <img src="/lb_logo.png" alt="LB" className="home-logo" />

          <div className="home-title-top">
            {firstName ? `${firstName}'s Student Center` : "Student Center"}
          </div>
        </div>

        {/* Right: icons (moved here from Home.jsx) */}
        <div className="home-right">
          {/* Profile icon */}
          <button className="icon-btn" aria-label="Profile">
            <svg
              width="22"
              height="22"
              viewBox="0 0 24 24"
              fill="currentColor"
              aria-hidden="true"
            >
              <path d="M12 12a5 5 0 1 0-5-5 5 5 0 0 0 5 5Zm0 2c-5 0-9 2.5-9 5.5V22h18v-2.5C21 16.5 17 14 12 14Z" />
            </svg>
          </button>

          {/* Email icon */}
          <button className="icon-btn" aria-label="Email">
            <svg
              width="22"
              height="22"
              viewBox="0 0 24 24"
              fill="currentColor"
              aria-hidden="true"
            >
              <path d="M20 4H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2Zm0 4-8 5-8-5V6l8 5 8-5v2Z" />
            </svg>
          </button>
        </div>
      </header>

      {/* Global slide-out drawer + overlay */}
      <div
        className={`home-drawer-overlay ${drawerOpen ? "show" : ""}`}
        onClick={() => setDrawerOpen(false)}
      />
      <aside className={`home-drawer ${drawerOpen ? "open" : ""}`}>
        <div className="drawer-header">Menu</div>
        <button
          onClick={() => {
            setDrawerOpen(false);
            nav("/home");
          }}
        >
          Home
        </button>
        <button
          onClick={() => {
            setDrawerOpen(false);
            nav("/courses");
          }}
        >
          Courses
        </button>
        <button
          onClick={() => {
            setDrawerOpen(false);
            nav("/enrollment");
          }}
        >
          Enrollment
        </button>
        <button
          onClick={() => {
            setDrawerOpen(false);
            nav("/colleges");
          }}
        >
          Colleges
        </button>
        <button
          onClick={() => {
            setDrawerOpen(false);
            nav("/services");
          }}
        >
          Services
        </button>
        <button onClick={logout}>Logout</button>
      </aside>

      {/* Page content */}
      <main className="home-main">
        <Outlet />
      </main>
    </div>
  );
}
