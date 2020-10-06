/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useEffect }       from "react";

import PropTypes                              from "prop-types";

import { InteractionContext }                 from "../../contexts/InteractionContext";

import "./auditlog.scss";


export default function AuditLogHandler(props) {

  const interactionContext    = useContext(InteractionContext);

 
  const auditLog              = props.auditLog;

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

    if (!auditLog || Object.keys(auditLog).length === 0) {

      /* 
       * There is nothing to display... 
       */
     
      dialogDisplay = (
      
      <div className="dialog-text">
            
        <p>
        There is no audit log information to display.
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
       * There is audit log information to display... 
       */

      let childAuditReportsSorted = auditLog.childAuditLogReports.sort(function(a,b){
        return a.reportingComponent.componentName > b.reportingComponent.componentName});

      let resultsDisplay = (         

        <div>
        
          <div className="div-bold">Originator Server Name: {auditLog.originatorProperties["Server Name"]}</div>
          <div>Originator Server Type: {auditLog.originatorProperties["Server Type"]}</div>
          <div>Originator Organization Name: {auditLog.originatorProperties["Organization Name"]}</div>
          <div>Reporting Component Name: {auditLog.reportingComponent.componentName}</div>
          <div>Conponent Wiki: {auditLog.reportingComponent.componentWikiURL}</div>
          
          <hr/>

          <div className="div-bold">Audit Log Destinations:
          </div>
          <ul>
              {auditLog.destinationsReport.logStoreReports.map(dest =>
                <li className="details-list-item" key={dest.destinationName}>
                  <div>Destination: {dest.destinationName}</div>
                  <div>Implementation Class: {dest.implementationClass}</div>
                </li>
              )}
          </ul>


          <hr/>

          <div className="div-bold">
  
            Audited Events:
          </div>
          <ul>
              {Object.keys(auditLog.severityIdentification).map(sevIdent => 
               <li className="details-list-item" key={sevIdent}> {auditLog.severityIdentification[sevIdent]} occurred {auditLog.severityCount[sevIdent]} times</li>
            )}
          </ul>
   

          <hr/>

          <div className="div-bold">Child Audit Log Reports:
          </div>
          <br/>
              <ul>
                {childAuditReportsSorted.map(child => 
          
                  child.reportingComponent.componentName !== "Enterprise Repository Connector" &&

                  <li className="details-list-item" key={child.reportingComponent.componentName}>
                    <div className="div-bold">Reporting Component: {child.reportingComponent.componentName}</div>
                    <div>Component Wiki: {child.reportingComponent.componentWikiURL}</div>
                    <div>
                      {child.severityIdentification &&
                        <ul>
                          {Object.keys(child.severityIdentification).map(sevIdent => 
                            <li key={child.reportingComponent.componentName + ":" + sevIdent}> {child.severityIdentification[sevIdent]} occurred {child.severityCount[sevIdent]} times</li>
                          )}  
                        </ul>
                      }
                    </div>
                  </li>
                  
                )}
              </ul>
        </div>

        
      );


      dialogDisplay = (
      
        <div className="dialog-text">
          
          <p  className="dialog-text">
          The audit log contains the following information:
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

AuditLogHandler.propTypes = {  
  status               : PropTypes.string,
  onCancel             : PropTypes.func.isRequired, 
  onSubmit             : PropTypes.func.isRequired, 
  auditLog             : PropTypes.object
   
};
