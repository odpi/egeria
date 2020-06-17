/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }           from "react";

import PropTypes                       from "prop-types";

import { TypesContext }                from "../../contexts/TypesContext";

import { FocusContext }                from "../../contexts/FocusContext";

import "./details-panel.scss";



export default function EnumTypeDisplay(props) {

    const typesContext = useContext(TypesContext);

    const focusContext = useContext(FocusContext);

    const typeName     = props.typeName;

    const enumExpl     = typesContext.getEnumType(typeName);


  
  const expandValues = (elementDefs) => {
    let values = elementDefs.map( (element) => 
        <li className="details-sublist-item" key={element.ordinal}> 
           {element.ordinal} : {element.value} : {element.description}                
        </li>        
    );
    return values;
  };

  const viewLinkHandler = () => {
    focusContext.typeSelected(focusContext.prevView.category,focusContext.prevView.typeName ); 
    // TODO - could add a focusContext resetView function instead...

  }


  return (
    <div className="type-details-container">
      <br/>
      <button className="backlink" id="back-link" onClick={viewLinkHandler}> Back to {focusContext.prevView.category} {focusContext.prevView.typeName} </button>
      <br/>
      <hr/>

      <div className="type-details-item-bold">Enum Type : {typeName}</div>
      <div className="type-details-item">{enumExpl.description}</div>
      <div className="type-details-item">Possible Values : { !enumExpl.elementDefs ? "none" :
        expandValues( enumExpl.elementDefs ) }
      </div>
    </div>
  );
}

EnumTypeDisplay.propTypes = {
  typeName: PropTypes.string
};
  