/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext }           from "react";

import { RepositoryServerContext }     from "../../contexts/RepositoryServerContext";

import { TypesContext }                from "../../contexts/TypesContext";  

import PropTypes                       from "prop-types";

import "./connection-details.scss"




export default function ConnectionDetails(props) {
  
  const repositoryServerContext = useContext(RepositoryServerContext);

  const typesContext      = useContext(TypesContext);

  /*
   * Handler for change to instance GUID field
   */
  const updatedServerName = (evt) => {
    repositoryServerContext.setRepositoryServerName(evt.target.value);
  }

  /*
   * Handler for change to instance GUID field
   */
  const updatedServerURLRoot = (evt) => {
    repositoryServerContext.setRepositoryServerURLRoot(evt.target.value);
  }

  /*
   * Handler for change to instance GUID field
   */
  const updatedEnterpriseOption = (evt) => {
    repositoryServerContext.setRepositoryServerEnterpriseOption(!repositoryServerContext.repositoryServerEnterpriseOption);
  }


  const setServerDetails = () => {
    /*
     * There is nothing to actually 'set' here - but this operation initiates the load of type information
     * from the specified repository server
     */
    typesContext.loadTypeInfo()
  }
  
  // TODO - alignment

  return (

      <div className="connection-controls">  

        <p>Repository Server</p>

        <label htmlFor="serverNameField">Server name: </label>
        <input name="serverNameField"
           value = { repositoryServerContext.repositoryServerName }      
           onChange = { updatedServerName }   />
        <br />

        <label htmlFor="serverURLRootField">Server URL root: </label>
        <input name="serverURLRootField"
           value = { repositoryServerContext.repositoryServerURLRoot }      
           onChange = { updatedServerURLRoot }  /> 
       <br />
         
        <label htmlFor="cbEnterprise">Enterprise : </label>
        <input type="checkbox" 
               id="cbEnterprise" 
               name="cbEnterprise" 
               onChange={updatedEnterpriseOption} 
               checked={ repositoryServerContext.repositoryServerEnterpriseOption } 
               value={ repositoryServerContext.repositoryServerEnterpriseOption }  />
        <br />
          
        <button className="connection-button"  onClick = { setServerDetails } >
          Set Server Details
        </button>
      </div>

  );

}


ConnectionDetails.propTypes = {  
  className  : PropTypes.string
}

