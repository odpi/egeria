/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext } from "react";
import {
  RadioTile,
  TextInput,
  TileGroup,
} from "carbon-components-react";

import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";

export default function BasicConfig() {
  
  const {
    newServerName, setNewServerName,
    newServerLocalURLRoot, setNewServerLocalURLRoot,
    newServerLocalUserId, setNewServerLocalUserId,
    newServerLocalPassword, setNewServerLocalPassword,
    newServerMaxPageSize, setNewServerMaxPageSize,
    newServerSecurityConnector, setNewServerSecurityConnector,
    newServerLocalServerType,
    newServerRepository, setNewServerRepository,
    basicConfigFormStartRef,
  } = useContext(ServerAuthorContext);

  return (

    <div style={{ textAlign: "left" }}>

      <fieldset className="bx--fieldset" style={{ marginBottom: "32px" }}>

        <TextInput
          id="new-server-name"
          name="new-server-name"
          type="text"
          labelText="Server name"
          value={newServerName}
          onChange={e => setNewServerName(e.target.value)}
          placeholder="cocoMDS1"
          invalid={newServerName == ""}
          style={{marginBottom: "16px", width: "100%"}}
          ref={basicConfigFormStartRef}
          autoComplete="off"
        />

        <TextInput
          id="new-server-local-url-root"
          name="new-server-local-url-root"
          type="text"
          labelText="Local URL root"
          value={newServerLocalURLRoot}
          onChange={e => setNewServerLocalURLRoot(e.target.value)}
          placeholder="https://localhost:9443"
          invalid={newServerLocalURLRoot == ""}
          style={{marginBottom: "16px"}}
        />

        <TextInput
          id="new-server-local-user-id"
          name="new-server-local-user-id"
          type="text"
          labelText="Local user ID"
          value={newServerLocalUserId}
          onChange={e => setNewServerLocalUserId(e.target.value)}
          placeholder="myMetadataServerUserId"
          invalid={newServerLocalUserId == ""}
          style={{marginBottom: "16px"}}
          autoComplete="off"
        />

        <TextInput.PasswordInput
          id="new-server-local-password"
          name="new-server-local-password"
          labelText="Local password"
          value={newServerLocalPassword}
          onChange={e => setNewServerLocalPassword(e.target.value)}
          placeholder="myMetadataServerPassword"
          invalid={newServerLocalPassword == ""}
          style={{marginBottom: "16px"}}
          autoComplete="new-password"
        />

        <TextInput
          id="new-server-max-page-size"
          name="new-server-max-page-size"
          type="text"
          labelText="Max page size"
          value={newServerMaxPageSize}
          onChange={e => setNewServerMaxPageSize(e.target.value)}
          placeholder="1000"
          invalid={newServerMaxPageSize == ""}
          style={{marginBottom: "16px"}}
        />

        <TextInput
          id="new-server-security-connector"
          name="new-server-security-connector"
          type="text"
          labelText="Security Connector (Class Name)"
          value={newServerSecurityConnector}
          onChange={e => setNewServerSecurityConnector(e.target.value)}
          placeholder="Fully Qualified Java Class Name"
          helperText="Note: This field is optional. Leave blank to skip."
          style={{marginBottom: "16px"}}
        />

      </fieldset>

      {
        // If server type is Metadata Server, show local repository tiles
        (newServerLocalServerType == "Metadata Server") &&
        <fieldset className="bx--fieldset" style={{ marginBottom: "32px" }}>
          <legend className="bx--label" style={{ textAlign: "left" }}>Server repository type</legend>
          <TileGroup
            defaultSelected="in-memory-repository"
            name="repository-types"
            valueSelected={newServerRepository}
            legend=""
            onChange={value => setNewServerRepository(value)}
            style={{marginTop: "16px", textAlign: "left"}}
          >
            <RadioTile
              id="in-memory-repository"
              light={false}
              name="in-memory-repository"
              tabIndex={0}
              value="in-memory-repository"
            >
              In Memory
            </RadioTile>
            <RadioTile
              id="local-graph-repository"
              light={false}
              name="local-graph-repository"
              tabIndex={1}
              value="local-graph-repository"
            >
              Janus Graph
            </RadioTile>
            <RadioTile
              id="read-only-repository"
              light={false}
              name="read-only-repository"
              tabIndex={2}
              value="read-only-repository"
            >
              Read Only
            </RadioTile>
          </TileGroup>
        </fieldset>
      }

    </div>

  )

}