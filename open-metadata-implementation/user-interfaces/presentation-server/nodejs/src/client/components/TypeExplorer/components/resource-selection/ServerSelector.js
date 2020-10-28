/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useState }                    from "react";

import { RequestContext }                                 from "../../contexts/RequestContext";

import { TypesContext }                                   from "../../contexts/TypesContext";

import "./resource-selector.scss"



/*
 * The ServerSelector displays the names of the servers that are configured as resource endpoints
 * in the configuration of the view service. They are retrieved from the view service at strartup.  
 * The server selector control will present servers by name to the user and allow the user to select
 * one. During server load, the platformName is passed to the view service, which resolves it to the 
 * configured platformRootURL for that platform. 
 * The server's details are retrieved and the server will become the new focus object.
 */
export default function ServerSelector() {
  
  
  const requestContext                    = useContext(RequestContext);

  const typesContext                      = useContext(TypesContext);

  const [serversLoaded, setServersLoaded] = useState(false);

  /*
   * availableServers is a list of names of the servers to which requests can be sent.
   * This list comes from the view-service on initialisation.
   * If the configuration of the V-S is changed, refresh the page - or we could provide a
   * list refresh capability behind a button. 
   */
  const [availableServers, setAvailableServers]       = useState({});

  /*
   * Populate the available server list by retrieving
   * the available server names from the view-service
   */
  const getServers = () => {
    requestContext.callGET("resource-endpoints", _getServers);
    setServersLoaded(true);
  }

  const _getServers = (json) => {
    if (json !== null) {
      if (json.relatedHTTPCode === 200 ) {
        let serverList = json.serverList;
        if (serverList) {
          let newServers = {};
          serverList.forEach(svr => {
            const newServer = { "serverInstanceName"  : svr.serverInstanceName, 
                                "description"         : svr.resourceDescription, 
                                "platformName"        : svr.platformName,
                                "serverName"          : svr.serverName  };
            newServers[svr.serverInstanceName] = newServer;
          });
          setAvailableServers(newServers);
          return;
        }
      }
    }
    /*
     * On failure ... json could be null or contain a bad relatedHTTPCode
     */
    reportFailedOperation("getServers",json);
  }


  /*
   * Always accept the operation name because operation name is needed even in the case where json is null
   */
  const reportFailedOperation = (operation, json) => {
    if (json !== null) {
      if (json.relatedHTTPCode === 200 ) {
        /*
         * Operation succeeded but did not return anything useful...
         */
        alert("No servers were found - please check the configuration of the Repository Explorer View Service");
      }
      else {
        /*
         * Operation reported failure
         */
        const relatedHTTPCode = json.relatedHTTPCode;
        const exceptionMessage = json.exceptionErrorMessage;
        /*
         * TODO - could be changed to cross-UI means of user notification... for now rely on alerts
         */
        alert("Operation "+operation+" failed with status "+relatedHTTPCode+" and message "+exceptionMessage);
      }
    }
    else {
      alert("Operation "+operation+" did not get a response from the view server");
    }
  }



  if (!serversLoaded) {
    getServers();
  }

  /*
   * Handler for change to server selector - the server entries are really server instances
   * and are keyed (in availableServers) by serverInstanceName, which is what is used to display them in the 
   * selector.
   */
  const serverSelected = (evt) => {

    let serverInstanceName = evt.target.value;
    /*
     * When the server is selected Tex will retrieve the types for that server.
     */
    let serverInstance = availableServers[serverInstanceName];
    let serverName     = serverInstance.serverName;
    let platformName   = serverInstance.platformName;

    /*
     *  ...this operation initiates the load of type information from the specified repository server
     */
    typesContext.loadTypeInfo(serverName, platformName);
  }

  /*
   * Enable user to click on the same server again...
   */
  const selectorFocus = (evt) => {
    let selector = evt.target;
    selector.selectedIndex = -1;
    selector.blur();
  }

 
  const serverNameList = Object.keys(availableServers);
  const serverNameListSorted = serverNameList.sort(function (a, b) {
    return a.toLowerCase().localeCompare(b.toLowerCase());
  });



  return (
    <div className="resource-controls">

      <p className="descriptive-text">Servers</p>

      <select className="server-selector"
              id="serverSelector"
              name="serverSelector"
              onChange = { serverSelected }
              onFocus= { selectorFocus }  // Enable re-click of same entry in selector
              size = "5" >
        {
          serverNameListSorted.length === 0 && 
          ( <option value="dummy" disabled defaultValue>No servers...</option> )
        }
        {
          serverNameListSorted.length > 0 && 
          (
            serverNameListSorted.map(serverName => 
              ( <option key={serverName} value={serverName}> {serverName} </option> )
            )      
          )                                         
        }    

      </select>

    </div>

  );
}
