/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }         from "react";

import PropTypes                     from "prop-types";

import { FocusContext }              from "../../contexts/FocusContext";

import "./details-panel.scss";



export default function RelationshipEntitiesDisplay(props) {

  const focusContext    = useContext(FocusContext);

  const explorer        = props.expl;

  
  const entityLinkHandler = (evt) => {
    const typeName = evt.target.id;
    focusContext.typeSelected("Entity",typeName);
  };

  const formatEnd = (endDef) => {
    const endEntry = (
      <ul className="details-sublist">
        <li className="details-sublist-item"> Entity Type : <button className="linkable"
            id={endDef.entityType.name} onClick={entityLinkHandler}>
            {endDef.entityType.name}
          </button>
        </li>
        <li className="details-sublist-item"> Cardinality : {endDef.attributeCardinality} </li>
        <li className="details-sublist-item"> Attribute Name : {endDef.attributeName} </li>
      </ul>
    );
    return endEntry;
  };

  

  let ends;

  const expandEnds = (relDef) => {
    let endsList = (
    <div className="details-sub-container">
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
