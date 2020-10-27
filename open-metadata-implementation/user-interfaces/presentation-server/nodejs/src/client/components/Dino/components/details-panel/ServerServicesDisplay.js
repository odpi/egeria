/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }               from "react";

import { ResourcesContext }                from "../../contexts/ResourcesContext";

import PropTypes                           from "prop-types";

import "./details-panel.scss";


export default function ServerServicesDisplay(props) {

  const resourcesContext = useContext(ResourcesContext);

  const inServices  = props.serviceList;
  const serverName  = props.serverName;
  
  let outServices;

 
  /*
   * As the user flips a service section, expand the service details display and add the service 
   * to the gens so that it appears in the topology diagram.
   */
  const flipServiceSection = (evt) => {

    let serviceName = evt.target.id;
    
    let requestService = false;

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
      requestService = true;
    }

    if (requestService) {
      resourcesContext.loadService(serverName, serviceName);
    }
  };



  const expandServices = (inServices) => {

    /*
     * Sort the service names
     */
    let serviceNamesSorted   = inServices.sort();
    
    /*
     * Use the name to index into the map in sorted order and display service
     */
    let serviceList = serviceNamesSorted.map( (svcName) => 
        <li className="details-sublist-item" key={svcName}> 
          <button className="collapsible" id={svcName} onClick={flipServiceSection}> Service : {svcName} </button>
          <div className="content">
             {resourcesContext.resourceExists("SERVICE_"+svcName) && <span>Service {svcName} has been added to diagram</span>}
             {!resourcesContext.resourceExists("SERVICE_"+svcName) && <span>Service {svcName} has been removed from diagram</span>}
          </div>
        </li>
    );

    return serviceList;
  };



  /*
   * Render services
   */
  if (!inServices) {
    outServices = (
      <div>
        list is empty
      </div>
    )
  }
  else {
    outServices = (              
      <ul className="type-details-container">       
       {expandServices(inServices)}          
      </ul>
      
    );
  }

  return outServices;
}

ServerServicesDisplay.propTypes = {
  serverName : PropTypes.string,
  serviceList: PropTypes.array
};
