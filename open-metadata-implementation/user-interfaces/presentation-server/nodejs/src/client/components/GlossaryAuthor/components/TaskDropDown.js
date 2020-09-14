/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import { withRouter } from "react-router-dom";

export default function TaskDropDown({ onChange }) {
  return (
    <select
      id="tasks"
      float="right"
      border-bottom-width="3px"
      onChange={onChange}
    >
      <option value="glossaries">Glossary Authoring</option>
      <option value="search">Search</option>
      <option selected value="crud">
        CRUD
      </option>
    </select>
  );
}
