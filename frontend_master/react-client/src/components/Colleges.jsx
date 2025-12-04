import React from "react";
import "./Colleges.css";
import { useNavigate } from "react-router-dom";

const colleges = [
  { id: "cota", name: "College of the Arts (COTA)", dean: "Royce W. Smith" },
  { id: "cob", name: "College of Business (COB)", dean: "Mark Suazo" },
  { id: "ced", name: "College of Education (CED)", dean: "Anna Ortiz" },
  { id: "coe", name: "College of Engineering (COE)", dean: "Jinny Rhee" },
  { id: "chhs", name: "Health & Human Services (CHHS)", dean: "Grace Reynolds-Fisher" },
  { id: "cla", name: "College of Liberal Arts (CLA)", dean: "Dan O'Connor" },
  { id: "cnsm", name: "Natural Sciences & Mathematics (CNSM)", dean: "Curtis Bennett" },
  { id: "cpace", name: "College of Professional & Continuing Education (CPaCE)", dean: "Chris Swarat" },
];

export default function Colleges() {
  const nav = useNavigate();

  return (
    <div className="colleges-container">
      <h1 className="colleges-title">Colleges & Departments</h1>

      <div className="colleges-grid">
        {colleges.map((c) => (
          <div
            key={c.id}
            className="college-card"
            onClick={() => nav(`/colleges/${c.id}`)}
          >
            <div className="college-icon">🏛️</div>

            {/* NEW: Show the college name! */}
            <h2 className="college-name">{c.name}</h2>

            <p className="college-dean"><strong>Dean:</strong> {c.dean}</p>

            <p className="college-link">View Departments ➝</p>
          </div>
        ))}
      </div>
    </div>
  );
}
