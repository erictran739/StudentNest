import React, { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

/**
 * Admin page — scaffold only.
 * - Lists users (id, name/email) and lets you select a new role (Admin | Student | Professor)
 * - “Apply Changes” button prepares PATCH/PUT calls
 * - Only accessible to admins: guard code is present but COMMENTED OUT per your request.
 * - Uses your Home layout/theme classes.
 *
 * Back-end references (from your auth-test.js):
 *   GET  /api/users          -> list users                                  (implemented in auth-test.js)
 *   // TODO: Update role endpoint is not defined in auth-test.js.
 *   // Example we’ll prep for (adjust to your real API):
 *   // PATCH /api/users/:id   body: { role: "Admin" | "Student" | "Professor" }
 *
 * See: listUsers() in auth-test.js (GET /api/users)
 */

const ROLES = ["Admin", "Student", "Professor"];

export default function Admin() {
  const nav = useNavigate();

  // ----- Admin-only access (commented as requested) -----
  // useEffect(() => {
  //   const token = localStorage.getItem("authToken");
  //   const userRole = localStorage.getItem("role"); // or decode from token
  //   if (!token || userRole !== "Admin") {
  //     nav("/"); // or nav("/login")
  //   }
  // }, [nav]);

  const [drawerOpen, setDrawerOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [status, setStatus] = useState("");

  // Raw users fetched from API
  const [users, setUsers] = useState([]);
  // Proposed edits keyed by user id: { [id]: "Admin" | "Student" | "Professor" }
  const [pending, setPending] = useState({});

  // Load users — prepared; you can uncomment when you’re ready to hook up
  useEffect(() => {
    let isMounted = true;
    const fetchUsers = async () => {
      setLoading(true);
      setStatus("Loading users...");
      try {
        // Uncomment when ready:
        // const r = await fetch("/api/users");
        // const data = await r.json();
        // if (!r.ok) throw new Error(data?.message || "Failed to fetch users");
        // if (isMounted) setUsers(Array.isArray(data) ? data : (data?.users || []));

        // Temporary mock UI (remove once you enable fetch above):
        if (isMounted) {
          setUsers([
            { id: "u1", email: "alice@example.com", firstName: "Alice", lastName: "A.", role: "Student" },
            { id: "u2", email: "bob@example.com",   firstName: "Bob",   lastName: "B.", role: "Professor" },
            { id: "u3", email: "admin@example.com", firstName: "Chris", lastName: "C.", role: "Admin" },
          ]);
        }
        setStatus("");
      } catch (e) {
        if (isMounted) setStatus(e.message || "Error loading users");
      } finally {
        if (isMounted) setLoading(false);
      }
    };
    fetchUsers();
    return () => { isMounted = false; };
  }, []);

  const hasChanges = useMemo(
    () => Object.keys(pending).length > 0,
    [pending]
  );

  const setUserRole = (userId, newRole) => {
    setPending(prev => {
      const next = { ...prev };
      next[userId] = newRole;
      // Remove pending entry if it equals the current role (optional)
      const current = users.find(u => u.id === userId)?.role;
      if (current === newRole) delete next[userId];
      return next;
    });
  };

  const applyChanges = async () => {
    if (!hasChanges) return;
    setStatus("Applying changes...");
    try {
      // Example batch apply (adjust to your real API):
      // for (const [id, newRole] of Object.entries(pending)) {
      //   const r = await fetch(`/api/users/${encodeURIComponent(id)}`, {
      //     method: "PATCH",
      //     headers: { "Content-Type": "application/json" },
      //     body: JSON.stringify({ role: newRole }),
      //   });
      //   const data = await r.json().catch(() => ({}));
      //   if (!r.ok) throw new Error(data?.message || `Failed to update user ${id}`);
      // }

      // Update local UI immediately (remove once server is wired)
      setUsers(prev => prev.map(u => pending[u.id] ? { ...u, role: pending[u.id] } : u));
      setPending({});
      setStatus("Changes applied.");
    } catch (e) {
      setStatus(e.message || "Failed to apply changes.");
    }
  };

  const logout = () => {
    try { localStorage.removeItem("authToken"); } catch {}
    nav("/");
  };

  return (
    <div className="home-page">{/* reuse home classes for theme consistency */}{/* :contentReference[oaicite:1]{index=1} */}
      {/* Top bar */}
      <header className="home-topbar">
        <div className="home-left">
          <button
            className="home-menu-btn"
            onClick={() => setDrawerOpen(true)}
            aria-label="Open navigation"
          >
            ☰
          </button>

          <img src="/lb_logo.png" alt="LB" className="home-logo" />
          <div className="home-title-top">Admin — User Roles</div>
        </div>

        <div className="home-right">
          {/* Return Home button */}
          <button className="icon-btn" aria-label="Return Home" onClick={() => nav("/home")}>
            <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
              <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"/>
            </svg>
          </button>
          {/* (optional) admin profile icon */}
          <button className="icon-btn" aria-label="Logout" onClick={logout}>
            <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
              <path d="M16 13v-2H7V8l-5 4 5 4v-3zM20 3h-8v2h8v14h-8v2h8a2 2 0 0 0 2-2V5a2 2 0 0 0-2-2z"/>
            </svg>
          </button>
        </div>
      </header>

      {/* Slide-out drawer + overlay (same pattern as Home) */}
      <div
        className={`home-drawer-overlay ${drawerOpen ? "show" : ""}`}
        onClick={() => setDrawerOpen(false)}
      />
      <aside className={`home-drawer ${drawerOpen ? "open" : ""}`}>
        <div className="drawer-header">Menu</div>
        <button onClick={() => { setDrawerOpen(false); nav("/home"); }}>Home</button>
        <button onClick={() => { setDrawerOpen(false); /* nav("/admin") */ }}>Admin</button>
        <button onClick={logout}>Logout</button>
      </aside>

      {/* Main content */}
      <main className="home-content">
        <section className="home-card">
          <div className="home-card-header" style={{ alignItems: "center", display: "flex", justifyContent: "space-between" }}>
            <h2>User Role Management</h2>
            <div>
              <button
                className="home-shortcut"
                onClick={() => nav("/home")}
                aria-label="Return Home"
                style={{ padding: "8px 12px" }}
              >
                ⬅️ Return Home
              </button>
            </div>
          </div>

          {status && (
            <div className="note" style={{ marginBottom: 12 }}>
              {status}
            </div>
          )}

          <div className="home-table-wrap">
            <table className="home-table">
              <thead>
                <tr>
                  <th style={{ minWidth: 120 }}>User ID</th>
                  <th style={{ minWidth: 180 }}>Name / Email</th>
                  <th style={{ minWidth: 160 }}>Current Role</th>
                  <th style={{ minWidth: 200 }}>Set New Role</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td colSpan={4}>Loading…</td></tr>
                ) : users.length === 0 ? (
                  <tr><td colSpan={4}>No users found.</td></tr>
                ) : (
                  users.map(u => {
                    const fullName = [u.firstName, u.lastName].filter(Boolean).join(" ") || "(no name)";
                    const currentRole = u.role || "Student";
                    const pendingRole = pending[u.id] ?? currentRole;

                    return (
                      <tr key={u.id}>
                        <td>{u.id}</td>
                        <td>
                          <div style={{ fontWeight: 600 }}>{fullName}</div>
                          <div style={{ opacity: 0.8 }}>{u.email}</div>
                        </td>
                        <td>{currentRole}</td>
                        <td>
                          <select
                            value={pendingRole}
                            onChange={(e) => setUserRole(u.id, e.target.value)}
                          >
                            {ROLES.map(r => (
                              <option key={r} value={r}>{r}</option>
                            ))}
                          </select>
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>

          <div style={{ display: "flex", gap: 12, justifyContent: "flex-end", marginTop: 12 }}>
            <button className="home-shortcut" onClick={() => nav("/home")}>⬅️ Return Home</button>
            <button
              className="home-shortcut"
              onClick={applyChanges}
              disabled={!hasChanges}
              aria-disabled={!hasChanges}
              title={hasChanges ? "Apply role changes" : "No changes to apply"}
            >
              ✅ Apply Changes
            </button>
          </div>
        </section>
      </main>
    </div>
  );
}
