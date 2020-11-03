/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useEffect }       from "react";

import PropTypes                              from "prop-types";

import { InteractionContext }                 from "../../contexts/InteractionContext";

import "./auditlog.scss";


export default function ConfigLogHandler(props) {

  const interactionContext    = useContext(InteractionContext);

 
  const configLog              = props.configLog;

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

    if (!configLog || !configLog.serverName || !configLog.configAuditTrail || Object.keys(configLog.configAuditTrail).length === 0) {

      /* 
       * There is nothing to display... 
       */
     
      dialogDisplay = (
      
      <div className="dialog-text">
            
        <p>
        There is no config audit trail information to display.
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
       * There is config audit trail information to display... 
       */

      /*
       * The audit trail is a list of strings
       */

      let resultsDisplay = (         

        <ul>
          {formattedConfigLogEntries(configLog.configAuditTrail)}
        </ul>
      );
 
      dialogDisplay = (
      
        <div className="dialog-text">
          
          <p  className="dialog-text">
          The config audit trail for server {configLog.serverName} contains the following information:
          </p>    
   
          <hr></hr>
               
          <div className="history-results-area">                        
            {resultsDisplay}
          </div>

          <div className="dismiss-button-container">                     
            <button className="multiselect-button" onClick={cancelCallback}>  Cancel  </button>
            <button className="multiselect-button" onClick={submitCallback}>  OK     </button>
          </div>
            
        </div>
      
      );
    }

    interactionContext.showPortal(dialogDisplay);
  };

  const formattedConfigLogEntries = (configAuditTrail) => {
    let listEntries = null;
    if (configAuditTrail) {
      listEntries = configAuditTrail.map( (entry) =>
        <li className="details-sublist-item" key={entry}>
          {entry}
        </li>
      );
    }
    return listEntries;
  }

        
 

  /*
   * Emulate componentDidMount - to append the wrapper element
   */
  const componentDidMount = () => {    
    if (props.status === "complete") {      
      triggerPortal();
    }   
  };
  useEffect (componentDidMount ,[ props.status, props.history ]);
  
  /*
   * Render nothing - this component is invisible but controls what is displayed by the portal
   */
  return null;

}

ConfigLogHandler.propTypes = {  
  status               : PropTypes.string,
  onCancel             : PropTypes.func.isRequired, 
  onSubmit             : PropTypes.func.isRequired, 
  configLog            : PropTypes.object
   
};
