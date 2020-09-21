/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }               from "react";

import { ResourcesContext }                from "../../contexts/ResourcesContext";

import PropTypes                           from "prop-types";

import "./details-panel.scss";


export default function ServerCohortsDisplay(props) {

  const resourcesContext = useContext(ResourcesContext);

  const inCohortDetails  = props.cohortDetails;
  const serverName       = props.serverName;
  
  let outCohorts;

  /*
   * As the user flips a cohort section, expand the cohort display and add the cohort 
   * to the gens so that it appears in the topology diagram. When the user collpases
   * the section, leave the cohort in the graph.
   */
  const flipCohortSection = (evt) => {

    let cohortName = evt.target.id;
    
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
      resourcesContext.loadCohortFromServer(serverName, cohortName);
    }
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


  /*
   * As the user flips a cohort section, expand the cohort details display and add the cohort 
   * to the gens so that it appears in the topology diagram.
   */
  //const flipSectionAndLoadCohort = (evt) => {
  //
  //  let cohortName = evt.target.id;
  //  resourcesContext.loadCohortFromServer(serverName, cohortName);
  //
  //  /*
  //   * Use currentTarget (not target) - because we need to operate relative to the button,
  //   * which is where the handler is defined, in order for the content section to be the sibling.
  //   */
  //  const element = evt.currentTarget;
  //  element.classList.toggle("active");
  //  const content = element.nextElementSibling;
  //  if (content.style.display === "block") {
  //    content.style.display = "none";
  //  }
  //  else {
  //    content.style.display = "block";
  //  }
  //};



  const formatCohort = (cohortDetails) => {
    let cohortName = cohortDetails.cohortDescription.cohortName;
    let connectionStatus = cohortDetails.cohortDescription.connectionStatus;
    let localRegistration = cohortDetails.localRegistration;
    let remoteRegistrations = cohortDetails.remoteRegistrations;
    let topicConnection = cohortDetails.cohortDescription.topicConnection;

    return (
      <div>
        
          <button className="collapsible" id={cohortName} onClick={flipCohortSection}> Cohort : {cohortName} </button>
          <div className="content">

            <ul>
              <li className="type-details-item-bold">Status : {connectionStatus ? connectionStatus : <i>blank</i>}</li>
              <li>
                <button className="collapsible" onClick={flipSection}> Local Registration: </button>
                <div className="content">
                  <div>Registration time : {localRegistration.registrationTime}</div>
                  <div>MetadataCollectionName : {localRegistration.metadataCollectionName}</div>
                  <div>MetadataCollectionId : {localRegistration.metadataCollectionId}</div>
                </div>      
              </li>
              <li>
                <button className="collapsible" onClick={flipSection}> Remote Registrations: </button>
                <div className="content">
                <ul className="type-details-list">     
                  {formatRegistrations(remoteRegistrations)}
                </ul>
                </div>      
              </li>
              <li>
                <button className="collapsible" onClick={flipSection}> Connections: </button>
                <div className="content">
                <ul className="type-details-list">     
                  {expandConnections(topicConnection.embeddedConnections)}
                </ul>
                </div>   
              </li>
            </ul>
          </div>
   
      </div>
    );
  }

 

  const expandConnections = (connections) => {
    return (
      <ul className="type-details-list">     
        {formatConnections(connections)}
      </ul>
    );
  }

  const formatConnections = (connections) => {
    let formattedConnections = connections.map( (connection) => 
      <li className="type-details-list-item" key={connection.position}> 
        {formatConnection(connection)}
      </li>  
    );
    return formattedConnections;
  }

  const formatConnection = (connection) => {
    return (
      <div>
        <ul className="details-sublist">
          <li className="details-sublist-item">Name : {connection.displayName ? connection.displayName  : <i>blank</i>}</li>
          <li className="details-sublist-item">Type : {connection.embeddedConnection.connectorType ? 
            connection.embeddedConnection.connectorType.displayName : <i>blank</i>}</li>
          <li className="details-sublist-item">Address : {connection.embeddedConnection.endpoint && connection.embeddedConnection.endpoint.address ? 
            connection.embeddedConnection.endpoint.address : <i>blank</i>}</li>
        </ul>
      </div>
    );
  }

  const formatRegistrations = (registrations) => {
    let formattedRegistrations = registrations.map( (registration) => 
      <li className="details-sublist-item" key={registration.registrationTime}> 
        {formatRegistration(registration)}
      </li>  
    );
    return formattedRegistrations;
  }

  const formatRegistration = (registration) => {
    return (
      <div>
        <ul className="details-sublist">
          <li className="details-sublist-item">Server Name : {registration.serverName ? registration.serverName  : <i>blank</i>}</li>
          <li className="details-sublist-item">Server Type : {registration.serverType ? registration.serverType : <i>blank</i>}</li>
          <li className="details-sublist-item">Registration time : {registration.registrationTime ? registration.registrationTime : <i>blank</i>}</li>
          <li className="details-sublist-item">MetadataCollectionName : {registration.metadataCollectionName ? registration.metadataCollectionName : <i>blank</i>}</li>
          <li className="details-sublist-item">MetadataCollectionName : {registration.metadataCollectionId ? registration.metadataCollectionId : <i>blank</i>}</li>
          <li className="details-sublist-item">Address : {registration.repositoryConnection && 
                                                          registration.repositoryConnection.endpoint && 
                                                          registration.repositoryConnection.endpoint.address ? 
                                                          registration.repositoryConnection.endpoint.address : <i>blank</i>}</li>
        </ul>
      </div>
    );
  }

  const expandCohorts = (inCohortDetails) => {

    /*
     * Sort the cohort names
     */
    let cohortNamesUnsorted = Object.keys(inCohortDetails);
    let cohortNamesSorted   = cohortNamesUnsorted.sort();
    
    /*
     * Use the name to index into the map in sorted order and display cohort
     */
    let cohortList = cohortNamesSorted.map( (chtName) => 
        <li className="type-details-item" key={chtName}> 
          {formatCohort(inCohortDetails[chtName])}
        </li>
    );

    return cohortList;
  };


  /*
   * Render cohorts
   */
  if (!inCohortDetails) {
    outCohorts = (
      <div>
        list is empty
      </div>
    )
  }
  else {
   
    outCohorts = (              
      <ul className="type-details-container">       
       {expandCohorts(inCohortDetails)}          
      </ul>
      
    );
  }

  return outCohorts;
}

ServerCohortsDisplay.propTypes = {
  serverName : PropTypes.string,
  cohortDetails: PropTypes.object
};
