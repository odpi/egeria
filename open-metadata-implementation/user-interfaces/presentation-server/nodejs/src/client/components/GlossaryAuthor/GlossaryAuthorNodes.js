/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";

import NodeSCUD from "./NodeSCUD";
import NodeTypeChooser from "./NodeTypeChooser";

const GlossaryAuthorNodes = props => {
  console.log("GlossaryAuthorNodes");
  return (
    <div>
      <div class="bx--grid">
        <div class="bx--row">
          <NodeTypeChooser />
        </div>
        <div class="bx--row">
          <NodeSCUD />
        </div>
      </div>
    </div>
  );
};

export default GlossaryAuthorNodes;
