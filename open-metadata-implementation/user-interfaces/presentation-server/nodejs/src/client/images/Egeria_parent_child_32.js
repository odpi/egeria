/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import icon from '../../../public/images/parent-child.svg';

export default function Egeria_parent_child_32(props) {
  return (
    <img src={icon} height="32" width="32" onClick={props.onClick} />
  );
}
