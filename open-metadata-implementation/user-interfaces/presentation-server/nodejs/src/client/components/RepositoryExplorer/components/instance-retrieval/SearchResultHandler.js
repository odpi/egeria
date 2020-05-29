/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useEffect }       from "react";

import PropTypes                              from "prop-types";

import { InteractionContext }                 from "../../contexts/InteractionContext";

import "./search-results.scss"


export default function SearchResultHandler(props) {

  const interactionContext    = useContext(InteractionContext);

  
  const searchCategory        = props.searchCategory; 
  const searchType            = props.searchType;  
  const searchText            = props.searchText;
  const searchClassifications = props.searchClassifications; 
  const serverName            = props.serverName;  
  const results               = props.results; 
  const selectCallback        = props.selectCallback;
  const setAllCallback        = props.setAllCallback;
 
  


  const resultToggled = (evt) => {
    selectCallback(evt.target.id);    
  }

  const checkAll = () => {
    setAllCallback(true);
  }

  const uncheckAll = () => {
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



  const triggerPortal = () => {

    let dialogDisplay;

    if (props.status === "pending") {

      /*
       *  Operation in progress...
       */

      dialogDisplay = (
        <div  className="dialog-text">       
          <p  className="dialog-text">
          {searchCategory} search on server {serverName} using expression &quot;{searchText}&quot;
          </p>
          <p  className="dialog-text">
          Type filter : 
            {searchType !== "" ? " "+searchType : " none"}
          </p>
          <p  className="dialog-text">
          Classification filters : 
            {searchClassifications !== null && searchClassifications.length>0 
              ? searchClassifications.map(c => <li className="details-sublist-item" key={c}> {c}  </li>)
              : " none"}
          </p>
          <p  className="status-update">
          Searching for instances.... 
          </p>
          <div className="dismiss-1-button-container">               
            <button className="multiselect-button" onClick={cancelCallback}>  Cancel  </button>         
          </div>
         </div>
      );
    }


    else if (props.status === "complete") {

      /*
       *  Operation complete...
       */

      if (results === undefined || results === null || results.length === 0) {
        
        /* 
         * There is nothing to display... 
         */
      
        dialogDisplay = (
        <div  className="dialog-text">
          <p>
          {searchCategory} search on server {serverName} using expression &quot;{searchText}&quot;
          </p>
          <p>
          Type filter : 
            {searchType !== "" ? " "+searchType : " none"}
          </p>
          <p>
          Classification filters : 
            {searchClassifications !== null && searchClassifications.length>0 
              ? searchClassifications.map(c => <li className="details-sublist-item" key={c}> {c}  </li>)
              : " none"}
          </p>
          <p className="status-update">
          No matching instances were found.  
          </p>
          <div className="dismiss-button-container">               
            <button className="multiselect-button" onClick={cancelCallback}>  Cancel  </button>
             <button className="multiselect-button" onClick={submitCallback}>  OK     </button>
          </div>
         </div>
        );
      }

      else {

        /* 
         * There are results to display... 
         */

        let resultsDisplay;
        if (searchCategory === "Entity") {
          resultsDisplay = (  
            <div>
              <p  className="dialog-text">
              <b>Entities</b>
              </p>
              <div className="search-results-list">
                {results.map(res => ( 
                 <div key={res.entityGUID}>
                   <label className="search-result-label" key={res.entityGUID}> 
                   <input type="checkbox" id={res.entityGUID} value={res.checked} onChange={resultToggled} checked={res.checked}/>
                     {res.label} ({res.entityGUID}) homed in repository {res.metadataCollectionName}          
                   </label> 
                   <br/> 
                 </div>
                ))}    
              </div>
            </div>
          );
        }
        else { // Relationships...
          resultsDisplay = (  
            <div>
              <p  className="dialog-text">
              <b>Relationships</b>
              </p>
              <div className="search-results-list">
                {results.map(res => ( 
                  <div key={res.relationshipGUID}>
                    <label  className="search-result-label" key={res.relationshipGUID}> 
                    <input type="checkbox" id={res.relationshipGUID} value={res.checked} onChange={resultToggled} checked={res.checked}/>
                      {res.label} ({res.relationshipGUID}) homed in repository {res.metadataCollectionName}          
                    </label> 
                    <br/> 
                 </div>
               ))}    
             </div>
           </div>
         );
        }


        dialogDisplay = (
         <div  className="dialog-text">
           <p  className="dialog-text">
           {searchCategory} search on server {serverName} using expression &quot;{searchText}&quot;
           </p>
           <p  className="dialog-text">
           Type filter : 
             {searchType !== "" ? " "+searchType : " none"}
           </p>
           <p  className="dialog-text">
           Classification filters : 
             {searchClassifications !== null && searchClassifications.length>0 
               ? searchClassifications.map(c => <li className="details-sublist-item" key={c}> {c}  </li>)
               : " none"}
           </p>
           <p  className="dialog-text">
           Please select instances to add to the graph.  
           </p>     
           <hr></hr>
           <div className="search-results-area">
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

    interactionContext.showPortal(dialogDisplay);
  };


  /* 
   * Emulate componentDidMount - to append the wrapper element
   */
  const componentDidMount = () => {

    if (props.status === "idle") {
      /* 
       * NO OP
       */
    }
    if (props.status === "pending") {
      triggerPortal();
    }
    if (props.status === "cancelled") {
      /*
       * NO OP
       */
    }
    if (props.status === "complete") {
      triggerPortal();
    }

    
  };
  useEffect (componentDidMount ,[props.status, props.results]);
  
  /*
   * Render nothing - this component is invisible but controls what is displayed by the portal
   */
  return null;

}

SearchResultHandler.propTypes = {  
  status                : PropTypes.string,
  onCancel              : PropTypes.func.isRequired, 
  onSubmit              : PropTypes.func.isRequired, 
  selectCallback        : PropTypes.func.isRequired, 
  setAllCallback        : PropTypes.func.isRequired,   
  results               : PropTypes.array,
  searchCategory        : PropTypes.string,
  searchText            : PropTypes.string,
  searchClassifications : PropTypes.array,
  searchType            : PropTypes.string,
  serverName            : PropTypes.string

};
