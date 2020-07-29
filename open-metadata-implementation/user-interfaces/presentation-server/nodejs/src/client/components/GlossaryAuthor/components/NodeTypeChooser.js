/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";
import { GlossaryAuthorContext } from "../contexts/GlossaryAuthorContext";
/**
 * This component is driven off the state of my project and glossary and whetehr they are being editted.
 * In the cases where we are editting one of them then some text is displayed indicating what is being editted.
 * In the case where the My glossary and my project are set and we are not editing them, then we display a dropdown
 * for the user to choose the type of node they want to author.
 * 
 * All of the conditional checks on the context should be mutually exclusive.
 */
const NodeTypeChooser = () => {
  console.log("GlossaryAuthorNodes");

  const glossaryAuthorContext = useContext(GlossaryAuthorContext);

  const handleNodeChange = (e) => {
    const value = e.target.value;
    console.log("handleSelectChange (() " + value);
    glossaryAuthorContext.doSetNodeType(value);
  };
  return (
    <div>
      {/* case one nothing set no editing being attempted  */}
      {glossaryAuthorContext.myGlossary === undefined &&
        !glossaryAuthorContext.isEdittingMyGlossary() &&
       (
          <div>
            Current Glossary needs to be set; as authored content will
            be stored there.
          </div>
        )}
      {/* case two nothing set, editting my project  */}
      { glossaryAuthorContext.isEdittingMyProject() && (
          <div>
            Creating Current Project.
          </div>
        )}
      {/* case three nothing set, editting my glossary  */}
      { glossaryAuthorContext.myGlossary &&
        glossaryAuthorContext.isEdittingMyGlossary() && (
          <div>
            Creating Current Glossary.
          </div>
        )}
      {glossaryAuthorContext.isSetupComplete() && (
          <div>
            <div>Choose what to author:</div>
            <div className="bx--col-lg-2 bx--col-md-2">
              <div margin="0 auto">
                <select
                  id="nodes"
                  float="right"
                  border-bottom-width="3px"
                  onChange={handleNodeChange}
                >
                  <option value="term">Term</option>
                  <option value="category">Category</option>
                  <option selected value="glossary">
                    Glossary
                  </option>
                  <option value="project">Project</option>
                </select>
              </div>
            </div>
          </div>
        )}
    </div>
  );
};

export default NodeTypeChooser;
