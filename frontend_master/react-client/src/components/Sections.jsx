import React, { useEffect, useState } from "react";
import { getAvailableSections } from "../api/enrollment";

export default function Sections() {
  const [sections, setSections] = useState([]);
  const [loading, setLoading] = useState(false);
  const [deptFilter, setDeptFilter] = useState("");
  const [search, setSearch] = useState("");

  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        const res = await getAvailableSections();
        const data = res.data?.data ?? res.data ?? [];
        setSections(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error("Error loading sections:", err);
        setSections([]);
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  const filtered = sections.filter((sec) => {
    const dept =
      (sec.department_abbreviation ??
        sec.departmentCode ??
        sec.department ??
        ""
      ).toLowerCase();

    const courseName = (sec.courseName ?? sec.course_name ?? "").toLowerCase();
    const id = String(sec.sectionID ?? sec.section_id ?? "").toLowerCase();

    const matchesDept = deptFilter ? dept === deptFilter.toLowerCase() : true;
    const matchesSearch =
      !search ||
      courseName.includes(search.toLowerCase()) ||
      id.includes(search.toLowerCase());

    return matchesDept && matchesSearch;
  });

  // collect unique departments for filter
  const departments = Array.from(
    new Set(
      sections.map(
        (s) =>
          s.department_abbreviation ||
          s.departmentCode ||
          s.department ||
          ""
      )
    )
  ).filter(Boolean);

  return (
    <div className="home-page" style={{ padding: "32px 80px" }}>
      <h1 style={{ fontSize: 28, marginBottom: 16 }}>All Sections</h1>

      {/* Filters */}
      <div style={{ display: "flex", gap: 16, marginBottom: 16 }}>
        <input
          style={{
            padding: "8px 12px",
            borderRadius: 999,
            border: "1px solid #ddd",
            flex: 1,
          }}
          placeholder="Search by section or course name…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />

        <select
          style={{
            padding: "8px 12px",
            borderRadius: 999,
            border: "1px solid #ddd",
            minWidth: 140,
          }}
          value={deptFilter}
          onChange={(e) => setDeptFilter(e.target.value)}
        >
          <option value="">All Departments</option>
          {departments.map((d) => (
            <option key={d} value={d}>
              {d}
            </option>
          ))}
        </select>
      </div>

      {/* Table */}
      <div className="home-card">
        <div className="home-table-wrap">
          <table className="home-table">
            <thead>
              <tr>
                <th>Section</th>
                <th>Course Name</th>
                <th>Department</th>
                <th>Units</th>
                <th>Room</th>
                <th>Days</th>
                <th>Time</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="7" style={{ textAlign: "center" }}>
                    Loading…
                  </td>
                </tr>
              ) : filtered.length === 0 ? (
                <tr>
                  <td colSpan="7" style={{ textAlign: "center" }}>
                    No sections found.
                  </td>
                </tr>
              ) : (
                filtered.map((sec) => (
                  <tr key={sec.sectionID ?? sec.section_id}>
                    <td>{sec.sectionID ?? sec.section_id}</td>
                    <td>{sec.courseName ?? sec.course_name}</td>
                    <td>
                      {sec.department_abbreviation ??
                        sec.departmentCode ??
                        sec.department ??
                        "—"}
                    </td>
                    <td>{sec.units ?? sec.credits ?? "—"}</td>
                    <td>{sec.room ?? sec.location ?? "—"}</td>
                    <td>{sec.days ?? sec.meetingDays ?? "—"}</td>
                    <td>{sec.time ?? sec.classTime ?? "—"}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
