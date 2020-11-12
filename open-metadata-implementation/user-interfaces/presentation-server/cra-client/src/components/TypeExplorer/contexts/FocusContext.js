/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { createContext, useContext, useState, useEffect }   from "react";

import PropTypes                                                   from "prop-types";

import { TypesContext }                                            from "./TypesContext";



export const FocusContext = createContext();

export const FocusContextConsumer = FocusContext.Consumer;




const FocusContextProvider = (props) => {

  const typesContext = useContext(TypesContext);

  /*
   * The focus state tracks the user's focus on an entity type. The type name is stored. It is initially empty... 
   */
  const [focus, setFocus]              = useState("");

  /*
   * view object is initially empty and tracks the user's attention - which may be a relationship or classification
   * type related to the the focus entity type. The view state stores an object containing the type name and 
   * category.
   *    { typeName  : string ,
   *      category  : string }
   * 
   * where category is { "Entity" | "Relationship" | "Classification" | "Enum" }
   * 
   * View is initially empty.....
   */
  const [view, setView]                = useState({ typeName : "" , catgeory : ""});

  const [prevView, setPrevView]        = useState({ typeName : "" , catgeory : ""});


  /*
   * When the view is changed - it does not alter the focus. However, it does reset
   * the other view selector. Therfore, the relationship and classification type 
   * selectors are cross-coupled - selection of one clears the other.
   */
  const typeSelected = (category, typeName) => {

    switch (category) {
      case "Entity":
        setFocus(typeName);
        setView({ typeName : typeName , category : category });
        break;
      case "Relationship":
        setView({ typeName : typeName , category : category });
        break;
      case "Classification":
        setView({ typeName : typeName , category : category });
        break;
      case "Enum":
        setPrevView(view);  // Only required on Enum Type display.
        setView({ typeName : typeName , category : category });
        break;
    }
  };


  useEffect(
    () => {
      /*
       * If types have been reloaded we want to reset our focus and view selections.
       */
      setFocus("");
      setView({ typeName : "" , catgeory : ""});
    },

    [typesContext.tex]
  )

  return (
    <FocusContext.Provider
      value={{
        focus,
        setFocus,
        view,
        setView,
        prevView,
        setPrevView,
        typeSelected
      }}>
     {props.children}
    </FocusContext.Provider>
  );
};

FocusContextProvider.propTypes = {
  children: PropTypes.node
};

export default FocusContextProvider;

