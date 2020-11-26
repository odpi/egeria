/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { createContext, useContext, useState, useEffect }   from "react";

import PropTypes                                                   from "prop-types";

import { RequestContext }                                          from "./RequestContext";

import { InteractionContext }                                      from "./InteractionContext";



export const TypesContext = createContext();

export const TypesContextConsumer = TypesContext.Consumer;



const TypesContextProvider = (props) => {

  const requestContext         = useContext(RequestContext);

  const interactionContext     = useContext(InteractionContext);



  /*
   * tex object is initially empty
   */
  const [tex, setTex]              = useState({});    

  const [platformName, setPlatformName] = useState("");
  const [serverName,   setServerName]   = useState("");

  /*
   * loadTypeInfo function is an asynchronous function that triggers loading of types and (in _loadTypeInfo) sets the state for tex,
   * which can then be accessed by getter functions below
   */

  /* Without parameters, the function will use the incumbent platform and server (from RequestContext)
   * This version of the function is intended for anything that requires a reload and is not initiated
   * by the user selecting a server in the ServerSelector.
   */
  const loadTypeInfoForExisting = () => {
    if (platformName !== "" && serverName !== "") {
      loadTypeInfo(serverName, platformName);
    }
    else {
      alert("No server has been selected yet");
    }
  };

  /*
   * This version of the function is called directly from ServerSelector and accepts a (potentially new)
   * platform and server name. These are remembered.
   */
  const loadTypeInfo = (serverName,platformName ) => {

    setPlatformName(platformName);
    setServerName(serverName);

    requestContext.callPOST("server", serverName,  "types", 
      { serverName        : serverName,
        platformName      : platformName,
        enterpriseOption  : requestContext.enterpriseOption,
        deprecationOption : requestContext.deprecatedTypeOption,
      }, _loadTypeInfo);
  };

  const _loadTypeInfo = (json) => {
    
    if (json !== null) {
      if (json.relatedHTTPCode === 200 ) {
        let typeExplorer = json.typeExplorer;
        if (typeExplorer !== null) {
          setTex(typeExplorer);
          return;
        }
      }
    }   
    /*
     * On failure ...     
     */
    interactionContext.reportFailedOperation("get types for server",json);
  }



  /*
   * Helper function to retrieve entities from tex
   */
  const getEntityTypes = () => {
    if (tex !== undefined && tex.entities !== undefined) {
        return tex.entities;
    }
    else {
      return null;
    }
  }

  /*
   * Helper function to retrieve relationships from tex
   */
  const getRelationshipTypes = () => {
    if (tex !== undefined && tex.relationships !== undefined) {
      return tex.relationships;
    }
    else {
      return null;
    }
  }

  /*
   * Helper function to retrieve classifications from tex
   */
  const getClassificationTypes = () => {
    if (tex !== undefined && tex.classifications !== undefined) {
      return tex.classifications;
    }
    else {
      return null;
    }
  }

  /*
   * Helper functions to retrieve specific named types from tex
   */
  const getEntityType = (typeName) => {
    if (tex !== undefined && tex.entities !== undefined) {
      return tex.entities[typeName];
    }
    else {
      return null;
    }
  }

  const getRelationshipType = (typeName) => {
    if (tex !== undefined && tex.relationships !== undefined) {
      return tex.relationships[typeName];
    }
    else {
      return null;
    }
  }

  const getClassificationType = (typeName) => {
    if (tex !== undefined && tex.classifications !== undefined) {
      return tex.classifications[typeName];
    }
    else {
      return null;
    }
  }

  const getEnumType = (typeName) => {
    if (tex !== undefined && tex.enums !== undefined) {
      return tex.enums[typeName];
    }
    else {
      return null;
    }
  }


  /*
   * Function to verify whether a type has deprecated status.
   * This function should ONLY be used for a type that is present in the explorer,
   * i.e. it is OK to invoke it from a diagram or from the type category's own
   * details display module. But it should NOT be used for associated type
   * validation - e.g. for a relationship type display to validate the status of an
   * entity type used at one end of the relationship - if the entity has been
   * deprecated it will not be in the explorer at all. In this example, the
   * relationship should use the entityType included in the relationshipEndDef.
   * A similar approach applies to classificationDef - it should use the status
   * in the TypeDefLink of the validEntityDefs entry for the entity type.
   * If this function is called and the type is not present in thge explorer it
   * will throw an exception.
   */
  const isTypeDeprecated = (cat, typeName) => {
    if (cat === "Entity") {
      if (tex && tex.entities) {
        let entityType = tex.entities[typeName];
        if (!entityType) {
           throw new Error("Entity Type "+typeName+" was not found!");
        }
        else {
          let entityDef = entityType.entityDef;
          let deprecated = entityDef.status === "DEPRECATED_TYPEDEF";
          return deprecated;
        }
     }
    }
    else if (cat === "Relationship") {
      if (tex && tex.relatiosnhips) {
        let relationshipType = tex.relationships[typeName];
        if (!relationshipType) {
          throw new Error("Relationship Type "+typeName+" was not found!");
        }
        let relationshipDef = relationshipType.relationshipDef;
        let deprecated = relationshipDef.status === "DEPRECATED_TYPEDEF";
        return deprecated;
     }
    }
    else if (cat === "Classification") {
      if (tex && tex.classifications) {
        let classificationType = tex.classifications[typeName];
        if (!classificationType) {
          throw new Error("Classification Type "+typeName+" was not found!");
        }
        let classificationDef = classificationType.classificationDef;
        let deprecated = classificationDef.status === "DEPRECATED_TYPEDEF";
        return deprecated;
      }
     }
     else {
      console.log("Type Explorer TypesContext detected unknown type category "+cat+ "for tyoe "+typeName);
      return false;
    }
  }


  /*
   * Detect when the deprecation option has changed state and trigger a reload
   * of the type information from the server
   */
  useEffect(
    () => {
      if (platformName && serverName)
        loadTypeInfoForExisting();
    },
    [requestContext.deprecatedTypeOption]
  )


  return (
    <TypesContext.Provider
      value={{
        tex,
        setTex,
        loadTypeInfo,
        _loadTypeInfo,
        getEntityTypes,
        getRelationshipTypes,
        getClassificationTypes,
        getEntityType,
        getRelationshipType,
        getClassificationType,
        getEnumType,
        isTypeDeprecated
      }}
    >
     {props.children}
    </TypesContext.Provider>
  );
};

TypesContextProvider.propTypes = {
  children: PropTypes.node
};

export default TypesContextProvider;

