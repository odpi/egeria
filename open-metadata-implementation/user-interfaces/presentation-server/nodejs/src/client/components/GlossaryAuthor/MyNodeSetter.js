/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";

import { Button } from "react-bootstrap";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

const MyNodeSetter = props => {
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  console.log("MyNodeSetter");
  const [typeKey] = useState(props.typeKey);

  if (typeKey == "glossary") {
    return (
      <span>
        <Button kind="primary" onClick={() => glossaryAuthorContext.settingMyGlossaryState()}>
          {glossaryAuthorContext.myGlossaryLabel}
        </Button>
      </span>
    );
  } else if (typeKey == "project") {
    return (
      <span>
       <Button kind="primary" onClick={() => glossaryAuthorContext.settingMyProjectState()}>
          {glossaryAuthorContext.myProjectLabel}
        </Button>
      </span>
    );
  } else {
      console.log("MyNodeSetter typeKey null", typeKey);
      return null;
  }
};

export default MyNodeSetter;
