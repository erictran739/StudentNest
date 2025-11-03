//src/components/Courses.jsx
import React, { useState } from "react";
import { getCourse, getSectionOfCourse } from "../api/courses";

export default function Courses() {
  const [courseId, setCourseId] = useState("");
  const [course, setCourse] = useState(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState("");

  const [sectionId, setSectionId] = useState("");
  const [section, setSection] = useState(null);
  const [secErr, setSecErr] = useState("");

  const fetchCourse = async (e) => {
    e?.preventDefault();
    setErr(""); setSection(null); setSecErr("");
    if (!courseId.trim()) return setErr("Please enter a Course ID.");
    setLoading(true);
    try {
      const data = await getCourse(courseId.trim());
      setCourse(data.course || data);
    } catch (e) {
      setCourse(null);
      setErr(e.message || "Failed to load course.");
    } finally {
      setLoading(false);
    }
  };

  const fetchSection = async (e) => {
    e?.preventDefault();
    setSecErr("");
    if (!course) return setSecErr("Load a course first.");
    if (!sectionId.trim()) return setSecErr("Enter a Section ID.");
    try {
      const data = await getSectionOfCourse(
        course.courseID || course.id || courseId,
        sectionId.trim()
      );
      setSection(data.section || data);
    } catch (e) {
      setSection(null);
      setSecErr(e.message || "Failed to load section.");
    }
  };

  return (
    <div className="page courses-page" style={{ padding: 24 }}>
      <h1>Course Search</h1>

      <form onSubmit={fetchCourse} className="card" style={{ maxWidth: 520, padding: 16, marginBottom: 16 }}>
        <label>Course ID</label>
        <input
          type="number"
          value={courseId}
          onChange={(e) => setCourseId(e.target.value)}
          placeholder="e.g., 1"
        />
        <button type="submit" disabled={loading}>
          {loading ? "Loading…" : "Get Course"}
        </button>
        {err && <div className="note error" style={{ marginTop: 8 }}>{err}</div>}
      </form>

      {course && (
        <div className="card" style={{ maxWidth: 900, padding: 16, marginBottom: 24 }}>
          <h2 style={{ marginTop: 0 }}>
            {course.name}{" "}
            <small style={{ fontWeight: 400 }}>
              (ID: {course.courseID ?? course.id})
            </small>
          </h2>
          <p>{course.description}</p>
          <p><b>Department:</b> {String(course.department)}</p>
          <p><b>Credits:</b> {course.credits}</p>

          {Array.isArray(course.sections) && course.sections.length > 0 && (
            <>
              <h3>Sections</h3>
              <div style={{ overflowX: "auto" }}>
                <table>
                  <thead>
                    <tr>
                      <th>Section ID</th>
                      <th>Instructor</th>
                      <th>Days</th>
                      <th>Time</th>
                      <th>Room</th>
                    </tr>
                  </thead>
                  <tbody>
                    {course.sections.map((s) => (
                      <tr key={s.sectionID ?? s.id}>
                        <td>{s.sectionID ?? s.id}</td>
                        <td>
                          {s.professor?.lastName
                            ? `${s.professor.firstName} ${s.professor.lastName}`
                            : "—"}
                        </td>
                        <td>{s.days ?? "—"}</td>
                        <td>{s.time ?? "—"}</td>
                        <td>{s.room ?? s.location ?? "—"}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </>
          )}
        </div>
      )}

      <form onSubmit={fetchSection} className="card" style={{ maxWidth: 520, padding: 16 }}>
        <label>Get Section (of the loaded Course)</label>
        <input
          type="number"
          value={sectionId}
          onChange={(e) => setSectionId(e.target.value)}
          placeholder="Section ID (e.g., 1)"
        />
        <button type="submit" disabled={!course}>Get Section</button>
        {secErr && <div className="note error" style={{ marginTop: 8 }}>{secErr}</div>}
      </form>

      {section && (
        <div className="card" style={{ maxWidth: 900, padding: 16, marginTop: 16 }}>
          <h3>Section Detail</h3>
          <pre style={{ whiteSpace: "pre-wrap", background: "#f7f7f7", padding: 12, borderRadius: 8 }}>
{JSON.stringify(section, null, 2)}
          </pre>
        </div>
      )}
    </div>
  );
}
