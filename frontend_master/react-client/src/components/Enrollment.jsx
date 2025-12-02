// src/components/Enrollment.jsx
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
  // logged-in student from localStorage (set during login)
  const authUser = JSON.parse(localStorage.getItem("authUser") || "null");
  const autoStudentId = authUser?.user_id ?? "";

  const [studentId, setStudentId] = useState(autoStudentId);
  const [sectionId, setSectionId] = useState("");
  const [enrollments, setEnrollments] = useState([]);
  const [sections, setSections] = useState([]);

  const [loadingTable, setLoadingTable] = useState(false);
  const [enrolling, setEnrolling] = useState(false);
  const [dropping, setDropping] = useState(false);

  // Load enrollments + sections once we know studentId
  useEffect(() => {
    if (!studentId) return;
    loadEnrollments(studentId);
    loadSections();
  }, [studentId]);

  const loadEnrollments = async (sid) => {
    const idToUse = sid || studentId;
    if (!idToUse) return;

    try {
      setLoadingTable(true);
      const res = await getEnrollments(idToUse);
      const data = res.data?.data ?? res.data ?? [];
      setEnrollments(Array.isArray(data) ? data : []);
      console.log("Enrollments:", data);
    } catch (err) {
      console.error("Error loading enrollments:", err);
      toast.error("Failed to load enrollments");
      setEnrollments([]);
    } finally {
      setLoadingTable(false);
    }
  };

  const loadSections = async () => {
    try {
      const res = await getAvailableSections();
      const data = res.data?.data ?? res.data ?? [];
      setSections(Array.isArray(data) ? data : []);
      console.log("Available sections:", data);
    } catch (err) {
      console.error("Error loading sections:", err);
      // no toast here; not critical
    }
  };

  const handleEnroll = async () => {
    if (!studentId || !sectionId) {
      toast.error("Please enter or select a Section ID");
      return;
    }
    try {
      setEnrolling(true);
      const res = await enrollStudent(studentId, sectionId);
      const msg = res.data?.message ?? "Student successfully added to section";
      toast.success(msg);
      await loadEnrollments();
      setSectionId("");
    } catch (err) {
      console.error("Enroll error:", err);
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
      toast.error("Please enter or select a Section ID to drop");
      return;
    }
    try {
      setDropping(true);
      const res = await dropStudent(studentId, sectionId);
      const msg = res.data?.message ?? "Dropped student";
      toast.success(msg);
      await loadEnrollments();
      setSectionId("");
    } catch (err) {
      console.error("Drop error:", err);
      const msg =
        err.response?.data?.message ??
        err.response?.data?.error ??
        "Error dropping class";
      toast.error(msg);
    } finally {
      setDropping(false);
    }
  };

  const handleSelectSection = (e) => {
    const value = e.target.value;
    setSectionId(value);
  };

  return (
    <div className="enrollment-page">
      <ToastContainer position="bottom-right" autoClose={2500} />

      <h1 className="enrollment-title">Enrollment</h1>

      <div className="enrollment-card">
        {/* Student ID */}
        <div className="enrollment-row">
          <label className="enrollment-label">Student ID</label>
          <input
            className="enrollment-input"
            value={studentId}
            onChange={(e) => setStudentId(e.target.value)}
            placeholder="Student ID"
          />
        </div>

        {/* Section ID + dropdown of all sections */}
        <div className="enrollment-row">
          <label className="enrollment-label">Section ID</label>
          <div className="enrollment-section-row">
            <input
              className="enrollment-input"
              value={sectionId}
              onChange={(e) => setSectionId(e.target.value)}
              placeholder="eg. 12"
            />

            <select
              className="enrollment-input section-dropdown"
              value={sectionId || ""}
              onChange={handleSelectSection}
            >
              <option value="">Select from available sections…</option>
              {sections.map((sec) => {
                const id = sec.sectionID ?? sec.section_id;
                const courseName =
                  sec.courseName ?? sec.course_name ?? "Untitled Course";
                const dept =
                  sec.department_abbreviation ??
                  sec.departmentCode ??
                  sec.department ??
                  "";

                return (
                  <option key={id} value={id}>
                    {id} – {courseName} {dept ? `(${dept})` : ""}
                  </option>
                );
              })}
            </select>
          </div>
        </div>

        {/* Buttons */}
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

      {/* Enrollment table */}
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
                <td colSpan="5" className="center-cell">
                  <span className="spinner" /> Loading…
                </td>
              </tr>
            ) : enrollments.length === 0 ? (
              <tr>
                <td colSpan="5" className="center-cell">
                  No enrollments
                </td>
              </tr>
            ) : (
              enrollments.map((enr) => (
                <tr key={enr.sectionID ?? enr.section_id}>
                  <td>{enr.sectionID ?? enr.section_id}</td>
                  <td>
                    {enr.className ??
                      enr.courseName ??
                      enr.course_name ??
                      "—"}
                  </td>
                  <td>
                    {enr.department_abbreviation ??
                      enr.departmentName ??
                      enr.department ??
                      "—"}
                  </td>
                  <td>
                    {enr.enrolledOn ??
                      enr.enrollmentDate ??
                      enr.enrolled_on ??
                      "—"}
                  </td>
                  <td>{enr.grade ?? "—"}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
