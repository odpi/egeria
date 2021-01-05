/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect, useContext } from "react";

import { IdentificationContext } from "../../../contexts/IdentificationContext";
import { ContentSwitcher, Switch } from "carbon-components-react";
import GlossaryAuthorTermsNavigation from "./navigations/GlossaryAuthorTermsNavigation";
import GlossaryAuthorCategoriesNavigation from "./navigations/GlossaryAuthorCategoriesNavigation";
import GlossaryAuthorChildCategoriesNavigation from "./navigations/GlossaryAuthorChildCategoriesNavigation";
import getNodeType from "./properties/NodeTypes";
import { useHistory, withRouter } from "react-router-dom";

function NodeChildren(props) {
  const identificationContext = useContext(IdentificationContext);
  console.log("NodeChildren(props) " + props);
  const [selectedContentIndex, setSelectedContentIndex] = useState(0);
  /**
   * this useEffect is required so that the content in the content switcher is kept in step with the url.
   * This is required when the back button is pressed returning from a child component.
   */
  useEffect(() => {
    const arrayOfURLSegments = window.location.pathname.split("/");
    const lastSegment = arrayOfURLSegments[arrayOfURLSegments.length - 1];
    let index = 0;
    if (lastSegment === "terms") {
      index = 1;
    }
    console.log(
      "NodeChildren useEffect url=" +
        window.location.pathname +
        " ,lastSegment=" +
        lastSegment +
        " ,index=" +
        index
    );
    setSelectedContentIndex(index);
  }, []);
  const guid = props.parentguid;
  let history = useHistory();

  const onChange = (e) => {
    const chosenContent = `${e.name}`;
    const url = props.match.url + "/" + chosenContent;
    console.log("pushing url " + url);

    // Use replace rather than push so the content switcher changes are not navigated through the back button, which would be uninituitive.
    history.replace(url);

    if (chosenContent === "terms") {
      setSelectedContentIndex(1);
    } else {
      setSelectedContentIndex(0);
    }
  };
  const getChildrenURL = () => {
    let childName;
    if (selectedContentIndex === 1) {
      childName = "terms";
    } else if (props.parentNodeTypeName === "glossary") {
      childName = "categories";
    } else if (props.parentNodeTypeName === "category") {
      childName = "child-categories";
    }
    console.log("getChildrenURL guid " + guid);
    const url =
      getNodeType(identificationContext.getRestURL("glossary-author"), props.parentNodeTypeName).url + "/" + guid + "/" + childName;
    console.log("getChildrenURL url " + url);
    return url;
  };

  return (
    <div>
      <ContentSwitcher selectedIndex={selectedContentIndex} onChange={onChange}>
        <Switch name="categories" text="Categories" />
        <Switch name="terms" text="Terms" />
      </ContentSwitcher>

      {selectedContentIndex === 0 &&  (props.parentNodeTypeName === "glossary") && (
        <GlossaryAuthorCategoriesNavigation
          getCategoriesURL={getChildrenURL()}
        />
      )}
          {selectedContentIndex === 0 &&  (props.parentNodeTypeName === "category") && (
        <GlossaryAuthorChildCategoriesNavigation
          getCategoriesURL={getChildrenURL()}
        />
      )}
      {selectedContentIndex === 1 && (
        <GlossaryAuthorTermsNavigation getTermsURL={getChildrenURL()} />
      )}
    </div>
  );
}
export default withRouter(NodeChildren);