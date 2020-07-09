/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";
import NodeManagement from "./NodeManagement.js";
import NodeCreate from "./NodeCreate.js";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

const NodeActionContent = props => {
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  console.log("NodeActionContent", props.showCreate + ",propskey = " + props.key);
    
    if (glossaryAuthorContext.authoringActionState == 1 || glossaryAuthorContext.authoringActionState == 2) {
      return <NodeCreate/>;
    } else  if (glossaryAuthorContext.authoringActionState == 3 || glossaryAuthorContext.authoringActionState == 4) {
      return <NodeManagement/>;
    } else {
      return <NodeCreate/>;
    }
};

export default NodeActionContent;
