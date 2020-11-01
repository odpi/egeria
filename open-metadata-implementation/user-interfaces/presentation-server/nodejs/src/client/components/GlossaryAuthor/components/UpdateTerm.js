/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import getNodeType from "./properties/NodeTypes.js";
import UpdateNode from "./UpdateNode";

export default function UpdateTerm(props) {
  console.log("UpdateTerm");
  return (
   <UpdateNode currentNodeType={getNodeType("term")} guid={props.match.params.guidtoedit} />
  );
}
