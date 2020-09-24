/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React                              from "react";

import PropTypes                          from "prop-types";

import "./details-panel.scss";



export default function PlatformServicesDisplay(props) {

  const serviceList          = props.serviceList;


  /*
   * Marshall a map of services to display 
   * The serviceList contains service objects
   * Process it into a map keyed by service name, which can then be processed in name order
   */
  let serviceMap = {};

  serviceList.forEach(svc => {
    serviceMap[svc.serviceName] = svc;
  });
    

  const formatService = (serviceName, service) => {
    let formattedService;
    
    formattedService = (
      <div>
        <button className="collapsible" onClick={flipSection}> {serviceName} </button>
        <div className="content">
          <ul>
          <li className="details-sublist-item">             
          Service Description: </li>
          <li className="details-sublist-item">
          { service.serviceDescription }</li>
          <li className="details-sublist-item">
          Service URL Marker: </li>
          <li className="details-sublist-item">
          { service.serviceURLMarker }</li>
          <li className="details-sublist-item">
          Service Wiki: </li>
          <li className="details-sublist-item">
          { service.serviceWiki } </li>
          </ul>
        </div>
      </div>
    );   

    return formattedService
  };


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

  

  let services;

  
  const expandServices = (serviceMap) => {

    const serviceNamesUnsorted = Object.keys(serviceMap);
    const serviceNamesSorted = serviceNamesUnsorted.sort();

    let attributeList = serviceNamesSorted.map( (svcName) => 
      <li className="details-sublist-item" key={svcName}>
        {formatService(svcName, serviceMap[svcName])}
      </li>
    );

    return attributeList;
  };

  if (serviceMap === undefined || serviceMap === null || Object.keys(serviceMap).length == 0) {
    services = (
      <div>
        list is empty
      </div>
    )
  }
  else {
    services = (              
      <ul className="details-sublist">       
       {expandServices(serviceMap)}          
      </ul>
    );
  }

  return services;
}

PlatformServicesDisplay.propTypes = {
  serviceList: PropTypes.array
};
