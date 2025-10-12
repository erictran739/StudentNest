import React from "react";
import Login from "./components/Login";

function App() {
  return (
    <div className="ring" style={{ "--clr": "#ff6ec7" }}>
      <i></i>
      <i></i>
      <i></i>
      <div className="login">
        <Login />
      </div>
    </div>
  );
}

export default App;
