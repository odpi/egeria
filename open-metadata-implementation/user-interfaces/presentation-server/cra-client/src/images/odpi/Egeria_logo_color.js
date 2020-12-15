/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import icon from '../../imagesHolder/odpi/egeria-color.svg';

export default function Egeria_logo_color(props) {
  return (
    <a href="https://egeria.odpi.org/" target="_blank" rel="noopener noreferrer">
         <img src={icon} alt="Egeria color logo" />
    </a>
  );
}
