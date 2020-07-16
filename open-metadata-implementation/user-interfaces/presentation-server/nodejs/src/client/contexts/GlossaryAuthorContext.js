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
  const termNodeType = getNodeType("term");
  const categoryNodeType = getNodeType("category");
  const glossaryNodeType = getNodeType("glossary");
  const projectNodeType = getNodeType("project");

  const [currentNodeType, setCurrentNodeType] = useState();
  const [selectedNode, setSelectedNode] = useState();
  const [myProject, setMyProject] = useState();
  const [myGlossary, setMyGlossary] = useState();
  const [myProjectLabel, setMyProjectLabel] = useState("Set my project");
  const [myGlossaryLabel, setMyGlossaryLabel] = useState("Set my glossary");

   // The myState refers to the default Glossary and Project that need to be set in order to create terms and categories.
  // 0 = unset 1 = setting my glossary 2 = setting my project 3 = glossary set project not, 4 = project set glossary not, 5 authoring
  const [myState, setMyState] = useState(0);

  const updateSelectedNode = async (nodeIn) => {
    await setSelectedNode(nodeIn);
  }
  const settingMyGlossaryState = () => {
    console.log("settingMyGlossaryState");
    setMyState(1);
    setAuthoringActionState(1);
    setCurrentNodeType(glossaryNodeType);
  };
  const settingMyProjectState = () => {
    console.log("settingMyProjectState");
    setMyState(2);
    setAuthoringActionState(1);
    setCurrentNodeType(projectNodeType);
  };
  const setMyGlossaryState = () => {
    console.log("setMyGlossaryState");
    if (myProject) {
      setMyState(5);
    } else {
      setMyState(3);
      setCurrentNodeType(glossaryNodeType);
    }
  };
  const setMyProjectState = () => {
    console.log("setMyProjectState");
    if (myGlossary) {
      setMyState(5);
      setCurrentNodeType(termNodeType);
    } else {
      setMyState(4);
      setCurrentNodeType(projectNodeType);
    }
  };

  // 0 = unset 1 = creating 2 = created 3 = searching 4 searched 5 refresh search
  const [authoringActionState, setAuthoringActionState] = useState(0);

  const setCreatingActionState = () => {
    console.log("setCreatingActionState");
    setAuthoringActionState(1);
  };
  const setCreatedActionState = () => {
    console.log("setCreatedActionState");
    setAuthoringActionState(2);
  };
  const setSearchingActionState = () => {
    console.log("setSearchingActionState");
    setAuthoringActionState(3);
  };

  const setSearchedActionState = () => {
    console.log("setActionSearchedState");
    setAuthoringActionState(4);
  };
  const setRefreshSearchingActionState = () => {
    console.log("setRefreshSearchingActionState");
    setAuthoringActionState(5);
  };
  const removeSelectedNode = () => {
    console.log("removeSelectedNode");
    setSelectedNode(undefined);
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
  const isMyState = (state) => {
    return state == myState;
  };
  return (
    <GlossaryAuthorContext.Provider
      value={{
        currentNodeType,
        setCurrentNodeType,
        setNodeTypeFromKey,
        selectedNode,
        myProject,
        setMyProject,
        myGlossary,
        setMyGlossary,
        myState,
        setMyState,
        isMyState,
        myProjectLabel,
        setMyProjectLabel,
        myGlossaryLabel,
        setMyGlossaryLabel,
        settingMyGlossaryState,
        settingMyProjectState,
        setMyGlossaryState,
        setMyProjectState,
        authoringActionState,
        setCreatingActionState,
        setCreatedActionState,
        setSearchingActionState,
        setSearchedActionState,
        setRefreshSearchingActionState,
        updateSelectedNode,
        removeSelectedNode
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
