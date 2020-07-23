/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import icon from '../../../public/images/launch.svg';
function createTarget(props) {
    let html = '';
    if (props.external) {
      html = '_blank';
    }
    return html;

}

export default function Launch_32(props) {
  return (
    <a href={props.link} target={createTarget(props)}>
         <img src={icon} height="32" width="32" />
    </a>
  );
}
