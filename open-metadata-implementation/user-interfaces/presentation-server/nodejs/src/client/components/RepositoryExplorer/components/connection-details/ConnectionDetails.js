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
    //console.log("ConnectionDetails: serverName being updated to :"+evt.target.value);
    repositoryServerContext.setRepositoryServerName(evt.target.value);
  }

  /*
   * Handler for change to instance GUID field
   */
  const updatedServerURLRoot = (evt) => {
    //console.log("ConnectionDetails: serverURLRoot updated, now :"+evt.target.value);
    repositoryServerContext.setRepositoryServerURLRoot(evt.target.value);
  }

  /*
   * Handler for change to instance GUID field
   */
  const updatedEnterpriseOption = (evt) => {
    //console.log("ConnectionDetails: enterpriseOption toggled, current value is: "+repositoryServerContext.repositoryServerEnterpriseOption.toString());
    repositoryServerContext.setRepositoryServerEnterpriseOption(!repositoryServerContext.repositoryServerEnterpriseOption);
  }


  const setServerDetails = () => {
    /*
     * There is nothing to actually 'set' here - but this operation initiates the load of type information
     * from the specified repository server
     */
    typesContext.loadTypeInfo()
  }
  
  return (
    
    <div className={props.className}>
     
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
          
        <button className="top-control-button"  onClick = { setServerDetails } >
          Set Server Details
        </button>
      </div>

    </div>     
  
  );

}


ConnectionDetails.propTypes = {  
  className  : PropTypes.string
}


