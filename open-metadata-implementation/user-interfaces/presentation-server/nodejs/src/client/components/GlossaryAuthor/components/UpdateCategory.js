/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import getNodeType from "./properties/NodeTypes.js";
import UpdateNode from "./UpdateNode";

export default function UpdateCategory(props) {
  console.log("UpdateCategory");
  return (
   <UpdateNode currentNodeType={getNodeType("category")} guid={props.match.params.guidtoedit} />
  );
}
