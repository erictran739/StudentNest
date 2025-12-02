import React, { useEffect, useState } from "react";
import {
  getEnrollments,
  enrollStudent,
  dropStudent,
  getAvailableSections,
} from "../api/enrollment";

import "./Enrollment.css";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function Enrollment() {
  const storedStudentId = localStorage.getItem("studentId");
  const [studentId, setStudentId] = useState(storedStudentId || "");

  const [sectionId, setSectionId] = useState("");
  const [term] = useState("FALL");
  const [year] = useState(2024);

  const [enrollments, setEnrollments] = useState([]);
  const [loadingTable, setLoadingTable] = useState(false);
  const [enrolling, setEnrolling] = useState(false);
  const [dropping, setDropping] = useState(false);

  // Auto-load history
  useEffect(() => {
    if (studentId) loadData();
  }, [studentId]);

  const loadData = async () => {
    try {
      setLoadingTable(true);

      const historyRes = await getEnrollments(studentId, term, year);
      const history = historyRes.data || [];

      setEnrollments(history);
      toast.success("Enrollment history loaded");
    } catch (err) {
      toast.error("Failed to load enrollments");
      console.error("History load error:", err);
    } finally {
      setLoadingTable(false);
    }
  };

  const handleEnroll = async () => {
    if (!sectionId) {
      toast.error("Enter a section ID");
      return;
    }

    try {
      setEnrolling(true);
      const res = await enrollStudent(studentId, sectionId);
      toast.success(res.data?.message || "Enrolled!");
      await loadData();
      setSectionId("");
    } catch (err) {
      toast.error(err.response?.data?.message || "Error enrolling");
    } finally {
      setEnrolling(false);
    }
  };

  const handleDrop = async () => {
    if (!sectionId) {
      toast.error("Enter a section ID");
      return;
    }

    try {
      setDropping(true);
      const res = await dropStudent(studentId, sectionId);
      toast.success(res.data?.message || "Dropped");
      await loadData();
      setSectionId("");
    } catch (err) {
      toast.error(err.response?.data?.message || "Error dropping class");
    } finally {
      setDropping(false);
    }
  };

  return (
    <div className="enrollment-page">
      <ToastContainer position="bottom-right" autoClose={2500} />

      <h1 className="enrollment-title">Enrollment</h1>

      {/* INPUT CARD */}
      <div className="enrollment-card">
        <div className="enrollment-row">
          <label className="enrollment-label">Student ID</label>
          <input
            className="enrollment-input"
            value={studentId}
            onChange={(e) => setStudentId(e.target.value)}
          />
        </div>

        <div className="enrollment-row">
          <label className="enrollment-label">Section ID</label>
          <input
            className="enrollment-input"
            value={sectionId}
            onChange={(e) => setSectionId(e.target.value)}
            placeholder="e.g. 12"
          />
        </div>

        <div className="buttons-row">
          <button
            className="enrollment-button enroll"
            onClick={handleEnroll}
            disabled={enrolling}
          >
            {enrolling ? "..." : "Enroll"}
          </button>

          <button
            className="enrollment-button drop"
            onClick={handleDrop}
            disabled={dropping}
          >
            {dropping ? "..." : "Drop"}
          </button>
        </div>
      </div>

      {/* ENROLLMENT TABLE */}
      <div className="enrollment-table-wrapper">
        <table className="enrollment-table">
          <thead>
            <tr>
              <th>Section</th>
              <th>Course Name</th>
              <th>Department</th>
              <th>Enrolled On</th>
              <th>Grade</th>
            </tr>
          </thead>

          <tbody>
            {loadingTable ? (
              <tr>
                <td colSpan="5" className="center-cell">Loading…</td>
              </tr>
            ) : enrollments.length === 0 ? (
              <tr>
                <td colSpan="5" className="center-cell">No enrollments</td>
              </tr>
            ) : (
              enrollments.map((row) => (
                <tr key={row.section_id}>
                  <td>{row.section_id}</td>
                  <td>{row.course_name}</td>
                  <td>{row.department_abbreviation}</td>
                  <td>{row.enrolled_on || "—"}</td>
                  <td>{row.grade || "-"}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
