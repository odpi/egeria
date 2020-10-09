/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useState }                    from "react";

import { RequestContext }                                 from "../../contexts/RequestContext";

import "./resource-selector.scss"



/*
 * The ServerSelector displays the names of the servers that are configured as resource endpoints
 * in the configuration of the view service. They are retrieved from the view service at strartup.  
 * The server selector control will present servers by name to the user and allow the user to select
 * one. During server load, the platformName is passed to the view service, which resolves it to the 
 * configured platformRootURL for that platform. 
 * The server's details are retrieved and the server will become the new focus object.
 */
export default function EnterpriseControl() {
  
  
  const requestContext                    = useContext(RequestContext);

  

  return (

    <div className="resource-controls">

      <label htmlFor="cbEnterprise">Enterprise : </label>
      <input type="checkbox"
             id="cbEnterprise"
             name="cbEnterprise"
             onChange={requestContext.updateEnterpriseOption}
             checked={ requestContext.enterpriseOption }
             value={ requestContext.enterpriseOption }  />
      <br />

    </div>

  );
}
