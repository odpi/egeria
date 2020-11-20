/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import TaskDropDown from "./TaskDropDown";
import { useHistory } from "react-router-dom";

export default function GlossaryAuthorTaskRouting({ glossaryAuthorURL }) {
  console.log("BaseGlossaryAuthorTaskRouting history");
  let history = useHistory();

  /**
   * This event is bubbled up to this component from the dropdown. 
   * The e.target.value is the value associated with the operation selected in the dropdown.
   * The events needs to processed here because the history here is the react router history, which allows us to 
   * change the url.
   * 
   * In the dropdown, the history in scope is that of the window, so cannot be used to change the url.  
   * @param {*} e on change event from dropdown   
   */

  const onChange = (e) => {
    const url = glossaryAuthorURL + "/" +`${e.target.value}`;
    history.push(url);
  };
  return <TaskDropDown onChange={onChange} />;
}