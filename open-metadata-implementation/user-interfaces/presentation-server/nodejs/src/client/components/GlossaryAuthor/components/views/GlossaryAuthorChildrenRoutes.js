/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import { Route, Switch } from "react-router-dom";
import GlossaryAuthorCategoriesNavigation from "../GlossaryAuthorCategoriesNavigation";
import GlossaryAuthorTermsNavigation from "../GlossaryAuthorTermsNavigation";

export default function GlossaryAuthorChildrenRoutes(props) {

  console.log("GlossaryAuthorChildrenRoutes " + location.pathname);

  function getTermsPath() {
    const path = location.pathname + "/terms";
    console.log("getTermsPath " + path);
    return path;
  }
  function getCategoriesPath() {
    const path = location.pathname + "/categories";
    console.log("getCategoriesPath " + path);
    return path;
  }
  
  return (
    <Switch>
      <Route
        path={getTermsPath()}
        component={GlossaryAuthorTermsNavigation}
      ></Route>
      <Route
        path={getCategoriesPath()}
        component={GlossaryAuthorCategoriesNavigation}
      ></Route>
      
    </Switch>
  );
}
