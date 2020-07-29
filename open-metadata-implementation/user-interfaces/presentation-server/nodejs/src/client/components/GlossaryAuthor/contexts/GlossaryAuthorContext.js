/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { createContext, useReducer } from "react";
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

  const Types = {
    CREATING_MY_GLOSSARY: Symbol("changingMyGlossary"),
    CREATED_MY_GLOSSARY: Symbol("changedMyGlossary"),
    CREATING_MY_PROJECT: Symbol("changingMyProject"),
    CREATED_MY_PROJECT: Symbol("changedMyProject"),
    CREATING: Symbol("creating"),
    CREATED: Symbol("created"),
    SEARCHING: Symbol("searching"),
    SEARCHED: Symbol("searched"),
    REFRESH: Symbol("refresh"),
    DELETING: Symbol("deleting"),
    SET_CURRENT_NODE_TYPE: Symbol("SET_CURRENT_NODE_TYPE"),
    UPDATE_SELECTED_NODE: Symbol("updatedSelectedNode"),
    RESET: Symbol("reset"),
  };
  Object.freeze(Types);
  const MyEdittingTypes = {
    NONE: Symbol("None"),
    GLOSSARY: Symbol("Glossary"),
    PROJECT: Symbol("Project"),
  };
  // freeze the "enum" so it cannot change
  Object.freeze(MyEdittingTypes);

  const Operations = {
    NONE: Symbol("none"),
    CREATING: Symbol("creating"),
    CREATED: Symbol("created"),
    SEARCHING: Symbol("searching"),
    SEARCHED: Symbol("searched"),
    REFRESH: Symbol("refresh"),
    DELETING: Symbol("deleting"),
  };
  // freeze the "enum" so it cannot change
  Object.freeze(Operations);
  /**
   * Reducer to allow actions to make state transitions within state. Multiple partsof state can be updated and
   * should result in one re-render. Without useReducer each state change resulted in a re-render, leading to renders on
   * partial state changes.
   */
  const [state, dispatch] = useReducer(stateReducer, initialiseState);
  /**
   * state is the object that the reducer reduces to . It is a single object representing the current state.
   *
   */
  const {
    myGlossary, // this is the glossary that is used for authoring content in
    myProject, // this is a project used for authoring content in
    myEdittingType, // this indicates that we are editing either myGlossary, myProject or neither
    operation, // this is a more granular state
    currentNodeType,
    selectedNode,
  } = state;

  function initialiseState() {
    return {
      // myGlossary: undefined,
      // myProject: undefined,
      myEdittingType: MyEdittingTypes.NONE,
      operation: Operations.NONE,
      // currentNodeType: undefined,
      // selectedNode: undefined,
    };
  }
  /**
   * Reduce the setting of my glossary, my project, the authoring state and whether we are editting into one object.
   * The authoring state values are NONE, creating, created, searching, searched refreshsearch and deleting
   * @param {*} state
   * @param {*} action
   */
  function stateReducer(state, action) {
    switch (action.type.toString()) {
      case Types.CREATING_MY_GLOSSARY.toString():
        let operation;
        if (state.myGlossary) {
          // if we have a myGlossary then we are searching
          operation = Operations.SEARCHING;
        } else {
          // otherwise we are creating
          operation = Operations.CREATING;
        }
        return {
          ...state,
          myEdittingType: MyEdittingTypes.GLOSSARY,
          operation: operation,
          currentNodeType: glossaryNodeType,
          selectedNode: undefined,
        };
      case Types.CREATED_MY_GLOSSARY.toString():
        return {
          ...state,
          myGlossary: action.payload, // payload glossary to put into myGlossary
          myEdittingType: MyEdittingTypes.NONE,
          operation: Operations.CREATED,
          selectedNode: undefined,
        };
      case Types.CREATING_MY_PROJECT.toString(): {
        if (state.myProject) {
          // if we have a myProject then we are searching
          operation = Operations.SEARCHING;
        } else {
          // otherwise we are creating
          operation = Operations.CREATING;
        }
        return {
          ...state,
          myEdittingType: MyEdittingTypes.PROJECT,
          operation: operation,
          currentNodeType: projectNodeType,
          selectedNode: undefined,
        };
      }
      case Types.CREATED_MY_PROJECT.toString():
        return {
          ...state,
          myProject: action.payload, // payload project to put into myProject
          myEdittingType: MyEdittingTypes.NONE,
          operation: Operations.CREATED,
        };
      case Types.CREATING.toString():
        return {
          ...state,
          operation: Operations.CREATING,
        };
      case Types.CREATED.toString():
        return {
          ...state,
          operation: Operations.CREATED,
        };
      case Types.SEARCHING.toString():
        return {
          ...state,
          operation: Operations.SEARCHING,
          selectedNode: undefined,
        };
      case Types.SEARCHED.toString():
        return {
          ...state,
          myEdittingType: MyEdittingTypes.NONE,
          operation: Operations.SEARCHED,
        };
      case Types.REFRESH.toString():
        return {
          ...state,
          operation: Operations.REFRESH,
        };
      case Types.DELETING.toString():
        return {
          ...state,
          myEdittingType: MyEdittingTypes.NONE,
          operation: Operations.DELETING,
        };
      case Types.SET_CURRENT_NODE_TYPE.toString():
        let nodeType;
        // payload is the nodeType's key
        if (action.payload == "term") {
          nodeType = termNodeType;
        } else if (action.payload == "category") {
          nodeType = categoryNodeType;
        } else if (action.payload == "glossary") {
          nodeType = glossaryNodeType;
        } else if (action.payload == "project") {
          nodeType = projectNodeType;
        }
        return {
          ...state,
          currentNodeType: nodeType,
        };
      case Types.UPDATE_SELECTED_NODE.toString():
        return {
          ...state,
          selectedNode: action.payload, // payload is the selected Node
        };
      case Types.RESET.toString():
        return initialiseState();
      default:
        return state;
    }
  }
  // The following do methods result in actions on the reducer, that will cause state changes.

  const doUpdateSelectedNode = (nodeIn) => {
    dispatch({ type: Types.UPDATE_SELECTED_NODE, payload: nodeIn });
  };
  /**
   * About to set my glossary
   */
  const doCreatingMyGlossary = () => {
    console.log("settingMyGlossary");
    dispatch({ type: Types.CREATING_MY_GLOSSARY });
  };
  /**
   * About to set my project
   */
  const doCreatingMyProject = () => {
    // if myProject is not set then this is a create
    dispatch({ type: Types.CREATING_MY_PROJECT });
  };
  /**
   * Is setup complete - i.e. have we set uyp the glossary and project we want to uthor in and were not currently editting them
   */
  const isSetupComplete = () => {
    return (
      myGlossary &&
      myProject &&
      myEdittingType && 
      myEdittingType.toString() == MyEdittingTypes.NONE.toString()
    );
  };
  /**
   * Update My Glossary to the provided value.
   */
  const doCreatedMyGlossary = (glossary) => {
    console.log("doCreatedMyGlossary " + glossary.name);
    dispatch({ type: Types.CREATED_MY_GLOSSARY, payload: glossary });
  };
  /**
   * Update My Project to the provided value.
   */
  const doCreatedMyProject = (project) => {
    console.log("doCreatedMyProject" + project.name);
    dispatch({ type: Types.CREATED_MY_PROJECT, payload: project });
  };

  const doCreatingAction = () => {
    console.log("doCreatingAction");
    dispatch({ type: Types.CREATING });
  };

  const doCreatedAction = () => {
    dispatch({ type: Types.CREATED});  
  };

  const doSearchingAction = () => {
    console.log("doSearchingAction");
    dispatch({ type: Types.SEARCHING });
  };
  const doSearchedAction = () => {
    console.log("setActionSearchedState");
    dispatch({ type: Types.SEARCHED });
  };
  const doRefreshSearchAction = () => {
    console.log("setRefreshSearchAction");
    dispatch({ type: Types.REFRESH });
  };

  const doDeletingAction = () => {
    console.log("setDeletingAction");
    dispatch({ type: Types.DELETING });
  };

  const doSetNodeType = (key) => {
    dispatch({ type: Types.SET_CURRENT_NODE_TYPE, payload: key });
  };

  // The following is methods are queries on state
  /**
   * We are in the process of editting my glossary
   */
  const isEdittingMyGlossary = () => {
    const flag =
      myEdittingType && // this can be undefined, because we use a function to initialise - so it does a lazy initialization of the reducer.
      MyEdittingTypes.GLOSSARY.toString() == myEdittingType.toString() &&
      currentNodeType &&
      currentNodeType.key == "glossary";
    if (myEdittingType) {
      console.log(
        "isEdittingMyGlossary myEdittingType" + myEdittingType.toString()
      );
    }
    console.log("currentNodeType" + currentNodeType);
    console.log("flag " + flag);
    return flag;
  };
  /**
   * We are in the process of editting my project
   */
  const isEdittingMyProject = () => {
    const flag =
      myEdittingType && // this can be undefined, because we use a function to initialise - so it does a lazy initialization of the reducer.
      MyEdittingTypes.PROJECT.toString() == myEdittingType.toString() &&
      currentNodeType &&
      currentNodeType.key == "project";
    if (myEdittingType) {
      console.log(
        "isEdittingMyProject myEdittingType" + myEdittingType.toString()
      );
    }
    console.log("currentNodeType" + currentNodeType);
    console.log("flag " + flag);
    return flag;
  };
  const isCreatingOperation = () => {
    return Operations.CREATING.toString() == operation.toString();
  };
  const isCreatedOperation = () => {
    return Operations.CREATED.toString() == operation.toString();
  };
  const isSearchingOperation = () => {
    return Operations.SEARCHING.toString() == operation.toString();
  };
  const isSearchedOperation = () => {
    return Operations.SEARCHED.toString() == operation.toString();
  };
  const isRefreshSearchOperation = () => {
    return Operations.REFRESH.toString() == operation.toString();
  };
  const isDeletingOperation = () => {
    return Operations.DELETING.toString() == operation.toString();
  };
  const isUndefinedOperation = () => {
    return Operations.NONE.toString() == operation.toString();
  };

  return (
    <GlossaryAuthorContext.Provider
      value={{
        // exposed state
        currentNodeType,
        selectedNode,
        myProject,
        myGlossary,
        // do methods requesting actions
        doSetNodeType,
        doCreatingMyGlossary,
        doCreatingMyProject,
        doCreatedMyGlossary,
        doCreatedMyProject,
        doCreatingAction,
        doCreatedAction,
        doSearchingAction,
        doSearchedAction,
        doRefreshSearchAction,
        doDeletingAction,
        doUpdateSelectedNode,
        // is methods interrogate state
        isEdittingMyGlossary,
        isEdittingMyProject,
        isCreatingOperation,
        isCreatedOperation,
        isSearchingOperation,
        isSearchedOperation,
        isRefreshSearchOperation,
        isDeletingOperation,
        isUndefinedOperation,
        isSetupComplete,
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
