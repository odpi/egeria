/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }             from "react";

import PropTypes                         from "prop-types";

import { TypesContext }                    from "../../contexts/TypesContext";

import { FocusContext }                    from "../../contexts/FocusContext";

import ClassificationPropertiesDisplay   from "./ClassificationPropertiesDisplay";

import "./details-panel.scss";




export default function EntityClassificationsDisplay(props) {

  const explorer           = props.expl;

  const typesContext       = useContext(TypesContext);

  const focusContext       = useContext(FocusContext);



  /*
   * Marshall a map of classifications to display 
   */
  let classificationEntries = {};
    
  // Inherited attributes
  const inheritedClassificationNames = explorer.inheritedClassificationNames;
  if (inheritedClassificationNames !== undefined) {
    const inheritedClassificationNamesSorted = inheritedClassificationNames.sort();
    inheritedClassificationNamesSorted.forEach(inheritedClassificationName => {
      //console.log("inherited classification: "+inheritedClassificationName);
      classificationEntries[inheritedClassificationName] = {};
      classificationEntries[inheritedClassificationName].inherited = true;
    });
  }

  // Local classifications
  const classificationNames = explorer.classificationNames;
  if (classificationNames !== undefined) {
    const classificationNamesSorted = classificationNames.sort();
    classificationNamesSorted.forEach(classificationName => {
      //console.log("local classification: "+classificationName);
      classificationEntries[classificationName]={};
      classificationEntries[classificationName].inherited=false;
    });
  }




  const formatClassification = (classificationName, classification) => {
    let formattedClassification;
    
    if (classification.inherited) {      
      
      formattedClassification = (
        <div>
          <button className="collapsible" onClick={flipSection}> <span className="italic">{classificationName}</span> </button>
          <div className="content">
            Attributes: 
            <ClassificationPropertiesDisplay expl={typesContext.getClassificationType(classificationName)} />
            <button className="linkable" id={classificationName} onClick={classificationLinkHandler}> More Details </button>
          </div>
        </div>
      );   
    }
    else {
      
      formattedClassification = (
        <div>
          <button className="collapsible" onClick={flipSection}> {classificationName} </button>
          <div className="content">
            Attributes: 
            <ClassificationPropertiesDisplay expl={typesContext.getClassificationType(classificationName)} />
            <button className="linkable" id={classificationName} onClick={classificationLinkHandler}> More Details </button>
          </div>
        </div>
      );   
    }
    
    return formattedClassification
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

  const classificationLinkHandler = (evt) => {
    const typeName = evt.target.id;
    focusContext.typeSelected("Classification",typeName);
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
