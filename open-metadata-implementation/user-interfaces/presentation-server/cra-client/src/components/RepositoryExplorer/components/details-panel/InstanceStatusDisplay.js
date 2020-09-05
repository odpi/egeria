/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React       from "react";

import PropTypes   from "prop-types";


export default function InstanceStatusDisplay(props) {

  let status;
  const inst = props.inst;
  if (inst.status !== "DELETED") {
    status = inst.status;
  }
  else {
    status = inst.status +"(Status on delete: "+inst.statusOnDelete;
  }
  return status;
}

InstanceStatusDisplay.propTypes = {
  inst: PropTypes.object
};
