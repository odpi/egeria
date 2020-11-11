/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext } from "react";
import {
  // Button,
  TextInput,
} from "carbon-components-react";
// import {
//   Add16,
//   Subtract16,
// } from "@carbon/icons-react";

import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";

export default function ConfigureRepositoryProxyConnectors() {
  
  const {
    newServerProxyConnector, setNewServerProxyConnector,
    newServerEventMapperConnector, setNewServerEventMapperConnector,
    newServerEventSource, setNewServerEventSource,
  } = useContext(ServerAuthorContext);

  return (

    <div style={{ textAlign: 'left' }}>

      <TextInput
        id="new-server-proxy-connector"
        name="new-server-proxy-connector"
        type="text"
        labelText="Open Metadata Repository Services (OMRS) Repository Connector"
        defaultValue={newServerProxyConnector}
        onChange={e => setNewServerProxyConnector(e.target.value)}
        autoComplete="off"
      />

      <TextInput
        id="new-server-event-mapper-connector"
        name="new-server-event-mapper-connector"
        type="text"
        labelText="Open Metadata Repository Services (OMRS) Event Mapper Connector"
        defaultValue={newServerEventMapperConnector}
        onChange={e => setNewServerEventMapperConnector(e.target.value)}
        style={{marginBottom: "16px"}}
        autoComplete="off"
      />

      <TextInput
        id="new-server-event-source"
        name="new-server-event-source"
        type="text"
        labelText="Event Source"
        defaultValue={newServerEventSource}
        onChange={e => setNewServerEventSource(e.target.value)}
        style={{marginBottom: "16px"}}
        autoComplete="off"
      />
      
      {/* TODO: Add button to test connection */}
      {/* <Button
        kind="tertiary"
        renderIcon={Add16}
        iconDescription="Add"
        tooltipAlignment="start"
        tooltipPosition="right"
        onClick={handleAddCohort}
        style={{display: "inline-block"}}
      >
        Test Connection
      </Button> */}

    </div>

  )

}