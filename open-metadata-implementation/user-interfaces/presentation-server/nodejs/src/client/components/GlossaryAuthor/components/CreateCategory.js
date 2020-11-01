/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import getNodeType from "./properties/NodeTypes.js";
import CreateNode from "./CreateNode";

export default function CreateCategory(props) {
  console.log("CreateCategory");
  return (
   <CreateNode currentNodeType={getNodeType("category")} glossaryGuid={props.match.params.glossaryguid}/>
  );
}
