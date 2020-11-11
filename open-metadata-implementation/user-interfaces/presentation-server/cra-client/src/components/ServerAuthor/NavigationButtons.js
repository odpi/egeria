/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext } from "react";
import {
  Button
} from "carbon-components-react";
import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";

export default function NavigationButtons({ handlePreviousStep, handleNextStep }) {

  const {
    newServerLocalServerType,
    progressIndicatorIndex,
    serverConfigurationSteps,
    hideConfigForm,
  } = useContext(ServerAuthorContext);

  const steps = serverConfigurationSteps(newServerLocalServerType);

  // First step

  if (progressIndicatorIndex == 0) {

    return (
      <div className="bx--btn-set">
        <Button
          kind="danger"
          style={{margin: "16px auto"}}
          onClick={hideConfigForm}
        >
          Cancel Configuration
        </Button>
        <Button
          type="submit"
          kind="primary"
          style={{margin: "16px auto"}}
          onClick={handleNextStep}
        >
          Proceed to {steps[progressIndicatorIndex + 1].toLowerCase()}
        </Button>
      </div>
    )

  }

  // Last step

  if (progressIndicatorIndex == steps.length - 1) {

    return (

      <div className="bx--btn-set">
        <Button
          kind="danger"
          style={{margin: "16px auto"}}
          onClick={hideConfigForm}
        >
          Cancel Configuration
        </Button>
        <Button
          kind="secondary"
          style={{margin: "16px auto"}}
          onClick={handlePreviousStep}
        >
          Back to {steps[progressIndicatorIndex - 1].toLowerCase()}
        </Button>
        <Button
          type="submit"
          kind="primary"
          style={{margin: "16px auto"}}
          onClick={handleNextStep}
        >
          Deploy instance
        </Button>
      </div>
  
    )

  }

  // In between step
  
  return (

    <div className="bx--btn-set">
      <Button
        kind="danger"
        style={{margin: "16px auto"}}
        onClick={hideConfigForm}
      >
        Cancel Configuration
      </Button>
      <Button
        kind="secondary"
        style={{margin: "16px auto"}}
        onClick={handlePreviousStep}
      >
        Back to {steps[progressIndicatorIndex - 1].toLowerCase()}
      </Button>
      <Button
        type="submit"
        kind="primary"
        style={{margin: "16px auto"}}
        onClick={handleNextStep}
      >
        Proceed to {steps[progressIndicatorIndex + 1].toLowerCase()}
      </Button>
    </div>

  )

}