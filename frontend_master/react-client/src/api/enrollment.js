// src/api/enrollment.js
import axios from "axios";

const API = "https://puggu.dev";

/**
 * 1. Get student's enrollments
 * Preferred: POST /api/students/history
 * Fallback: GET  /api/enrollment/user/{id}
 */
export async function getEnrollments(studentId, term = "FALL", year = 2024) {
  const sid = Number(studentId);

  // Try new /api/students/history first
  try {
    const res = await axios.post(`${API}/api/students/history`, {
      student_id: sid,
      term,
      year,
    });
    return res;
  } catch (err) {
    console.warn("history endpoint failed, trying /api/enrollment/user/{id}", err);

    // Fallback to older endpoint if present
    const res2 = await axios.get(`${API}/api/enrollment/user/${sid}`);
    return res2;
  }
}

/**
 * 2. Enroll student in section
 * POST /api/students/enroll  { student_id, section_id }
 */
export function enrollStudent(studentId, sectionId) {
  return axios.post(`${API}/api/students/enroll`, {
    student_id: Number(studentId),
    section_id: Number(sectionId),
  });
}

/**
 * 3. Drop student from section
 * POST /api/students/drop  { student_id, section_id, reason }
 */
export function dropStudent(studentId, sectionId, reason = "Dropped from UI") {
  return axios.post(`${API}/api/students/drop`, {
    student_id: Number(studentId),
    section_id: Number(sectionId),
    reason,
  });
}

/**
 * 4. Get all available sections
 * GET /api/sections
 */
export function getAvailableSections() {
  return axios.get(`${API}/api/sections`);
}
