import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import Login from "./components/Login";
import Home from "./components/Home";
import Register from "./components/Register";
import AccountSuccess from "./components/AccountSuccess";
import AccountFailure from "./components/AccountFailure";
import Downtime from "./components/Downtime";

function CenteredLayout({ children }) {
  return <div className="auth-page">{children}</div>;
}

export default function App() {
  return (
    <Routes>
      {/* App pages */}
      <Route path="/home" element={<Home />} />
      <Route path="/downtime" element={<Downtime />} />

      {/* Auth pages (centered on bg image) */}
      <Route path="/" element={<CenteredLayout><Login /></CenteredLayout>} />
      <Route path="/login" element={<CenteredLayout><Login /></CenteredLayout>} />
      <Route path="/register" element={<CenteredLayout><Register /></CenteredLayout>} />
      <Route path="/account-success" element={<CenteredLayout><AccountSuccess /></CenteredLayout>} />
      <Route path="/account-failure" element={<CenteredLayout><AccountFailure /></CenteredLayout>} />

      {/* Fallback */}
      <Route path="*" element={<Navigate to="/home" replace />} />
    </Routes>
  );
}
