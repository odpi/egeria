/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext, useReducer } from "react";
import {
  Button,
  Checkbox,
} from "carbon-components-react";

import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";
import auditLogDestinations from "./defaults/auditLogDestinations";
import auditLogSeverities from "./defaults/auditLogSeverities";

export default function ConfigureAuditLog({ previousAction, nextAction }) {

  if (!previousAction || !nextAction) {
    throw new Error("ConfigureAuditLog component requires both a previousAction and a nextAction property.");
  }

  const {
    newServerLocalServerType,
    progressIndicatorIndex,
    serverConfigurationSteps
  } = useContext(ServerAuthorContext);

  const steps = serverConfigurationSteps(newServerLocalServerType);

  const previousIndex = (progressIndicatorIndex > 0) ? progressIndicatorIndex - 1 : 0;
  const nextIndex = (progressIndicatorIndex < steps.length - 1) ? progressIndicatorIndex + 1 : steps.length - 1;

  // Initial State has no destinations selected, but all severities selected
  const initialState = auditLogDestinations.map((d, i) => {
    return {
      ...d,
      selected: d.id == "default",
      severities: d.id == "default" ? [] : auditLogSeverities.map((s) => {
        return {
          ...s,
          selected: true
        }
      })
    }
  })

  const reducer = (destinations, action) => {
    if (action.type == 'selectDestination') {
      return destinations.map(dest => {
        if (dest.id == action.destination) {
          dest.selected = true;
        }
        return dest;
      });
    }
    if (action.type == 'deselectDestination') {
      return destinations.map(destination => {
        if (destination.id == action.destination) {
          destination.selected = false;
        }
        return destination;
      });
    }
    if (action.type == 'selectSeverity') {
      return destinations.map(dest => {
        if (dest.id == action.destination) {
          dest.severities = dest.severities.map(sev => {
            if (sev.id == action.severity) {
              sev.selected = true;
            }
            return sev;
          });
        }
        return dest;
      });
    }
    if (action.type == 'deselectSeverity') {
      return destinations.map(dest => {
        if (dest.id == action.destination) {
          dest.severities = dest.severities.map(sev => {
            if (sev.id == action.severity) {
              sev.selected = false;
            }
            return sev;
          });
        }
        return dest;
      });
    }
  }
  
  const [state, dispatch] = useReducer(reducer, initialState);

  const handleClick = async () => {
    console.log({
      action: nextAction,
      state,
    })
    await nextAction(state);
  }

  return (

    <div style={{ textAlign: 'left' }}>

      <fieldset className="bx--fieldset" style={{ marginBottom: "32px" }}>
        <legend className="bx--label">Audit Log Destinations</legend>
        {state.map((destination, i) => (
          <div key={`audit-log-destination-wrapper-${i}`}>
            <Checkbox
              key={`audit-log-destination-${i}`}
              labelText={destination.label}
              id={destination.id}
              checked={destination.selected}
              onChange={(value, id, e) => dispatch({ type: value ? 'selectDestination' : 'deselectDestination', destination: id })}
            />
            {(destination.selected && destination.severities.length > 0) ?
              <div style={{ marginLeft: "32px" }}>
                {destination.severities.map((severity, j) => (
                  <Checkbox
                    key={`audit-log-severity-${j}`}
                    labelText={severity.label}
                    id={severity.id}
                    checked={severity.selected}
                    onChange={(value, id, e) => dispatch({ type: value ? 'selectSeverity' : 'deselectSeverity', destination: destination.id, severity: id })}
                  />
                ))}
              </div> : null
            }
          </div>
        ))}
      </fieldset>

      <div className="bx--btn-set">
        <Button
          kind="secondary"
          style={{margin: "16px auto"}}
          onClick={previousAction}
        >
          Back to {steps[previousIndex].toLowerCase()}
        </Button>
        <Button
          type="submit"
          kind="primary"
          style={{margin: "16px auto"}}
          onClick={() => handleClick()}
        >
          Proceed to {steps[nextIndex].toLowerCase()}
        </Button>
      </div>

    </div>

  )

}