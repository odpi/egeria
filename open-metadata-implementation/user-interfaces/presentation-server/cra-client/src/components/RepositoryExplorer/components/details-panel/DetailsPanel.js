/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React               from "react";

import PropTypes           from "prop-types";

import InstanceDisplay     from "./InstanceDisplay";


export default function DetailsPanel(props) {

  return (
    
    <div className={props.className}>       
        <InstanceDisplay />        
    </div>     
  
  );
}

DetailsPanel.propTypes = {  
  className  : PropTypes.string
}

