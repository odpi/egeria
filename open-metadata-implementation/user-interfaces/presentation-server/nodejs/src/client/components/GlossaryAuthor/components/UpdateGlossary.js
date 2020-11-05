/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import getNodeType from "./properties/NodeTypes.js";
import UpdateNode from "./UpdateNode";

export default function UpdateGlossary(props) {
  console.log("UpdateGlossary");
  return (
   <UpdateNode currentNodeType={getNodeType("glossary")} guid={props.match.params.guidtoedit} />
  );
}
