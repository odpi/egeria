/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }        from "react";

import PropTypes                    from "prop-types";

import { FocusContext }             from "../../contexts/FocusContext";

import "./details-panel.scss";




export default function ClassificationEntitiesDisplay(props) {

  const explorer           = props.expl;

  const focusContext       = useContext(FocusContext);

  
  const formattedEntityName = (name) => {

    let formattedEntity = (
      <div>
        <button className="linkable" id={name} onClick={entityLinkHandler}> {name} </button>
      </div>
    );
    return formattedEntity;
  }


  const entityLinkHandler = (evt) => {
    const typeName = evt.target.id;
    focusContext.typeSelected("Entity",typeName);
  };


  const expandEntities = (clsDef) => {

    let validEntityNames = [];

    const validEntityDefs = clsDef.validEntityDefs;
    if (validEntityDefs !== undefined) {
      validEntityDefs.forEach(validEntityDef => {
        validEntityNames.push(validEntityDef.name);
      });
    }

    const validEntityNamesSorted = validEntityNames.sort();
   
    let entityList =  validEntityNamesSorted.map( (vename) => 
      <li className="details-sublist-item" key={vename}>  {formattedEntityName(vename)}  </li>
    );

    return entityList;
  };

  let ends;

    ends = (              
      <ul className="details-sublist">       
       {expandEntities(explorer.classificationDef)}          
      </ul>
      
    );
  

  return ends;
}

ClassificationEntitiesDisplay.propTypes = {
  expl: PropTypes.object 
};
