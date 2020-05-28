/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useEffect }       from "react";

import PropTypes                              from "prop-types";

import { InteractionContext }                 from "../../contexts/InteractionContext";

import "./history.scss";


export default function HistoryResultHandler(props) {

  const interactionContext    = useContext(InteractionContext);

 
  const history               = props.history;    

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

    if (!history || history.length === 0) {

      /* 
       * There is nothing to display... 
       */
     
      dialogDisplay = (
      
      <div className="dialog-text">
            
        <p>
        There is no history yet, as no operations have been performed and saved into the graph.
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
       * There is history to display... 
       */

      let resultsDisplay = (         

        <div className="table" >
          {history.map( item => (
            <div key={item.gen} className="row">
              <div className="gencolumn">
                {item.gen}
              </div>
              <div className="qrycolumn">
                <div className="query-text">
                  {item.query}
                </div>
              </div>
              <div className="inscolumn">
                <ul>
                  {item.instances.map(inst => (
                    <li key={inst.label}>{inst.category} {inst.label} ({inst.guid})</li>
                  ))}                                     
                </ul>
              </div>
            </div>
          ))}
        </div>
      );


      dialogDisplay = (
      
        <div className="dialog-text">
          
          <p  className="dialog-text">
          The following operations have been performed, with the instances added at each stage:
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

HistoryResultHandler.propTypes = {  
  status               : PropTypes.string,
  onCancel             : PropTypes.func.isRequired, 
  onSubmit             : PropTypes.func.isRequired, 
  history              : PropTypes.array
   
};