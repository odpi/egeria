/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useEffect }       from "react";

import PropTypes                              from "prop-types";

import { InteractionContext }                 from "../../contexts/InteractionContext";

import "./auditlog.scss";


export default function ServerRunHistoryHandler(props) {

  const interactionContext    = useContext(InteractionContext);

 
  const history              = props.history;
  const serverName           = props.serverName;


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

    if (!history || !serverName || Object.keys(history).length === 0) {

      /* 
       * There is nothing to display... 
       */
     
      dialogDisplay = (
      
      <div className="dialog-text">
            
        <p>
        There is no run history information to display.
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
       * There is run history information to display... 
       */

      /*
       * The run history is a list of strings
       */

      let resultsDisplay = (         

        <ul>
          {serverRunHistory(history)}
        </ul>
      );
 
      dialogDisplay = (
      
        <div className="dialog-text">
          
          <p  className="dialog-text">
          The run history for server {serverName} contains the following information:
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

  const serverRunHistory = (history) => {

    /*
     * Use the name to index into the map in sorted order and display cohort
     */
    let historyReport = history.map( (histItem) => 
      <li className="details-sublist-item" key={histItem.startTime}> 
        {formatItem(histItem)}
      </li>
    );

    return historyReport;
  }

  const formatItem = (item) => {
    return (
      <div>
        <ul className="details-sublist">
          <li className="details-sublist-item" key={item.startTime}>Start Time : {item.startTime}</li>
          <li className="details-sublist-item" key={item.endTime}>End Time : {item.endTime}</li>  
        </ul>
      </div>
    );
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

ServerRunHistoryHandler.propTypes = {  
  status               : PropTypes.string,
  onCancel             : PropTypes.func.isRequired, 
  onSubmit             : PropTypes.func.isRequired, 
  history              : PropTypes.object,
  serverName           : PropTypes.string
   
};
