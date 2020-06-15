/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }      from "react";

import { TypesContext }           from "../../contexts/TypesContext";

import { FocusContext }           from "../../contexts/FocusContext";

import PropTypes                  from "prop-types";



import "./details-panel.scss";


export default function ClassificationPropertiesDisplay(props) {

  const typesContext       = useContext(TypesContext);

  const focusContext       = useContext(FocusContext);

  const explorer           = props.expl;


    /*
     * Marshall a map of attributes to display 
     */
    let attributeEntries = {};   

    // Local attributes
    const localProps = explorer.classificationDef.propertiesDefinition;
    if (localProps !== undefined) {
      const localPropsSorted = localProps.sort();
      localPropsSorted.forEach(localProp => {
          //console.log("local attribute: "+localProp.attributeName);
          attributeEntries[localProp.attributeName] = {};
          attributeEntries[localProp.attributeName].inherited = false;
          attributeEntries[localProp.attributeName].attributeTypeName = localProp.attributeType.name;
      });
    }

  
  /*const formatAttribute = (attributeName, attribute) => {
    let formattedAttribute;
 
    formattedAttribute = <div>{attributeName} : {attribute.attributeTypeName}</div>;   
    
    return formattedAttribute
  };*/
  const formatAttribute = (attributeName, attribute) => {
    let formattedAttribute;
    const formattedAttributeType = formatAttributeType(attributeName, attribute);
    formattedAttribute = <div>{attributeName} : {formattedAttributeType}</div>;   
    return formattedAttribute
  };

  const formatAttributeType = (attributeName, attribute) => {
    const attributeTypeName = attribute.attributeTypeName;
    let formattedAttributeType;
    if (typesContext.getEnumType(attributeTypeName)) {
      // ENUM      
      formattedAttributeType = <button className="linkable" id={attributeTypeName} onClick={enumLinkHandler}> {attributeTypeName} </button>   
    }
    else {
      // NOT AN ENUM
      formattedAttributeType = attributeTypeName ;   
    }
    return formattedAttributeType;
  }

const enumLinkHandler = (evt) => {
  const typeName = evt.target.id;
  focusContext.typeSelected("Enum",typeName);
}


  


  let properties;

  
  
  const expandProperties = (attributeEntries) => {

    const attributeNamesUnsorted = Object.keys(attributeEntries);
    const attributeNamesSorted = attributeNamesUnsorted.sort();

    let attributeList = attributeNamesSorted.map( (propName) => 
        <li className="details-sublist-item" key={propName}> 
           {formatAttribute(propName,attributeEntries[propName])}                  
        </li>
        
    );

    return attributeList;
  };

  if (attributeEntries === undefined || attributeEntries === null || Object.keys(attributeEntries).length == 0) {
    properties = (
      <div>
        list is empty
      </div>
    )
  }
  else {
   
    properties = (              
      <ul className="details-sublist">       
       {expandProperties(attributeEntries)}          
      </ul>
      
    );
  }

  return properties;
}

ClassificationPropertiesDisplay.propTypes = {
  expl: PropTypes.object 
};
