/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";
import { useHistory } from "react-router-dom";
import "./Login.css";
import Egeriacolor from "../images/Egeria_logo_color";
import { IdentificationContext } from "../contexts/IdentificationContext";

const Login = () => {
  const identificationContext = useContext(IdentificationContext);
  const [password, setPassword] = useState("");
  const [userId, setUserId] = useState("");
  const [errorMsg, setErrorMsg] = useState();
  let history = useHistory();

  // }
  const handleOnClick = e => {
    console.log("login handleClick(()");
    e.preventDefault();
    const url = identificationContext.getBrowserURL('login') + "?" + new URLSearchParams({
      username: userId,
      password: password
  });
    fetch(url, {
      method: "post",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
      }
    })
      .then(res => res.json())
      .then(res => {
        if (res.status == "success") {
          console.log("login worked " + JSON.stringify(res));
          identificationContext.setUserId(userId);
          const path = identificationContext.getBrowserURL(''); 
          history.push(path);
        } else {
          if (res.errno) {
            setErrorMsg("Login Failed with errno" + res.errno);
          } else {
            setErrorMsg("Login Failed");
          }
          setErrorMsg("Login Failed with " + res);
        }
      })
      .catch(res => {
        setErrorMsg("Create Failed - logic error");
      });
  };

  const handleOnChange = event => {
    const value = event.target.value;
    sessionStorage.setItem("egeria-userId", value);
    setUserId(value);
    console.log("handleOnChange :" + value);
  };

  const validateForm = () => {
    return userId && password && userId.length > 0 && password.length > 0;
  };

  return (
    <div>
      <Egeriacolor />
      <form id="egeria-login-form">
        <div>
          <label>Username:</label>
          <input
            type="text"
            name="username"
            value={userId}
            onChange={handleOnChange}
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
          />
        </div>
        <div>
          <input onClick={handleOnClick}  disabled={!validateForm()} value="Log In" />
        </div>
      </form>
      {/* <div style={errorStyle}>{message}</div> */}
      <div> {errorMsg} </div>
    </div>
  );
};

export default Login;

