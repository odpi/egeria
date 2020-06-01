/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";
import getNodeType from "./NodeTypes.js";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

const NodeTypeChooser = (props) => {
  console.log("GlossaryAuthorNodes");

  const glossaryAuthorContext = useContext(GlossaryAuthorContext);

  const handleNodeChange = (e) => {
    const value = e.target.value;
    console.log("handleSelectChange (() " + value);
    glossaryAuthorContext.setNodeTypeFromKey(value);
  };
  return (
    <div>
      {glossaryAuthorContext.isAuthoringState(0) && (
        <div>
          My Project and My Glossary need to be set; as authored content will be
          stored there.{" "}
        </div>
      )}
      {glossaryAuthorContext.isAuthoringState(1) && (
        <div>
          Setting My Glossary (the glossary that will be used to hold created
          terms and categories).
        </div>
      )}
      {glossaryAuthorContext.isAuthoringState(2) && (
        <div>
          Setting My Project (the project that will be used to hold created
          glossaries, terms and categories).
        </div>
      )}
      {glossaryAuthorContext.isAuthoringState(3) && (
        <div>My Glossary has been set, but My Project needs to be set. </div>
      )}
      {glossaryAuthorContext.isAuthoringState(4) && (
        <div>My Project has been set, but My Glossary needs to be set. </div>
      )}

      {glossaryAuthorContext.isAuthoringState(5) && (
        <div>
          <div>Choose a node type:</div>
          <div class="bx--col-lg-2 bx--col-md-2">
            <div margin="0 auto">
              <select
                id="nodes"
                float="right"
                border-bottom-width="3px"
                onChange={handleNodeChange}
              >
                <option value="term">Term</option>
                <option value="category">Category</option>
                <option value="glossary">Glossary</option>
                <option value="project">Project</option>
              </select>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default NodeTypeChooser;
