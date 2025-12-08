import React from "react";
import { Link } from "react-router-dom";
import "./Services.css";

export default function Services() {
  const services = [
    {
      title: "Academic Advising",
      description: "Meet with academic advisors for class planning, major changes, and degree progress.",
      icon: "🎓",
      link: "https://www.csulb.edu/academic-advising-at-csulb",
      external: true
    },
    {
      title: "Financial Aid",
      description: "Information about grants, scholarships, FAFSA, and tuition support.",
      icon: "💰",
      link: "https://www.csulb.edu/financial-aid-and-scholarships",
      external: true
    },
    {
      title: "Tutoring & Support",
      description: "Get help from tutors in math, computer science, writing, and more.",
      icon: "📘",
      link: "https://www.csulb.edu/academic-affairs/undergraduate-studies/tutoring-at-csulb",
      external: true
    },
    {
      title: "Enrollment Help",
      description: "Questions about adding/dropping classes, prerequisites, or registration dates.",
      icon: "📝",
      link: "https://www.csulb.edu/enrollment-services",
      external: true
    },
    {
      title: "Degree Planning",
      description: "Track your major requirements and graduation progress.",
      icon: "📄",
      link: "https://www.csulb.edu/student-records/degree-planner",
      external: true
    },
    {
      title: "Tech Support",
      description: "Get help with StudentNest login, account issues, or technical errors.",
      icon: "🛠️",
      link: "/contact",          // KEEP INTERNAL ROUTE
      external: false
    }
  ];  

  return (
    <div className="services-page">
      <h1 className="services-title">Student Services</h1>

      <div className="services-grid">
      {services.map((s, i) =>
        s.external ? (
          <a
            key={i}
            href={s.link}
            target="_blank"
            rel="noopener noreferrer"
            className="service-card"
          >
            <div className="service-icon">{s.icon}</div>
            <h3>{s.title}</h3>
            <p>{s.description}</p>
          </a>
        ) : (
          <Link key={i} to={s.link} className="service-card">
            <div className="service-icon">{s.icon}</div>
            <h3>{s.title}</h3>
            <p>{s.description}</p>
          </Link>
        )
      )}
      </div>
    </div>
  );
}
