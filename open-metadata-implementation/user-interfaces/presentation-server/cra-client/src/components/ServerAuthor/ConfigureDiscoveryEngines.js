/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext } from "react";
import {
  Button,
  SelectableTile,
  TextInput,
} from "carbon-components-react";

import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";

export default function ConfigureDiscoveryEngines() {

  const {
    availableDiscoveryEngines,
    setSelectedDiscoveryEngines,
    newServerDiscoveryEngineRemoteServerName, setNewServerDiscoveryEngineRemoteServerName,
    newServerDiscoveryEngineRemoteServerURLRoot, setNewServerDiscoveryEngineRemoteServerURLRoot,
    discoveryEnginesFormStartRef,
  } = useContext(ServerAuthorContext);

  const handleDeselectAllDiscoveryEngines = () => {
    const boxes = document.getElementsByName('discovery-engines');
    for (let b = 0; b <= boxes.length - 1; b++) {
      const isChecked = boxes[b].checked;
      if (isChecked)
        boxes[b].click();
    };
  }

  const handleSelectAllDiscoveryEngines = () => {
    const boxes = document.getElementsByName('discovery-engines');
    for (let b = 0; b <= boxes.length - 1; b++) {
      const isChecked = boxes[b].checked;
      if (!isChecked)
        boxes[b].click();
    };
  }

  const updateDiscoveryEngines = () => {
    const boxes = document.getElementsByName('discovery-engines');
    const selectedDiscoveryEngines = Array.from(boxes).filter((box) => box.checked).map((box) => box.value);
    setSelectedDiscoveryEngines(selectedDiscoveryEngines);
  }

  return (

    <div style={{ textAlign: 'left' }}>

      <fieldset className="bx--fieldset" style={{ marginBottom: "32px" }}>
      
        <legend className="bx--label">Discovery Engine Client</legend>

        <TextInput
          id="new-server-discovery-engine-remote-server-url-root"
          name="new-server-discovery-engine-remote-server-url-root"
          type="text"
          labelText="Metadata Server URL Root"
          defaultValue={newServerDiscoveryEngineRemoteServerURLRoot}
          onChange={e => setNewServerDiscoveryEngineRemoteServerURLRoot(e.target.value)}
          style={{marginBottom: "16px"}}
          inline
          ref={discoveryEnginesFormStartRef}
          autoComplete="off"
        />

        <TextInput
          id="new-server-discovery-engine-remote-server-name"
          name="new-server-discovery-engine-remote-server-name"
          type="text"
          labelText="Metadata Server Name"
          defaultValue={newServerDiscoveryEngineRemoteServerName}
          onChange={e => setNewServerDiscoveryEngineRemoteServerName(e.target.value)}
          inline
          style={{marginBottom: "16px"}}
          autoComplete="off"
        />
        
      </fieldset>

      <fieldset className="bx--fieldset" style={{ marginBottom: "32px" }}>

        <legend className="bx--label">Discovery Engines</legend>

        <div className="bx--btn-set" style={{ marginBotton: "16px"}}>
          <Button
            kind="secondary"
            style={{margin: "4px auto"}}
            onClick={handleDeselectAllDiscoveryEngines}
          >
            Deselect All
          </Button>
          <Button
            kind="secondary"
            style={{margin: "4px auto"}}
            onClick={handleSelectAllDiscoveryEngines}
          >
            Select All
          </Button>
        </div>
        
        <div aria-label="selectable tiles" role="group">
          {availableDiscoveryEngines.map((s, i) => (
            <SelectableTile
              id={s.serviceURLMarker}
              key={s.serviceURLMarker}
              light={false}
              name={'discovery-engines'}
              selected={false}
              tabIndex={i}
              title={s.serviceName}
              value={s.serviceURLMarker}
              handleClick={updateDiscoveryEngines}
            >
              {s.serviceName}
            </SelectableTile>
          ))}
        </div>

      </fieldset>
    </div>

  )

}