/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";

import { Button } from "react-bootstrap";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

const MyNodeView = props => {
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  console.log("MyNodeView");
  const [typeKey] = useState(props.typeKey);

  if (typeKey == "glossary") {
    return (
      <span>
        <Button kind="primary" onClick={() => glossaryAuthorContext.doSettingMyGlossary()}>
          {glossaryAuthorContext.myGlossary ? glossaryAuthorContext.myGlossary.name : "None"}
        </Button>
      </span>
    );
  } else if (typeKey == "project") {
    return (
      <span>
       <Button kind="primary" onClick={() => glossaryAuthorContext.doSettingMyProject()}>
          {glossaryAuthorContext.myProject ? glossaryAuthorContext.myProject.name : "None"}
        </Button>
      </span>
    );
  } else {
      console.log("MyNodeView typeKey null", typeKey);
      return null;
  }
};

export default MyNodeView;