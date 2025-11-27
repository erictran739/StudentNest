import React from "react";
import { Link } from "react-router-dom";
import "./Services.css";

export default function Services() {
  const services = [
    {
      title: "Academic Advising",
      description: "Meet with academic advisors for class planning, major changes, and degree progress.",
      icon: "🎓",
      link: "/services/advising"
    },
    {
      title: "Financial Aid",
      description: "Information about grants, scholarships, FAFSA, and tuition support.",
      icon: "💰",
      link: "/services/financial-aid"
    },
    {
      title: "Tutoring & Support",
      description: "Get help from tutors in math, computer science, writing, and more.",
      icon: "📘",
      link: "/services/tutoring"
    },
    {
      title: "Enrollment Help",
      description: "Questions about adding/dropping classes, prerequisites, or registration dates.",
      icon: "📝",
      link: "/services/enrollment"
    },
    {
      title: "Degree Planning",
      description: "Track your major requirements and graduation progress.",
      icon: "📄",
      link: "/degree"
    },
    {
      title: "Tech Support",
      description: "Get help with StudentNest login, account issues, or technical errors.",
      icon: "🛠️",
      link: "/contact"
    }
  ];

  return (
    <div className="services-page">
      <h1 className="services-title">Student Services</h1>

      <div className="services-grid">
        {services.map((s, i) => (
          <Link to={s.link} className="service-card" key={i}>
            <div className="service-icon">{s.icon}</div>
            <h3>{s.title}</h3>
            <p>{s.description}</p>
          </Link>
        ))}
      </div>
    </div>
  );
}
