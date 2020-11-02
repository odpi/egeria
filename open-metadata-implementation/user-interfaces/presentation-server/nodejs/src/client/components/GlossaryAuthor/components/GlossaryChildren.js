/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import { ContentSwitcher, Switch } from "carbon-components-react";
import GlossaryAuthorTermsNavigation from "./GlossaryAuthorTermsNavigation";
import GlossaryAuthorCategoriesNavigation from "./GlossaryAuthorCategoriesNavigation";
import getNodeType from "./properties/NodeTypes";

export default function GlossaryChildren(props) {
  console.log("NodeChildren(props) " + props);
  const guid = props.match.params.glossaryguid;

  const [showTerms, setShowTerms] = useState(false);
  const onChange = (e) => {
    const chosenContent = `${e.name}`;
    const url = props.match.url + "/" + chosenContent;
    console.log("pushing url " + url);

    props.history.push(url);
    if (chosenContent == "terms") {
      setShowTerms(true);
    } else {
      setShowTerms(false);
    }
  };
  const getChildrenURL = () => {
    let childName;
    if (showTerms) {
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
      <ContentSwitcher onChange={onChange}>
        <Switch name="categories" text="Categories" />
        <Switch name="terms" text="Terms" />
      </ContentSwitcher>
      {showTerms && (
        <GlossaryAuthorTermsNavigation getTermsURL={getChildrenURL()} />
      )}
      {!showTerms && (
        <GlossaryAuthorCategoriesNavigation
          getCategoriesURL={getChildrenURL()}
        />
      )}
    </div>
  );
}
