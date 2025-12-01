// src/api/enrollment.js
import axios from "axios";

const API = "https://puggu.dev/auth-test"; // or your local base URL

// Get enrollment history for a student
// backend DTO: CourseHistoryRequest(student_id, term, year)
export const getEnrollments = (studentId, term = "FALL", year = 2024) => {
  return axios.post(`${API}/api/student/history`, {
    student_id: Number(studentId),
    term,
    year,
  });
};

// Enroll student in a section
// backend DTO: EnrollSectionRequest(student_id, section_id)
export const enrollStudent = (studentId, sectionId) => {
  return axios.post(`${API}/api/students/enroll`, {
    student_id: Number(studentId),
    section_id: Number(sectionId),
  });
};

// Drop student from a section
// backend DTO: DropSectionRequest(student_id, section_id, reason)
export const dropStudent = (studentId, sectionId, reason = "Dropped from UI") => {
  return axios.post(`${API}/api/students/drop`, {
    student_id: Number(studentId),
    section_id: Number(sectionId),
    reason,
  });
};

// Optional helper – list sections this student can still enroll in
// (update the endpoint to whatever your backend uses)
export const getAvailableSections = (studentId, term = "FALL", year = 2024) => {
  // TEMP: example path – change to real one if different
  return axios.get(
    `${API}/api/sections/available?student_id=${studentId}&term=${term}&year=${year}`
  );
};
