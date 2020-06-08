/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React                  from "react";

import PropTypes              from "prop-types";



import "./details-panel.scss";


export default function RelationshipPropertiesDisplay(props) {

  const explorer           = props.expl;


    /*
     * Marshall a map of attributes to display 
     */
    let attributeEntries = {};
    
     // Inherited attributes
    const inheritedProps = explorer.inheritedAttributes;
    if (inheritedProps !== undefined) {
      const inheritedPropsSorted = inheritedProps.sort();
      inheritedPropsSorted.forEach(inheritedProp => {
          console.log("inherited attribute: "+inheritedProp.attributeName);
          attributeEntries[inheritedProp.attributeName] = {};
          attributeEntries[inheritedProp.attributeName].inherited = true;
          attributeEntries[inheritedProp.attributeName].attributeTypeName = inheritedProp.attributeType.name;
      });
    }

    // Local attributes
    const localProps = explorer.relationshipDef.propertiesDefinition;
    if (localProps !== undefined) {
      const localPropsSorted = localProps.sort();
      localPropsSorted.forEach(localProp => {
          console.log("local attribute: "+localProp.attributeName);
          attributeEntries[localProp.attributeName] = {};
          attributeEntries[localProp.attributeName].inherited = false;
          attributeEntries[localProp.attributeName].attributeTypeName = localProp.attributeType.name;
      });
    }

  
  const formatAttribute = (attributeName, attribute) => {
    let formattedAttribute;
    if (attribute.inherited) {
      formattedAttribute = <div> <span className="italic">{attributeName}</span> : {attribute.attributeTypeName}</div>;      
    }
    else {
      formattedAttribute = <div>{attributeName} : {attribute.attributeTypeName}</div>;   
    }
    return formattedAttribute
  };

  


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
      <ul className="details-sublist">
        <li className="details-sublist-item"> none </li>
      </ul>
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

RelationshipPropertiesDisplay.propTypes = {
  expl: PropTypes.object 
};
