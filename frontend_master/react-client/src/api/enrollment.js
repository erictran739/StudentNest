// src/api/enrollment.js
import axios from "axios";

const API = "https://puggu.dev";

// ------- GET ENROLLMENT HISTORY (POST instead of GET) -------
export const getEnrollments = (studentId, term = "FALL", year = 2024) => {
  return axios.post(`${API}/api/students/history`, {
    student_id: Number(studentId),
    term,
    year,
  });
};

// ------- ENROLL STUDENT -------
export const enrollStudent = (studentId, sectionId) => {
  return axios.post(`${API}/api/students/enroll`, {
    student_id: Number(studentId),
    section_id: Number(sectionId),
  });
};

// ------- DROP STUDENT -------
export const dropStudent = (studentId, sectionId) => {
  return axios.post(`${API}/api/students/drop`, {
    student_id: Number(studentId),
    section_id: Number(sectionId),
    reason: "Dropped from UI",
  });
};

// ------- AVAILABLE SECTIONS (Not implemented backend) -------
export const getAvailableSections = () => Promise.resolve({ data: [] });
