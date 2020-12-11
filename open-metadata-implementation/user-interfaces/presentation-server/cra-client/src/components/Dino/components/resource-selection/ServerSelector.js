/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useState, useEffect }         from "react";

import { ResourcesContext }                               from "../../contexts/ResourcesContext";

import { RequestContext }                                 from "../../contexts/RequestContext";

import { InteractionContext }                             from "../../contexts/InteractionContext";

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
  
  const resourcesContext                  = useContext(ResourcesContext);

  const requestContext                    = useContext(RequestContext);

  const interactionContext                    = useContext(InteractionContext);


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
      else {
        /*
         * This is not the best way to determine the type of error, but it will
         * suffice for now. The better solution requires an overhaul of index.js
         * to improve its error reporting.
         */
        let message = json.exceptionErrorMessage;

        if (message.includes("invalid supplied URL")) {
          let tokens = message.split("/");
          let tenantName = tokens[2];
          json.exceptionErrorMessage = "Check the Presentation Server for tenant "+tenantName+" is configured. [Detail "+ json.exceptionErrorMessage + "]";
        }
        else if (message.includes("ECONNREFUSED")) {
          let url = json.requestURL;
          let tokens = url.split("/");
          let tenantName = tokens[4];
          let detailedErrorMessage = json.exceptionErrorMessage;
          json.exceptionErrorMessage = "Could not connect to the view service";
          json.exceptionSystemAction = "The system could not connect to the view service [Detail "+ detailedErrorMessage + "]";
          json.exceptionUserAction = "Check the configuration of the Presentation Server for tenant "+tenantName+", and that the corresponding View Server is running";
        }
        else if (message.includes("OMAG-MULTI-TENANT-404-001")) {
          let skipLength = "OMAG-MULTI-TENANT-404-001".length+1;
          let shortMessage = message.substring(skipLength);
          json.exceptionErrorMessage = shortMessage;
          json.exceptionSystemAction = "The system could not retrieve the servers because the view service is not running";
          json.exceptionUserAction = "Check the configuration of the Presentation Server and that the corresponding View Server is running, then retry the request.";
        }
        interactionContext.reportFailedOperation("get servers from view service",json);
      }
    }
    else {
      /*
       * If there is no JSON in the response this constitutes a coding error, so raise
       * an alert to make it conspicuous...
       */
      alert("ServerSelector received a response with no JSON. Please raise an issue on the Egeria github page.");
    }
  }



  if (!serversLoaded) {
    getServers();
  }

  /*
   * Handler for change to server selector - the server eentries are really server instances
   * and are keyed (in availableServers) by serverInstanceName, which is what is used to display them in the 
   * selector.
   */
  const serverSelected = (evt) => {
    let serverInstanceName = evt.target.value;
    /*
     * When the server is selected need to ask resourcesContext to load it.
     * The selector needs to pass to the resources context the real server name and the 
     * the name of the platform on which to find it. These are both in the configured 
     * server entry.
     */
    let serverInstance = availableServers[serverInstanceName];
    let serverName     = serverInstance.serverName;
    let platformName   = serverInstance.platformName;
    let description    = serverInstance.description;
    resourcesContext.loadServerFromSelector(serverName, platformName, serverInstanceName, description);
    
  }

  /*
   * Enable user to click on the same server again...
   */
  const selectorFocus = (evt) => {
    let selector = evt.target;
    selector.selectedIndex = -1;
    selector.blur();
  }


  /*
   * It's important to reset (clear) the selector if the focus changes to anything other than the selected
   * option - including (and especially) if an option from a different selector is chosen. This is to ensure
   * that if/when the user navigates back to this selector, if they click on the same (previously selected) 
   * option then we will see it as a change. Otherwise the user action will be ignored and the focus will 
   * not change.
   */
  let selectorValue;
  useEffect(
    () => {
      
      const focus = resourcesContext.focus;
    
      /*
       * If the focus is a server then ensure we track the current focus
       */
      if (focus.category === "server") {
        selectorValue = (focus.name === "") ? "none" : focus.name;
        const selector = document.getElementById("serverSelector");
        if (selector)
          selector.value = selectorValue;
      }
      else {
        /* 
         * If the focus is not a server we need to clear the server selector
         */
        selectorValue = "none";
        const selector = document.getElementById("serverSelector");
        if (selector)
          selector.value = selectorValue;
      }
    },
    [resourcesContext.focus]
  )
 
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
