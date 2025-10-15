// api.js
export const API = import.meta.env.VITE_API_BASE ?? "http://localhost:8080";

export async function login(email, password) {
  const res = await fetch(`${API}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });

  const text = await res.text();
  let data = {};
  try { data = JSON.parse(text); } catch {}

  if (!res.ok) throw new Error(data.message || data.status || `HTTP ${res.status}`);
  return data; // e.g. { status: "Login successful" }
}
