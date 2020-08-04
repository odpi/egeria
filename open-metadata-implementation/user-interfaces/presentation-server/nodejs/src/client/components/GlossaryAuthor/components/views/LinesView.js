/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";
import { FormGroup } from "carbon-components-react";
import Info16 from "@carbon/icons-react/lib/information/16";

function LinesView(props) {
  console.log("LinesView");

  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  console.log("LinesView glossaryAuthorContext", glossaryAuthorContext);

  return (
    <div>
      <FormGroup>
        <div>
          <h4>
            Lines for{" "}
            {glossaryAuthorContext.currentNodeType
              ? glossaryAuthorContext.currentNodeType.typeName
              : ""}{" "}
            {glossaryAuthorContext.selectedNode.name}
            <Info16 />
          </h4>
        </div>
        {glossaryAuthorContext.selectedNodeLines &&
          glossaryAuthorContext.selectedNodeLines.map((line) => {
            return (
              <div>
                <div key={line.name}>name {line.name} </div>
                <div key={line.entity1Name}>e1 name {line.entity1Name} </div>
                <div key={line.entity2Name}>e2 name {line.entity2Name} </div>
                <div key={line.entity1Type}>e1 type {line.entity1Type} </div>
                <div key={line.entity2Type}>e2 type {line.entity2Type} </div>
              </div>
            );
          })}
      </FormGroup>
    </div>
  );
}

export default LinesView;
