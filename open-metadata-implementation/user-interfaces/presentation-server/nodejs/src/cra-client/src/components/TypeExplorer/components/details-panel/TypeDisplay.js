/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }      from "react";

import { FocusContext }           from "../../contexts/FocusContext";

import PropTypes                  from "prop-types";

import EntityTypeDisplay          from "./EntityTypeDisplay";

import RelationshipTypeDisplay    from "./RelationshipTypeDisplay";

import ClassificationTypeDisplay  from "./ClassificationTypeDisplay";

import EnumTypeDisplay            from "./EnumTypeDisplay";




export default function TypeDisplay() {


  const focusContext = useContext(FocusContext);

  /*
   * Until there is a focus type display an advisory message
   * Once there is a focus type, display the type selected for the current view category (not simply the focus)
   */

  if (focusContext.view.typeName === "") {

    /* 
     * No entity type has been selected as the focus - display an 'empty' message
     */
    return <p>Type information will appear here when a type is selected</p>    

  }
  else {

    /*
     * Display the currently selected view type.
     */
    switch (focusContext.view.category) {

      case "Entity":
        return <EntityTypeDisplay typeName={focusContext.view.typeName} />  
        break;

      case "Relationship":
        return <RelationshipTypeDisplay typeName={focusContext.view.typeName} />  
        break;

      case "Classification":
        return <ClassificationTypeDisplay typeName={focusContext.view.typeName} />  
        break;

      case "Enum":
        return <EnumTypeDisplay typeName={focusContext.view.typeName} />
        break;

    }
  }
}


TypeDisplay.propTypes = {
  children: PropTypes.node 
};