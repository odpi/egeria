/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React                  from "react";

import PropTypes              from "prop-types";



import "./details-panel.scss";


export default function EntityRelationshipsDisplay(props) {

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
      console.log("inherited relationship: "+inheritedRelName);
      relationshipEntries[inheritedRelName] = {};
      relationshipEntries[inheritedRelName].inherited = true;
    });
  }

  // Local relationships
  const relationshipNames = explorer.relationshipNames;
  if (relationshipNames !== undefined) {
    const relationshipNamesSorted = relationshipNames.sort();
    relationshipNamesSorted.forEach(relationshipName => {
      console.log("local relationship: "+relationshipName);
      relationshipEntries[relationshipName]={};
      relationshipEntries[relationshipName].inherited=false;
    });
  }


  
  const formatRelationship = (relationshipName, relationship) => {
    let formattedRelationship;
    if (relationship.inherited) {
      formattedRelationship = <div> <span className="italic">{relationshipName}</span> </div>;      
    }
    else {
      formattedRelationship = <div>{relationshipName} </div>;   
    }
    return formattedRelationship
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
