/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import getNodeType from "./properties/NodeTypes.js";
import CreateNode from "./CreateNode";

export default function CreateTerm(props) {
  console.log("CreateTerm");
  return (
   <CreateNode currentNodeType={getNodeType("term")} glossaryGuid={props.match.params.glossaryguid}/>
  );
}
