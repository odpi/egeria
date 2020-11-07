/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext } from "react";
import {
  Button,
  SelectableTile,
  TextInput,
} from "carbon-components-react";

import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";

export default function ConfigureViewServices() {

  const {
    availableViewServices,
    setSelectedViewServices,
    newServerViewServiceRemoteServerName, setNewServerViewServiceRemoteServerName,
    newServerViewServiceRemoteServerURLRoot, setNewServerViewServiceRemoteServerURLRoot,
  } = useContext(ServerAuthorContext);

  const handleDeselectAllViewServices = () => {
    const boxes = document.getElementsByName('view-services');
    for (let b = 0; b <= boxes.length - 1; b++) {
      const isChecked = boxes[b].checked;
      if (isChecked)
        boxes[b].click();
    };
  }

  const handleSelectAllViewServices = () => {
    const boxes = document.getElementsByName('view-services');
    for (let b = 0; b <= boxes.length - 1; b++) {
      const isChecked = boxes[b].checked;
      if (!isChecked)
        boxes[b].click();
    };
  }

  const updateViewServices = () => {
    const boxes = document.getElementsByName('view-services');
    const selectedViewServices = Array.from(boxes).filter((box) => box.checked).map((box) => box.value);
    setSelectedViewServices(selectedViewServices);
  }

  return (

    <div style={{ textAlign: 'left' }}>

      <TextInput
        id="new-server-view-service-remote-server-name"
        name="new-server-view-service-remote-server-name"
        type="text"
        labelText="Open Metadata View Services (OMVS) Remote Server Name"
        defaultValue={newServerViewServiceRemoteServerName}
        onChange={e => setNewServerViewServiceRemoteServerName(e.target.value)}
        style={{marginBottom: "16px"}}
        autoComplete="off"
      />

      <TextInput
        id="new-server-view-service-remote-server-url-root"
        name="new-server-view-service-remote-server-url-root"
        type="text"
        labelText="Open Metadata View Services (OMVS) Remote Server URL Root"
        defaultValue={newServerViewServiceRemoteServerURLRoot}
        onChange={e => setNewServerViewServiceRemoteServerURLRoot(e.target.value)}
        style={{marginBottom: "16px"}}
        autoComplete="off"
      />

      <div className="bx--btn-set" style={{ marginBotton: "16px"}}>
        <Button
          kind="secondary"
          style={{margin: "4px auto"}}
          onClick={handleDeselectAllViewServices}
        >
          Deselect All
        </Button>
        <Button
          kind="secondary"
          style={{margin: "4px auto"}}
          onClick={handleSelectAllViewServices}
        >
          Select All
        </Button>
      </div>

      <fieldset className="bx--fieldset" style={{ marginBottom: "32px" }}>
        <div aria-label="selectable tiles" role="group">
          {availableViewServices.map((s, i) => (
            <SelectableTile
              id={s.serviceURLMarker}
              key={s.serviceURLMarker}
              light={false}
              name={'view-services'}
              selected={false}
              tabIndex={i}
              title={s.serviceName}
              value={s.serviceURLMarker}
              handleClick={updateViewServices}
            >
              {s.serviceName}
            </SelectableTile>
          ))}
        </div>
      </fieldset>
    </div>

  )

}