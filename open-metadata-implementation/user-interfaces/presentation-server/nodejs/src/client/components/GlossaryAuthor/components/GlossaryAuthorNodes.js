/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";

import NodeController from "./NodeController";
import NodeTypeChooser from "./NodeTypeChooser";

const GlossaryAuthorNodes = props => {
  console.log("GlossaryAuthorNodes");
  return (
    <div>
      <div className="bx--grid">
        <div className="bx--row">
          <NodeTypeChooser />
        </div>
        <div className="bx--row"> 
          <NodeController/>
        </div>
      </div>
    </div>
  );
};

export default GlossaryAuthorNodes;
