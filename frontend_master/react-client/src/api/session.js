export function getCurrentUser() {
  try {
    const raw = localStorage.getItem("authUser");
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function getCurrentUserId() {
    const user = getCurrentUser();
    if (!user) return null;
    return user.user_id;
  }

export function clearSession() {
  try {
    localStorage.removeItem("authToken");
    localStorage.removeItem("authUser");
  } catch {}
}
