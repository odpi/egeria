/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext }                    from "react";

import { RequestContext }                       from "../../contexts/RequestContext";

import "./resource-selector.scss"


/*
 * The EnterpriseControl provides a checkbox that the user can check to issue queries 
 * at the Enterprise level - which means that results will be collected from across 
 * the cohorts that the target server is a member of, rather than just locally.
 */

export default function EnterpriseControl() {
  
  
  const requestContext = useContext(RequestContext);

  

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
