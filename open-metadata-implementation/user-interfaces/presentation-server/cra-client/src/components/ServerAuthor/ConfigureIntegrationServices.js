/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext } from "react";
import {
  Checkbox,
  Dropdown,
  TextInput,
} from "carbon-components-react";

import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";

export default function ConfigureIntegrationServices() {

  const {
    availableIntegrationServices,
    setSelectedIntegrationService,
    newServerIntegrationServiceRemoteServerName, setNewServerIntegrationServiceRemoteServerName,
    newServerIntegrationServiceRemoteServerURLRoot, setNewServerIntegrationServiceRemoteServerURLRoot,
    newServerIntegrationServiceConnectorName, setNewServerIntegrationServiceConnectorName,
    newServerIntegrationServiceConnectorUserId, setNewServerIntegrationServiceConnectorUserId,
    newServerIntegrationServiceConnection, setNewServerIntegrationServiceConnection,
    newServerIntegrationServiceMetadataSource, setNewServerIntegrationServiceMetadataSource,
    newServerIntegrationServiceRefreshTimeInterval, setNewServerIntegrationServiceRefreshTimeInterval,
    newServerIntegrationServiceUsesBlockingCalls, setNewServerIntegrationServiceUsesBlockingCalls,
    newServerIntegrationServicePermittedSynchronization, setNewServerIntegrationServicePermittedSynchronization,
    integrationServicesFormStartRef,
  } = useContext(ServerAuthorContext);


  // const updateIntegrationServices = () => {
  //   const boxes = document.getElementsByName('integration-services');
  //   const selectedIntegrationServices = Array.from(boxes).filter((box) => box.checked).map((box) => box.value);
  //   setSelectedIntegrationServices(selectedIntegrationServices);
  // }

  return (

    <div style={{ textAlign: 'left' }}>

      <legend className="bx--label" style={{ fontWeight: 'bold' }}>Choose an Integration Service</legend>

        <Dropdown
          id="new-server-integration-service"
          name="new-server-integration-service"
          titleText="Integration Services"
          label=""
          items={availableIntegrationServices}
          itemToString={item => { return item ? item.serviceName : "" }}
          onChange={({ selectedItem }) => setSelectedIntegrationService(selectedItem)}
          // style={{marginBottom: "16px"}}
        />

        <br />
        <br />
      
        <legend className="bx--label" style={{ fontWeight: 'bold' }}>Integration Service Client Configuration</legend>

        <TextInput
          id="new-server-integration-service-remote-server-url-root"
          name="new-server-integration-service-remote-server-url-root"
          type="text"
          labelText="Metadata Server URL Root"
          defaultValue={newServerIntegrationServiceRemoteServerURLRoot}
          onChange={e => setNewServerIntegrationServiceRemoteServerURLRoot(e.target.value)}
          // style={{marginBottom: "16px"}}
          ref={integrationServicesFormStartRef}
          autoComplete="off"
        />

        <br />

        <TextInput
          id="new-server-integration-service-remote-server-name"
          name="new-server-integration-service-remote-server-name"
          type="text"
          labelText="Metadata Server Name"
          defaultValue={newServerIntegrationServiceRemoteServerName}
          onChange={e => setNewServerIntegrationServiceRemoteServerName(e.target.value)}
          // style={{marginBottom: "16px"}}
          autoComplete="off"
        />
        
        <br />
        <br />
        
        <legend className="bx--label" style={{ fontWeight: 'bold' }}>Integration Connector Configuration</legend>

        <TextInput
          id="new-server-integration-service-connector-name"
          name="new-server-integration-service-connector-name"
          type="text"
          labelText="Connector Name"
          defaultValue={newServerIntegrationServiceConnectorName}
          onChange={e => setNewServerIntegrationServiceConnectorName(e.target.value)}
          // style={{marginBottom: "16px"}}
          autoComplete="off"
          helperText="This name is used for routing refresh calls to the connector as well as being used for diagnostics. Ideally it should be unique amongst the connectors for the integration service."
        />
        
        <br />

        <TextInput
          id="new-server-integration-service-connector-user-id"
          name="new-server-integration-service-connector-user-id"
          type="text"
          labelText="Connector User ID"
          defaultValue={newServerIntegrationServiceConnectorUserId}
          onChange={e => setNewServerIntegrationServiceConnectorUserId(e.target.value)}
          // style={{marginBottom: "16px"}}
          autoComplete="off"
          helperText="If this is null, the integration daemon’s userId is used on requests to the Open Metadata Access Service (OMAS)."
        />
        
        <br />

        <TextInput
          id="new-server-integration-service-connection"
          name="new-server-integration-service-connection"
          type="text"
          labelText="Connection"
          defaultValue={newServerIntegrationServiceConnection}
          onChange={e => setNewServerIntegrationServiceConnection(e.target.value)}
          // style={{marginBottom: "16px"}}
          autoComplete="off"
        />
        
        <br />

        <TextInput
          id="new-server-integration-service-metadata-source"
          name="new-server-integration-service-metadata-source"
          type="text"
          labelText="Metadata Source (Fully Qualified Name)"
          defaultValue={newServerIntegrationServiceMetadataSource}
          onChange={e => setNewServerIntegrationServiceMetadataSource(e.target.value)}
          // style={{marginBottom: "16px"}}
          autoComplete="off"
          helperText="This is the qualified name of an appropriate software server capability stored in open metadata. This software server capability is accessed via the partner OMAS."
        />
        
        <br />

        <TextInput
          id="new-server-integration-service-refresh-time-interval"
          name="new-server-integration-service-refresh-time-interval"
          type="text"
          labelText="Refresh Time Interval"
          defaultValue={newServerIntegrationServiceRefreshTimeInterval}
          onChange={e => setNewServerIntegrationServiceRefreshTimeInterval(e.target.value)}
          // style={{marginBottom: "16px"}}
          autoComplete="off"
          helperText="The number of minutes between each call to the connector to refresh the metadata. Zero means that refresh is only called at server start up and whenever the refresh REST API request is made to the integration daemon."
        />
        
        <br />

        <Checkbox
          id="new-server-integration-service-uses-blocking-calls"
          name="new-server-integration-service-uses-blocking-calls"
          labelText="Uses Blocking Calls"
          checked={newServerIntegrationServiceUsesBlockingCalls}
          onChange={(isChecked) => setNewServerIntegrationServiceUsesBlockingCalls(isChecked)}
          // style={{marginBottom: "16px"}}
        />
        
        <br />

        <Dropdown
          id="new-server-integration-service-permitted-synchronization"
          name="new-server-integration-service-permitted-synchronization"
          titleText="Permitted Synchronization"
          label="Metadata Flows"
          items={["TO_THIRD_PARTY", "FROM_THIRD_PARTY", "BOTH_DIRECTIONS"]}
          selectedItem={newServerIntegrationServicePermittedSynchronization}
          onChange={({ selectedItem }) => setNewServerIntegrationServicePermittedSynchronization(selectedItem)}
          // style={{marginBottom: "16px"}}
        />
        
        <br />
        <br />
        
    </div>

  )

}