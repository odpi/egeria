/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import NodeChildren from "./NodeChildren";
import { withRouter } from "react-router-dom";

function GlossaryChildren(props) {
  const getParentGuid = () => {
    return props.match.params.glossaryguid;
  };
  return <NodeChildren parentNodeTypeName="glossary" parentguid={getParentGuid()} />;
}
export default withRouter(GlossaryChildren);