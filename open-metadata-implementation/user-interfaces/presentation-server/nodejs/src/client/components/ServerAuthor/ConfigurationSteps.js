/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import {
  ProgressIndicator,
  ProgressStep,
} from "carbon-components-react";

export default function ConfigurationSteps({ serverType, progressIndicatorIndex }) {

  const steps = [
    "Select server type",
    "Basic configuration",
    "Configure audit log destinations",
    "Preview configuration and deploy instance"
  ]

  switch(serverType) {

    case "Metadata Access Point":
    case "Metadata Server":
      steps.splice(2, 0, "Select access services");
      steps.splice(steps.length - 1, 0, "Register to a cohort");
      steps.splice(steps.length - 1, 0, "Configure the open metadata archives");
      break;

    case "Repository Proxy":
      steps.splice(steps.length - 1, 0, "Register to a cohort");
      steps.splice(steps.length - 1, 0, "Configure the open metadata archives");
      steps.splice(steps.length - 1, 0, "Configure the repository proxy connectors");
      break;

    case "Conformance Test Server":
      steps.splice(steps.length - 1, 0, "Register to a cohort");
      break;

    case "View Server":
      steps.splice(steps.length - 1, 0, "Configure the Open Metadata View Services (OMVS)");
      break;

    case "Discovery Server":
      steps.splice(steps.length - 1, 0, "Configure the discovery engine services");
      break;

    case "Security Sync Server":
      steps.splice(steps.length - 1, 0, "Configure the security sync services");
      break;

    case "Stewardship Server":
      steps.splice(steps.length - 1, 0, "Configure the stewardship engine services");
      break;

  }

  console.log({ steps });
  
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
          label={step}
          description={`Step ${i + 1}: OMAG server wizard`}
        />
      ))}
    </ProgressIndicator>

  )

}