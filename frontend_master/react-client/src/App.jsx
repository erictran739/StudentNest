import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import Login from "./components/Login";
import Home from "./components/Home";
import Register from "./components/Register";
import AccountSuccess from "./components/AccountSuccess";
import AccountFailure from "./components/AccountFailure";
import Downtime from "./components/Downtime";
import Contact from "./components/Contact";
import FAQ from "./components/FAQ";
import NotFound from "./components/NotFound";
import ForgotPassword from "./components/ForgotPassword";
import Courses from "./components/Courses";
import CourseDetails from "./components/CourseDetails";
import Services from "./components/Services";
import Colleges from "./components/Colleges";
import CollegeDetails from "./components/CollegeDetails";
import Enrollment from "./components/Enrollment.jsx";
import Sections from "./components/Sections";
import AppShell from "./components/AppShell"; // ⬅️ new

function CenteredLayout({ children }) {
  return <div className="auth-page">{children}</div>;
}

export default function App() {
  return (
    <Routes>
      {/* Auth pages (centered on bg image) */}
      <Route
        path="/"
        element={
          <CenteredLayout>
            <Login />
          </CenteredLayout>
        }
      />
      <Route
        path="/login"
        element={
          <CenteredLayout>
            <Login />
          </CenteredLayout>
        }
      />
      <Route
        path="/forgot-password"
        element={
          <CenteredLayout>
            <ForgotPassword />
          </CenteredLayout>
        }
      />
      <Route
        path="/register"
        element={
          <CenteredLayout>
            <Register />
          </CenteredLayout>
        }
      />
      <Route
        path="/account-success"
        element={
          <CenteredLayout>
            <AccountSuccess />
          </CenteredLayout>
        }
      />
      <Route
        path="/account-failure"
        element={
          <CenteredLayout>
            <AccountFailure />
          </CenteredLayout>
        }
      />

      {/* Main app (all pages that should share the left bar / header) */}
      <Route element={<AppShell />}>
        <Route path="/home" element={<Home />} />
        <Route path="/courses" element={<Courses />} />
        <Route path="/courses/:courseId" element={<CourseDetails />} />
        <Route path="/downtime" element={<Downtime />} />
        <Route path="/services" element={<Services />} />
        <Route path="/contact" element={<Contact />} />
        <Route path="/faq" element={<FAQ />} />
        <Route path="/colleges" element={<Colleges />} />
        <Route path="/colleges/:collegeId" element={<CollegeDetails />} />
        <Route path="/enrollment" element={<Enrollment />} />
        <Route path="/sections" element={<Sections />} />
      </Route>

      {/* Fallback */}
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}