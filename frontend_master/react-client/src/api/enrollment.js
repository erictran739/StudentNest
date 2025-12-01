// src/api/enrollment.js
import axios from "axios";

// Adjust base URL if your backend is different in production
const BASE_URL = "http://localhost:8080";

// ---- HISTORY ----
// POST /api/student/history
export const fetchCourseHistory = async (studentId, term, year) => {
  return axios.post(`${BASE_URL}/api/student/history`, {
    student_id: Number(studentId),
    term,
    year: Number(year),
  });
};

// ---- ENROLL ----
// POST /api/students/enroll
export const enrollStudent = async (studentId, sectionId) => {
  return axios.post(`${BASE_URL}/api/students/enroll`, {
    student_id: Number(studentId),
    section_id: Number(sectionId),
  });
};

// ---- DROP ----
// POST /api/students/drop
export const dropStudent = async (studentId, sectionId) => {
  return axios.post(`${BASE_URL}/api/students/drop`, {
    student_id: Number(studentId),
    section_id: Number(sectionId),
  });
};
