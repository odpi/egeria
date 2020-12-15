/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";

import { IdentificationContext } from "../../../../contexts/IdentificationContext";
import getNodeType from "../properties/NodeTypes.js";
import UpdateNode from "./UpdateNode";

export default function UpdateGlossary(props) {
  const identificationContext = useContext(IdentificationContext);
  console.log("UpdateGlossary");
  return (
   <UpdateNode currentNodeType={getNodeType(identificationContext.getRestURL("glossary-author"), "glossary")} guid={props.match.params.guidtoedit} />
  );
}
