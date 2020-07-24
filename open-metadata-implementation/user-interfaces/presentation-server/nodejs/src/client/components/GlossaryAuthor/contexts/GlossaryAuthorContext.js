/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { createContext, useState } from "react";
import PropTypes from "prop-types";
import getNodeType from "../components/properties/NodeTypes.js";
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

  // myEditting context values are : undefined, glossary or project.
  const [myEditting, setMyEditting] = useState();

  // The myState refers to the default Glossary and Project that need to be set in order to create terms and categories.
  // 0 = unset 1 = setting my glossary 2 = setting my project 3 = glossary set project not, 4 = project set glossary not, 5 authoring
  //const [myState, setMyState] = useState(0);

  const updateSelectedNode = (nodeIn) => {
    setSelectedNode(nodeIn);
  };
  /**
   * About to set my glossary
   */
  const settingMyGlossary = () => {
    console.log("settingMyGlossary");
    // if myGlossary is not set then this is a create
    setMyEditting("glossary");
    if (!myGlossary) {
      // we are creating create my glossary
      setCreatingActionState();
    } else {
      // we should show search to select a my glossary
      setSearchingActionState();
    }
    // set glossary as the current node type
    setCurrentNodeType(glossaryNodeType);
  };
  /**
   * About to set my project
   */
  const settingMyProject = () => {
    // if myProject is not set then this is a create
    setMyEditting("project");
    if (!myProject) {
      // we are creating create my project
      setCreatingActionState();
    } else {
      // we should show search to select a my project
      setSearchingActionState();
    }
    // set project as the current node type
    setCurrentNodeType(projectNodeType);
  };
  /**
   * We are in the process of editting my glossary
   */
  const isEdittingMyGlossary = () => {
    return (
      myEditting === "glossary" &&
      currentNodeType !== undefined &&
      currentNodeType.key == "glossary"
    );
  };
  /**
   * We are in the process of editting my project
   */
  const isEdittingMyProject = () => {
    return (
      myEditting === "project" &&
      currentNodeType !== undefined &&
      currentNodeType.key == "project"
    );
  };
  /**
   * Update My Glossary to the provided value. If we have not got a myProject, then we need to be editting project now
   */
  const updateMyGlossary = (glossary) => {
    console.log("setMyGlossaryState");
    setMyGlossary(glossary);

    if (myProject) {
      // no longer editting my glossary or my project
      setMyEditting(undefined);
    } else {
      setMyEditting("project");
      setCurrentNodeType(glossaryNodeType);
    }
  };
  /**
   * Update My Project to the provided value. If we have not got a myGlossary, then indicate we are editting my glossary
   */
  const updateMyProject = (project) => {
    console.log("setMyProjectState");
    setMyProject(project);
    if (myGlossary) {
      // no longer editting my glossary or my project
      setMyEditting(undefined);
    } else {
      setMyEditting("project");
      setCurrentNodeType(glossaryNodeType);
    }
  };

  // 0 = unset 1 = creating 2 = created 3 = searching 4 searched 5 refresh search, 6 deleting
  const [authoringActionState, setAuthoringActionState] = useState(0);
  const isUndefinedActionState = () => {
    return authoringActionState === undefined;
  };
  const setCreatingActionState = () => {
    console.log("setCreatingActionState");
    setAuthoringActionState(1);
  };
  const isCreatingActionState = () => {
    console.log("Expecting 1 " +authoringActionState  + " " + (1 == authoringActionState))
    return (1 == authoringActionState);
  };
  const setCreatedActionState = () => {
    setAuthoringActionState(2);
  };
  const isCreatedActionState = () => {
    console.log("Expecting 2 " +authoringActionState )
    return (2 == authoringActionState);
  };
  const setSearchingActionState = () => {
    console.log("setSearchingActionState");
    setAuthoringActionState(3);
  };
  const isSearchingActionState = () => {
    return (3 == authoringActionState);
  };

  const setSearchedActionState = () => {
    console.log("setActionSearchedState");
    setAuthoringActionState(4);
  };
  const isSearchedActionState = () => {
    return (4 == authoringActionState);
  };
  const setRefreshSearchActionState = () => {
    console.log("setRefreshSearchingActionState");
    setAuthoringActionState(5);
  };
  const isRefreshSearchActionState = () => {
    return 5 == authoringActionState;
  };
  const setDeletingActionState = () => {
    console.log("setDeletingActionState");
    setAuthoringActionState(6);
  };
  const isDeletingActionState = () => {
    return (6 == authoringActionState);
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
        myProjectLabel,
        setMyProjectLabel,
        myGlossaryLabel,
        setMyGlossaryLabel,
        myEditting,
        settingMyGlossary,
        settingMyProject,
        isEdittingMyGlossary,
        isEdittingMyProject,
        updateMyGlossary,
        updateMyProject,
        setCreatingActionState,
        setCreatedActionState,
        setSearchingActionState,
        setSearchedActionState,
        setRefreshSearchActionState,
        setDeletingActionState,
        isCreatingActionState,
        isCreatedActionState,
        isSearchingActionState,
        isSearchedActionState,
        isRefreshSearchActionState,
        isDeletingActionState,
        isUndefinedActionState,
        updateSelectedNode,
        removeSelectedNode,
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
