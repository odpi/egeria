/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import NodeChildren from "./NodeChildren";
import { withRouter } from "react-router-dom";

function CategoryChildren(props) {
  const getParentGuid = () => {
    return props.match.params.categoryguid;
  };
  return <NodeChildren parentNodeTypeName="category" parentguid={getParentGuid()} />;
}
export default withRouter(CategoryChildren);
