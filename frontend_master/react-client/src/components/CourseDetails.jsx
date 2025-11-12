import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { getCourse } from "../api/courses";

export default function CourseDetails() {
  const { courseId } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  // If we came from the list, we’ll have the course object here.
  const prefetched = location.state?.course ?? null;

  const [course, setCourse] = useState(prefetched);
  const [loading, setLoading] = useState(!prefetched);
  const [err, setErr] = useState("");

  useEffect(() => {
    if (prefetched) return; // already have it
    let ignore = false;

    (async () => {
      setErr("");
      setLoading(true);
      try {
        const data = await getCourse(String(courseId));
        if (!ignore) setCourse(data.course || data);
      } catch (e) {
        if (!ignore) setErr(e.message || "Failed to load course.");
      } finally {
        if (!ignore) setLoading(false);
      }
    })();

    return () => { ignore = true; };
  }, [courseId, prefetched]);

  if (loading) return <div className="page" style={{ padding: 24 }}>Loading…</div>;
  if (err) return (
    <div className="page" style={{ padding: 24 }}>
      <button onClick={() => navigate(-1)}>&larr; Back</button>
      <div className="note error" style={{ marginTop: 12 }}>{err}</div>
    </div>
  );
  if (!course) return (
    <div className="page" style={{ padding: 24 }}>
      <button onClick={() => navigate(-1)}>&larr; Back</button>
      <div style={{ marginTop: 12 }}>Course not found.</div>
    </div>
  );

  const id = course.courseID ?? course.id;

  return (
    <div className="page" style={{ padding: 24 }}>
      <button onClick={() => navigate(-1)}>&larr; Back</button>

      <div className="card" style={{ padding: 16, marginTop: 16 }}>
        <h1 style={{ marginTop: 0 }}>
          {course.name} <small style={{ fontWeight: 400 }}>(ID: {id})</small>
        </h1>
        {course.fullName && (
          <p><b>Full Name:</b> {course.fullName}</p>
        )}
        <p><b>Department:</b> {String(course.department)}</p>
        <p><b>Units/Credits:</b> {course.credits}</p>
        <p><b>Description:</b><br />{course.description}</p>
      </div>

      {Array.isArray(course.sections) && course.sections.length > 0 && (
        <div className="card" style={{ padding: 16, marginTop: 16 }}>
          <h3 style={{ marginTop: 0 }}>Sections</h3>
          <div style={{ overflowX: "auto" }}>
            <table>
              <thead>
                <tr>
                  <th>Section ID</th>
                  <th>Instructor</th>
                  <th>Days</th>
                  <th>Time</th>
                  <th>Location</th>
                </tr>
              </thead>
              <tbody>
                {course.sections.map((s) => (
                  <tr key={s.sectionID ?? s.id}>
                    <td>{s.sectionID ?? s.id}</td>
                    <td>{s.instructor ?? "-"}</td>
                    <td>{s.days ?? "-"}</td>
                    <td>{s.time ?? "-"}</td>
                    <td>{s.location ?? "-"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}