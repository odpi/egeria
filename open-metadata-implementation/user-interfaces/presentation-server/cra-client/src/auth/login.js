/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";
import { useHistory } from "react-router-dom";
import "./Login.css";
import Egeriacolor from "../images/Egeria_logo_color";
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

const Login = (props) => {
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
        if (res.status == "success") {
          console.log("login worked " + JSON.stringify(res));
          identificationContext.setUserId(userId);
          identificationContext.setUser(res.user);
          identificationContext.setAuthenticated(true);
          // redirect user to page they were previously on if they refresh or enter an exact URL
          const path = props.currentURL ? identificationContext.getBrowserURL(props.currentURL) : identificationContext.getBrowserURL(''); 
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
                  id="login-username"
                  type="text"
                  labelText="Username"
                  name="username"
                  value={userId}
                  onChange={handleOnChange}
                  placeholder="Username"
                  required
                />
                <TextInput
                  id="login-password"
                  type="password"
                  labelText="Password"
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

