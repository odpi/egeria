/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useState }                from "react";

import PropTypes                           from "prop-types";

import "./details-panel.scss";

import ServerRunHistoryHandler             from "./ServerRunHistoryHandler";


export default function ServerStatusDisplay(props) {

  const inStatus         = props.serverStatus;
  const serverName       = props.serverName;

  let outStatus;



  const [historyStatus, setHistoryStatus]     = useState("idle");
  const [history, setHistory]                 = useState({});

  
  /*
   * Handler for flopping a collapsible
   */
  const flipSection = (evt) => {
    /*
     * Use currentTarget (not target) - because we need to operate relative to the button,
     * which is where the handler is defined, in order for the content section to be the sibling.
     */
    const element = evt.currentTarget;
    element.classList.toggle("active");
    const content = element.nextElementSibling;
    if (content.style.display === "block") {
      content.style.display = "none";
    }
    else {
      content.style.display = "block";
    }
  };

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
   * Handler to render sevrer config audit trail in portal
   */
  const displayServerRunHistory = (serverName, history) => {
    setHistory({"serverName" : serverName , "history" : history });
    setHistoryStatus("complete");
  }

  const cancelHistoryModal = () => {
    setHistoryStatus("idle");
  };

  const submitHistoryModal = () => {
    setHistoryStatus("idle");
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

  const expandStatus = (inStatus) => {

    let statusDisplay = (
      <div>
        <ul className="details-sublist">
          <li className="details-sublist-item">Server Status : {inStatus.isActive ? <span>ACTIVE</span> : <span>STOPPED</span>}</li>
          <li className="details-sublist-item">Server Start Time : {inStatus.isActive ? inStatus.serverStartTime : <i>not applicable</i>}</li>
          <li className="details-sublist-item">Server End Time : {inStatus.isActive ? <i>not applicable</i>  : inStatus.serverEndTime}</li>


          <li>
          <button onClick = { () => displayServerRunHistory(serverName, inStatus.serverHistory) }>
            Server Run History
            </button>
            <ServerRunHistoryHandler   status                = { historyStatus }
                                       serverName            = { serverName }
                                       history               = { inStatus.serverHistory }
                                       onCancel              = { cancelHistoryModal }
                                       onSubmit              = { submitHistoryModal } />
          </li>
        </ul>
      </div>
    );

    return statusDisplay;
  };




  if (!inStatus) {
    outStatus = (
      <div>
        nothing to display
      </div>
    )
  }
  else {
   
    outStatus = (              
      <ul className="type-details-item">       
       {expandStatus(inStatus)}          
      </ul>
      
    );
  }

  return outStatus;
}

ServerStatusDisplay.propTypes = {
  serverName   : PropTypes.string,
  serverStatus : PropTypes.object
};
