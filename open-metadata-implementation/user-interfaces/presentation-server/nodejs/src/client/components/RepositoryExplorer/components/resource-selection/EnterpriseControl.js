/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext }                    from "react";

import { RepositoryServerContext }              from "../../contexts/RepositoryServerContext";

import "./resource-selector.scss"



/*
 * The EnterpriseControl provides a checkbox that the user can check if they want queryies to be 
 * issued at the Enterprise level - which means that results will be collected from across 
 * the cohorts that the target server is a member of, rather than just locally.
 */
export default function EnterpriseControl() {
  
  
  const repositoryServerContext = useContext(RepositoryServerContext);

  

  return (

    <div className="resource-controls">

      <label htmlFor="cbEnterprise">Enterprise : </label>
      <input type="checkbox"
             id="cbEnterprise"
             name="cbEnterprise"
             onChange={repositoryServerContext.updateEnterpriseOption}
             checked={ repositoryServerContext.enterpriseOption }
             value={ repositoryServerContext.enterpriseOption }  />
      <br />

    </div>

  );
}
