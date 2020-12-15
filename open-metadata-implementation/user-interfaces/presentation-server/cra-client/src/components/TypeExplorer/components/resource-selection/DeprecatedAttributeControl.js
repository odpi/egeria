/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext }                    from "react";

import { RequestContext }                       from "../../contexts/RequestContext";

import { TypesContext }                         from "../../contexts/TypesContext";

import "./resource-selector.scss"


/*
 * The DeprecatedAttributeControl provides a checkbox that the user can check if they want
 * display of deprecated attributes. If checked, the attributes are read in the explorer and
 * displayed in diagrams or on details panels. If unchecked, the attributes are still read
 * in the explorer, but are not displayed.
 */

export default function DeprecatedAttributeControl() {
  
  const requestContext         = useContext(RequestContext);

  const typesContext           = useContext(TypesContext);

  const updateDeprecatedAttributeOption = () => {
    requestContext.updateDeprecatedAttributeOption();
  };

  return (

    <div className="resource-controls">

      <label htmlFor="cbAttributeDeprecation">Include deprecated attributes : </label>
      <input type="checkbox"
             id="cbAttributeDeprecation"
             name="cbAttributeDeprecation"
             onChange={updateDeprecatedAttributeOption}
             checked={ requestContext.deprecatedAttributeOption }
             value={ requestContext.deprecatedAttributeOption }  />
      <br />

    </div>

  );
}
