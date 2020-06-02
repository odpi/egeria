/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { createContext, useState } from "react";
import PropTypes from "prop-types";
import getNodeType from "../components/GlossaryAuthor/NodeTypes.js";
export const GlossaryAuthorContext = createContext();
export const GlossaryAuthorContextConsumer = GlossaryAuthorContext.Consumer;
const GlossaryAuthorContextProvider = (props) => {
  console.log(
    "GlossaryAuthorContext GlossaryAuthorContextProvider",
    GlossaryAuthorContextProvider
  );
  // get the nodeTypes here as the getNodeType accesses a context, which must be accessed at the top level of functional React component
  const [termNodeType] = useState(getNodeType("term"));
  const [categoryNodeType] = useState(getNodeType("category"));
  const [glossaryNodeType] = useState(getNodeType("glossary"));
  const [projectNodeType] = useState(getNodeType("project"));

  const [currentNodeType, setCurrentNodeType] = useState();
  const [selectedNode, setSelectedNode] = useState();
  const [myProject, setMyProject] = useState();
  const [myGlossary, setMyGlossary] = useState();
  const [myProjectLabel, setMyProjectLabel] = useState("Set my project");
  const [myGlossaryLabel, setMyGlossaryLabel] = useState("Set my glossary");
  // 0 = unset 1 = setting glossary 2 = setting project 3 = glossary set project not, 4 = project set glossary not, 5 = node authoring
  const [authoringState, setAuthoringState] = useState(0);

  const settingMyGlossaryState = () => {
    console.log("settingMyGlossaryState");
    setAuthoringState(1);
    setCurrentNodeType(glossaryNodeType);
  };
  const settingMyProjectState = () => {
    console.log("settingMyProjectState");
    setAuthoringState(2);
    setCurrentNodeType(projectNodeType);
  };
  const setMyGlossaryState = () => {
    console.log("setMyGlossaryState");
    if (myProject) {
      setAuthoringState(5);
    } else {
      setAuthoringState(3);
    }
  };
  const setMyProjectState = () => {
    console.log("setMyProjectState");
    if (myGlossary) {
      setAuthoringState(5);
    } else {
      setAuthoringState(4);
    }
  };

  const setNodeTypeFromKey = (key) => {
    if (key == "term") {
      setCurrentNodeType(termNodeType);
    } else if (key == "category") {
      setCurrentNodeType(categoryNodeType);
    } else if (key == "glossary") {
      setCurrentNodeType(glossaryNodeType);
    } else if (key == "project") {
      setCurrentNodeType(projectNodeType);
    }
  };
  const isAuthoringState = (state) => {
    return state == authoringState;
  };
  return (
    <GlossaryAuthorContext.Provider
      value={{
        currentNodeType,
        setCurrentNodeType,
        selectedNode,
        setSelectedNode,
        myProject,
        setMyProject,
        myGlossary,
        setMyGlossary,
        authoringState,
        setAuthoringState,
        myProjectLabel,
        setMyProjectLabel,
        myGlossaryLabel,
        setMyGlossaryLabel,
        settingMyGlossaryState,
        settingMyProjectState,
        setMyGlossaryState,
        setMyProjectState,
        setNodeTypeFromKey,
        isAuthoringState,
      }}
    >
      {props.children}
    </GlossaryAuthorContext.Provider>
  );
};
GlossaryAuthorContextProvider.propTypes = {
  children: PropTypes.node,
};
export default GlossaryAuthorContextProvider;
