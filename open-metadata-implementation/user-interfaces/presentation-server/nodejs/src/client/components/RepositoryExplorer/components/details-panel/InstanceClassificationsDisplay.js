/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React     from "react";

import PropTypes from "prop-types";

import InstancePropertiesDisplay from "./InstancePropertiesDisplay";

import "./details-panel.scss";



export default function InstanceClassificationsDisplay(props) {

  
  const instClassifications = props.classifications;

  let classifications;


  const expandClassifications = (inClassifications) => {
    let classificationMap = {};
    inClassifications.forEach(cls => {
      classificationMap[cls.name] = cls;
    });    
    let classificationNamesSorted = Object.keys(classificationMap).sort();
    
    let classificationList = classificationNamesSorted.map( (clsName) => 
        <li className="details-sublist-item" key={clsName}> {clsName} :
          { 
            <div><InstancePropertiesDisplay properties={classificationMap[clsName].properties} /></div>       
          } 
        </li>
        
    );

    return classificationList;
  };

  if (instClassifications === undefined || instClassifications === null || instClassifications.length ===0 ) {

    classifications = (
      <div>
        <ul className="details-sublist">           
          <li>list is empty</li>     
        </ul>        
      </div>
    )
  }

  else {
   
    classifications = (  
      <div>       
         <ul className="details-sublist">           
           {expandClassifications(instClassifications)}          
        </ul>
      </div>
    );
  }

  return classifications;
}

InstanceClassificationsDisplay.propTypes = {
  //children: PropTypes.node ,
  classifications: PropTypes.array 
};
