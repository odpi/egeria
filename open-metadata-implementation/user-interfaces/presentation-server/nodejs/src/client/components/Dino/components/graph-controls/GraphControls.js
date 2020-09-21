/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }                     from "react";

import PropTypes                                 from "prop-types";

import { ResourcesContext }                      from "../../contexts/ResourcesContext";

import "../../dino.scss";



export default function GraphControls(props) {

  const resourcesContext        = useContext(ResourcesContext);
  

  return (
    
    <div className={props.className}>
        <p className="descriptive-text">
          Traversal count : {resourcesContext.getNumGens()}
        </p>
        <button className="graph-control-button"
          onClick = { () => resourcesContext.removeGen() }  >
          Undo
        </button>
        <button className="graph-control-button"
          onClick = { () => resourcesContext.clear() }  >
          Clear
        </button>
    </div>

  );

}

GraphControls.propTypes = {  
  className  : PropTypes.string
}

