/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import icon from '../../../public/images/edit.svg';

export default function Egeria_edit_16(props) {
  return (
    <img src={icon} height="16" width="16" onClick={props.onClick} />
  );
}
