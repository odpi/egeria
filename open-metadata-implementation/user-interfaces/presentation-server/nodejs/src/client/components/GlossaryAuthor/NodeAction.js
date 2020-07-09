/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";
import Add16 from "../../images/Egeria_add_16";
import Search16 from "../../images/Egeria_search_16";
import NodeActionContent from "./NodeActionContent";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

const NodeAction = props => {
  console.log("NodeAction");
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  const onClickAdd = () => {
    glossaryAuthorContext.setCreatingActionState()
  };
  const onClickSearch = () => {
    glossaryAuthorContext.setSearchingActionState()
  };
  console.log("NodeAction " + glossaryAuthorContext.myState);
  if (glossaryAuthorContext.myState == 0) {
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
          <NodeActionContent></NodeActionContent>
        </div>
      </div>
    );
  }
};

export default NodeAction;
