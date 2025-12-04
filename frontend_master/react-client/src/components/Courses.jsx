// src/components/Courses.jsx
import React, { useState } from "react";
import { Link } from "react-router-dom";
import {
  getCourse,
  getSectionOfCourse,
  getCoursesByDepartment,
} from "../api/courses";

const DEPARTMENTS = ["CECS", "MATH", "ENGL"];

export default function Courses() {
  // ---- Department search state ----
  const [dept, setDept] = useState("CECS");
  const [deptLoading, setDeptLoading] = useState(false);
  const [deptErr, setDeptErr] = useState("");
  const [deptCourses, setDeptCourses] = useState([]);

  // ---- Single course lookup ----
  const [courseId, setCourseId] = useState("");
  const [course, setCourse] = useState(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState("");

  // ---- Section lookup ----
  const [sectionId, setSectionId] = useState("");
  const [section, setSection] = useState(null);
  const [secErr, setSecErr] = useState("");

  // ----------------------------------------------------
  // FETCH BY DEPARTMENT
  // ----------------------------------------------------
  const fetchByDept = async (e) => {
    e?.preventDefault();
    setDeptErr("");
    setDeptCourses([]);
    setDeptLoading(true);

    try {
      const list = await getCoursesByDepartment(dept);
      setDeptCourses(list || []);
    } catch (e) {
      setDeptErr(e.message || "Failed to load courses.");
    } finally {
      setDeptLoading(false);
    }
  };

  // ----------------------------------------------------
  // FETCH SINGLE COURSE
  // ----------------------------------------------------
  const fetchCourse = async (e) => {
    e?.preventDefault();
    setErr("");
    setSection(null);
    setSecErr("");

    if (!courseId.trim()) return setErr("Please enter a Course ID.");

    setLoading(true);
    try {
      const data = await getCourse(courseId.trim());
      setCourse(data); // normalized
    } catch (e) {
      setCourse(null);
      setErr(e.message || "Failed to load course.");
    } finally {
      setLoading(false);
    }
  };

  // ----------------------------------------------------
  // FETCH SECTION
  // ----------------------------------------------------
  const fetchSection = async (e) => {
    e?.preventDefault();
    setSecErr("");

    if (!course) return setSecErr("Load a course first.");
    if (!sectionId.trim()) return setSecErr("Enter a Section ID.");

    try {
      const courseIdToUse = course.id;
      const data = await getSectionOfCourse(courseIdToUse, sectionId.trim());
      setSection(data);
    } catch (e) {
      setSection(null);
      setSecErr(e.message || "Failed to load section.");
    }
  };

  return (
    <div className="page" style={{ padding: 24 }}>
      <h1>Course Search</h1>

      <div
        style={{
          display: "grid",
          gridTemplateColumns: "minmax(320px, 420px) 1fr",
          gap: 24,
        }}
      >
        {/* LEFT SIDE CONTROLS */}
        <div>
          {/* Search by Department */}
          <div className="card" style={{ padding: 16, marginBottom: 16 }}>
            <h3 style={{ marginTop: 0 }}>Find by Department</h3>
            <label>Department</label>
            <select value={dept} onChange={(e) => setDept(e.target.value)}>
              {DEPARTMENTS.map((d) => (
                <option key={d} value={d}>
                  {d}
                </option>
              ))}
            </select>
            <button onClick={fetchByDept} style={{ marginTop: 10 }}>
              {deptLoading ? "Loading…" : "Search"}
            </button>
            {deptErr && (
              <div className="note error" style={{ marginTop: 8 }}>
                {deptErr}
              </div>
            )}
          </div>

          {/* Lookup by Course ID */}
          <form
            onSubmit={fetchCourse}
            className="card"
            style={{ padding: 16, marginBottom: 16 }}
          >
            <h3 style={{ marginTop: 0 }}>Lookup by Course ID</h3>
            <label>Course ID</label>
            <input
              type="number"
              value={courseId}
              onChange={(e) => setCourseId(e.target.value)}
              placeholder="e.g., 12"
            />
            <button type="submit" disabled={loading}>
              {loading ? "Loading…" : "Get Course"}
            </button>
            {err && (
              <div className="note error" style={{ marginTop: 8 }}>
                {err}
              </div>
            )}
          </form>

          {/* Lookup Section */}
          <form onSubmit={fetchSection} className="card" style={{ padding: 16 }}>
            <h3 style={{ marginTop: 0 }}>Get a Section (for loaded course)</h3>
            <label>Section ID</label>
            <input
              type="number"
              value={sectionId}
              onChange={(e) => setSectionId(e.target.value)}
              placeholder="e.g., 101"
            />
            <button type="submit" disabled={!course}>
              Get Section
            </button>
            {secErr && (
              <div className="note error" style={{ marginTop: 8 }}>
                {secErr}
              </div>
            )}
          </form>
        </div>

        {/* RIGHT SIDE RESULTS */}
        <div>
          {/* Department table */}
          <div className="card" style={{ padding: 16, marginBottom: 16 }}>
            <h3 style={{ marginTop: 0 }}>Results</h3>

            {deptCourses.length === 0 ? (
              <div style={{ color: "#666" }}>
                Pick a department and click Search.
              </div>
            ) : (
              <div style={{ overflowX: "auto" }}>
                <table>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Credits</th>
                      <th>Description</th>
                    </tr>
                  </thead>
                  <tbody>
                    {deptCourses.map((c) => (
                      <tr key={c.id}>
                        <td>{c.id}</td>
                        <td>
                          <Link
                            to={`/courses/${c.id}`}
                            state={{ course: c }}
                            style={{ textDecoration: "none" }}
                          >
                            {c.name}
                          </Link>
                        </td>
                        <td>{c.credits}</td>
                        <td>{c.description}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>

          {/* Loaded course detail */}
          {course && (
            <div className="card" style={{ padding: 16 }}>
              <h3 style={{ marginTop: 0 }}>
                {course.name}{" "}
                <small style={{ fontWeight: 400 }}>(ID: {course.id})</small>
              </h3>
              <p>{course.description}</p>
              <p>
                <b>Department:</b> {course.department}
              </p>
              <p>
                <b>Credits:</b> {course.credits}
              </p>
            </div>
          )}

          {/* Loaded section */}
          {section && (
            <div className="card" style={{ padding: 16, marginTop: 16 }}>
              <h3>Section Detail</h3>
              <pre
                style={{
                  whiteSpace: "pre-wrap",
                  background: "#f7f7f7",
                  padding: 12,
                  borderRadius: 8,
                }}
              >
                {JSON.stringify(section, null, 2)}
              </pre>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
