/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";

import { Button } from "react-bootstrap";
import { GlossaryAuthorCRUDContext } from "../../contexts/GlossaryAuthorCRUDContext";

const MyNodeView = props => {
  const glossaryAuthorCRUDContext = useContext(GlossaryAuthorCRUDContext);
  console.log("MyNodeView");
  const [typeKey] = useState(props.typeKey);

  if (typeKey == "glossary") {
    return (
      <span>
        <Button kind="primary" onClick={() => glossaryAuthorCRUDContext.doSettingMyGlossary()}>
          {glossaryAuthorCRUDContext.myGlossary ? glossaryAuthorCRUDContext.myGlossary.name : "None"}
        </Button>
      </span>
    );
  } else if (typeKey == "project") {
    return (
      <span>
       <Button kind="primary" onClick={() => glossaryAuthorCRUDContext.doSettingMyProject()}>
          {glossaryAuthorCRUDContext.myProject ? glossaryAuthorCRUDContext.myProject.name : "None"}
        </Button>
      </span>
    );
  } else {
      console.log("MyNodeView typeKey null", typeKey);
      return null;
  }
};

export default MyNodeView;