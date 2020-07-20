/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";

import NodeAction from "./NodeAction";
import NodeTypeChooser from "./NodeTypeChooser";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

const GlossaryAuthorNodes = props => {
  console.log("GlossaryAuthorNodes");
  return (
    <div>
      <div className="bx--grid">
        <div className="bx--row">
          <NodeTypeChooser />
        </div>
        <div className="bx--row"> 
          <NodeAction/>
        </div>
      </div>
    </div>
  );
};

export default GlossaryAuthorNodes;
