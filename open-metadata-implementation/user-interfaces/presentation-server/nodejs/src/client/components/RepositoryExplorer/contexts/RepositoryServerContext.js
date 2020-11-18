/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { createContext, useState, useContext } from "react";

import PropTypes                                      from "prop-types";

import { IdentificationContext }                      from "../../../contexts/IdentificationContext";




export const RepositoryServerContext = createContext();

export const RepositoryServerContextConsumer = RepositoryServerContext.Consumer;




const RepositoryServerContextProvider = (props) => {

  
  const identificationContext                    = useContext(IdentificationContext);
  
  const [repositoryServer, setRepositoryServer]  = useState( { serverName : "", platformName : "" } );

  const [enterpriseOption, setEnterpriseOption]  = useState(true);



  /*
   * Handler for change to enterprise option checkbox
   */
  const updateEnterpriseOption = () => {
    setEnterpriseOption(!enterpriseOption);
  }

  /*
   * Getter method to retrieve enterpriseOption
   */
  const getEnterpriseOption = () => {
    return enterpriseOption;
  }

  /*
   * Function to construct the basic body parameters that are common to requests to the platform or server
   * resourceCategory is either "platform" or "server"
   */
  const buildBaseBody = () => {
    const base = {
      serverName       : repositoryServer.serverName,
      platformName     : repositoryServer.platformName,
      enterpriseOption : enterpriseOption };
    return base;
  };

 
   /*
   * This method will POST to the repository server appending the supplied URI to a multi-tenant URL.
   * This method must be passed the serverName and platformName.
   * It also requires the tail portion of the URI, the operation-specific body parameters and an 
   * operation-specific callback function, 
   * 
   * e.g.
   *   callPOST(serverName, platformName, "types", null, _loadTypeInfo)      - there are no operation-specific body parms for this operation
   * 
   *   callPOST(serverName, platformName, "types", { searchText: <String> , typeName : <String> , etc.. }, _findEntitiesByPropertyValue)
   * 
   * This call is provided for initial operations such as loadTypes for which the repositoryServer in the context will not already have been set.
   * It therefore requires that the caller pass the serverName and platformName. The current value of enterpriseOption is used.
   */ 
  const callPOST = (serverName, platformName, uri, bodyParms, callback) => {

    if (identificationContext.userId === "") {
      alert("There is no user context, please login to the UI");
      return;
    }

    if (!serverName || !platformName) {
      alert("No server or platform has been specified");
      return;
    }

    // Prime the base - the local repositoryServer state will not have been set yet.
    let base = {
      serverName        : serverName,
      platformName      : platformName,
      enterpriseOption  : enterpriseOption
    }
    
    
    const url =  identificationContext.getRestURL("rex") + "/" + uri;
    
    /* 
     * Add any (optional) bodyParms to the baseBody
     */
    const body = Object.assign(base, bodyParms);
  
    fetch(url, {
      method     : "POST",
      headers    : { Accept: "application/json", "Content-Type": "application/json" },
      body       : JSON.stringify(body)
    })    

    /*
     * The response from fetch() should always:
     * either:
     *  - be ok and comtain a JSON body, which is retrieved using json(),
     * or:
     *  - be !ok and contain status, statusText and a text body, which is retrieved using text().
     * In either case, this function will call the callback with a JSON object.
     *
     * The relatedHTTPCode (200, 400, etc.) should be checked in the callback,
     * where there is contextual information about the operation that was performed
     * and where more specific error context can be supplied in the message to the
     * user.
     */

    .then(response => {
      if (response.ok) {
        response.json()
        .then(json => {
          /*
           * No need to check status code here - leave it to the callback which knows the operation context.
           */
          callback(json)
        });
      }
      else {
        /*
         * response was not 'ok'. Parse the status fields and body text and contruct
         a json response to pass to the callback...
         */
        let json = {};
        json.relatedHTTPCode = response.status;
        json.requestURL      = response.url;
        response.text() // returns a promise...
        .then(text => {
          json.exceptionErrorMessage = text;
          callback(json);
        })
      }
    })
  };
  

  /*
   * This method will POST to the repository server appending the supplied URI to a multi-tenant URL.
   * There is no serverName or platformName parameter to this method - it will use the values from the 
   * repositoryServer in this context.
   * It needs to be passed the tail portion of the URI, the operation-specific body parameters and 
   * an operation-specific callback function, 
   * 
   * e.g.
   * repositoryPOST("types", null, _loadTypeInfo)      - there are no operation-specific body parms for this operation
   * 
   * repositoryPOST("types", { searchText: <String> , typeName : <String> , etc ... }, _findEntitiesByPropertyValue)
   * 
   * The context must already have the serverName and platformName in repositiryServer. 
   * The current value of enterpriseOption is used.
   */ 
  const repositoryPOST = (uri, bodyParms, callback) => {

    if (identificationContext.userId === "") {
      alert("There is no user context, please login to the UI");
      return;
    }

    if (repositoryServer.serverName === "" || repositoryServer.platformName === "") {
      alert("No repository server has been selected, please use the server selector to choose one");
      return;
    }
    
    const url =  identificationContext.getRestURL("rex") + "/" + uri;
    
    /* 
     * Add any (optional) bodyParms to the baseBody
     */
    const body = Object.assign(buildBaseBody(), bodyParms);
  
    fetch(url, {
      method     : "POST",
      headers    : { Accept: "application/json", "Content-Type": "application/json" },
      body       : JSON.stringify(body)
    })    

    /*
     * The response from fetch() should always:
     * either:
     *  - be ok and comtain a JSON body, which is retrieved using json(),
     * or:
     *  - be !ok and contain status, statusText and a text body, which is retrieved using text().
     * In either case, this function will call the callback with a JSON object.
     *
     * The relatedHTTPCode (200, 400, etc.) should be checked in the callback,
     * where there is contextual information about the operation that was performed
     * and where more specific error context can be supplied in the message to the
     * user.
     */

    .then(response => {
      if (response.ok) {
        response.json()
        .then(json => {
          /*
           * No need to check status code here - leave it to the callback which knows the operation context.
           */
          callback(json)
        });
      }
      else {
        /*
         * response was not 'ok'. Parse the status fields and body text and contruct
         a json response to pass to the callback...
         */
        let json = {};
        json.relatedHTTPCode = response.status;
        json.requestURL      = response.url;
        response.text() // returns a promise...
        .then(text => {
          json.exceptionErrorMessage = text;
          callback(json);
        })
      }
    })
  };

  /*
   * This method will GET from the view service appending the supplied URI to a multi-tenant URL.
   * It should be called with the tail portion of the URI. This is only used for a light-weight 
   * GET operations and there are no operation-specific body parameters. These could be added if
   * needed.
   *
   * The caller must specify an operation-specific callback function.
   *
   * All promises are fully resolved in this context, so the callback code (if invoked) is
   * fully synchronous and will always be passed a json object.
   *
   * In the event of ECONNREFUSED the PS server will forward the response with status code 400.
   *
   */

  const callGET = (uri, callback) => {
  
    if (identificationContext.userId === "") {
      alert("There is no user context, please login to the UI");
      return;
    }
      
    const url = identificationContext.getRestURL("rex") + "/" + uri;
  
    /* 
     * No body needed
     */

    fetch(url, {
      method     : "GET",
      headers    : { Accept: "application/json", "Content-Type": "application/json" },
    })

    /*
     * The response from fetch() should always:
     * either:
     *  - be ok and comtain a JSON body, which is retrieved using json(),
     * or:
     *  - be !ok and contain status, statusText and a text body, which is retrieved using text().
     * In either case, this function will call the callback with a JSON object.
     *
     * The relatedHTTPCode (200, 400, etc.) should be checked in the callback,
     * where there is contextual information about the operation that was performed
     * and where more specific error context can be supplied in the message to the
     * user.
     */

    .then(response => {
      if (response.ok) {
        response.json()
        .then(json => {
          /*
           * No need to check status code here - leave it to the callback which knows the operation context.
           */
          callback(json)
        });
      }
      else {
        /*
         * response was not 'ok'. Parse the status fields and body text and contruct
         a json response to pass to the callback...
         */
        let json = {};
        json.relatedHTTPCode = response.status;
        json.requestURL      = response.url;
        response.text() // returns a promise...
        .then(text => {
          json.exceptionErrorMessage = text;
          callback(json);
        })
      }
    })
  };

  return (
    <RepositoryServerContext.Provider
      value={{       
        repositoryServer, 
        setRepositoryServer,
        updateEnterpriseOption,
        getEnterpriseOption,
        enterpriseOption,   
        repositoryPOST,
        callGET,
        callPOST     
      }}
    >      
      {props.children}
    </RepositoryServerContext.Provider>
  );
};

RepositoryServerContextProvider.propTypes = {
  children: PropTypes.node  
};

export default RepositoryServerContextProvider;

