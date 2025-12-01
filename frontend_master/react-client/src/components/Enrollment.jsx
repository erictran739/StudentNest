import React, { useEffect, useState } from "react";
import "./Enrollment.css";

export default function Enrollment() {
  const [student, setStudent] = useState(null);
  const [sectionEnroll, setSectionEnroll] = useState("");
  const [sectionDrop, setSectionDrop] = useState("");
  const [message, setMessage] = useState("");

  const user = JSON.parse(localStorage.getItem("authUser") || "{}");
  const userId = user?.user_id || user?.id || user?.userID;

  useEffect(() => {
    fetch(`/api/students/${userId}`)
      .then((res) => res.json())
      .then(setStudent)
      .catch(() => setMessage("Failed to load student info."));
  }, [userId]);

  const enroll = async () => {
    setMessage("Enrolling...");
    const res = await fetch(`/api/students/enroll`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        student_id: userId,
        section_id: Number(sectionEnroll),
      }),
    });

    const data = await res.json();
    if (!res.ok) return setMessage(data.message || "Failed to enroll");
    setMessage(`✅ Successfully enrolled in section ${sectionEnroll}`);
  };

  const drop = async () => {
    setMessage("Dropping...");
    const res = await fetch(`/api/students/drop`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        student_id: userId,
        section_id: Number(sectionDrop),
      }),
    });

    const data = await res.json();
    if (!res.ok) return setMessage(data.message || "Failed to drop course");
    setMessage(`🗑️ Dropped section ${sectionDrop}`);
  };

  return (
    <div className="enrollment-container">
      <h1 className="title">Enrollment Center</h1>

      {student && (
        <div className="student-card">
          <h2>{student.firstName} {student.lastName}</h2>
          <p><b>Email:</b> {student.email}</p>
          <p><b>Major:</b> {student.major}</p>
          <p><b>GPA:</b> {student.gpa}</p>
          <p><b>Enrollment Year:</b> {student.enrollmentYe}</p>
        </div>
      )}

      <div className="actions">
        <div className="card enroll">
          <h3>📚 Enroll in a Class</h3>
          <input
            type="number"
            placeholder="Enter Section ID"
            value={sectionEnroll}
            onChange={(e) => setSectionEnroll(e.target.value)}
          />
          <button onClick={enroll}>Enroll</button>
        </div>

        <div className="card drop">
          <h3>🗑️ Drop a Class</h3>
          <input
            type="number"
            placeholder="Enter Section ID"
            value={sectionDrop}
            onChange={(e) => setSectionDrop(e.target.value)}
          />
          <button onClick={drop}>Drop</button>
        </div>
      </div>

      {message && <div className="message">{message}</div>}
    </div>
  );
}
