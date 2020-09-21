/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React             from "react";

import PropTypes         from "prop-types";

import "./details-panel.scss";


export default function ServerConfigAuditDisplay(props) {

  const inTrail  = props.trail;

  let outTrail;

  

  /*
   * Cannot use the normal pattern here as entries in the audit can repeat (including timestamp and content)
   * so if you use all/part of the entry as the key the key will not be unique, and if you were to use 
   * indexOf you would get repeating keys. Therefore need to generate the key as we iterate over the trail.
   */
  const formatAuditTrail = (trail) => {
    let auditList = [];
    let key = 0;
    for (key = 0; key < trail.length; key++) {
      let auditEntry = trail[key];
      let auditListEntry = (
        <li className="details-sublist-item" key={key}> 
          {formatAuditEntry(auditEntry)}
        </li>
      )
      auditList.push(auditListEntry);
    }
    return auditList;
  }


  const formatAuditEntry = (entry) => {
    return (
    <div>
      {entry}
    </div>
    );
  }
 

  if (!inTrail) {
    outTrail = (
      <div>
        audit trail is empty
      </div>
    )
  }
  else {
   
    outTrail = (              
      <ul className="details-sublist">       
       {formatAuditTrail(inTrail)}          
      </ul>
      
    );
  }

  return outTrail;
}

ServerConfigAuditDisplay.propTypes = {
  trail: PropTypes.array 
};
