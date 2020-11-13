/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import icon from '../../../public/images/launch.svg';

export default function Launch_inline_32(props) {
  return (
    <a href={props.link}>
         <img src={icon} height="32" width="32" />
    </a>
  );
}
