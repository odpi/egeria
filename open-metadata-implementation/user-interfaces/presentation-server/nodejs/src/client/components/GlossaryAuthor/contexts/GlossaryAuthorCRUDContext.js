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
    SETTING_MY_GLOSSARY: Symbol("changingMyGlossary"),
    SET_MY_GLOSSARY: Symbol("changedMyGlossary"),
    SETTING_MY_PROJECT: Symbol("changingMyProject"),
    SET_MY_PROJECT: Symbol("changedMyProject"),
    CREATING: Symbol("creating"),
    CREATED: Symbol("created"),
    NODE_SELECTED: Symbol("node selected"),
    SEARCHING: Symbol("searching"),
    SEARCHED: Symbol("searched"),
    REFRESH: Symbol("refresh"),
    DELETING: Symbol("deleting"),
    SET_CURRENT_NODE_TYPE: Symbol("setCurrentNodeType"),
    UPDATING_NODE: Symbol("updatingNode"),
    UPDATE_NODE: Symbol("updateNode"),
    UPDATE_CANCELLED: Symbol("update cancelled"),
    UPDATE_NODE_LINES: Symbol("updateNodeLines"),
    UPDATE_NODELINES_CANCELLED: Symbol("updateNodeLines cancelled"),
    RESET_SELECTED: Symbol("reset selected"),
    RESET: Symbol("reset"),
  };
  Object.freeze(Types);
  const Operations = {
    NONE: Symbol("none"),
    CREATING: Symbol("creating"),
    CREATED: Symbol("created"),
    UPDATING: Symbol("updating"),
    NODE_SELECTED: Symbol("Node selected"),
    SEARCHING: Symbol("searching"),
    SEARCHED: Symbol("searched"),
    REFRESH: Symbol("refresh"),
    DELETING: Symbol("deleting"),
    GETTING_CURRENT_NODE_LINES: Symbol("gettingCurrentNodeLines"),
  };
  // freeze the "enum" so it cannot change
  Object.freeze(Operations);
  const myStates = {
    SETTING: Symbol("Setting"),
    SET: Symbol("Set"),
  };
  // freeze the "enum" so it cannot change
  Object.freeze(myStates);
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
    myGlossaryState, // SETTING, SET
    myProjectState, // SETTING, SET
    operation, // this is a more granular state
    currentNodeType, // current node type
    selectedNode, // selected Node
    selectedNodeLines, // Lines associated with the selected node
  } = state;

  function initialiseState() {
    return {
      operation: Operations.NONE,
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
      case Types.SETTING_MY_GLOSSARY.toString(): {
        return {
          ...state,
          currentNodeType: glossaryNodeType,
          selectedNode: undefined,
          selectedNodeLines: undefined,
          myGlossaryState: myStates.SETTING,
        };
      }
      case Types.SETTING_MY_PROJECT.toString(): {
        return {
          ...state,
          myProjectState: myStates.SETTING,
          currentNodeType: projectNodeType,
          selectedNode: undefined,
          selectedNodeLines: undefined,
        };
      }

      case Types.CREATING.toString():
        console.log("Types.CREATING");
        return {
          ...state,
          operation: Operations.CREATING,
          selectedNodeLines: undefined,
          selectedNode: undefined
        };
      case Types.CREATED.toString(): {
        console.log("Types.CREATED");
        let newMyGlossaryState = state.myGlossaryState;
        let newMyProjectState = state.myProjectState;
        let newMyGlossary = state.myGlossary;
        let newMyProject = state.myProject;

        if (
          state.myProjectState &&
          myStates.SETTING.toString() == state.myProjectState.toString()
        ) {
          newMyProjectState = myStates.SET;
          newMyProject = action.payload;
        } else if (
          state.myGlossaryState &&
          myStates.SETTING.toString() == state.myGlossaryState.toString()
        ) {
          newMyGlossaryState = myStates.SET;
          newMyGlossary = action.payload;
        }
        return {
          ...state,
          operation: Operations.CREATED,
          myGlossaryState: newMyGlossaryState,
          myProjectState: newMyProjectState,
          myGlossary: newMyGlossary,
          myProject: newMyProject,
          selectedNodeLines: undefined,
          selectedNode: undefined
        };
      }
      case Types.NODE_SELECTED.toString(): {
        let newMyGlossaryState = state.myGlossaryState;
        let newMyProjectState = state.myProjectState;
        let newMyGlossary = state.myGlossary;
        let newMyProject = state.myProject;
        let newSelectedNode = state.selectedNode;
        if (
          state.myProjectState &&
          myStates.SETTING.toString() == state.myProjectState.toString()
        ) {
          newMyProjectState = myStates.SET;
          newMyProject = action.payload;
        } else if (
          state.MyGlossaryState &&
          myStates.SETTING.toString() == state.MyGlossaryState.toString()
        ) {
          newMyGlossaryState = myStates.SET;
          newMyGlossary = action.payload;
        } else {
          newSelectedNode = action.payload;
        }
        return {
          ...state,
          myGlossaryState: newMyGlossaryState,
          myProjectState: newMyProjectState,
          myGlossary: newMyGlossary,
          myProject: newMyProject,
          selectedNode: newSelectedNode,
          selectedNodeLines: undefined,
        };
      }
      case Types.SEARCHING.toString():
        return {
          ...state,
          operation: Operations.SEARCHING,
          selectedNode: undefined,
          selectedNodeLines: undefined,
        };
      case Types.SEARCHED.toString():
        return {
          ...state,
          operation: Operations.SEARCHED,
          selectedNodeLines: undefined,
          selectedNode: undefined
        };
      case Types.REFRESH.toString():
        return {
          ...state,
          operation: Operations.REFRESH,
          selectedNodeLines: undefined,
          selectedNode: undefined,
        };
      case Types.DELETING.toString():
        return {
          ...state,
          operation: Operations.DELETING,
          selectedNodeLines: undefined,
        };
      case Types.SET_CURRENT_NODE_TYPE.toString(): {
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
          selectedNodeLines: undefined,
        };
      }
      case Types.UPDATING_NODE.toString():
        return {
          ...state,
          selectedNodeLines: undefined,
        };
      case Types.UPDATE_NODE_LINES.toString():
        return {
          ...state,
          selectedNodeLines: action.payload, // payload is the selected Node Lines
          selectedNode: undefined
        };
      case Types.UPDATE_NODELINES_CANCELLED.toString():
        return {
          ...state,
          selectedNodeLines: undefined,
          selectedNode: undefined
        };
      case Types.UPDATE_CANCELLED.toString():
        return {
          ...state,
          selectedNodeLines: undefined,
          selectedNode: undefined
        };
      case Types.RESET_SELECTED.toString():
        return {
          ...state,
          selectedNodeLines: undefined,
          selectedNode: undefined
        };
      case Types.RESET.toString():
        return initialiseState();
      default:
        return state;
    }
  }
  // The following do methods result in actions on the reducer, that will cause state changes.

  const doSelectedNode = (nodeIn) => {
    dispatch({ type: Types.NODE_SELECTED, payload: nodeIn });
  };
  const doUpdatingAction = (nodeIn) => {
    dispatch({ type: Types.UPDATING_NODE, payload: nodeIn });
  };
  const doUpdateNodeAction = (nodeIn) => {
    dispatch({ type: Types.UPDATE_NODE, payload: nodeIn });
  };
  const doUpdateNodeLines = (lines) => {
    dispatch({ type: Types.UPDATE_NODE_LINES, payload: lines });
  };

  /**
   * About to set my glossary
   */
  const doSettingMyGlossary = () => {
    console.log("settingMyGlossary");
    dispatch({ type: Types.SETTING_MY_GLOSSARY });
  };
  /**
   * About to set my project
   */
  const doSettingMyProject = () => {
    // if myProject is not set then this is a create
    dispatch({ type: Types.SETTING_MY_PROJECT });
  };
  /**
   * Is setup complete - i.e. have we set up the current glossary we want to author in and were not currently editting current glossary or current project
   */
  const isSetupComplete = () => {
    return (
      myGlossaryState && myGlossaryState.toString() == myStates.SET.toString()
    );
  };

  const doCreatingAction = () => {
    console.log("doCreatingAction");
    dispatch({ type: Types.CREATING });
  };

  const doCreatedAction = (node) => {
    dispatch({ type: Types.CREATED, payload: node });
  };

  const doSearchingAction = () => {
    console.log("doSearchingAction");
    dispatch({ type: Types.SEARCHING });
  };
  const doSearchedAction = () => {
    console.log("doSearchedAction");
    dispatch({ type: Types.SEARCHED });
  };
  const doRefreshSearchAction = () => {
    console.log("doRefreshSearchAction");
    dispatch({ type: Types.REFRESH });
  };
  const doCancelUpdate = () => {
    console.log("doCancelUpdate");
    dispatch({ type: Types.UPDATE_CANCELLED });
  };
  const doCancelUpdateNodeLines = () => {
    console.log(" doCancelUpdateNodeLines");
    dispatch({ type: Types.UPDATE_NODELINES_CANCELLED });
  };

  const doDeletingAction = () => {
    console.log("setDeletingAction");
    dispatch({ type: Types.DELETING });
  };
  const doResetSelectedAction = () => {
    console.log("doResetSelectedAction");
    dispatch({ type: Types.RESET_SELECTED });
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
      myGlossaryState &&
      myGlossaryState.toString() == myStates.SETTING.toString() &&
      currentNodeType &&
      currentNodeType.key == "glossary";

    console.log("currentNodeType" + currentNodeType);
    console.log("flag " + flag);
    return flag;
  };
  /**
   * We are in the process of editting my project
   */
  const isEdittingMyProject = () => {
    const flag =
      myProjectState &&
      myProjectState.toString() == myStates.SETTING.toString() &&
      currentNodeType &&
      currentNodeType.key == "project";
    console.log("currentNodeType" + currentNodeType);
    console.log("flag " + flag);
    return flag;
  };
  const isRefreshSearchOperation = () => {
    return operation && Operations.REFRESH.toString() == operation.toString();
  };
  const isDeletingOperation = () => {
    return operation && Operations.DELETING.toString() == operation.toString();
  };
  const isGettingCurrentNodeLines = () => {
    return (
      operation &&
      Operations.GETTING_CURRENT_NODE_LINES.toString() == operation.toString()
    );
  };
  const isUndefinedOperation = () => {
    return !operation || Operations.NONE.toString() == operation.toString();
  };
  const showSearchComponent = () => {
    let show = false;
    if (operation) {
      const operationStr = operation.toString();
      if (
        operationStr == Operations.SEARCHING.toString() ||
        operationStr == Operations.SEARCHED.toString()
      ) {
        show = true;
      }
    }
    return show;
  };
  const showCreatingNodeComponent = () => {
    let show = false;
    if (operation) {
      const operationStr = operation.toString();
      if (operationStr == Operations.CREATING.toString()) {
        show = true;
      }
    }
    return show;
  };
  const showCreatedNodeComponent = () => {
    let show = false;
    if (operation) {
      const operationStr = operation.toString();
      if (operationStr == Operations.CREATED.toString()) {
        show = true;
      }
    }
    return show;
  };

  return (
    <GlossaryAuthorContext.Provider
      value={{
        // exposed state
        currentNodeType,
        selectedNode,
        selectedNodeLines,
        myProject,
        myGlossary,
        // do methods requesting actions
        doSetNodeType,
        doSettingMyGlossary,
        doSettingMyProject,
        doCancelUpdate,
        doCreatingAction,
        doCreatedAction,
        doSearchingAction,
        doSearchedAction,
        doRefreshSearchAction,
        doDeletingAction,
        doSelectedNode,
        doUpdateNodeLines,
        doCancelUpdateNodeLines,
        doUpdatingAction,
        doUpdateNodeAction,
        doResetSelectedAction,
        // show methods interrogate whether a component should be shown
        showSearchComponent,
        showCreatingNodeComponent,
        showCreatedNodeComponent,
        // is methods interrogate state
        isEdittingMyGlossary,
        isEdittingMyProject,
        isRefreshSearchOperation,
        isDeletingOperation,
        isUndefinedOperation,
        isSetupComplete,
        isGettingCurrentNodeLines,
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
