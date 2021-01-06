/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";
import { useHistory } from "react-router-dom";
import "./Login.css";
import Egeriacolor from "../images/odpi/Egeria_logo_color";
import { IdentificationContext } from "../contexts/IdentificationContext";

import {
  Grid,
  Row,
  Column,
  Form,
  FormGroup,
  TextInput,
  Button
} from 'carbon-components-react';

const Login = () => {
  const identificationContext = useContext(IdentificationContext);
  const [password, setPassword] = useState("");
  const [userId, setUserId] = useState("");
  const [errorMsg, setErrorMsg] = useState();
  let history = useHistory();

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
        if (res.status === "success") {
          console.log("login worked " + JSON.stringify(res));
          identificationContext.setUserId(userId);
          identificationContext.setUser(res.user);
          identificationContext.setAuthenticated(true);
          // TODO original url prop. 
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
      <Grid>
        <Row>
          <Column
            sm={{ span: 4 }}
            md={{ span: 4, offset: 2 }}
            lg={{ span: 4, offset: 6 }}
          >
            <Form id="egeria-login-form">
              <FormGroup legendText="">
                <TextInput
                  type="text"
                  labelText="Username"
                  id="login-user"
                  name="username"
                  value={userId}
                  onChange={handleOnChange}
                  placeholder="Username"
                  required
                />
                <TextInput
                  type="password"
                  labelText="Password"
                  id="login-password"
                  name="password"
                  value={password}
                  onChange={e => setPassword(e.target.value)}
                  placeholder="Password"
                  required
                  />
              </FormGroup>
              <Button
                type="submit"
                onClick={handleOnClick}
                disabled={!validateForm()}
              >
                Log In
              </Button>
            </Form>
          </Column>
        </Row>
      </Grid>
      {/* <div style={errorStyle}>{message}</div> */}
      <div> {errorMsg} </div>
    </div>
  );
};

export default Login;

