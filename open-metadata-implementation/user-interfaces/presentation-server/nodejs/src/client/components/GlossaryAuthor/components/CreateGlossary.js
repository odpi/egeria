/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import getNodeType from "./properties/NodeTypes.js";
import CreateNode from "./CreateNode";

export default function CreateGlossary(props) {
  console.log("CreateGlossary");
  return (
   <CreateNode currentNodeType={getNodeType("glossary")} />
  );
}
