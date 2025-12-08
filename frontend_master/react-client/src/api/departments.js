
const API_BASE = ""; // or http://localhost:8080 for local

async function handleResponse(res) {
  const text = await res.text();
  let data;
  try {
    data = text ? JSON.parse(text) : null;
  } catch {
    data = text;
  }

  if (!res.ok) {
    const message =
      data && data.detail
        ? data.detail
        : `Request failed (${res.status})`;
    throw new Error(message);
  }
  return data;
}

export async function fetchDepartments() {
  const res = await fetch(`${API_BASE}/api/departments`);
  return handleResponse(res); // -> array of DepartmentResponse
}

export async function fetchDepartment(id) {
  const res = await fetch(`${API_BASE}/api/departments/${id}`);
  return handleResponse(res);
}

export async function createDepartment(payload) {
  const res = await fetch(`${API_BASE}/api/departments`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  return handleResponse(res);
}

export async function updateDepartment(id, payload) {
  const res = await fetch(`${API_BASE}/api/departments/${id}`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  return handleResponse(res);
}

export async function deleteDepartment(id) {
  const res = await fetch(`${API_BASE}/api/departments/${id}`, {
    method: "DELETE",
  });
  if (res.status === 204) return true;
  return handleResponse(res);
}

/**
 * Assign or change chair for a department.
 * chairUserId must be a DepartmentChair user (from your backend).
 */
export async function assignDepartmentChair(deptId, chairUserId) {
  const res = await fetch(
    `${API_BASE}/api/departments/${deptId}/chair?chairUserId=${encodeURIComponent(
      chairUserId
    )}`,
    { method: "PUT" }
  );
  return handleResponse(res);
}

/**
 * Remove chair from a department.
 */
export async function removeDepartmentChair(deptId, chairUserId) {
  const res = await fetch(
    `${API_BASE}/api/departments/${deptId}/chair?chairUserId=${encodeURIComponent(
      chairUserId
    )}`,
    { method: "DELETE" }
  );
  if (res.status === 204) return true;
  return handleResponse(res);
}
