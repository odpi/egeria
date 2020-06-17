/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }              from "react";

import PropTypes                           from "prop-types";

import { TypesContext }                    from "../../contexts/TypesContext";

import { FocusContext }                    from "../../contexts/FocusContext";

import RelationshipPropertiesDisplay       from "./RelationshipPropertiesDisplay";

import "./details-panel.scss";



export default function EntityRelationshipsDisplay(props) {

  const typesContext = useContext(TypesContext);

  const focusContext = useContext(FocusContext);

  const explorer           = props.expl;


  /*
   * Marshall a map of relationships to display 
   */
  let relationshipEntries = {};
    
  // Inherited attributes
  const inheritedRelNames = explorer.inheritedRelationshipNames;
  if (inheritedRelNames !== undefined) {
    const inheritedRelNamesSorted = inheritedRelNames.sort();
    inheritedRelNamesSorted.forEach(inheritedRelName => {
      //console.log("inherited relationship: "+inheritedRelName);
      relationshipEntries[inheritedRelName] = {};
      relationshipEntries[inheritedRelName].inherited = true;
    });
  }

  // Local relationships
  const relationshipNames = explorer.relationshipNames;
  if (relationshipNames !== undefined) {
    const relationshipNamesSorted = relationshipNames.sort();
    relationshipNamesSorted.forEach(relationshipName => {
      //console.log("local relationship: "+relationshipName);
      relationshipEntries[relationshipName]={};
      relationshipEntries[relationshipName].inherited=false;
    });
  }


  
  
  
  const formatRelationship = (relationshipName, relationship) => {
    let formattedRelationship;
    
    if (relationship.inherited) {      
      
      formattedRelationship = (
        <div>
          <button className="collapsible" onClick={flipSection}> <span className="italic">{relationshipName}</span> </button>
          <div className="content">
            Attributes: { !typesContext.getRelationshipType(relationshipName).relationshipDef.propertiesDefinition ? "none" :
              <RelationshipPropertiesDisplay expl={typesContext.getRelationshipType(relationshipName)} />}
            <div className="v-spreader">
              <button className="linkable" id={relationshipName} onClick={relationshipLinkHandler}> More Details </button>
            </div>
          </div>
        </div>
      );   
    }
    else {
      
      formattedRelationship = (
        <div>
          <button className="collapsible" onClick={flipSection}> {relationshipName} </button>
          <div className="content">
            Attributes: { !typesContext.getRelationshipType(relationshipName).relationshipDef.propertiesDefinition ? "none" :
              <RelationshipPropertiesDisplay expl={typesContext.getRelationshipType(relationshipName)} />}
            <div className="v-spreader">
              <button className="linkable" id={relationshipName} onClick={relationshipLinkHandler}> More Details </button>
            </div>
          </div>
        </div>
        
      );   
    }
    
    return formattedRelationship
  };


  const flipSection = (evt) => {
    // Important to use currentTarget (not target) - because we need to operate relative to the button,
    // which is where the handler is defined, in order for the content section to be the sibling.
    const element = evt.currentTarget;
    element.classList.toggle("active");
    const content = element.nextElementSibling;
    if (content.style.display === "block") {
        content.style.display = "none";
    } else {
        content.style.display = "block";
    }
  };

  const relationshipLinkHandler = (evt) => {
    const typeName = evt.target.id;
    focusContext.typeSelected("Relationship",typeName);
  };



  let relationships;

  
  
  const expandRelationships = (relationshipEntries) => {

    const relationshipNamesUnsorted = Object.keys(relationshipEntries);
    const relationshipNamesSorted = relationshipNamesUnsorted.sort();

    let attributeList = relationshipNamesSorted.map( (relName) => 
        <li className="details-sublist-item" key={relName}> 
           {formatRelationship(relName, relationshipEntries[relName])}                  
        </li>
        
    );

    return attributeList;
  };

  if (relationshipEntries === undefined || relationshipEntries === null || Object.keys(relationshipEntries).length == 0) {
    relationships = (
      <div>
        list is empty
      </div>
    )
  }
  else {
   
    relationships = (              
      <ul className="details-sublist">       
       {expandRelationships(relationshipEntries)}          
      </ul>
      
    );
  }

  return relationships;
}

EntityRelationshipsDisplay.propTypes = {
  expl: PropTypes.object 
};
