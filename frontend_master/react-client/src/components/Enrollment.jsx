// src/components/Enrollment.jsx
import React, { useEffect, useState } from "react";
import "./Enrollment.css";

import {
  fetchCourseHistory,
  enrollStudent,
  dropStudent,
} from "../api/enrollment";

// If your friend added this helper, use it.
// Otherwise, swap this import to whatever you use to read the current user.
import { getCurrentUser } from "../api/session"; // or "../api/authHelpers"

import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function Enrollment() {
  const [studentId, setStudentId] = useState("");
  const [sectionId, setSectionId] = useState("");
  const [term, setTerm] = useState("Fall");
  const [year, setYear] = useState(new Date().getFullYear());
  const [enrollments, setEnrollments] = useState([]);

  const [loadingHistory, setLoadingHistory] = useState(false);
  const [loadingEnroll, setLoadingEnroll] = useState(false);
  const [loadingDrop, setLoadingDrop] = useState(false);

  // ---------- Auto-fill student from login ----------
  useEffect(() => {
    try {
      const user = getCurrentUser && getCurrentUser();
      // Adjust this depending on your actual user object shape
      if (user) {
        // some backends use user.id, some use user.student_id
        const id = user.student_id || user.id;
        if (id) {
          setStudentId(String(id));
        }
      }
    } catch (e) {
      console.warn("Could not load current user:", e);
    }
  }, []);

  // ---------- Load history ----------
  const handleLoadHistory = async () => {
    if (!studentId) {
      toast.error("Please enter a student ID");
      return;
    }

    setLoadingHistory(true);
    try {
      const response = await fetchCourseHistory(studentId, term, year);
      // adjust if your backend wraps data differently
      setEnrollments(response.data || []);
      toast.success("Enrollments loaded");
    } catch (error) {
      console.error("Error loading enrollments:", error);
      toast.error("Failed to load enrollments");
    } finally {
      setLoadingHistory(false);
    }
  };

  // ---------- Enroll ----------
  const handleEnroll = async () => {
    if (!studentId || !sectionId) {
      toast.error("Please enter both student ID and section ID");
      return;
    }

    setLoadingEnroll(true);
    try {
      await enrollStudent(studentId, sectionId);
      toast.success("Enrolled successfully");
      // reload history to show the new class
      await handleLoadHistory();
    } catch (error) {
      console.error("Error enrolling student:", error);
      toast.error("Error enrolling student");
    } finally {
      setLoadingEnroll(false);
    }
  };

  // ---------- Drop ----------
  const handleDrop = async () => {
    if (!studentId || !sectionId) {
      toast.error("Please enter both student ID and section ID");
      return;
    }

    setLoadingDrop(true);
    try {
      await dropStudent(studentId, sectionId);
      toast.success("Dropped successfully");
      // reload history to reflect changes
      await handleLoadHistory();
    } catch (error) {
      console.error("Error dropping student:", error);
      toast.error("Error dropping student");
    } finally {
      setLoadingDrop(false);
    }
  };

  return (
    <div className="enrollment-page">
      <ToastContainer position="bottom-right" />

      <h1 className="enrollment-title">Enrollment</h1>

      {/* Top inputs */}
      <div className="enrollment-input-row">
        <input
          className="enrollment-input"
          placeholder="Student ID"
          value={studentId}
          onChange={(e) => setStudentId(e.target.value)}
        />

        <select
          className="enrollment-select"
          value={term}
          onChange={(e) => setTerm(e.target.value)}
        >
          <option value="Spring">Spring</option>
          <option value="Summer">Summer</option>
          <option value="Fall">Fall</option>
        </select>

        <input
          className="enrollment-input small"
          type="number"
          placeholder="Year"
          value={year}
          onChange={(e) => setYear(e.target.value)}
        />
      </div>

      <button
        className="enrollment-btn load-btn"
        onClick={handleLoadHistory}
        disabled={loadingHistory}
      >
        {loadingHistory ? "Loading..." : "Load Enrollments"}
      </button>

      {/* Section input + Enroll / Drop */}
      <div className="enrollment-input-row">
        <input
          className="enrollment-input"
          placeholder="Section ID"
          value={sectionId}
          onChange={(e) => setSectionId(e.target.value)}
        />
      </div>

      <div className="enrollment-actions-row">
        <button
          className="enrollment-btn enroll-btn"
          onClick={handleEnroll}
          disabled={loadingEnroll}
        >
          {loadingEnroll ? "Enrolling..." : "Enroll"}
        </button>

        <button
          className="enrollment-btn drop-btn"
          onClick={handleDrop}
          disabled={loadingDrop}
        >
          {loadingDrop ? "Dropping..." : "Drop"}
        </button>
      </div>

      {/* Table */}
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
          {enrollments.length === 0 ? (
            <tr>
              <td colSpan="4" className="enrollment-empty">
                No enrollments to show
              </td>
            </tr>
          ) : (
            enrollments.map((row) => (
              <tr key={row.section_id || `${row.section}-${row.name}`}>
                <td>{row.section || row.section_id}</td>
                <td>{row.className || row.course_name}</td>
                <td>{row.enrolledOn || row.enrolled_date}</td>
                <td>{row.grade ?? "-"}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
