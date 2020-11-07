/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect } from "react";
import { ContentSwitcher, Switch } from "carbon-components-react";
import StartingGlossaryNavigation from "./StartingGlossaryNavigation";
import StartingTermNavigation from "./StartingTermNavigation";
import StartingCategoryNavigation from "./StartingCategoryNavigation";
import { useHistory, withRouter } from "react-router-dom";

function GlossaryAuthorNavigation(props) {
  console.log("GlossaryAuthorNavigation");
  const [selectedContentIndex, setSelectedContentIndex] = useState(0);
  /**
   * this useEffect is required so that the content in the content switcher is kept in step with the url.
   * This is required when the back button is pressed returning from a child component.
   */
  useEffect(() => {
    const arrayOfURLSegments = location.pathname.split("/");
    const lastSegment = arrayOfURLSegments[arrayOfURLSegments.length - 1];
    let index = 0;
    if (lastSegment == "categories") {
      index = 1;
    }
    if (lastSegment == "terms") {
      index = 2;
    }
    console.log(
      "glossaryAuthorNavigation useEffect url=" +
      location.pathname +
        " ,lastSegment=" +
        lastSegment +
        " ,index=" +
        index
    );
    setSelectedContentIndex(index);
  }, []);

  let history = useHistory();

  const onChange = (e) => {
    const chosenContent = `${e.name}`;

    // props.match.url could be anything

    const arrayOfURLSegments = props.match.url.split("/");
    let workingUrl = "";
    let keepAppending = true;
    for (let i = 0; i < arrayOfURLSegments.length; i++) {
      const currentSegment = arrayOfURLSegments[i];
      if (keepAppending && currentSegment.length > 0) {
        workingUrl = workingUrl + "/" + currentSegment;
      }
      if (currentSegment == "glossary-author") {
        keepAppending = false;
      }
    }

    const url = workingUrl + "/" + chosenContent;
    console.log("pushing url " + url);

    history.push(url);

    let index = 0;
    if (chosenContent == "categories") {
      index = 1;
    } else if (chosenContent == "terms") {
      index = 2;
    }
    setSelectedContentIndex(index);
  };

  return (
    <div>
      <ContentSwitcher selectedIndex={selectedContentIndex} onChange={onChange}>
        <Switch name="glossaries" text="Glossaries" />
        <Switch name="categories" text="Categories" />
        <Switch name="terms" text="Terms" />
      </ContentSwitcher>
      {selectedContentIndex == 0 && (
        <StartingGlossaryNavigation match={props.match} />
      )}
      {selectedContentIndex == 1 && (
        <StartingCategoryNavigation match={props.match} />
      )}
      {selectedContentIndex == 2 && (
        <StartingTermNavigation match={props.match} />
      )}
    </div>
  );
}
export default withRouter(GlossaryAuthorNavigation);
