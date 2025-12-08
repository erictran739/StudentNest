import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import "./Colleges.css";

const collegeData = {
  cota: {
    name: "College of the Arts (COTA)",
    programs: [
      "Bob Cole Conservatory of Music",
      "Dance",
      "Design",
      "Film and Electronic Arts",
      "School of Art",
      "Theatre Arts",
    ],
  },
  cob: {
    name: "College of Business (COB)",
    programs: [
      "Accountancy",
      "Finance",
      "HR Management",
      "Information Systems",
      "International Business",
      "Management",
      "Marketing",
      "Supply Chain Management",
    ],
  },
  ced: {
    name: "College of Education (CED)",
    programs: [
      "Teacher Education",
      "Educational Leadership",
      "Liberal Studies",
      "Counseling",
      "Special Education",
    ],
  },
  coe: {
    name: "College of Engineering (COE)",
    programs: [
      "Computer Science",
      "Mechanical Engineering",
      "Electrical Engineering",
      "Aerospace Engineering",
      "Civil Engineering",
      "Biomedical Engineering",
    ],
  },
  chhs: {
    name: "Health & Human Services (CHHS)",
    programs: [
      "Nursing",
      "Health Science",
      "Kinesiology",
      "Social Work",
      "Criminology",
    ],
  },
  cla: {
    name: "College of Liberal Arts (CLA)",
    programs: [
      "Psychology",
      "Sociology",
      "Political Science",
      "History",
      "Journalism",
      "Philosophy",
      "Women's, Gender, and Sexuality Studies",
    ],
  },
  cnsm: {
    name: "Natural Sciences & Mathematics (CNSM)",
    programs: [
      "Biology",
      "Chemistry",
      "Physics",
      "Mathematics",
      "Earth Science",
    ],
  },
  cpace: {
    name: "CPaCE",
    programs: [
      "Professional Development",
      "Online Bachelor's Degrees",
      "Online Master's Degrees",
      "Certification Programs",
    ],
  },
};

export default function CollegeDetails() {
  const { collegeId } = useParams();
  const nav = useNavigate();

  const college = collegeData[collegeId];

  if (!college) {
    return <h2>College not found</h2>;
  }

  return (
    <div className="college-details-container">
      <button className="back-btn" onClick={() => nav("/colleges")}>← Back</button>
      <h1 className="college-details-title">{college.name}</h1>

      <div className="program-list">
        {college.programs.map((p, i) => (
          <div key={i} className="program-card">
            🎓 {p}
          </div>
        ))}
      </div>
    </div>
  );
}
