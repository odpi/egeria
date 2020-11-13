/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext } from "react";
import {
  Button,
  SelectableTile,
  TextInput,
} from "carbon-components-react";

import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";

export default function ConfigureStewardshipEngines() {

  const {
    availableStewardshipEngines,
    setSelectedStewardshipEngines,
    newServerStewardshipEngineRemoteServerName, setNewServerStewardshipEngineRemoteServerName,
    newServerStewardshipEngineRemoteServerURLRoot, setNewServerStewardshipEngineRemoteServerURLRoot,
    stewardshipEnginesFormStartRef,
  } = useContext(ServerAuthorContext);

  const handleDeselectAllStewardshipEngines = () => {
    const boxes = document.getElementsByName('stewardship-engines');
    for (let b = 0; b <= boxes.length - 1; b++) {
      const isChecked = boxes[b].checked;
      if (isChecked)
        boxes[b].click();
    };
    updateStewardshipEngines();
  }

  const handleSelectAllStewardshipEngines = () => {
    const boxes = document.getElementsByName('stewardship-engines');
    for (let b = 0; b <= boxes.length - 1; b++) {
      const isChecked = boxes[b].checked;
      if (!isChecked)
        boxes[b].click();
    };
    updateStewardshipEngines();
  }

  const updateStewardshipEngines = () => {
    const boxes = document.getElementsByName('stewardship-engines');
    const selectedStewardshipEngines = Array.from(boxes).filter((box) => box.checked).map((box) => box.value);
    setSelectedStewardshipEngines(selectedStewardshipEngines);
  }

  return (

    <div style={{ textAlign: 'left' }}>

      <fieldset className="bx--fieldset" style={{ marginBottom: "32px" }}>
      
        <legend className="bx--label">Stewardship Engine Client</legend>

        <TextInput
          id="new-server-stewardship-engine-remote-server-url-root"
          name="new-server-stewardship-engine-remote-server-url-root"
          type="text"
          labelText="Metadata Server URL Root"
          defaultValue={newServerStewardshipEngineRemoteServerURLRoot}
          onChange={e => setNewServerStewardshipEngineRemoteServerURLRoot(e.target.value)}
          style={{marginBottom: "16px"}}
          inline
          ref={stewardshipEnginesFormStartRef}
          autoComplete="off"
        />

        <TextInput
          id="new-server-stewardship-engine-remote-server-name"
          name="new-server-stewardship-engine-remote-server-name"
          type="text"
          labelText="Metadata Server Name"
          defaultValue={newServerStewardshipEngineRemoteServerName}
          onChange={e => setNewServerStewardshipEngineRemoteServerName(e.target.value)}
          inline
          style={{marginBottom: "16px"}}
          autoComplete="off"
        />
        
      </fieldset>

      <fieldset className="bx--fieldset" style={{ marginBottom: "32px" }}>

        <legend className="bx--label">Stewardship Engines</legend>

        <div className="bx--btn-set" style={{ marginBotton: "16px"}}>
          <Button
            kind="secondary"
            style={{margin: "4px auto"}}
            onClick={handleDeselectAllStewardshipEngines}
          >
            Deselect All
          </Button>
          <Button
            kind="secondary"
            style={{margin: "4px auto"}}
            onClick={handleSelectAllStewardshipEngines}
          >
            Select All
          </Button>
        </div>
        
        <div aria-label="selectable tiles" role="group">
          {availableStewardshipEngines.map((s, i) => (
            <SelectableTile
              id={s.serviceURLMarker}
              key={s.serviceURLMarker}
              light={false}
              name={'stewardship-engines'}
              selected={false}
              tabIndex={i}
              title={s.serviceName}
              value={s.serviceURLMarker}
              handleClick={updateStewardshipEngines}
            >
              {s.serviceName}
            </SelectableTile>
          ))}
        </div>

      </fieldset>
    </div>

  )

}