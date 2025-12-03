// src/components/Home.jsx
import React from "react";
import { useNavigate } from "react-router-dom";

const MOCK_SCHEDULE = [
  {
    id: "CECS 428-02",
    name: "Analysis of Algorithms",
    room: "ECS 416",
    time: "9:30 AM – 10:45 AM",
    days: "Mon/Wed",
  },
  {
    id: "CECS 470-01",
    name: "Web Programming & Accesibility",
    room: "ECS 412",
    time: "3:30 PM – 4:45 PM",
    days: "Mon/Wed",
  },
  {
    id: "CECS 475-01",
    name: "Software Development with Frameworks",
    room: "VEC 518",
    time: "11:00 AM – 12:15 PM",
    days: "Mon/Wed",
  },
  {
    id: "CECS 491B-01",
    name: "Computer Science Senior Project II",
    room: "ECS 308",
    time: "2:00 PM – 3:15 PM",
    days: "Mon/Wed",
  },
];

export default function Home() {
  const nav = useNavigate();

  return (
    // AppShell wraps this with <main className="home-main">...</main>
    <div className="home-content">
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
          <button
            className="home-shortcut"
            onClick={() => nav("/courses")}
            aria-label="Courses"
          >
            <div className="home-shortcut-icon">📚</div>
            <div className="home-shortcut-text">Courses</div>
          </button>

          <button
            className="home-shortcut"
            onClick={() => nav("/enrollment")}
            aria-label="Enrollment"
          >
            <div className="home-shortcut-icon">📄</div>
            <div className="home-shortcut-text">Enrollment</div>
          </button>

          <button
            className="home-shortcut"
            onClick={() => nav("/colleges")}
            aria-label="Colleges"
          >
            <div className="home-shortcut-icon">🏫</div>
            <div className="home-shortcut-text">Colleges</div>
          </button>

          <button
            className="home-shortcut"
            onClick={() => nav("/services")}
            aria-label="Services"
          >
            <div className="home-shortcut-icon">🧰</div>
            <div className="home-shortcut-text">Services</div>
          </button>
        </div>
      </section>
    </div>
  );
}
