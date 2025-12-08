// src/api/courses.js
// Unified helper for safe JSON
const j = async (res) => {
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.message || data.error || res.statusText);
  return data;
};

// -----------------------------
// NORMALIZE BACKEND → FRONTEND
// -----------------------------
const normalizeCourse = (raw) => {
  if (!raw) return null;

  return {
    id: raw.course_id ?? raw.id ?? null,
    name: raw.name ?? "",
    credits: raw.credits ?? 0,
    description: raw.description ?? "",
    dept: raw.department_abbreviation ?? "",
    department: raw.department_name ?? "",
  };
};

// -----------------------------
// GET SINGLE COURSE
// -----------------------------
export const getCourse = async (id) => {
  const res = await fetch(`/api/courses/get/${id}`);
  const data = await j(res);
  return normalizeCourse(data);
};

// -----------------------------
// GET SPECIFIC SECTION OF COURSE
// -----------------------------
export const getSectionOfCourse = async (courseId, sectionId) => {
  const data = await fetch(`/api/courses/${courseId}/section/${sectionId}`).then(j);
  return {
    sectionId: data.section_id ?? data.id ?? null,
    building: data.building ?? "",
    room: data.room_number ?? "",
    type: data.type ?? "",
    term: data.term ?? "",
    days: data.days ?? "",
    start: data.start_time ?? "",
    end: data.end_time ?? "",
    course: normalizeCourse(data.course || {}),
  };
};

// -----------------------------
// GET COURSES BY DEPARTMENT
// -----------------------------
// Backend returns ARRAY of CourseResponse
export const getCoursesByDepartment = async (dept) => {
  const res = await fetch(`/api/courses/get/dept_abbr/${dept}`);
  const arr = await j(res);
  if (!Array.isArray(arr)) return [];
  return arr.map(normalizeCourse);
};

// -----------------------------
// ADMIN — CREATE COURSE
// -----------------------------
export const createCourse = (payload) =>
  fetch(`/api/courses/create`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  }).then(j);

// -----------------------------
// ADMIN — ADD SECTION
// -----------------------------
export const addSection = (payload) =>
  fetch(`/api/courses/add/section`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  }).then(j);

