/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext }                    from "react";

import { RequestContext }                       from "../../contexts/RequestContext";

import { TypesContext }                         from "../../contexts/TypesContext";

import "./resource-selector.scss"


/*
 * The DeprecatedTypeControl provides a checkbox that the user can check if they want
 * display of deprecated types. If the option is checked, the types are read in 
 * the explorer and will be displayed in diagrams or on details panels. If not 
 * checked, the types are not read in the explorer.
 */

export default function DeprecatedTypeControl() {
  
  const requestContext         = useContext(RequestContext);

  const typesContext           = useContext(TypesContext);

  const updateDeprecatedTypeOption = () => {
    requestContext.updateDeprecatedTypeOption();
  };

  return (

    <div className="resource-controls">

      <label htmlFor="cbDeprecation">Include deprecated types : </label>
      <input type="checkbox"
             id="cbDeprecation"
             name="cbDeprecation"
             onChange={updateDeprecatedTypeOption}
             checked={ requestContext.deprecatedTypeOption }
             value={ requestContext.deprecatedTypeOption }  />
      <br />

    </div>

  );
}
