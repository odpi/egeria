/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import { ContentSwitcher, Switch } from "carbon-components-react";
import StartingGlossaryNavigation from "./StartingGlossaryNavigation";
import StartingTermNavigation from "./StartingTermNavigation";
import StartingCategoryNavigation from "./StartingCategoryNavigation";
import { useHistory, withRouter } from "react-router-dom";

function GlossaryAuthorNavigation(props) {
  console.log("GlossaryAuthorNavigation");
  const [currentNodeTypeToShow, setCurrentNodeTypeToShow] = useState(
    "glossaries"
  );

  let history = useHistory();

  const onChange = (e) => {
    const chosenContent = `${e.name}`;

   // props.match.url could be anything 

    const arrayOfURLSegments = props.match.url.split("/");
    let workingUrl = ""
    let keepAppending = true;
    for (let i=0;i<arrayOfURLSegments.length;i++) {
      const currentSegment =  arrayOfURLSegments[i];
      if (keepAppending && currentSegment.length > 0) {
        workingUrl = workingUrl +"/" + currentSegment;
      } 
      if (currentSegment == "glossary-author" ) {
        keepAppending=false;
       } 
    }
   
    const url = workingUrl + "/" + chosenContent;
    console.log("pushing url " + url);

    history.push(url);
    setCurrentNodeTypeToShow(chosenContent);
  };

  return (
    <div>
      <ContentSwitcher onChange={onChange}>
        <Switch name="glossaries" text="Glossaries" />
        <Switch name="categories" text="Categories" />
        <Switch name="terms" text="Terms" />
      </ContentSwitcher>
      {currentNodeTypeToShow == "glossaries" && (
        <StartingGlossaryNavigation match={props.match} />
      )}
      {currentNodeTypeToShow == "terms" && (
        <StartingTermNavigation match={props.match} />
      )}
      {currentNodeTypeToShow == "categories" && (
        <StartingCategoryNavigation match={props.match} />
      )}
    </div>
  );
}
export default withRouter(GlossaryAuthorNavigation);
