/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { createContext, useState, useContext } from "react";

import PropTypes                                      from "prop-types";

import { IdentificationContext }                      from "../../../contexts/IdentificationContext";




export const RepositoryServerContext = createContext();

export const RepositoryServerContextConsumer = RepositoryServerContext.Consumer;




const RepositoryServerContextProvider = (props) => {

  
  const identificationContext = useContext(IdentificationContext);
  
  /*
   * It is possible to set up defaults for the context here .... although not actually wanted in production....
   */
  const [repositoryServerName, setRepositoryServerName]                         = useState("Schema_Server");  
  const [repositoryServerURLRoot, setRepositoryServerURLRoot]                   = useState("http://localhost:8082");  
  const [repositoryServerEnterpriseOption, setRepositoryServerEnterpriseOption] = useState(false);  

   
 
  const getRepositoryServerEnterpriseOption = () => {
    return repositoryServerEnterpriseOption;
  }

  const getRepositoryServerName = () => {
    return repositoryServerName;
  }

  /*
   * Define the basic body parameters that are mandatory across requests to the Repository Explorer API
   */
  const buildBaseBody = () => {
    const base = {
      serverName       : repositoryServerName,
      serverURLRoot    : repositoryServerURLRoot,
      enterpriseOption : repositoryServerEnterpriseOption };
    return base;
  };

  /*
   * This method wil POST to the repository server appending the supplied URI to a multi-tenant URL.
   * It should be called with the tail portion of the URI, the operation-specific body parameters and 
   * an operation-specific callback function, 
   * e.g.
   * repositoryPOST("types", null, _loadTypeInfo)      - there are no operation-specific body parms for this operation
   * repositoryPOST("types", { searchText: <String> , typeName : <String> , gen : <Integer> }, _findEntitiesByPropertyValue)
   * 
   * The caller does not need to specfiy the serverName, serverURLRoot or enterpriseOption. The other components using
   * this context have access to repositoryServerName, etc. but there is no point passing them in every time wehen they 
   * are already in the context.
   */ 
  const repositoryPOST = (uri, bodyParms, callback) => {

    if (identificationContext.userId === "") {
      alert("There is no user context, please login to the UI");
      return;
    }
    
    const url =  identificationContext.getRestURL("rex") + "/" + uri;
    
    // Add any (optional) bodyParms to the baseBody
    const body = Object.assign(buildBaseBody(), bodyParms);
  
    fetch(url, {
      method     : "POST",
      headers    : { Accept: "application/json", "Content-Type": "application/json" },
      body       : JSON.stringify(body)
    })    
    .then(res => res.json())
    .then(res => callback(res))
    
  };

  

  return (
    <RepositoryServerContext.Provider
      value={{       
        repositoryServerName, 
        setRepositoryServerName,
        getRepositoryServerName,
        repositoryServerURLRoot, 
        setRepositoryServerURLRoot,
        repositoryServerEnterpriseOption, 
        setRepositoryServerEnterpriseOption,      
        getRepositoryServerEnterpriseOption,      
        repositoryPOST              
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

