/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import icon from '../../../public/images/trash-can.svg';

export default function Egeria_delete_32(props) {
  return (
    <img src={icon} height="32" width="32" onClick={props.onClick} />
  );
}
