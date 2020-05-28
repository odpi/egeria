/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { createContext, useContext, useState }   from "react";

import PropTypes                                        from "prop-types";

import { RepositoryServerContext }                      from "./RepositoryServerContext";




export const TypesContext = createContext();

export const TypesContextConsumer = TypesContext.Consumer;






const TypesContextProvider = (props) => {

  const repositoryServerContext    = useContext(RepositoryServerContext);

  const [tex, setTex]              = useState({});    // tex object is initially empty



  /*
   * loadTypeInfo function is an asynchronous funcition that triggers loading of types and (in _loadTypeInfo) sets the state for tex, 
   * which can then be accessed by getter functions below
   */
  const loadTypeInfo = () => {

    //console.log("TypesContextProvider: Retrieve the types from the repository server...");      
    repositoryServerContext.repositoryPOST("types", null, _loadTypeInfo);
  };

  const _loadTypeInfo = (json) => {
    
    if (json !== null) {
      let typeExplorer = json.typeExplorer;
      if (typeExplorer !== null) {
        console.log("loadTypeInfo: got back typeExplorer with enums..."+typeExplorer);
        setTex(typeExplorer);
        return;
      }
    }   
    // On failure of any of the above...     
    alert("Could not get types from repository server");
  }
  
        



  /*
   * Helper function to retrieve entities from tex
   */
  const getEntityTypes = () => {
    if (tex !== undefined && tex.entities !== undefined) {  
      return tex.entities;
    }
    else {
      //console.log("Could not retrieve entity types: Type information has not been loaded");
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
    //console.log("Could not retrieve relationship types: Type information has not been loaded");
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
    //console.log("Could not retrieve classification types: Type information has not been loaded");
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
    //console.log("Could not retrieve entity type: Type information has not been loaded");
    return null;
  }
}

const getRelationshipType = (typeName) => {
  if (tex !== undefined && tex.relationships !== undefined) {  
    return tex.relationships[typeName];
  }
  else {
    //console.log("Could not retrieve relationship type: Type information has not been loaded");
    return null;
  }
}

const getClassificationType = (typeName) => {
  if (tex !== undefined && tex.classifications !== undefined) {  
    return tex.classifications[typeName];
  }
  else {
    //console.log("Could not retrieve classification type: Type information has not been loaded");
    return null;
  }
}

const getEnumType = (typeName) => {
  if (tex !== undefined && tex.enums !== undefined) {  
    return tex.enums[typeName];
  }
  else {
    //console.log("Could not retrieve enum type: Type information has not been loaded");
    return null;
  }
}



 
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
        getEnumType         
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

