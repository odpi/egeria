/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import "./app.scss";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Login from "./auth/login";
import Frame from "./Frame";
import IdentificationContext  from "./contexts/IdentificationContext";

export default function App() {
  return (
    <div>
      <IdentificationContext>
        <Router>
          <Switch>
            <Route path="/*/login" exact>
              <Login />
            </Route>
            <Route path="/*/">
              <Frame />
            </Route>
          </Switch>
        </Router>
      </IdentificationContext>
    </div>
  );
}
