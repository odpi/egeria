/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React                  from "react";

import PropTypes              from "prop-types";



import "./details-panel.scss";


export default function ClassificationEntitiesDisplay(props) {

  const explorer           = props.expl;

  

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
    <li className="details-sublist-item" key={vename}> 
       {vename}                 
    </li>
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
