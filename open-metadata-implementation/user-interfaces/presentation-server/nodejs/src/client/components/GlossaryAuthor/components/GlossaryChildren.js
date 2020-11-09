/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect } from "react";
import { ContentSwitcher, Switch } from "carbon-components-react";
import GlossaryAuthorTermsNavigation from "./GlossaryAuthorTermsNavigation";
import GlossaryAuthorCategoriesNavigation from "./GlossaryAuthorCategoriesNavigation";
import getNodeType from "./properties/NodeTypes";
import { useHistory, withRouter } from "react-router-dom";

function GlossaryChildren(props) {
  console.log("NodeChildren(props) " + props);
  const [selectedContentIndex, setSelectedContentIndex] = useState(0);
 /**
  * this useEffect is required so that the content in the content switcher is kept in step with the url.
  * This is required when the back button is pressed returning from a child component.  
  */
  useEffect(() => {
    const arrayOfURLSegments = location.pathname.split("/");
    const lastSegment = arrayOfURLSegments[arrayOfURLSegments.length - 1];
    let index = 0;
    if (lastSegment == "terms") {
      index = 1;
    }
    console.log(
      "GlossaryChildren useEffect url=" +
      location.pathname +
        " ,lastSegment=" +
        lastSegment +
        " ,index=" +
        index
    );
    setSelectedContentIndex(index);
  }, []);
  const guid = props.match.params.glossaryguid;
  let history = useHistory();

  const onChange = (e) => {
    const chosenContent = `${e.name}`;
    const url = props.match.url + "/" + chosenContent;
    console.log("pushing url " + url);

    history.push(url);
    if (chosenContent == "terms") {
      setSelectedContentIndex(1);
    } else {
      setSelectedContentIndex(0);
    }
  };
  const getChildrenURL = () => {
    let childName;
    if (selectedContentIndex == 1) {
      childName = "terms";
    } else {
      childName = "categories";
    }
    console.log("getChildrenURL guid " + guid);
    const url = getNodeType("glossary").url + "/" + guid + "/" + childName;
    console.log("getChildrenURL url " + url);
    return url;
  };

  return (
    <div>
      <ContentSwitcher selectedIndex={selectedContentIndex} onChange={onChange}>
        <Switch name="categories" text="Categories" />
        <Switch name="terms" text="Terms" />
      </ContentSwitcher>

      {selectedContentIndex == 0 && (
        <GlossaryAuthorCategoriesNavigation
          getCategoriesURL={getChildrenURL()}
        />
      )}
      {selectedContentIndex == 1 && (
        <GlossaryAuthorTermsNavigation getTermsURL={getChildrenURL()} />
      )}
    </div>
  );
}
export default withRouter(GlossaryChildren);