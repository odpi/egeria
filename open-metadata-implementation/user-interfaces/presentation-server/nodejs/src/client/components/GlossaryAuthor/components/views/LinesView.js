/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";
import { FormGroup } from "carbon-components-react";
import Info16 from "@carbon/icons-react/lib/information/16";
import Close16 from "../../../../images/Egeria_close_16";

function LinesView(props) {
  console.log("LinesView");

  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  console.log("LinesView glossaryAuthorContext", glossaryAuthorContext);
  const onClickLine = (e) => {
    console.log("onClickLine");
    alert("Got " + e.target.id);
    e.preventDefault();
  };
  const handleOnClose = (e) => {
    console.log("LinesView handleOnClose");
    e.preventDefault();
    props.onClose();
  };

  const onClickNode = (e) => {
    console.log("onClickNode");
    e.preventDefault();
  };
  return (
    <div>
        <div className="close-title">
        <Close16 onClick={handleOnClose}/>
      </div>
      <FormGroup>
        <div>
          <h4>
            Lines for{" "}
            {glossaryAuthorContext.currentNodeType
              ? glossaryAuthorContext.currentNodeType.typeName
              : ""}
           
            <Info16 />
          </h4>
        </div>
        {glossaryAuthorContext.selectedNodeLines && (
          <div>
            <div className="flex-grid-halves">
              <div className="col">
                <button className={`nodeline-button lineShape nodeline-title`} disabled>
                  Lines
                </button>
              </div>
              <div className="col">
                <button className={`nodeline-button nodeShape nodeline-title`} disabled>
                  Connected Nodes
                </button>
              </div>
            </div>
            {glossaryAuthorContext.selectedNodeLines.map((line) => {
              return (
                <div className="flex-grid-halves">
                  <div key={line.guid} className="col">
                    <button
                      id={line.guid}
                      className={`nodeline-button lineShape clickable`}
                      onClick={onClickLine}
                    >
                      {line.name}
                    </button>
                  </div>
                  <div className="col">
                    <button
                      className={`nodeline-button nodeShape clickable`}
                      onClick={onClickNode}
                    >
                      Term TestName
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </FormGroup>
    </div>
  );
}

export default LinesView;
