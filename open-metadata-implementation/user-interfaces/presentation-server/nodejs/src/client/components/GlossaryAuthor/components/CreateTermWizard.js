/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import {
  ProgressIndicator,
  ProgressStep,
  Button,
} from "carbon-components-react";

export default function CreateTermWizard(props) {
  const [currentStepIndex, setCurrentStepIndex] = useState(0);
  const [glossaryGuid, setGlossaryguid] = useState();
  console.log("CreateTermWizard");

  const handleOnClick = (e) => {
    e.preventDefault();
    if (currentStepIndex == 0) {
      setCurrentStepIndex(1);
      setGlossaryGuid("test");
    }
  }
  const validateGlossaryForm = () => {
    let isValid = false;
    if (glossaryGuid) {
      isValid= true;
    }
    return isValid;
  }
  return (
    <div className="some-container">
      <ProgressIndicator currentIndex={currentStepIndex}>
        <ProgressStep
          label="Set Glossary"
          description="Step 1: A glossary needs to be chosem becase a term can be created"
        />
        <ProgressStep
          disabled={!validateGlossaryForm()}
          label="Create Term"
          description="Create Term in the chosen Glossary"
        />
      </ProgressIndicator>
      <div>
        <Button
                type="submit"
                onClick={handleOnClick}
                disabled={!validateGlossaryForm()}
              >Next</Button>    
      </div>
    </div>
  );
}
