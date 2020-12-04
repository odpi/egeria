/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";
import { IdentificationContext } from "../../../../contexts/IdentificationContext";
import getNodeType from "../properties/NodeTypes.js";
import CreateNode from "./CreateNode";

export default function CreateTerm(props) {
  const identificationContext = useContext(IdentificationContext);
  console.log("CreateTerm");
  return (
   <CreateNode currentNodeType={getNodeType(identificationContext.getRestURL("glossary-author"), "term")} glossaryGuid={props.match.params.glossaryguid}/>
  );
}
