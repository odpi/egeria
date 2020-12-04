/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";

export default function TaskDropDown({ onChange }) {
  return (
    <select
      id="tasks"
      float="right"
      border-bottom-width="3px"
      onChange={onChange}
    >

      <option selected value="nodes">
        Node Authoring
      </option>
      <option value="lines">
        Line Authoring
      </option>
    </select>
  );
}
