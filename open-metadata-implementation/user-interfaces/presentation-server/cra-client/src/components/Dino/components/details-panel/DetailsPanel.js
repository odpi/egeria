/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }               from "react";

import { ResourcesContext }                from "../../contexts/ResourcesContext";

import PlatformDisplay                     from "./PlatformDisplay"

import ServerDisplay                       from "./ServerDisplay"

import "./details-panel.scss";


export default function DetailsPanel() {

  const resourcesContext = useContext(ResourcesContext);

  const displayLoadingMessage = (resourceName) => {
    return (
      <div className="details-message">Loading details for {resourceName}</div>
    );
  }

  return (
    <div className="details-panel">
      {
        resourcesContext.operationState.state === "loading" && displayLoadingMessage(resourcesContext.operationState.name)
      }
      {
        resourcesContext.operationState.state === "inactive" && resourcesContext.focus.category === "server" &&
          (<ServerDisplay />)
      }
      {
        resourcesContext.operationState.state === "inactive" && resourcesContext.focus.category === "platform" &&
          (<PlatformDisplay />)
      }
    </div>
  );
}

