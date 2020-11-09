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

export default function RegisterCohorts() {
  
  const { newServerCohorts, setNewServerCohorts, } = useContext(ServerAuthorContext);

  const handleAddCohort = e => {
    const cohortName = document.getElementById("new-server-cohort-name").value;
    console.log("handleAddCohort() called", { cohortName });
    if (cohortName.length == 0) return;
    setNewServerCohorts(newServerCohorts.concat(cohortName));
    document.getElementById("new-server-cohort-name").value = "";
  }

  const handleRemoveCohort = index => {
    console.log("handleRemoveCohort() called", { index });
    const cohortList = newServerCohorts.filter((v, i) => { return i !== index });
    setNewServerCohorts(cohortList);
  }

  return (

    <div style={{ textAlign: 'left' }}>

      <ul style={{marginBottom: "32px"}}>

        {newServerCohorts.map((cohort, i) => (
          <li key={`cohort-${i}`} style={{display: "flex"}}>
            {cohort}
            <Button
              hasIconOnly
              kind="tertiary"
              size="small"
              renderIcon={Subtract16}
              iconDescription="Remove"
              tooltipAlignment="start"
              tooltipPosition="right"
              onClick={() => handleRemoveCohort(i)}
            />
          </li>
        ))}

      </ul>

      <div style={{display: "flex"}}>

        <TextInput
          id="new-server-cohort-name"
          name="new-server-cohort-name"
          type="text"
          labelText="Cohort Name"
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
          onClick={handleAddCohort}
          style={{display: "inline-block"}}
        >
          Add
        </Button>
        
      </div>

    </div>

  )

}