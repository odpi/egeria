/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";
// import { IdentificationContext } from "../../../../contexts/IdentificationContext";
import { Route, Switch } from "react-router-dom";
import GlossaryAuthorCRUD from "../GlossaryAuthorCRUD";
import GlossaryAuthorNavigation from "../GlossaryAuthorNavigation";
import GlossaryAuthorSearch from "../GlossaryAuthorSearch";
import CreateGlossaryView from "./CreateGlossaryView";
import GlossaryEdit from "../GlossaryEdit";


export default function GlossaryAuthorRoutes({ glossaryAuthorURL }) {
  // const identificationContext = useContext(IdentificationContext);
  // const glossaryAuthorURL = identificationContext.getBrowserURL("glossary_author/");
  console.log("glossaryAuthorURL=" + glossaryAuthorURL);

  function getGlossariesPath() {
    const path = glossaryAuthorURL + "/glossaries";
    console.log("getGlossariesPath " + path);
    return path;
  }
  function getGlossariesAddPath() {
    const path = glossaryAuthorURL + "/glossaries/add-glossary";
    console.log("getGlossariesAddPath " + path);
    return path;
  }
  function getGlossariesEditPath() {
    return glossaryAuthorURL + "/glossaries/edit-glossary";
  }
  function getCrudPath() {
    const path = glossaryAuthorURL + "/crud";
    console.log("getCrudPath " + path);
    return path;
  }
  function getSearchPath() {
    const path = glossaryAuthorURL + "/search";
    console.log("getSearchPath " + path);
    return path;
  }
  return (
    <Switch>
      <Route
        path={getGlossariesAddPath()}
        //component={CreateGlossaryView}
        component={CreateGlossaryView}
      ></Route>
      <Route path={getGlossariesEditPath()} component={GlossaryEdit}></Route>
      <Route
        exact
        path={getGlossariesPath()}
        component={GlossaryAuthorNavigation}
      ></Route>
      <Route path={getSearchPath()} component={GlossaryAuthorSearch}></Route>
      <Route path={getCrudPath()} component={GlossaryAuthorCRUD}></Route>
      <Route
        exact
        path={getGlossariesPath()}
        component={GlossaryAuthorNavigation}
      ></Route>
      <Route
        path={glossaryAuthorURL}
        exact
        component={GlossaryAuthorCRUD}
      ></Route>
      <Route path="/" render={() => <h1>Route not recognised</h1>}></Route>
      {/* <Route render={() => <h1>Route not recognised!!</h1>}></Route> */}
    </Switch>
  );
}
