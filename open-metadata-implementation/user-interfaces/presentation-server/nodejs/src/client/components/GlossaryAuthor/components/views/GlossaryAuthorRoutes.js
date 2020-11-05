/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
// import { IdentificationContext } from "../../../../contexts/IdentificationContext";
import { Route, Switch } from "react-router-dom";
import GlossaryAuthorCRUD from "../GlossaryAuthorCRUD";
import GlossaryAuthorNavigation from "../GlossaryAuthorNavigation";
import QuickTerms from "../QuickTerms";
import GlossaryChildren from "../GlossaryChildren";
import CreateGlossary from "../CreateGlossary";
import UpdateGlossary from "../UpdateGlossary";
import CreateTerm from "../CreateTerm";
import UpdateTerm from "../UpdateTerm";
import CreateCategory from "../CreateCategory";
import UpdateCategory from "../UpdateCategory";
import GlossaryAuthorCategoriesNavigation from "../GlossaryAuthorCategoriesNavigation";
import GlossaryAuthorTermsNavigation from "../GlossaryAuthorTermsNavigation";

export default function GlossaryAuthorRoutes({ glossaryAuthorURL }) {
  console.log("glossaryAuthorURL=" + glossaryAuthorURL);

  function getGlossariesPath() {
    let path;
    const currentLocationArray = glossaryAuthorURL.split("/");
    const lastSegment = currentLocationArray[currentLocationArray.length - 1];
    if (lastSegment == "glossaries") {
      // if we are navigated to via the task drop down we get a url ending with glossaries
      path = glossaryAuthorURL;
    } else {
      // if we are navigated to as the default component loaded under glossary-author then we need to append the glossaries
      path = glossaryAuthorURL + "/glossaries";
    }
    console.log("getGlossariesPath " + path);
    return path;
  }
  function getGlossariesAddPath() {
    const path = getGlossariesPath() + "/add-node";
    console.log("getGlossariesAddPath " + path);
    return path;
  }
  function getQuickTermsPath() {
    const path = getGlossariesPath() + "/:glossaryguid/quick-terms";
    console.log("getQuickTerms " + path);
    return path;
  }
  function getGlossaryChildrenPath() {
    const path = getGlossariesPath() + "/:glossaryguid/children";
    console.log("getGlossaryChildren " + path);
    return path;
  }
  function getGlossaryChildrenAddTermPath() {
    const path = getGlossaryTermsChildrenPath() + "/add-term";
    console.log("getGlossaryChildrenAddTermPath() " + path);
    return path;
  }
  function getGlossaryChildrenAddCategoryPath() {
    const path = getGlossaryCategoriesChildrenPath() + "/add-category";
    console.log("getGlossaryChildrenAddCategoryPath() " + path);
    return path;
  }
  function getGlossaryChildrenEditTermPath() {
    const path = getGlossaryTermsChildrenPath() + "/edit-term/:guidtoedit";
    console.log("getGlossaryChildrenEditTermPath() " + path);
    return path;
  }
  function getGlossaryChildrenEditCategoryPath() {
    const path =
      getGlossaryCategoriesChildrenPath() + "/edit-category/:guidtoedit";
    console.log("getGlossaryChildrenEditCategoryPath() " + path);
    return path;
  }

  function getGlossaryTermsChildrenPath() {
    const path = getGlossaryChildrenPath() + "/terms";
    console.log("getGlossaryTermsChildrenPath " + path);
    return path;
  }
  function getGlossaryCategoriesChildrenPath() {
    const path = getGlossaryChildrenPath() + "/categories";
    console.log("getGlossaryCategoriesChildrenPath " + path);
    return path;
  }
  function getGlossariesEditPath() {
    return getGlossariesPath() + "/edit-glossary/:guidtoedit";
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
      <Route path={getGlossariesAddPath()} component={CreateGlossary}></Route>
      <Route path={getGlossariesEditPath()} component={UpdateGlossary}></Route>
      <Route
        exact
        path={getGlossariesPath()}
        component={GlossaryAuthorNavigation}
      ></Route>
      <Route
        path={getGlossaryChildrenAddTermPath()}
        exact
        component={CreateTerm}
      ></Route>
      <Route
        path={getGlossaryChildrenAddCategoryPath()}
        exact
        component={CreateCategory}
      ></Route>
      <Route
        path={getGlossaryChildrenEditTermPath()}
        exact
        component={UpdateTerm}
      ></Route>
      <Route
        path={getGlossaryChildrenEditCategoryPath()}
        exact
        component={UpdateCategory}
      ></Route>

      <Route path={getQuickTermsPath()} component={QuickTerms}></Route>
      <Route
        path={getGlossaryChildrenPath()}
        component={GlossaryChildren}
      ></Route>
      <Route
        path={getGlossaryTermsChildrenPath()}
        component={GlossaryAuthorTermsNavigation}
      ></Route>
      <Route
        path={getGlossaryCategoriesChildrenPath()}
        component={GlossaryAuthorCategoriesNavigation}
      ></Route>

      <Route
        path={getGlossariesPath()}
        component={GlossaryAuthorNavigation}
      ></Route>
      <Route path={getCrudPath()} component={GlossaryAuthorCRUD}></Route>
      <Route
        path={glossaryAuthorURL}
        exact
        component={GlossaryAuthorNavigation}
      ></Route>
      <Route path="/" render={() => <h1>Route not recognised</h1>}></Route>
      {/* <Route render={() => <h1>Route not recognised!!</h1>}></Route> */}
    </Switch>
  );
}
