import { useEffect, useState } from "react";
import {
  getEnrollments,
  enrollStudent,
  dropStudent
} from "../api/enrollment";

import "./Enrollment.css";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import ClipLoader from "react-spinners/ClipLoader";

export default function Enrollment() {
  const [studentId, setStudentId] = useState("");
  const [sectionId, setSectionId] = useState("");
  const [enrollments, setEnrollments] = useState([]);
  const [loading, setLoading] = useState(false);

  // Auto-fill student ID from login storage
  useEffect(() => {
    const data = JSON.parse(localStorage.getItem("user"));
    if (data?.id) setStudentId(data.id);
  }, []);

  const loadEnrollments = async () => {
    try {
      setLoading(true);
      const res = await getEnrollments(studentId);
      setEnrollments(res.data);
      toast.success("Enrollments loaded");
    } catch (err) {
      toast.error("Failed to load enrollments");
    } finally {
      setLoading(false);
    }
  };

  const handleEnroll = async () => {
    try {
      setLoading(true);
      await enrollStudent(studentId, sectionId);
      toast.success("Enrolled successfully");
      loadEnrollments();
    } catch (err) {
      toast.error("Error enrolling student");
    } finally {
      setLoading(false);
    }
  };

  const handleDrop = async () => {
    try {
      setLoading(true);
      await dropStudent(studentId, sectionId);
      toast.success("Dropped section");
      loadEnrollments();
    } catch (err) {
      toast.error("Error dropping section");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="enroll-container">
      <h2>Enrollment</h2>

      <input
        placeholder="Student ID"
        value={studentId}
        onChange={(e) => setStudentId(e.target.value)}
      />

      <button className="load-btn" onClick={loadEnrollments}>
        Load Enrollments
      </button>

      <input
        placeholder="Section ID"
        value={sectionId}
        onChange={(e) => setSectionId(e.target.value)}
      />

      <div className="button-row">
        <button className="enroll-btn" onClick={handleEnroll}>
          Enroll
        </button>

        <button className="drop-btn" onClick={handleDrop}>
          Drop
        </button>
      </div>

      {loading && (
        <div className="loader">
          <ClipLoader size={45} color="#444" />
        </div>
      )}

      <table className="enroll-table">
        <thead>
          <tr>
            <th>Section</th>
            <th>Class Name</th>
            <th>Enrolled On</th>
            <th>Grade</th>
          </tr>
        </thead>

        <tbody>
          {enrollments.map((e, idx) => (
            <tr key={idx}>
              <td>{e.section_id}</td>
              <td>{e.className}</td>
              <td>{e.enrollmentDate}</td>
              <td>{e.grade}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <ToastContainer position="bottom-right" />
    </div>
  );
}
