/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect } from "react";
import GlossaryAuthorContext from "./contexts/GlossaryAuthorCRUDContext";
import { Accordion, AccordionItem } from "carbon-components-react";
import getNodeType from "./components/properties/NodeTypes.js";
import Egeria_project_32 from "../../images/Egeria_project_32";
import Egeria_glossary_32 from "../../images/Egeria_glossary_32";
import MyNodeView from "./components/views/MyNodeView";
import GlossaryAuthorNodes from "./components/GlossaryAuthorNodes";

export default function GlossaryAuthor() {
  const [connected, setConnected] = useState();
  const [errorMsg, setErrorMsg] = useState();
  const [exceptionUserAction, setExceptionUserAction] = useState();
  const [responseCategory, setResponseCategory] = useState();
  const [exceptionErrorMessage, setExceptionErrorMessage] = useState();
  const [systemAction, setSystemAction] = useState();
  const [fullResponse, setFullResponse] = useState();


  return (
    <div>
       Placeholder for Graph navigation logic. 
       
    </div>
  );
}
