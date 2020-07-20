/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";
import Add16 from "../../images/Egeria_add_16";
import Search16 from "../../images/Egeria_search_16";
import Delete16 from "../../images/Egeria_delete_16";
import NodeActionContent from "./NodeActionContent";
import NodeDelete from "./NodeDelete";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

const NodeAction = (props) => {
  console.log("NodeAction");
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  const [errorMsg, setErrorMsg] = useState();
  const onClickAdd = () => {
    setErrorMsg("");
    glossaryAuthorContext.setCreatingActionState();
  };
  const onClickSearch = () => {
    setErrorMsg("");
    glossaryAuthorContext.setSearchingActionState();
  };
  const onClickDelete = () => {
    glossaryAuthorContext.setDeletingActionState();
  };
  console.log("NodeAction " + glossaryAuthorContext.myState);
  if (glossaryAuthorContext.myState == 0) {
    return null;
  } else {
    return (
      <div>
        <div className="bx--row">
          <div className="bx--col-lg-1 bx--col-md-1">
            <Add16 kind="primary" onClick={() => onClickAdd()} />
          </div>
          <div className="bx--col-lg-1 bx--col-md-1">
            <Search16 onClick={() => onClickSearch()} />
          </div>
          {glossaryAuthorContext.selectedNode && (
            <div className="bx--col-lg-1 bx--col-md-1">
              <Delete16 onClick={() => onClickDelete()} />
            </div> 
          )}
          {glossaryAuthorContext.selectedNode && glossaryAuthorContext.authoringActionState == 6 && <NodeDelete />}
          <div style={{ color: "red" }}>{errorMsg}</div>
        </div>

        <div className="bx--row">
          <NodeActionContent></NodeActionContent>
        </div>
      </div>
    );
  }
};

export default NodeAction;
