import React, { useEffect, useState } from "react";
import {
  getEnrollments,
  enrollStudent,
  dropStudent,
  getAvailableSections,
} from "../api/enrollment";
import { getAuthUser } from "../api/authHelpers";
import "./Enrollment.css";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function Enrollment() {
  // --- auth / student ID from JWT ---
  const auth = getAuthUser(); // whatever your helper returns
  const loggedInStudentId = auth?.user?.userID; // adjust if field name is slightly different

  const [studentId, setStudentId] = useState(loggedInStudentId ?? "");
  const [sectionId, setSectionId] = useState("");
  const [term, setTerm] = useState("FALL");
  const [year, setYear] = useState(2024);

  const [enrollments, setEnrollments] = useState([]);
  const [availableSections, setAvailableSections] = useState([]);

  const [loadingTable, setLoadingTable] = useState(false);
  const [enrolling, setEnrolling] = useState(false);
  const [dropping, setDropping] = useState(false);

  // Auto-load on page load when user is a student
  useEffect(() => {
    if (!loggedInStudentId) {
      console.warn("No logged-in student; make sure you are logged in.");
      return;
    }
    loadData(loggedInStudentId);
  }, [loggedInStudentId, term, year]);

  const loadData = async (sid) => {
    const idToUse = sid || studentId;
    if (!idToUse) return;

    try {
      setLoadingTable(true);

      const [historyRes, availableRes] = await Promise.all([
        getEnrollments(idToUse, term, year),
        getAvailableSections(idToUse, term, year).catch(() => ({ data: [] })), // don't block if this 404s
      ]);

      // most of your APIs look like { status: 200, data: [...] }
      const history = historyRes.data?.data ?? historyRes.data ?? [];
      const available = availableRes.data?.data ?? availableRes.data ?? [];

      setEnrollments(history);
      setAvailableSections(available);

      toast.success("Enrollments loaded");
    } catch (err) {
      console.error("Error loading enrollments", err);
      toast.error("Failed to load enrollments");
    } finally {
      setLoadingTable(false);
    }
  };

  const handleEnroll = async () => {
    if (!studentId || !sectionId) {
      toast.error("Please enter a section to enroll");
      return;
    }
    try {
      setEnrolling(true);
      const res = await enrollStudent(studentId, sectionId);
      const msg = res.data?.message ?? "Enrolled successfully";
      toast.success(msg);
      await loadData();
      setSectionId("");
    } catch (err) {
      console.error("Enroll error", err);
      const msg =
        err.response?.data?.message ??
        err.response?.data?.error ??
        "Error enrolling student";
      toast.error(msg);
    } finally {
      setEnrolling(false);
    }
  };

  const handleDrop = async () => {
    if (!studentId || !sectionId) {
      toast.error("Please enter a section to drop");
      return;
    }
    try {
      setDropping(true);
      const res = await dropStudent(studentId, sectionId);
      const msg = res.data?.message ?? "Dropped successfully";
      toast.success(msg);
      await loadData();
      setSectionId("");
    } catch (err) {
      console.error("Drop error", err);
      const msg =
        err.response?.data?.message ??
        err.response?.data?.error ??
        "Error dropping class";
      toast.error(msg);
    } finally {
      setDropping(false);
    }
  };

  const handleSelectAvailable = (e) => {
    const value = e.target.value;
    setSectionId(value);
  };

  return (
    <div className="enrollment-page">
      <ToastContainer position="bottom-right" autoClose={2500} />

      <h1 className="enrollment-title">Enrollment</h1>

      <div className="enrollment-card">
        <div className="enrollment-row">
          <label className="enrollment-label">Student ID</label>
          <input
            className="enrollment-input"
            value={studentId}
            onChange={(e) => setStudentId(e.target.value)}
            placeholder="Student ID"
          />
        </div>

        <div className="enrollment-row term-year-row">
          <div>
            <label className="enrollment-label">Term</label>
            <select
              className="enrollment-input"
              value={term}
              onChange={(e) => setTerm(e.target.value)}
            >
              <option value="FALL">Fall</option>
              <option value="SPRING">Spring</option>
              <option value="SUMMER">Summer</option>
            </select>
          </div>
          <div>
            <label className="enrollment-label">Year</label>
            <input
              className="enrollment-input"
              type="number"
              value={year}
              onChange={(e) => setYear(Number(e.target.value))}
            />
          </div>

          <button
            className="enrollment-button secondary"
            onClick={() => loadData()}
            disabled={loadingTable}
          >
            {loadingTable ? <span className="spinner" /> : "Load Enrollments"}
          </button>
        </div>

        <div className="enrollment-row">
          <label className="enrollment-label">Section to enroll / drop</label>
          <div className="enrollment-section-row">
            <input
              className="enrollment-input"
              value={sectionId}
              onChange={(e) => setSectionId(e.target.value)}
              placeholder="Section ID"
            />

            <select
              className="enrollment-input section-dropdown"
              value={sectionId || ""}
              onChange={handleSelectAvailable}
            >
              <option value="">Select from available sections…</option>
              {availableSections.map((sec) => (
                <option key={sec.sectionID} value={sec.sectionID}>
                  {sec.sectionID} – {sec.courseCode ?? sec.courseName}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div className="enrollment-row buttons-row">
          <button
            className="enrollment-button enroll"
            onClick={handleEnroll}
            disabled={enrolling}
          >
            {enrolling ? <span className="spinner" /> : "Enroll"}
          </button>

          <button
            className="enrollment-button drop"
            onClick={handleDrop}
            disabled={dropping}
          >
            {dropping ? <span className="spinner" /> : "Drop"}
          </button>
        </div>
      </div>

      <div className="enrollment-table-wrapper">
        <table className="enrollment-table">
          <thead>
            <tr>
              <th>Section</th>
              <th>Class Name</th>
              <th>Enrolled On</th>
              <th>Grade</th>
            </tr>
          </thead>
          <tbody>
            {loadingTable ? (
              <tr>
                <td colSpan="4" className="center-cell">
                  <span className="spinner" /> Loading…
                </td>
              </tr>
            ) : enrollments.length === 0 ? (
              <tr>
                <td colSpan="4" className="center-cell">
                  No enrollments for this term/year.
                </td>
              </tr>
            ) : (
              enrollments.map((enr) => (
                <tr key={enr.sectionID ?? enr.section_id}>
                  <td>{enr.sectionID ?? enr.section_id}</td>
                  <td>{enr.className ?? enr.courseName}</td>
                  <td>{enr.enrolledOn ?? enr.enrolled_on}</td>
                  <td>{enr.grade ?? "-"}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
