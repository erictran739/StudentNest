import React from "react";
import { Routes, Route, useLocation } from "react-router-dom";
import Login from "./components/Login";
import Home from "./components/Home";
import Register from "./components/Register";
import AccountSuccess from "./components/AccountSuccess";
import AccountFailure from "./components/AccountFailure";
import Downtime from "./components/Downtime";

function RingLayout({ children }) {
  return (
    <div className="ring" style={{ "--clr": "#ff6ec7" }}>
      <i></i><i></i><i></i>
      <div className="login">{children}</div>
    </div>
  );
}

export default function App() {
  const { pathname } = useLocation();
  const useRing = ["/", "/register", "/account-success", "/account-failure"].includes(pathname);

  return useRing ? (
    <RingLayout>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/account-success" element={<AccountSuccess />} />
        <Route path="/account-failure" element={<AccountFailure />} />
        {/* Fallback */}
        <Route path="/home" element={<Home />} />
      </Routes>
    </RingLayout>
  ) : (
    <Routes>
      <Route path="/downtime" element={<Downtime />} />
      <Route path="/home" element={<Home />} />
      {/* Safety fallback: if someone hits other routes while not using ring */}
      <Route path="*" element={<Home />} />
    </Routes>
  );
}
