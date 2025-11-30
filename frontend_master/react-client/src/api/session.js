// src/api/session.js

// ---- storage keys ----
const LS_TOKEN   = "authToken";   // if you also issue tokens later
const LS_USER_ID = "authUserId";
const LS_USER    = "authUser";    // cached profile

// ---- tiny utils ----
const safeJson = async (r) => r.json().catch(() => ({}));
const ok = (r, d) => {
  if (!r.ok) throw new Error(d?.message || d?.error || r.statusText);
  return d;
};

// Optionally expose token helpers if you start returning tokens
export const setAuthToken = (token) => { try { localStorage.setItem(LS_TOKEN, token); } catch {} };
export const getAuthToken = () => { try { return localStorage.getItem(LS_TOKEN); } catch { return null; } };

// ---- ID-first login flows ----

// 1) Preferred: /auth/login returns a user id
export async function loginAndCacheId({ email, password }) {
  const res = await fetch("/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });
  const data = await safeJson(res);
  ok(res, data);

  const userId = data.id || data.userId || data.uid || data.user_id;
  if (!userId) throw new Error("Login succeeded, but no user id was returned.");
  try { localStorage.setItem(LS_USER_ID, String(userId)); } catch {}
  // if a token is also returned, cache it
  const token = data.token || data.access_token || data.jwt;
  if (token) setAuthToken(token);

  // hydrate profile cache (non-blocking)
  fetchUserById(userId).catch(() => {});
  return userId;
}

// 2) Alternate: /api/users/id (body: email, password) also returns an id
export async function getIdByCredentials({ email, password }) {
  const res = await fetch("/api/users/id", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });
  const data = await safeJson(res);
  ok(res, data);

  const userId = data.id || data.userId || data.uid || data.user_id;
  if (!userId) throw new Error("No user id returned.");
  try { localStorage.setItem(LS_USER_ID, String(userId)); } catch {}
  // hydrate profile cache (non-blocking)
  fetchUserById(userId).catch(() => {});
  return userId;
}

// ---- User/profile helpers ----

export function getCurrentUserId() {
  try { return localStorage.getItem(LS_USER_ID); } catch { return null; }
}

// GET /api/users/{id} → { userID, firstName, lastName, email, status, ... }
export async function fetchUserById(id) {
  if (!id) throw new Error("Missing user id");
  const res = await fetch(`/api/users/${encodeURIComponent(id)}`);
  const data = await safeJson(res);
  ok(res, data);
  try { localStorage.setItem(LS_USER, JSON.stringify(data)); } catch {}
  return data;
}

// Returns cached user immediately if present; otherwise tries to fetch via id.
export async function getCurrentUser() {
  try {
    const raw = localStorage.getItem(LS_USER);
    if (raw) return JSON.parse(raw);
  } catch {}

  const id = getCurrentUserId();
  if (!id) return null;

  try {
    const user = await fetchUserById(id);
    return user;
  } catch {
    return null;
  }
}

// Clear everything
export function clearSession() {
  try {
    localStorage.removeItem(LS_TOKEN);
    localStorage.removeItem(LS_USER_ID);
    localStorage.removeItem(LS_USER);
  } catch {}
}
