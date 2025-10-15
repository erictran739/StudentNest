import React from "react";
import { Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Home from "./components/Home";
import Register from "./components/Register";
import AccountSuccess from "./components/AccountSuccess";
import AccountFailure from "./components/AccountFailure";

function App() {
  return (
    <div className="ring" style={{ "--clr": "#ff6ec7" }}>
      <i></i>
      <i></i>
      <i></i>

      <div className="login">
        <Routes>
          {/* Login */}
          <Route path="/" element={<Login />} />

          {/* Home (post-login) */}
          <Route path="/home" element={<Home />} />

          {/* Register */}
          <Route path="/register" element={<Register />} />

          {/* Registration outcomes */}
          <Route path="/account-success" element={<AccountSuccess />} />
          <Route path="/account-failure" element={<AccountFailure />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
