/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React                  from "react";

import PropTypes              from "prop-types";



import "./details-panel.scss";


export default function EntityClassificationsDisplay(props) {

  const explorer           = props.expl;


  /*
   * Marshall a map of classifications to display 
   */
  let classificationEntries = {};
    
  // Inherited attributes
  const inheritedClassificationNames = explorer.inheritedClassificationNames;
  if (inheritedClassificationNames !== undefined) {
    const inheritedClassificationNamesSorted = inheritedClassificationNames.sort();
    inheritedClassificationNamesSorted.forEach(inheritedClassificationName => {
      console.log("inherited classification: "+inheritedClassificationName);
      classificationEntries[inheritedClassificationName] = {};
      classificationEntries[inheritedClassificationName].inherited = true;
    });
  }

  // Local classifications
  const classificationNames = explorer.classificationNames;
  if (classificationNames !== undefined) {
    const classificationNamesSorted = classificationNames.sort();
    classificationNamesSorted.forEach(classificationName => {
      console.log("local classification: "+classificationName);
      classificationEntries[classificationName]={};
      classificationEntries[classificationName].inherited=false;
    });
  }


  
  const formatClassification = (classificationName, classification) => {
    let formattedClassification;
    if (classification.inherited) {
      formattedClassification = <div> <span className="italic">{classificationName}</span> </div>;      
    }
    else {
      formattedClassification = <div>{classificationName} </div>;   
    }
    return formattedClassification
  };

  


  let classifications;

  
  
  const expandClassifications = (classificationEntries) => {

    const classificationNamesUnsorted = Object.keys(classificationEntries);
    const classificationNamesSorted = classificationNamesUnsorted.sort();

    let classificationList = classificationNamesSorted.map( (clsName) => 
        <li className="details-sublist-item" key={clsName}> 
           {formatClassification(clsName, classificationEntries[clsName])}                  
        </li>
        
    );

    return classificationList;
  };

  if (classificationEntries === undefined || classificationEntries === null || Object.keys(classificationEntries).length == 0) {
    classifications = (
      <div>
        list is empty
      </div>
    )
  }
  else {
   
    classifications = (              
      <ul className="details-sublist">       
       {expandClassifications(classificationEntries)}          
      </ul>
      
    );
  }

  return classifications;
}

EntityClassificationsDisplay.propTypes = {
  expl: PropTypes.object 
};
