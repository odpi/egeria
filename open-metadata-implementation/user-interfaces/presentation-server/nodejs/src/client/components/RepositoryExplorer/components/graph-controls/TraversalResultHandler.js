/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useEffect }       from "react";

import PropTypes                              from "prop-types";

import { InteractionContext }                 from "../../contexts/InteractionContext";

import "./traversal.scss";


export default function TraversalResultHandler(props) {

  const interactionContext    = useContext(InteractionContext);

 
  const spec                  = props.spec;
  const entityTypes           = props.entityTypes; 
  const relationshipTypes     = props.relationshipTypes; 
  const classificationTypes   = props.classificationTypes; 
  const selectCallback        = props.selectCallback;
  const setAllCallback        = props.setAllCallback;
  //const submitCallback        = props.onSubmit;
  //const cancelCallback        = props.onCancel;

  

  const entityFilterChanged = (evt) => {
    console.log("Try to call selectCallback");
    selectCallback("Entity", evt.target.id);    
  }


  const relationshipFilterChanged = (evt) => {
    console.log("Try to call selectCallback");
    selectCallback("Relationship",evt.target.id);    
  }


  const classificationFilterChanged = (evt) => {
    console.log("Try to call selectCallback");
    selectCallback("Classification",evt.target.id);    
  }

  const checkAll = () => {
    console.log("Check All"); 
    setAllCallback(true);
  }

  const uncheckAll = () => {
    console.log("Uncheck All"); 
    setAllCallback(false);
  }  

  const cancelCallback = () => {
    interactionContext.hidePortal();
    props.onCancel();
  }

  const submitCallback = () => {
    interactionContext.hidePortal();
    props.onSubmit();
  }


  // TODO - consolidate the logic for the various results (emoty or not) 

  const triggerPortal = () => {

    let dialogDisplay;


    if (props.status === "pending") {

      dialogDisplay = (
      
        <div className="dialog-text">
            
          <p>
          To restrict the traversal to specific types, select the types to include.
          </p>
  
          <p>
          In each column:
          </p>
  
          <ul className="dialog-list">
            <li>If NO types are checked, there is no filtering for the category. All types are permitted.</li>
            <li>If ANY (or all) types are checked, the traversal will be restricted to the checked types.</li>
          </ul>
          
          <p>
          A neighboring entity can be reached if its entity type and connecting relationship type 
          are permitted and the neighboring entity has at least one of the checked classifications.          
          </p>
  
     
          <hr></hr>
                 
          <p  className="status-update">
          Analysing traversal...
          </p>     
  
          
          <div className="dismiss-1-button-container">                     
            <button className="multiselect-button" onClick={cancelCallback}>  Cancel  </button>          
          </div>
           
              
         </div>
        
      );
    }

    else if (props.status === "complete") {
    
    


      let resultsPresent = entityTypes && entityTypes.length > 0 ||
                       relationshipTypes && relationshipTypes.length > 0 ||
                       classificationTypes && classificationTypes.length > 0;
                      

      if (!resultsPresent) {
        /* 
         * There is nothing to display... 
         */
        console.log("TraversalResultHandler: no results, nothing to render");
     
        dialogDisplay = (
      
        <div className="dialog-text">
            
          <p>
          The attempted traversal did not find any neighbors.
          </p>
  
      
  
          <div className="dismiss-button-container">                     
            <button className="multiselect-button" onClick={cancelCallback}>  Cancel  </button>
            <button className="multiselect-button" onClick={submitCallback}>  OK     </button>
          </div>
              
         </div>
        
        );
      }
    
      else {


        let resultsDisplay = (

          <div>

          <div className="table" >
            <div className="row">
              <div className="column">
                <p><b>Entity Types</b></p>
                {entityTypes.map(type => ( 
                  <div key={type.name}>
                    <label key={type.name}> 
                      <input type="checkbox" id={type.name} value={type.checked} onChange={entityFilterChanged} checked={type.checked}/>
                      {type.name} ( {type.count} )         
                    </label> 
                    <br/> 
                  </div>
                ))}    
              </div>
              <div className="column">
                <p><b>Relationship Types</b></p>
                {relationshipTypes.map(type => ( 
                  <div key={type.name}>
                    <label key={type.name}> 
                      <input type="checkbox" id={type.name} value={type.checked} onChange={relationshipFilterChanged} checked={type.checked}/>
                      {type.name} ( {type.count} )         
                    </label> 
                    <br/> 
                  </div>
                ))}    
              </div>
              <div className="column">
                <p><b>Classification Types</b></p>
                {classificationTypes.map(type => ( 
                  <div key={type.name}>
                    <label key={type.name}> 
                      <input type="checkbox" id={type.name} value={type.checked} onChange={classificationFilterChanged} checked={type.checked}/>
                      {type.name} ( {type.count} )         
                    </label> 
                    <br/> 
                  </div>
                ))}    
              </div>

            </div>
          </div>    

          </div>

        );


        dialogDisplay = (
      
          <div className="dialog-text">
          
          <p>
          If you wish to restrict the traversal to specific types, please select the types to include. 
          </p>

          <p>
          For each category (column):
          </p>

          <ul className="dialog-list">
            <li>If NO types are checked, there is no filtering for the category. All types are permitted.</li>
            <li>If ANY (or all) types are checked, the traversal will be restricted to the checked types.</li>
          </ul>
        
          <p>
          A neighboring entity can be reached if its entity type is permitted, it has one or more of any
          required classifications and the connecting relationship type is permitted.
          </p>

   
          <hr></hr>
               
          <div className="traversal-results-area">
                            
            {resultsDisplay}
       
          </div>

        
    
          <div className="multiselect-button-container">               
            <button  className="multiselect-button" id="noneButton" onClick = { uncheckAll } >Clear All  </button>
            <button  className="multiselect-button" id="allButton" onClick = { checkAll } >Select All  </button>
          </div>

      

          <div className="dismiss-button-container">                     
            <button className="multiselect-button" onClick={cancelCallback}>  Cancel  </button>
            <button className="multiselect-button" onClick={submitCallback}>  OK     </button>
          </div>
            
        </div>
      
        );
      }

    }
 
    interactionContext.showPortal(dialogDisplay); //, () => submitCallback, () => cancelCallback);
  };


  // Emulate componentDidMount - to append the wrapper element
  const componentDidMount = () => {
    
    console.log("TRH: status is "+props.status);

    if (props.status === "idle") {
      console.log("TRH: do nothing");
    }
    if (props.status === "pending") {
      console.log("TRH: trigger portal");
      triggerPortal();
    }
    if (props.status === "cancelled") {
      console.log("TRH: do nothing");
    }
    if (props.status === "complete") {
      console.log("TRH: trigger portal");
      triggerPortal();
    }

   
  };
  useEffect (componentDidMount ,[ props.status, props.entityTypes, props.relationshipTypes, props.classificationTypes ]);
  
  /*
   * Render nothing - this component is invisible but controls what is displayed by the portal
   */
  return null;

}

TraversalResultHandler.propTypes = {  
  status               : PropTypes.string,
  onCancel             : PropTypes.func.isRequired, 
  onSubmit             : PropTypes.func.isRequired, 
  selectCallback       : PropTypes.func.isRequired, 
  setAllCallback       : PropTypes.func.isRequired, 
  spec                 : PropTypes.object,
  entityTypes          : PropTypes.array,                    
  relationshipTypes    : PropTypes.array,   
  classificationTypes  : PropTypes.array   
};