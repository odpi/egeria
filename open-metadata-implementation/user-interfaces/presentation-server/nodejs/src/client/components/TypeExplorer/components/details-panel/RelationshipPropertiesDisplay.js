/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React,  { useContext }     from "react";

import { TypesContext }           from "../../contexts/TypesContext";

import { FocusContext }           from "../../contexts/FocusContext";

import { RequestContext }         from "../../contexts/RequestContext";

import PropTypes                  from "prop-types";



import "./details-panel.scss";


export default function RelationshipPropertiesDisplay(props) {

  const typesContext       = useContext(TypesContext);

  const focusContext       = useContext(FocusContext);

  const requestContext     = useContext(RequestContext);

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
      attributeEntries[inheritedProp.attributeName].status = inheritedProp.attributeStatus;
      attributeEntries[inheritedProp.attributeName].replacedBy = inheritedProp.replacedByAttribute;
    });
  }

  /*
   * Local attributes
   */
  const localProps = explorer.relationshipDef.propertiesDefinition;
  if (localProps !== undefined) {
    const localPropsSorted = localProps.sort();
    localPropsSorted.forEach(localProp => {
      attributeEntries[localProp.attributeName] = {};
      attributeEntries[localProp.attributeName].inherited = false;
      attributeEntries[localProp.attributeName].attributeTypeName = localProp.attributeType.name;
      attributeEntries[localProp.attributeName].status = localProp.attributeStatus;
      attributeEntries[localProp.attributeName].replacedBy = localProp.replacedByAttribute;
    });
  }

  const formatAttribute = (attributeName, attribute) => {
    let formattedAttribute;
    const formattedAttributeName = formatAttributeName(attributeName, attribute);
    const formattedAttributeType = formatAttributeType(attributeName, attribute);
    formattedAttribute = <div>{formattedAttributeName} : {formattedAttributeType}
        {(attribute.status === "DEPRECATED_ATTRIBUTE") ? "  (use "+attribute.replacedBy+")" : ""}
    </div>;
    return formattedAttribute
  };

  const formatAttributeName = (attributeName, attribute) => {
    let formattedAttributeName;

    if (attribute.status === "DEPRECATED_ATTRIBUTE") {
      attributeName = "["+attributeName+"]";
    }
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
      /* Enumerated type */
      formattedAttributeType = <button className="linkable" id={attributeTypeName} onClick={enumLinkHandler}> {attributeTypeName} </button>   
    }
    else {
      /* Not an enumerated type */
      formattedAttributeType = attributeTypeName ;   
    }
    return formattedAttributeType;
  }

  const enumLinkHandler = (evt) => {
    const typeName = evt.target.id;
    focusContext.typeSelected("Enum",typeName);
  }


  


  let properties;

  
  /*
   * expandProperties
   */
  const expandProperties = (attributeEntries) => {

    const attributeNamesUnsorted = Object.keys(attributeEntries);
    const attributeNamesSorted = attributeNamesUnsorted.sort();

    const showDeprecatedAttributes = requestContext.deprecatedAttributeOption;

    let attributeList = attributeNamesSorted
        .filter( (propName) => (showDeprecatedAttributes || (attributeEntries[propName].status !== "DEPRECATED_ATTRIBUTE")))
        .map( propName => (
          <li className="details-sublist-item" key={propName}>
          {formatAttribute(propName,attributeEntries[propName])}
        </li>
        ));

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
