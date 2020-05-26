/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useState, useContext }     from "react";

import { TypesContext }                    from "../../contexts/TypesContext";

import PropTypes                           from "prop-types";

import "./filter.scss"




/*
 * When FilterManager is rendered it populates the type selectors with types that are 
 * available from the TypesContext.
 * 
 * The entity and relationship type selectors allow the user to select a single type,
 * to be used on the search.
 * The classification selector allows the user to select one or more classifications,
 * that will be used in conjunction with an entity search.
 */

export default function FilterManager(props) {

  const typesContext = useContext(TypesContext);

  const [classificationsExpanded, setClassificationsExpanded] = useState(false);
 
 
  const entityTypes                 = typesContext.getEntityTypes();
  const relationshipTypes           = typesContext.getRelationshipTypes();
  const classificationTypes         = typesContext.getClassificationTypes();
  let entityTypesSorted             = null;
  let relationshipTypesSorted       = null;
  let classificationTypesSorted     = null;
  if (entityTypes) {
    entityTypesSorted = Object.keys(entityTypes).sort();
  }
  if (relationshipTypes) {
    relationshipTypesSorted = Object.keys(relationshipTypes).sort();
  }
  if (classificationTypes) {
    classificationTypesSorted = Object.keys(classificationTypes).sort();
  }
 


  const entitySelectorHandler = (e) => {    
    const typeName = e.target.value;    
    typeSelected("Entity",typeName);
  };

  const relationshipSelectorHandler = (e) => {        
    const typeName = e.target.value;   
    typeSelected("Relationship",typeName);
  };

  /*
   * The type selectors are cross-coupled - entity selection clears 
   * relationships selector and v.v.
   */
  const typeSelected = (category, typeName) => {
    
    switch (category) {
      case "Entity":
        resetRelTypeSelector();                       
        break;
      case "Relationship":
        resetEntTypeSelector();                   
        break;        
    }   
    props.typeSelected(category, typeName);   

  };

  const classificationSelectorHandler = (e) => {    
    const typeName = e.target.id;
    const checked = e.target.checked;    
    props.clsChanged(typeName, checked);    
  };
  

  

  // Reset the entity type selector
  const resetEntTypeSelector = () => {
    const selector = document.getElementById('entityTypeSelector');
    selector.value = "none";
  };

  // Reset the relationship type selector
  const resetRelTypeSelector = () => {
    const selector = document.getElementById('relationshipTypeSelector');
    selector.value = "none";
  };
  
  // Handler for classification drop-down checkbox selector
  const showCheckboxes = () => {
    var checkboxes = document.getElementById("selectlist");
    if (!classificationsExpanded && classificationTypes) {
      checkboxes.style.display = "block";
      setClassificationsExpanded(true);
    } 
    else {
      checkboxes.style.display = "none";
      setClassificationsExpanded(false);
    }
  };
  

  
 
  return (
    
    <div className="filterControls">  
                
      <label htmlFor="entityTypeSelector">Entity Types:  </label>                                                                                                  
      <select className="typeSelector" id="entityTypeSelector" name="entityTypeSelector" onChange={entitySelectorHandler}>
        {
          entityTypesSorted === null && 
          ( <option value="dummy" disabled defaultValue>No types to display</option> )
        }
        {
          entityTypesSorted !== null &&
          ( <option value="none" defaultValue>Restrict search to a selected type...</option> )
        }
        {
          entityTypesSorted !== null && 
          (
            entityTypesSorted.map(typeName => 
              ( <option key={typeName} value={typeName}> {typeName} </option> )
            )      
          )                                         
        }      
      </select>

      <br/>

      <label htmlFor="relationshipTypeSelector">Relationship Types:  </label>                                                                                                      
      <select className="typeSelector" id="relationshipTypeSelector" name="relationshipTypeSelector" onChange={relationshipSelectorHandler}>
        {
          relationshipTypesSorted === null && 
          ( <option value="dummy" disabled defaultValue>No types to display</option> )
        }
        {
          relationshipTypesSorted !== null &&
          ( <option value="none" defaultValue>Restrict search to a selected type...</option> )
        }
        {
          relationshipTypesSorted !== null && 
          (
            relationshipTypesSorted.map(typeName => 
              ( <option key={typeName} value={typeName}> {typeName} </option> )
            )      
          )                                         
        }      
      </select>

      <br/>
            
      <form>      
        <div className="multiselect">
          <label htmlFor="filterSelectBox">Classification Types:  </label>  
          <div id="filterSelectBox" className="filterSelectors">
            <div className="selectBox" onClick={showCheckboxes}>
                        
              <select className="typeSelector" id="classificationTypeSelector" name="classificationTypeSelector">
                {
                  classificationTypesSorted === null && 
                    ( <option value="dummy" disabled defaultValue>No types to display</option> )
                }
                {
                  classificationTypesSorted !== null && 
                    ( <option value="none" defaultValue>Restrict search by classifications...</option> )
                }                                 
              </select>
              <div className="overSelect">              
              </div>
            </div>
            <div id="selectlist">
              {
                classificationTypesSorted !== null &&
                (
                  <div id="checkboxes">          
                    {
                      classificationTypesSorted.map(typeName => 
                        (
                          <label htmlFor={typeName} key={typeName}>
                            <input type="checkbox" key={typeName} id={typeName} onChange={classificationSelectorHandler}/>
                              {typeName}
                          </label>
                        )
                      )
                    }      
                  </div>
                )
              }
            </div>
          </div>
        </div>
      </form>
    </div>     
  
  );

}

FilterManager.propTypes = {
  searchCategory : PropTypes.string,
  typeSelected   : PropTypes.func.isRequired,   
  clsChanged     : PropTypes.func.isRequired, 
};

