/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";
import NodeSearch from "./NodeSearch.js";
import NodeCreate from "./NodeCreate.js";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

const NodeActionContent = props => {
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  console.log("NodeActionContent", props.showCreate);
 
    if (props.showCreate) {
      return <NodeCreate />;
    } else {
      return <NodeSearch />;
    }
};

export default NodeActionContent;
