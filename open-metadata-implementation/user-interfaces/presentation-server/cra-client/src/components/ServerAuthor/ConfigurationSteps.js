/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext } from "react";
import {
  ProgressIndicator,
  ProgressStep,
} from "carbon-components-react";
import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";

export default function ConfigurationSteps() {

  const {
    newServerLocalServerType,
    progressIndicatorIndex,
    serverConfigurationSteps
  } = useContext(ServerAuthorContext);

  const steps = serverConfigurationSteps(newServerLocalServerType);
  
  return (

    <ProgressIndicator
      id="config-progress-indicator"
      vertical={false}
      currentIndex={progressIndicatorIndex}
      spaceEqually={false}
      vertical={true}
      style={{marginTop: "98px"}}
    >
      {steps.map((step, i) => (
        <ProgressStep
          key={`server-configuration-step-${i}`}
          label={step}
          description={`Step ${i + 1}: OMAG server wizard`}
        />
      ))}
    </ProgressIndicator>

  )

}