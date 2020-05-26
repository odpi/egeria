/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext }           from "react";

import { RepoServerContext }           from "../../contexts/RepoServerContext";

import { TypesContext }                from "../../contexts/TypesContext";  

import PropTypes                       from "prop-types";

import "./connection-details.scss"




export default function ConnectionDetails(props) {
  
  const repoServerContext = useContext(RepoServerContext);

  const typesContext      = useContext(TypesContext);

  /*
   * Handler for change to instance GUID field
   */
  const updatedServerName = (evt) => {
    //console.log("ConnectionDetails: serverName updated, now :"+evt.target.value);
    repoServerContext.setRepositoryServerName(evt.target.value);
  }

  /*
   * Handler for change to instance GUID field
   */
  const updatedServerURLRoot = (evt) => {
    //console.log("ConnectionDetails: serverURLRoot updated, now :"+evt.target.value);
    repoServerContext.setRepositoryServerURLRoot(evt.target.value);
  }

  /*
   * Handler for change to instance GUID field
   */
  const updatedEnterpriseOption = (evt) => {
    //console.log("ConnectionDetails: enterpriseOption updated to :"+enterpriseOption?"false":"true");
    repoServerContext.setRepositoryServerEnterpriseOption(!repoServerContext.repositoryServerEnterpriseOption);
  }


  const setServerDetails = () => {
    //repoServerContext.setServerDetails(
    //  { serverName       : serverName, 
    //    serverURLRoot    : serverURLRoot, 
    //    enterpriseOption : enterpriseOption});
    typesContext.loadTypeInfo()
  }
  
  return (
    
    <div className={props.className}>
     
      <div className="connection-controls">  

        <p>Repository Server</p>

        <label htmlFor="serverNameField">Server name: </label>
        <input name="serverNameField"
           value = { repoServerContext.serverName }      
           onChange = { updatedServerName }   />
        <br />

        <label htmlFor="serverURLRootField">Server URL root: </label>
        <input name="serverURLRootField"
           value = { repoServerContext.serverURLRoot }      
           onChange = { updatedServerURLRoot }  /> 
       <br />
         
        <label htmlFor="cbEnterprise">Enterprise : </label>
        <input type="checkbox" 
               id="cbEnterprise" 
               name="cbEnterprise" 
               onChange={updatedEnterpriseOption} 
               value={ repoServerContext.enterpriseOption }  />
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


