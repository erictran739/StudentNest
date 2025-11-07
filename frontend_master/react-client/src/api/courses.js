// src/api/courses.js
const j = async (res) => {
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.message || data.error || res.statusText);
  return data;
};

export const getCourse = (id) =>
  fetch(`/api/courses/get/${id}`).then(j);

export const getSectionOfCourse = (courseId, sectionId) =>
  fetch(`/api/courses/${courseId}/section/${sectionId}`).then(j);

// Optional (admin flows)
export const createCourse = (payload) =>
  fetch(`/api/courses/create`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  }).then(j);

export const addSection = (payload) =>
  fetch(`/api/courses/add/section`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  }).then(j);
