/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }     from "react";

import PropTypes                 from "prop-types";

import { TypesContext }          from "../../contexts/TypesContext";

import { FocusContext }          from "../../contexts/FocusContext";

import "./details-panel.scss";



export default function EntityPropertiesDisplay(props) {

  const typesContext       = useContext(TypesContext);

  const focusContext       = useContext(FocusContext);

  const explorer           = props.expl;


  /*
   * Marshall a map of attributes to display
   */
  let attributeEntries = {};
    
  /*
   * Inherited attributes
   */
  const inheritedProps = explorer.inheritedAttributes;
  if (inheritedProps !== undefined) {
    const inheritedPropsSorted = inheritedProps.sort();
    inheritedPropsSorted.forEach(inheritedProp => {
        attributeEntries[inheritedProp.attributeName] = {};
        attributeEntries[inheritedProp.attributeName].inherited = true;
        attributeEntries[inheritedProp.attributeName].attributeTypeName = inheritedProp.attributeType.name;
    });
  }

  /*
   * Local attributes
   */
  const localProps = explorer.entityDef.propertiesDefinition;
  if (localProps !== undefined) {
    const localPropsSorted = localProps.sort();
    localPropsSorted.forEach(localProp => {
      attributeEntries[localProp.attributeName] = {};
      attributeEntries[localProp.attributeName].inherited = false;
      attributeEntries[localProp.attributeName].attributeTypeName = localProp.attributeType.name;
    });
  }

  /*
   * Function for formatting an attribute
   */
  const formatAttribute = (attributeName, attribute) => {
    let formattedAttribute;
    const formattedAttributeName = formatAttributeName(attributeName, attribute);
    const formattedAttributeType = formatAttributeType(attributeName, attribute);
    formattedAttribute = <div>{formattedAttributeName} : {formattedAttributeType}</div>;   
    return formattedAttribute
  };

  const formatAttributeName = (attributeName, attribute) => {
    let formattedAttributeName;
    if (attribute.inherited) {
      formattedAttributeName = <span className="italic">{attributeName}</span> ;      
    }
    else {
      formattedAttributeName = attributeName ;   
    }
    return formattedAttributeName;
  }

  const formatAttributeType = (attributeName, attribute) => {
    const attributeTypeName = attribute.attributeTypeName;
    let formattedAttributeType;
    if (typesContext.getEnumType(attributeTypeName)) {
      /* Enumeration */
      formattedAttributeType = <button className="linkable" id={attributeTypeName} onClick={enumLinkHandler}> {attributeTypeName} </button>   
    }
    else {
      /* Not an enumeration */
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

EntityPropertiesDisplay.propTypes = {
  expl: PropTypes.object 
};
