/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext } from "react";
import {
  Button,
  SelectableTile,
} from "carbon-components-react";

import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";

export default function ConfigureAccessServices() {

  const {
    availableAccessServices,
    setSelectedAccessServices,
  } = useContext(ServerAuthorContext);

  const handleDeselectAllAccessServices = () => {
    const boxes = document.getElementsByName('access-services');
    for (let b = 0; b <= boxes.length - 1; b++) {
      const isChecked = boxes[b].checked;
      if (isChecked)
        boxes[b].click();
    };
    updateAccessServices();
  }

  const handleSelectAllAccessServices = () => {
    const boxes = document.getElementsByName('access-services');
    for (let b = 0; b <= boxes.length - 1; b++) {
      const isChecked = boxes[b].checked;
      if (!isChecked)
        boxes[b].click();
    };
    updateAccessServices();
  }

  const updateAccessServices = () => {
    const boxes = document.getElementsByName('access-services');
    const selectedAccessServices = Array.from(boxes).filter((box) => box.checked).map((box) => box.value);
    setSelectedAccessServices(selectedAccessServices);
  }

  return (

    <div style={{ textAlign: 'left' }}>

      <div className="bx--btn-set">

        <Button
          kind="secondary"
          style={{margin: "4px auto"}}
          onClick={handleDeselectAllAccessServices}
        >
          Deselect All
        </Button>
        
        <Button
          kind="secondary"
          style={{margin: "4px auto"}}
          onClick={handleSelectAllAccessServices}
        >
          Select All
        </Button>

      </div>

      <fieldset className="bx--fieldset" style={{ marginBottom: "32px" }}>
        <div aria-label="selectable tiles" role="group">
          {availableAccessServices.map((s, i) => (
            <SelectableTile
              id={s.serviceURLMarker}
              key={s.serviceURLMarker}
              light={false}
              name={'access-services'}
              selected={true}
              tabIndex={i}
              title={s.serviceName}
              value={s.serviceURLMarker}
              handleClick={updateAccessServices}
            >
              {s.serviceName}
            </SelectableTile>
          ))}
        </div>
      </fieldset>
    </div>

  )

}