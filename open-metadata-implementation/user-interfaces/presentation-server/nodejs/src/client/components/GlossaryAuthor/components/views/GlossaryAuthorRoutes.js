/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
// import { IdentificationContext } from "../../../../contexts/IdentificationContext";
import { Route, Switch, Redirect } from "react-router-dom";
import GlossaryAuthorCRUD from "../GlossaryAuthorCRUD";
import GlossaryAuthorNavigation from "../GlossaryAuthorNavigation";
import TermAuthorNavigation from "../GlossaryAuthorNavigation";
import CategoryAuthorNavigation from "../GlossaryAuthorNavigation";
import QuickTerms from "../QuickTerms";
import GlossaryChildren from "../GlossaryChildren";
import CreateGlossary from "../CreateGlossary";
import UpdateGlossary from "../UpdateGlossary";
import CreateTerm from "../CreateTerm";
import CreateTermWizard from "../CreateTermWizard";
import UpdateTerm from "../UpdateTerm";
import CreateCategory from "../CreateCategory";
import UpdateCategory from "../UpdateCategory";
import GlossaryAuthorCategoriesNavigation from "../GlossaryAuthorCategoriesNavigation";
import GlossaryAuthorTermsNavigation from "../GlossaryAuthorTermsNavigation";

export default function GlossaryAuthorRoutes({ glossaryAuthorURL }) {
  console.log("glossaryAuthorURL=" + glossaryAuthorURL);

  // the top paths 
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
  function getTermsPath() {
    let path;
    const currentLocationArray = glossaryAuthorURL.split("/");
    const lastSegment = currentLocationArray[currentLocationArray.length - 1];
    if (lastSegment == "terms") {
      // if we are navigated to via the task drop down we get a url ending with terms
      path = glossaryAuthorURL;
    } else {
      // if we are navigated to as the default component loaded under glossary-author then we need to append the terms
      path = glossaryAuthorURL + "/terms";
    }
    console.log("getTermsPath " + path);
    return path;
  }
  function getCategoriesPath() {
    let path;
    const currentLocationArray = glossaryAuthorURL.split("/");
    const lastSegment = currentLocationArray[currentLocationArray.length - 1];
    if (lastSegment == "categories") {
      // if we are navigated to via the task drop down we get a url ending with terms
      path = glossaryAuthorURL;
    } else {
      // if we are navigated to as the default component loaded under glossary-author then we need to append the categories
      path = glossaryAuthorURL + "/categories";
    }
    console.log("getCategriesPath " + path);
    return path;
  }

  // top path + adds
  function getGlossariesAddPath() {
    const path = getGlossariesPath() + "/add-glossary";
    console.log("getGlossariesAddPath " + path);
    return path;
  }
  function getTermsAddPath() {
    const path = getTermsPath() + "/add-term";
    console.log("getTermsAddPath " + path);
    return path;
  }
  function getCategoriesAddPath() {
    const path = getCategoriesPath() + "/add-category";
    console.log("getCategoriesAddPath " + path);
    return path;
  }
  // top path + edit
  function getGlossariesEditPath() {
    return getGlossariesPath() + "/edit-glossary/:guidtoedit";
  }
  function getTermsEditPath() {
    return getTermsPath() + "/edit-term/:guidtoedit";
  }
  function getCategoriesEditPath() {
    return getCategoriesPath() + "/edit-category/:guidtoedit";
  }

  // quickterms for glossary
  function getQuickTermsPath() {
    const path = getGlossariesPath() + "/:glossaryguid/quick-terms";
    console.log("getQuickTerms " + path);
    return path;
  }
  // glossary children
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

  function getCrudPath() {
    const path = glossaryAuthorURL + "/crud";
    console.log("getCrudPath " + path);
    return path;
  }

  return (
    <Switch>
      <Route path={getGlossariesAddPath()} component={CreateGlossary}></Route>
      <Route path={getTermsAddPath()} component={CreateTermWizard}></Route>
      <Route path={getCategoriesAddPath()} component={CreateCategory}></Route>
      <Route path={getGlossariesEditPath()} component={UpdateGlossary}></Route>
      <Route path={getTermsEditPath()} component={UpdateTerm}></Route>
      <Route path={getCategoriesEditPath()} component={UpdateCategory}></Route>
      <Redirect path={glossaryAuthorURL} exact to={getGlossariesPath()} />
      <Route
        exact
        path={getGlossariesPath()}
        component={GlossaryAuthorNavigation}
      ></Route>
      <Route
        exact
        path={getTermsPath()}
        component={TermAuthorNavigation}
      ></Route>
      <Route
        exact
        path={getCategoriesPath()}
        component={CategoryAuthorNavigation}
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
