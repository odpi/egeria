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
        glossaryAuthorContext.myProject === undefined &&
        glossaryAuthorContext.isEdittingMyGlossary() &&
        glossaryAuthorContext.isEdittingMyProject() && (
          <div>
            My Project and My Glossary need to be set; as authored content will
            be stored there (1).
          </div>
        )}
      {/* case two nothing set, editting my project  */}
      {glossaryAuthorContext.myProject === undefined &&
        glossaryAuthorContext.myGlossary === undefined &&
        glossaryAuthorContext.isEdittingMyProject() && (
          <div>
            Changing My Project (2).
          </div>
        )}
      {/* case three nothing set, editting my glossary  */}
      {glossaryAuthorContext.myProject === undefined &&
        glossaryAuthorContext.myGlossary === undefined &&
        glossaryAuthorContext.isEdittingMyGlossary() && (
          <div>
            Changing My Glossary (3).
          </div>
        )}
      {/* case four for my project and glossary are set, changing project */}
      {glossaryAuthorContext.myProject &&
        glossaryAuthorContext.myGlossary &&
        glossaryAuthorContext.isEdittingMyProject() && (
          <div>
            Changing My Project (4).
          </div>
        )}
      {/* case five for my project and glossary are set, changing glossary */}
      {glossaryAuthorContext.myProject &&
        glossaryAuthorContext.myGlossary &&
        glossaryAuthorContext.isEdittingMyProject() && (
          <div>
            Changing My Glossary (5).
          </div>
        )}
      {/* case six for my project set and glossary not set, changing project */}
      {glossaryAuthorContext.myProject &&
        glossaryAuthorContext.myGlossary === undefined &&
        glossaryAuthorContext.isEdittingMyProject() && (
          <div>
            Changing My Project (6).
          </div>
        )}
      {/* case seven my glossary set and project not set, changing project */}
      {glossaryAuthorContext.myGlossary &&
        glossaryAuthorContext.myProject === undefined &&
        glossaryAuthorContext.isEdittingMyProject() && (
          <div>
            Changing My Project (7).
          </div>
        )}

      {/* case eight for my project set and glossary not set, changing glossary */}
      {glossaryAuthorContext.myProject &&
        glossaryAuthorContext.myGlossary === undefined &&
        glossaryAuthorContext.isEdittingMyGlossary() && (
          <div>
            Changing My Glossary (8).
          </div>
        )}
      {/* case nine my glossary set and project not set, changing glossary */}
      {glossaryAuthorContext.myGlossary &&
        glossaryAuthorContext.myProject === undefined &&
        glossaryAuthorContext.isEdittingMyGlossary() && (
          <div>
            Changing My Glossary (9).
          </div>
        )}
      {/* case nine my glossary set and project are set and are not being editted. 
      Authoring of other nodes that are not myProject or or My Glossary */}
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
