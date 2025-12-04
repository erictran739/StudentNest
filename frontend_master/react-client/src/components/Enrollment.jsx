// src/components/Enrollment.jsx
import React, { useEffect, useState } from "react";
import {
  getEnrollments,
  enrollStudent,
  dropStudent,
  getAvailableSections,
} from "../api/enrollment";
import "./Enrollment.css";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function Enrollment() {
  // logged-in student from localStorage (set during login)
  const authUser = JSON.parse(localStorage.getItem("authUser") || "null");
  const autoStudentId = authUser?.user_id ?? "";

  const studentFirst =
  authUser?.first_name ||
  authUser?.firstname ||
  authUser?.firstName ||
  (authUser?.name ? authUser.name.split(" ")[0] : "");

const studentLast =
  authUser?.last_name ||
  authUser?.lastname ||
  authUser?.lastName ||
  (authUser?.name ? authUser.name.split(" ").slice(1).join(" ") : "");

const studentDisplayName =
  [studentFirst, studentLast].filter(Boolean).join(" ") ||
  (autoStudentId ? `ID: ${autoStudentId}` : "Unknown student");


  const [studentId, setStudentId] = useState(autoStudentId);

  const [enrollments, setEnrollments] = useState([]);
  const [sections, setSections] = useState([]);

  const [loadingTable, setLoadingTable] = useState(false);
  const [loadingSections, setLoadingSections] = useState(false);

  // cart workflow
  const [step, setStep] = useState("cart"); // "cart" | "review"
  const [addCart, setAddCart] = useState([]); // sections to ADD
  const [dropCart, setDropCart] = useState([]); // enrollments to DROP
  const [processing, setProcessing] = useState(false);

  // Load enrollments + sections once we know studentId
  useEffect(() => {
    if (!studentId) return;
    loadEnrollments(studentId);
    loadSections();
  }, [studentId]);

  const loadEnrollments = async (sid) => {
    const idToUse = sid || studentId;
    if (!idToUse) return;

    try {
      setLoadingTable(true);
      const res = await getEnrollments(idToUse);
      const data = res.data?.data ?? res.data ?? [];
      setEnrollments(Array.isArray(data) ? data : []);
      console.log("Enrollments:", data);
    } catch (err) {
      console.error("Error loading enrollments:", err);
      toast.error("Failed to load enrollments");
      setEnrollments([]);
    } finally {
      setLoadingTable(false);
    }
  };

  const loadSections = async () => {
    try {
      setLoadingSections(true);
      const res = await getAvailableSections();
      const data = res.data?.data ?? res.data ?? [];
      setSections(Array.isArray(data) ? data : []);
      console.log("Available sections:", data);
    } catch (err) {
      console.error("Error loading sections:", err);
      // not fatal
    } finally {
      setLoadingSections(false);
    }
  };

  // helpers
  const sectionKey = (s) => s.sectionID ?? s.section_id;
  const isInAddCart = (sec) =>
    !!addCart.find((s) => sectionKey(s) === sectionKey(sec));
  const isInDropCart = (enr) =>
    !!dropCart.find((e) => sectionKey(e) === sectionKey(enr));
  const isAlreadyEnrolled = (sec) =>
    !!enrollments.find((e) => sectionKey(e) === sectionKey(sec));

  // CART: add a section to "addCart"
  const handleAddToCart = (sec) => {
    if (!studentId) {
      toast.error("Missing student ID");
      return;
    }
    if (!sectionKey(sec)) return;

    // don't allow adding if already enrolled
    if (isAlreadyEnrolled(sec)) {
      toast.info("Already enrolled in this section.");
      return;
    }

    setAddCart((prev) => {
      if (prev.find((s) => sectionKey(s) === sectionKey(sec))) return prev;
      return [...prev, sec];
    });

    // if it was in dropCart for some reason, remove it
    setDropCart((prev) =>
      prev.filter((e) => sectionKey(e) !== sectionKey(sec))
    );
  };

  // CART: mark an enrollment to drop
  const handleMarkToDrop = (enr) => {
    if (!studentId) {
      toast.error("Missing student ID");
      return;
    }
    if (!sectionKey(enr)) return;

    setDropCart((prev) => {
      if (prev.find((e) => sectionKey(e) === sectionKey(enr))) return prev;
      return [...prev, enr];
    });

    // if this section is queued to add (weird edge case), remove from addCart
    setAddCart((prev) =>
      prev.filter((s) => sectionKey(s) !== sectionKey(enr))
    );
  };

  const removeFromAddCart = (sec) => {
    setAddCart((prev) =>
      prev.filter((s) => sectionKey(s) !== sectionKey(sec))
    );
  };

  const removeFromDropCart = (enr) => {
    setDropCart((prev) =>
      prev.filter((e) => sectionKey(e) !== sectionKey(enr))
    );
  };

  const handleGoToReview = () => {
    if (addCart.length === 0 && dropCart.length === 0) {
      toast.info("Your cart is empty.");
      return;
    }
    setStep("review");
  };

  const handleBackToCart = () => {
    setStep("cart");
  };

  const handleConfirmChanges = async () => {
    if (!studentId) {
      toast.error("Missing student ID");
      return;
    }
    if (addCart.length === 0 && dropCart.length === 0) {
      toast.info("Nothing to submit.");
      return;
    }

    setProcessing(true);
    let addedCount = 0;
    let droppedCount = 0;
    let errors = 0;

    // process additions
    for (const sec of addCart) {
      const id = sectionKey(sec);
      if (!id) continue;

      try {
        await enrollStudent(studentId, id);
        addedCount++;
      } catch (err) {
        console.error("Error enrolling section", id, err);
        errors++;
      }
    }

    // process drops
    for (const enr of dropCart) {
      const id = sectionKey(enr);
      if (!id) continue;

      try {
        await dropStudent(studentId, id);
        droppedCount++;
      } catch (err) {
        console.error("Error dropping section", id, err);
        errors++;
      }
    }

    if (addedCount || droppedCount) {
      toast.success(
        `Enrollment updated. Added ${addedCount}, dropped ${droppedCount}.`
      );
    }
    if (errors) {
      toast.error(`There were ${errors} error(s) while processing.`);
    }

    setAddCart([]);
    setDropCart([]);
    await loadEnrollments();
    setStep("cart");
    setProcessing(false);
  };

  return (
    <div className="enrollment-page">
      <ToastContainer position="bottom-right" autoClose={2500} />

      <h1 className="enrollment-title">Enrollment</h1>

      {/* Student ID + step indicator */}
      <div className="enrollment-card">
      <div className="enrollment-row term-year-row">
    <div style={{ flex: 1 }}>
      <label className="enrollment-label">Student</label>
      <div
        className="enrollment-input"
        style={{
          backgroundColor: "#f5f5f5",
          color: "#555",
          cursor: "default",
          borderColor: "#ddd",
        }}
      >
        {studentDisplayName}
      </div>
    </div>

          <div style={{ minWidth: 220 }}>
            <div className="enrollment-label">Steps</div>
            <div style={{ display: "flex", gap: 8, marginTop: 4 }}>
              <div
                style={{
                  flex: 1,
                  padding: "6px 10px",
                  borderRadius: 999,
                  fontSize: 12,
                  textAlign: "center",
                  background:
                    step === "cart" ? "#ffa726" : "rgba(0,0,0,0.05)",
                  color: step === "cart" ? "#fff" : "#555",
                  fontWeight: 600,
                }}
              >
                1. Build Cart
              </div>
              <div
                style={{
                  flex: 1,
                  padding: "6px 10px",
                  borderRadius: 999,
                  fontSize: 12,
                  textAlign: "center",
                  background:
                    step === "review" ? "#ffa726" : "rgba(0,0,0,0.05)",
                  color: step === "review" ? "#fff" : "#555",
                  fontWeight: 600,
                }}
              >
                2. Review &amp; Enroll
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Main two-column layout */}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "minmax(0, 1.6fr) minmax(0, 1fr)",
          gap: 24,
          alignItems: "flex-start",
        }}
      >
        {/* LEFT: Available sections + current enrollments */}
        <div>
          {/* Available sections */}
          <div className="enrollment-card">
            <h3 style={{ marginTop: 0, marginBottom: 12 }}>
              Available Sections
            </h3>
            {loadingSections ? (
              <div className="center-cell">
                <span className="spinner" /> Loading sections…
              </div>
            ) : sections.length === 0 ? (
              <div className="center-cell">No sections found.</div>
            ) : (
              <div style={{ overflowX: "auto" }}>
                <table className="enrollment-table">
                  <thead>
                    <tr>
                      <th>Section</th>
                      <th>Course</th>
                      <th>Dept</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {sections.map((sec) => {
                      const id = sectionKey(sec);
                      const courseName =
                        sec.courseName ??
                        sec.course_name ??
                        "Untitled Course";
                      const dept =
                        sec.department_abbreviation ??
                        sec.departmentCode ??
                        sec.department ??
                        "";
                      const inCart = isInAddCart(sec);
                      const enrolled = isAlreadyEnrolled(sec);

                      return (
                        <tr key={id}>
                          <td>{id}</td>
                          <td>{courseName}</td>
                          <td>{dept}</td>
                          <td>
                            <button
                              className="enrollment-button secondary"
                              style={{ padding: "6px 12px", fontSize: 13 }}
                              onClick={() => handleAddToCart(sec)}
                              disabled={inCart || enrolled}
                            >
                              {enrolled
                                ? "Enrolled"
                                : inCart
                                ? "In Cart"
                                : "Add to Cart"}
                            </button>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            )}
          </div>

          {/* Current enrollments */}
          <div className="enrollment-table-wrapper">
            <h3 style={{ marginTop: 0, marginBottom: 12 }}>
              Current Enrollments
            </h3>
            <table className="enrollment-table">
              <thead>
                <tr>
                  <th>Section</th>
                  <th>Course Name</th>
                  <th>Department</th>
                  <th>Enrolled On</th>
                  <th>Grade</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {loadingTable ? (
                  <tr>
                    <td colSpan="6" className="center-cell">
                      <span className="spinner" /> Loading…
                    </td>
                  </tr>
                ) : enrollments.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="center-cell">
                      No enrollments
                    </td>
                  </tr>
                ) : (
                  enrollments.map((enr) => {
                    const id = sectionKey(enr);
                    const courseName =
                      enr.className ??
                      enr.courseName ??
                      enr.course_name ??
                      "—";
                    const dept =
                      enr.department_abbreviation ??
                      enr.departmentName ??
                      enr.department ??
                      "—";
                    const enrolledOn =
                      enr.enrolledOn ??
                      enr.enrollmentDate ??
                      enr.enrolled_on ??
                      "—";
                    const grade = enr.grade ?? "—";
                    const dropping = isInDropCart(enr);

                    return (
                      <tr key={id}>
                        <td>{id}</td>
                        <td>{courseName}</td>
                        <td>{dept}</td>
                        <td>{enrolledOn}</td>
                        <td>{grade}</td>
                        <td>
                          <button
                            className="enrollment-button secondary"
                            style={{ padding: "6px 12px", fontSize: 13 }}
                            onClick={() =>
                              dropping
                                ? removeFromDropCart(enr)
                                : handleMarkToDrop(enr)
                            }
                          >
                            {dropping ? "Undo Drop" : "Mark to Drop"}
                          </button>
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* RIGHT: Cart + review/confirm */}
        <div>
          {/* Cart summary */}
          <div className="enrollment-card">
            <h3 style={{ marginTop: 0 }}>Enrollment Cart</h3>
            <p
              style={{
                fontSize: 13,
                color: "#666",
                marginTop: 0,
                marginBottom: 12,
              }}
            >
              Build your changes here first. Nothing is sent to the server until
              you confirm.
            </p>

            <div style={{ marginBottom: 16 }}>
              <h4 style={{ margin: "8px 0" }}>To Add</h4>
              {addCart.length === 0 ? (
                <div style={{ fontSize: 13, color: "#777" }}>
                  No sections selected to add.
                </div>
              ) : (
                <ul style={{ paddingLeft: 18, fontSize: 13 }}>
                  {addCart.map((sec) => {
                    const id = sectionKey(sec);
                    const courseName =
                      sec.courseName ??
                      sec.course_name ??
                      "Untitled Course";
                    const dept =
                      sec.department_abbreviation ??
                      sec.departmentCode ??
                      sec.department ??
                      "";
                    return (
                      <li key={id} style={{ marginBottom: 4 }}>
                        <b>{id}</b> – {courseName}{" "}
                        {dept ? `(${dept})` : ""}
                        <button
                          style={{
                            marginLeft: 8,
                            border: "none",
                            background: "transparent",
                            color: "#b71c1c",
                            cursor: "pointer",
                            fontSize: 12,
                          }}
                          onClick={() => removeFromAddCart(sec)}
                        >
                          remove
                        </button>
                      </li>
                    );
                  })}
                </ul>
              )}
            </div>

            <div style={{ marginBottom: 16 }}>
              <h4 style={{ margin: "8px 0" }}>To Drop</h4>
              {dropCart.length === 0 ? (
                <div style={{ fontSize: 13, color: "#777" }}>
                  No sections selected to drop.
                </div>
              ) : (
                <ul style={{ paddingLeft: 18, fontSize: 13 }}>
                  {dropCart.map((enr) => {
                    const id = sectionKey(enr);
                    const courseName =
                      enr.className ??
                      enr.courseName ??
                      enr.course_name ??
                      "—";
                    const dept =
                      enr.department_abbreviation ??
                      enr.departmentName ??
                      enr.department ??
                      "—";
                    return (
                      <li key={id} style={{ marginBottom: 4 }}>
                        <b>{id}</b> – {courseName} ({dept})
                        <button
                          style={{
                            marginLeft: 8,
                            border: "none",
                            background: "transparent",
                            color: "#b71c1c",
                            cursor: "pointer",
                            fontSize: 12,
                          }}
                          onClick={() => removeFromDropCart(enr)}
                        >
                          remove
                        </button>
                      </li>
                    );
                  })}
                </ul>
              )}
            </div>

            {step === "cart" ? (
              <button
                className="enrollment-button enroll"
                onClick={handleGoToReview}
                disabled={addCart.length === 0 && dropCart.length === 0}
              >
                Review &amp; Enroll All
              </button>
            ) : (
              <div className="buttons-row">
                <button
                  className="enrollment-button secondary"
                  onClick={handleBackToCart}
                  disabled={processing}
                >
                  Back to Cart
                </button>
                <button
                  className="enrollment-button enroll"
                  onClick={handleConfirmChanges}
                  disabled={processing}
                >
                  {processing ? (
                    <span className="spinner" />
                  ) : (
                    "Confirm Changes"
                  )}
                </button>
              </div>
            )}
          </div>

          {/* Review state extra text */}
          {step === "review" && (
            <div className="enrollment-card">
              <h3 style={{ marginTop: 0 }}>Review Summary</h3>
              <p style={{ fontSize: 13, color: "#555" }}>
                When you click <b>Confirm Changes</b>, the system will:
              </p>
              <ul style={{ fontSize: 13, color: "#555" }}>
                <li>
                  Call <code>/api/students/enroll</code> for each section in{" "}
                  <b>To Add</b>.
                </li>
                <li>
                  Call <code>/api/students/drop</code> for each section in{" "}
                  <b>To Drop</b>.
                </li>
                <li>Reload your current enrollments afterwards.</li>
              </ul>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
