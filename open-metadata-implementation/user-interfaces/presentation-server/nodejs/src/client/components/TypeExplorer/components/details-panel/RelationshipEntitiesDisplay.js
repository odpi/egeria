/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React                  from "react";

import PropTypes              from "prop-types";



import "./details-panel.scss";


export default function RelationshipEntitiesDisplay(props) {

  const explorer           = props.expl;

  
  
  const formatEnd = (endDef) => {
    const endEntry = (
      <ul className="details-sublist">
        <li className="details-sublist-item"> {endDef.entityType.name} </li>
        <li className="details-sublist-item"> Cardinality : {endDef.attributeCardinality} </li>
        <li className="details-sublist-item"> Attribute Name : {endDef.attributeName} </li>
      </ul>
    );
        
    return endEntry;
  };

  

  let ends;

  const expandEnds = (relDef) => {
    let endsList = (
    <div>
      <li className="end-sublist" key="end1"> 
        Entity @ end1: {formatEnd(relDef.endDef1)}                  
      </li>
      <li className="end-sublist" key="end2"> 
        Entity @ end2: {formatEnd(relDef.endDef2)}                  
      </li>
    </div>
    );

    return endsList;
  };

 
    ends = (              
      <ul className="details-sublist">       
       {expandEnds(explorer.relationshipDef)}          
      </ul>
      
    );
  

  return ends;
}

RelationshipEntitiesDisplay.propTypes = {
  expl: PropTypes.object 
};
