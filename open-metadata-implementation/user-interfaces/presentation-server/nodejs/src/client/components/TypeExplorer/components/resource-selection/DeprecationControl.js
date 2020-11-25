/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext }                    from "react";

import { RequestContext }                       from "../../contexts/RequestContext";

import { TypesContext }                         from "../../contexts/TypesContext";

import "./resource-selector.scss"


/*
 * The DeprecationControl provides a checkbox that the user can check if they want to not
 * have display of deprecated types. The types are still read in the explorer but will not
 * be displayed in diagrams or on details panels.
 */

export default function DeprecationControl() {
  
  const requestContext         = useContext(RequestContext);

  const typesContext           = useContext(TypesContext);

  const updateDeprecationOption = () => {
    requestContext.updateDeprecationOption();
  };

  return (

    <div className="resource-controls">

      <label htmlFor="cbDeprecation">Include deprecated types : </label>
      <input type="checkbox"
             id="cbDeprecation"
             name="cbDeprecation"
             onChange={updateDeprecationOption}
             checked={ requestContext.deprecationOption }
             value={ requestContext.deprecationOption }  />
      <br />

    </div>

  );
}
