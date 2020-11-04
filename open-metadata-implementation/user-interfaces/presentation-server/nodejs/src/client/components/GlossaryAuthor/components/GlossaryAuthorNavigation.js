/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import { ContentSwitcher, Switch } from "carbon-components-react";
import GlossaryViewNavigation from "./GlossaryViewNavigation";
import { useHistory, withRouter } from "react-router-dom";

function GlossaryAuthorNavigation(props) {
  console.log("GlossaryAuthorNavigation");
  const [currentNodeTypeToShow, setCurrentNodeTypeToShow] = useState(
    "glossaries"
  );

  let history = useHistory();

  const onChange = (e) => {
    const chosenContent = `${e.name}`;
    const url = props.match.url + "/" + chosenContent;
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
      {currentNodeTypeToShow == "glossaries" && <GlossaryViewNavigation match={props.match}/>}
    </div>
  );
}
export default withRouter(GlossaryAuthorNavigation);
