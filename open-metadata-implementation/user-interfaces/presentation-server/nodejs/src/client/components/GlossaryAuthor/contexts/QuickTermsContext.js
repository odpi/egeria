/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { createContext, useState } from "react";
import PropTypes from "prop-types";
import getNodeType from "../components/properties/NodeTypes.js";
export const QuickTermsContext = createContext();
export const QuickTermsContextConsumer = QuickTermsContext.Consumer;
const QuickTermsContextProvider = (props) => {
  // const [creatingIndex, setCreatingIndex] = useState();
  // const [terms, setTerms] = useState([{ name: "", description: "" }]);

  console.log(
    "QuickTermsContext QuickTermsContextProvider",
    QuickTermsContextProvider
  );

  /**
   * state is the object that the reducer reduces to . It is a single object representing the current state.
   *
   */
  const {
    terms,
    creatingIndex
  } = state;


  const Types = {
    ADDING_TERM_NAME: Symbol("addingTermName"),
    ADDING_TERM_DESCRIPTION: Symbol("adding-term-description"),
    ADDING_BLANK_TERM: Symbol("adding-blank-term"),
    GETTING_TERMS: Symbol("getting-terms"),
    CREATE_SUCCEEDED: Symbol("create-succeeded"),
    CREATE_ERROR: Symbol("create-error"),
    ZERO_CREATING_INDEX: Symbol("zero-creating-index"),
    INCREMENT_CREATING_INDEX: Symbol("increment-creating-index"),
    RESET: Symbol("reset"),
  };
  Object.freeze(Types);


  function initialiseState() {
    return {
     
    };
  }


  /**
   * Reducer to allow actions to make state transitions within state. Multiple partsof state can be updated and
   * should result in one re-render. Without useReducer each state change resulted in a re-render, leading to renders on
   * partial state changes.
   */
  const [state, dispatch] = useReducer(stateReducer, initialiseState);

  function stateReducer(state, action) {
    switch (action.type.toString()) {
      case Types.ADDING_TERM_NAME.toString(): {
        let newTerms = terms;
        newTerms[action.index].name = action.newName;
        return {
          ...state,
          terms: newTerms
        };
      }
      case Types.ADDING_TERM_DESCRIPTION.toString(): {
        let newTerms = terms;
        newTerms[action.index].description = action.newDescription;
        return {
          ...state,
          terms: newTerms
        };
      }
      case Types.ADDING_BLANK_TERM.toString(): {
        const newTerms = [
          ...terms,
          {
            name: "",
            description: "",
          }
        ];

        return {
          ...state,
          terms: newTerms
        };
      }
      case Types.CREATE_SUCCEEDED.toString(): {
        let newTerms = terms;
        newTerms[action.index].status = "Success";   
        return {
          ...state,
          terms: newTerms,
          creatingIndex: creatingIndex+1
        };
      }
      case Types.CREATE_ERROR.toString(): {
        let newTerms = terms;
        newTerms[action.index].status = "Error";
        return {
          ...state,
          terms: newTerms,
          creatingIndex: creatingIndex+1
        };
      }
      case Types.ZERO_CREATING_INDEX.toString(): {

        return {
          ...state,
          creatingIndex: 0
        };
      }
      case Types.INCREMENT_CREATING_INDEX.toString(): {
        return {
          ...state,
          creatingIndex: creatingIndex+1
        };
      }
    }

  function doAddBlankTerm() {
    console.log("doAddBlankTerm");
    dispatch({ type: Types.ADD_BLANK_TERM });
  };
  function doAddTermNameAction(index, newName ) {
    console.log("doAddTermNameAction");
    dispatch({ type: Types.ADD_TERM_NAME, index: index, newName: newName });
  };
  function doAddTermDescriptionAction(index, newDescription ) {
    console.log("doAddTermDescriptionAction");
    dispatch({ type: Types.ADD_TERM_DESCRIPTION, index: index, newDescription: newDescription });
  };
  function doSuccessfulTermAction(index ) {
    console.log("doSuccessfulTermAction");
    dispatch({ type: Types.CREATE_SUCCEEDED, index: index });
  };

  function doErrorTermAction(index) {
    console.log("doErrorTermAction");
    dispatch({ type: Types.CREATE_ERROR, index: index });
  };
  function doZeroCreatingIndexAction(index) {
    console.log("doZeroCreatingIndexAction");
    dispatch({ type: Types.ZERO_CREATING_INDEX, index: index });
  };
  function doIncrementCreatingIndexAction(index) {
    console.log("doIncrementCreatingIndexAction");
    dispatch({ type: Types.INCREMENT_CREATING_INDEX, index: index });
  };
  // queries
  function getCreatingIndex() {
    console.log("getCreatingIndex");
    return creatingIndex;
  };
  function getTerms() {
    console.log("getCreatingIndex");
    return terms;
  };



  return (
    <QuickTermsContext.Provider
      value={{
        // terms,
        // resetCreatingIndex,
        doZeroCreatingIndex,
        doIncrementCreatingIndex,
        doAddBlankTerm,
        doAddTermNameAction,
        doAddTermDescriptionAction,
        doSuccessfulTermAction,
        doErrorTermAction,
        // query
        getCreatingIndex,
        getTerms,

      }}
    >
      {props.children}
    </QuickTermsContext.Provider>
  );
};
QuickTermsContextProvider.propTypes = {
  children: PropTypes.node,
};
export default QuickTermsContextProvider;
