/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";
import NodeSearch from "./NodeSearch";
import NodeUpdate from "./NodeUpdate";
import NodeCreate from "./NodeCreate.js";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

const NodeActionContent = (props) => {
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  console.log(
    "NodeActionContent",
    props.showCreate + ",propskey = " + props.key
  );

  if (
    glossaryAuthorContext.authoringActionState == 1 ||
    glossaryAuthorContext.authoringActionState == 2
  ) {
    return <NodeCreate />;
  } else if (
    glossaryAuthorContext.authoringActionState == 3 ||
    glossaryAuthorContext.authoringActionState == 4
  ) {
    return (
      <div className="actions-container">
        <div className="actions-item">
          <NodeSearch />
        </div>
        {glossaryAuthorContext.selectedNode && (
          <div className="actions-item">
            <NodeUpdate />
          </div>
        )}
      </div>
    );
  } else if (glossaryAuthorContext.authoringActionState == 5  || (glossaryAuthorContext.authoringActionState == 6 )) {
    return (
      <div className="actions-container">
        <div className="actions-item">
          <NodeSearch/>
        </div>
        {glossaryAuthorContext.selectedNode && (
          <div className="actions-item">
            <NodeUpdate key={glossaryAuthorContext.selectedNode.systemAttributes.guid}/>
          </div>
        )}
      </div>
    );
 } else {
    return <NodeCreate />;
  }
};

export default NodeActionContent;
