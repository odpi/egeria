/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext } from "react";
import {
  Button,
  TextInput,
} from "carbon-components-react";
import {
  Add16,
  Subtract16,
} from "@carbon/icons-react";

import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";

export default function ConfigureOMArchives() {
  
  const { newServerOMArchives, setNewServerOMArchives, } = useContext(ServerAuthorContext);

  const handleAddOMArchive = e => {
    const archiveName = document.getElementById("new-server-archive-file-name").value;
    console.log("handleAddOMArchive() called", { archiveName });
    if (archiveName.length == 0) return;
    setNewServerOMArchives(newServerOMArchives.concat(archiveName));
    document.getElementById("new-server-archive-file-name").value = "";
  }

  const handleRemoveOMArchive = index => {
    console.log("handleRemoveOMArchive() called", { index });
    const archiveList = newServerOMArchives.filter((v, i) => { return i !== index });
    setNewServerOMArchives(archiveList);
  }

  return (

    <div style={{ textAlign: 'left' }}>

      <ul style={{marginBottom: "32px"}}>

        {newServerOMArchives.map((archive, i) => (
          <li key={`archive-${i}`} style={{display: "flex"}}>
            {archive}
            <Button
              hasIconOnly
              kind="tertiary"
              size="small"
              renderIcon={Subtract16}
              iconDescription="Remove"
              tooltipAlignment="start"
              tooltipPosition="right"
              onClick={() => handleRemoveOMArchive(i)}
            />
          </li>
        ))}

      </ul>

      <div style={{display: "flex"}}>

        <TextInput
          id="new-server-archive-file-name"
          name="new-server-archive-file-name"
          type="text"
          labelText="Archive File"
          inline={true}
          style={{display: "inline-block"}}
          autoComplete="off"
        />

        <Button
          kind="tertiary"
          size="field"
          renderIcon={Add16}
          iconDescription="Add"
          tooltipAlignment="start"
          tooltipPosition="right"
          onClick={handleAddOMArchive}
          style={{display: "inline-block"}}
        >
          Add
        </Button>

      </div>

    </div>

  )

}