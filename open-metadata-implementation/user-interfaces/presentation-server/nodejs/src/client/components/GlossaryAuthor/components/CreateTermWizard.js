/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import {
  ProgressIndicator,
  ProgressStep,
  Button,
} from "carbon-components-react";
import StartingNodeNavigation from "./StartingNodeNavigation";
import CreateNode from "./CreateNode";
import getNodeType from "./properties/NodeTypes.js";

export default function CreateTermWizard(props) {
  const [currentStepIndex, setCurrentStepIndex] = useState(0);
  const [glossaryGuid, setGlossaryGuid] = useState();
  const [termCreated, setTermCreated] = useState();
  console.log("CreateTermWizard");

  const handleChoseGlossaryOnClick = (e) => {
    e.preventDefault();
    if (currentStepIndex == 0) {
      setCurrentStepIndex(1);
    }
  };
  const handleReChooseGlossaryOnClick = (e) => {
    e.preventDefault();
    if (currentStepIndex == 1) {
      setCurrentStepIndex(0);
    }
  };
  const validateGlossaryForm = () => {
    let isValid = false;
    if (glossaryGuid) {
      isValid = true;
    }
    return isValid;
  };
  const onGlossarySelect = (guid) => {
    setGlossaryGuid(guid);
  };
  const onCreate = () => {
    setTermCreated(true);
  };

  return (
    <div>
      <h1 Create Term Wizard />
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
        {currentStepIndex == 0 && (
          <Button
            kind="secondary"
            onClick={handleChoseGlossaryOnClick}
            disabled={!validateGlossaryForm()}
          >
            Next
          </Button>
        )}
        {currentStepIndex == 0 && !termCreated && (
          <h3>Choose a Glossary that you want the Term to be created in.</h3>
        )}
        {currentStepIndex == 0 && (
          <StartingNodeNavigation
            match={props.match}
            nodeTypeName="glossary"
            onSelectCallback={onGlossarySelect}
          />
        )}
        {currentStepIndex == 1 && !termCreated && (
          <div>
            <Button kind="secondary" onClick={handleReChooseGlossaryOnClick}>
              Previous
            </Button>
            <h3>Create the Term in the chosen Glossary.</h3>
          </div>
        )}
        {currentStepIndex == 1 && (
          <div>
            <CreateNode
              currentNodeType={getNodeType("term")}
              glossaryGuid={glossaryGuid}
              onCreateCallback={onCreate}
            />
          </div>
        )}
      </div>
    </div>
  );
}
