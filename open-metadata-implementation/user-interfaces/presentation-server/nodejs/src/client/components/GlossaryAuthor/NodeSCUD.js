/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";
import Add16 from "../../images/Egeria_add_16";
import Search16 from "../../images/Egeria_search_16";
import NodeActionContent from "./NodeActionContent";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

const NodeSCUD = props => {
  console.log("NodeSCUD");
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  const [showCreate, setShowCreate] = useState(true);

  const onClickAdd = () => {
    setShowCreate(true);
  };
  const onClickSearch = () => {
    setShowCreate(false);
  };
  console.log("NodeSCUD " + glossaryAuthorContext.authoringState);
  if (glossaryAuthorContext.authoringState == 0) {
    return null;
  } else {
    return (
      <div>
        <div class="bx--row">
          <div class="bx--col-lg-1 bx--col-md-1">
            <Add16 kind="primary" onClick={() => onClickAdd()} />
          </div>
          <div class="bx--col-lg-1 bx--col-md-1">
            <Search16 onClick={() => onClickSearch()} />
          </div>
        </div>
        <div class="bx--row">
          <NodeActionContent showCreate={showCreate}></NodeActionContent>
        </div>
      </div>
    );
  }
};

export default NodeSCUD;
