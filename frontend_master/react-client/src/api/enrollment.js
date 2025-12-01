import axios from "axios";

const API = "https://puggu.dev/auth-test/api/students";

// GET ALL enrolled classes
export const getEnrollments = (studentId) =>
  axios.get(`${API}/history`, {
    params: { student_id: studentId }
  });

// ENROLL student
export const enrollStudent = (studentId, sectionId) =>
  axios.post(`${API}/enroll`, {
    student_id: studentId,
    section_id: sectionId
  });

// DROP student
export const dropStudent = (studentId, sectionId) =>
  axios.post(`${API}/drop`, {
    student_id: studentId,
    section_id: sectionId
  });
