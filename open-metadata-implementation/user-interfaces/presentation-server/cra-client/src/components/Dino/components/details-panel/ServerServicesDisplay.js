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
  const serviceCat  = props.serviceCat;

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

  const formatService = (service) => {
    let formattedService = null;
    switch (serviceCat) {

      case "Integration":
        formattedService = formatIntegrationService(service);
        break;

      default:
        console.log("formatService: does not yet support this type of service!");
        break;
    }
    return formattedService;
  };

  const formatIntegrationService = (svc) => {
    return (
      <div>
        <ul>
          <li className="details-sublist-item">Name : {svc.serviceName ? svc.serviceName : <i>blank</i>}</li>
           <li className="details-sublist-item">Description : {svc.serviceDescription ? svc.serviceDescription : <i>blank</i>}</li>
           <li className="details-sublist-item">URL Marker : {svc.serviceURLMarker ? svc.serviceURLMarker : <i>blank</i>}</li>
          <li className="details-sublist-item">Wiki : {svc.serviceWiki ? svc.serviceWiki : <i>blank</i>}</li>
         </ul>
      </div>
    );
  }


  const expandServices = (inServices) => {
    let serviceList;

    /*
     * Put services into a map keyed by service name
     */
    let serviceMap = {};
    inServices.forEach(service => {
      const serviceName = service.serviceName;
      serviceMap[serviceName] = service;
    });

    /*
     * Sort the service names
     */
    let serviceNamesUnsorted = Object.keys(serviceMap);
    if (serviceNamesUnsorted) {
      let serviceNamesSorted   = serviceNamesUnsorted.sort();

      /*
       * Use the name to index into the map in sorted order and display service appropriately for the service type
       */
      serviceList = serviceNamesSorted.map( (svcName) =>
        <li className="details-sublist-item" key={svcName}> 
          <button className="collapsible" id={svcName} onClick={flipServiceSection}> Service : {svcName} </button>
          <div className="content">
           {formatService(serviceMap[svcName])}
          </div>
        </li>
      );
    }
    else {
      serviceList = null;
    }

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
  serviceList: PropTypes.array,
  serviceCat : PropTypes.string
};
