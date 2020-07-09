/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";

import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";
import NodeSearch from "./NodeSearch";
import NodeUpdate from "./NodeUpdate";

const NodeManagement = props => {
  console.log("NodeManagement");
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);

  return (
      <div>
          <div>
            <NodeSearch/>
          </div>
          <div>
            <NodeUpdate/>
          </div>
      </div>
    );
  }
};

export default NodeManagement;
