/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React          from "react";

import PropTypes      from "prop-types";

import "./details-panel.scss";


export default function ServerConfigServicesDisplay(props) {

  const inServices  = props.services;

  let outServices;


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



  const getServiceType = (service) => {
    return service.class;
  }
  
  const getServiceName = (service) => {
    let serviceName = null;
    switch (service.class) {
      /*
       * View Services
       */
      case "IntegrationViewServiceConfig":
        serviceName = service.viewServiceFullName;
        break;
      case "SolutionViewServiceConfig":
        serviceName = service.viewServiceFullName;
        break;
      case "ViewServiceConfig":
        alert("View service "+service.viewServiceFullName+" should be configured as either a SolutionViewService or an IntegrationViewService");
        serviceName = service.viewServiceFullName;
        break;
      /*
       * Access Services
       */
      case "AccessServiceConfig":
        serviceName = service.accessServiceFullName;
        break;
      
      default:
        console.log("getServiceName: does not yet support this type of service!");
        break;
    }
    return serviceName;
  }


  const formatService = (service) => {
    let formattedService = null;
    switch (getServiceType(service)) {
      /*
       * View Services
       */
      case "IntegrationViewServiceConfig":
        formattedService = formatIntegrationViewService(service);
        break;
      case "SolutionViewServiceConfig":
        formattedService = formatSolutionViewService(service);
        break;
      case "ViewServiceConfig":
        formattedService = formatVanillaViewService(service);
        break;
        
      /*
       * Access Services
       */
      case "AccessServiceConfig":
        formattedService = formatAccessService(service);
        break;

      default:
        console.log("formatService: does not yet support this type of service!");
        break;
    }
    return formattedService;
  }

  const formatIntegrationViewService = (svc) => {
    return (
      <div>
        <ul>
          <li className="details-sublist-item">Name : {svc.viewServiceName ? svc.viewServiceName : <i>blank</i>}</li>
          <li className="details-sublist-item">Full Name : {svc.viewServiceFullName ? svc.viewServiceFullName : <i>blank</i>}</li>
          <li className="details-sublist-item">Description : {svc.viewServiceDescription ? svc.viewServiceDescription : <i>blank</i>}</li>
          <li className="details-sublist-item">Operational Status : {svc.viewServiceOperationalStatus ? svc.viewServiceOperationalStatus : <i>blank</i>}</li>
          <li className="details-sublist-item">URL Marker : {svc.viewServiceURLMarker ? svc.viewServiceURLMarker : <i>blank</i>}</li>
          <li className="details-sublist-item">Wiki : {svc.viewServiceWiki ? svc.viewServiceWiki : <i>blank</i>}</li>
          <li className="details-sublist-item">Options : {svc.viewServiceOptions ? svc.viewServiceOptions : <i>blank</i>}</li>
          <li className="details-sublist-item">Resource Endpoints : {svc.resourceEndpoints ? formatResourceEndpointList(svc.resourceEndpoints) : <i>blank</i>}</li>
        </ul>
      </div>
    );
  }

  /* The case of omagserverName and omagserverPlatformRootURL is not consistent with the other fields but that
   * is how these fields appear in the solution view service configs. Needs a Git issue to resolve
   */
  const formatSolutionViewService = (svc) => {
    return (
      <div>
        <ul>
          <li className="details-sublist-item">Name : {svc.viewServiceName ? svc.viewServiceName : <i>blank</i>}</li>
          <li className="details-sublist-item">Full Name : {svc.viewServiceFullName ? svc.viewServiceFullName : <i>blank</i>}</li>
          <li className="details-sublist-item">Description : {svc.viewServiceDescription ? svc.viewServiceDescription : <i>blank</i>}</li>
          <li className="details-sublist-item">Operational Status : {svc.viewServiceOperationalStatus ? svc.viewServiceOperationalStatus : <i>blank</i>}</li>
          <li className="details-sublist-item">URL Marker : {svc.viewServiceURLMarker ? svc.viewServiceURLMarker : <i>blank</i>}</li>
          <li className="details-sublist-item">Wiki : {svc.viewServiceWiki ? svc.viewServiceWiki : <i>blank</i>}</li>
          <li className="details-sublist-item">Options : {svc.viewServiceOptions ? svc.viewServiceOptions : <i>blank</i>}</li>
          <li className="details-sublist-item">OMAG Server Name : {svc.omagserverName ? svc.omagserverName : <i>blank</i>}</li>
          <li className="details-sublist-item">OMAG Server Platform Root URL : {svc.omagserverPlatformRootURL ? svc.omagserverPlatformRootURL : <i>blank</i>}</li>
        </ul>
      </div>
    );
  }


  /* Each view service should be configured as one of the above types. If a (vanilla) view service is encountered
   * display what is possible and include a suitable message advising that the configuration is updated
   */
  const formatVanillaViewService = (svc) => {
    return (
      <div>
        <ul>
          <li className="details-sublist-item">Name : {svc.viewServiceName ? svc.viewServiceName : <i>blank</i>}</li>
          <li className="details-sublist-item">Full Name : {svc.viewServiceFullName ? svc.viewServiceFullName : <i>blank</i>}</li>
          <li className="details-sublist-item">Description : {svc.viewServiceDescription ? svc.viewServiceDescription : <i>blank</i>}</li>
          <li className="details-sublist-item">Operational Status : {svc.viewServiceOperationalStatus ? svc.viewServiceOperationalStatus : <i>blank</i>}</li>
          <li className="details-sublist-item">URL Marker : {svc.viewServiceURLMarker ? svc.viewServiceURLMarker : <i>blank</i>}</li>
          <li className="details-sublist-item">Wiki : {svc.viewServiceWiki ? svc.viewServiceWiki : <i>blank</i>}</li>
          <li className="details-sublist-item">Options : {svc.viewServiceOptions ? svc.viewServiceOptions : <i>blank</i>}</li>
          <li className="details-sublist-item-bold">To see full details, configure this view service as either an IntegrationViewService or SolutionViewService</li>
        </ul>
      </div>
    );
  }

  const formatAccessService = (svc) => {
    return (
      <div>
        <ul>
          <li className="details-sublist-item">Name : {svc.accessServiceName ? svc.accessServiceName : <i>blank</i>}</li>
          <li className="details-sublist-item">Full Name : {svc.accessServiceFullName ? svc.accessServiceFullName : <i>blank</i>}</li>
          <li className="details-sublist-item">Description : {svc.accessServiceDescription ? svc.accessServiceDescription : <i>blank</i>}</li>
          <li className="details-sublist-item">Operational Status : {svc.accessServiceOperationalStatus ? svc.accessServiceOperationalStatus : <i>blank</i>}</li>
          <li className="details-sublist-item">URL Marker : {svc.accessServiceURLMarker ? svc.accessServiceURLMarker : <i>blank</i>}</li>
          <li className="details-sublist-item">Wiki : {svc.accessServiceWiki ? svc.accessServiceWiki : <i>blank</i>}</li>
          <li className="details-sublist-item">Options : {svc.accessServiceOptions ? svc.accessServiceOptions : <i>blank</i>}</li>
          <li className="details-sublist-item">In Topic Connection : {svc.accessServiceInTopic ? formatAccessServiceTopic(svc.accessServiceInTopic) : <i>blank</i>}</li>
          <li className="details-sublist-item">Out Topic Connection : {svc.accessServiceOutTopic ? formatAccessServiceTopic(svc.accessServiceOutTopic) : <i>blank</i>}</li>
        </ul>
      </div>
    );
  }

  const formatResourceEndpointList = (endpointList) => {
    return (
      <ul>
        {formattedResourceEndpoints(endpointList)}
      </ul>
    );
  }
  
  const formattedResourceEndpoints = (endpointList) => {
    let listEntries = null;
    listEntries = endpointList.map( (ept) =>   
        <li className="details-sublist-item" key={ept.serverInstanceName}> 
          {ept.resourceCategory} {ept.serverInstanceName} {ept.platformRootURL}
        </li>
    );
    return listEntries;
  }


  const formatAccessServiceTopic = (topicCfg) => {
      return (
        <div>
          <ul>
            <li className="details-sublist-item">Connector Type : {topicCfg.connectorType.displayName} </li>
            <li className="details-sublist-item">Configuration Properties : {topicCfg.configurationProperties ? formatTopicConfigurationProperties(topicCfg.configurationProperties) : <i>blank</i>}</li>   
            <li className="details-sublist-item">Endpoint : {topicCfg.endpoint && topicCfg.endpoint.address ? topicCfg.endpoint.address : <i>blank</i>}</li>
          </ul>
        </div>
      );
      
  }

  const formatTopicConfigurationProperties = (cfgProperties) => {
    return (
      <div>
        <ul>
          <li className="details-sublist-item">Producer : 
            {cfgProperties.producer ? formatBootstrapEndpoints(cfgProperties.producer) : <i>blank</i>}</li>
          <li className="details-sublist-item">Consumer : 
            {cfgProperties.consumer ? formatBootstrapEndpoints(cfgProperties.consumer) : <i>blank</i>}</li> 
        </ul>
      </div>
    );
  }

  const formatBootstrapEndpoints = (be) => {
    let ret = (
      <div>Bootstrap Servers :
        <ul>
          {formattedBootstrapEndpoints(be["bootstrap.servers"])}
        </ul>
      </div>
    );
    return ret;
  }
  
  /*
   * Bootstrap servers is a comma-separated list of host and port pairs formatted as host:port.
   */
  const formattedBootstrapEndpoints = (endpointList) => {
    let listEntries = null;
    let endpoints = endpointList.split(',');
    listEntries = endpoints.map( (ept) =>   
        <li className="details-sublist-item" key={ept}> 
          {ept}
        </li>
    );
    return listEntries;
  }



  /*
   * Sort and iterate over the services adding each to the list of services
   * to be displayed
   */
  const expandServices = (inServices) => {

    /*
     * Put services into a map keyed by service name
     */
    let serviceMap = {};
    inServices.forEach(service => {
      const serviceName = getServiceName(service);
      serviceMap[serviceName] = service;
    });

    /*
     * Sort the service names
     */
    let serviceNamesUnsorted = Object.keys(serviceMap);
    let serviceNamesSorted   = serviceNamesUnsorted.sort();
    
    /* 
     * Use the name to index into the map in sorted order and display service appropriately for the service type
     */
    let serviceList = serviceNamesSorted.map( (svcName) => 
        <li className="details-sublist-item" key={svcName}> 
          <button className="collapsible" id={svcName} onClick={flipSection}> {svcName} : </button>
          <div className="content">
           {formatService(serviceMap[svcName])}
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

ServerConfigServicesDisplay.propTypes = {
  services: PropTypes.array
};
