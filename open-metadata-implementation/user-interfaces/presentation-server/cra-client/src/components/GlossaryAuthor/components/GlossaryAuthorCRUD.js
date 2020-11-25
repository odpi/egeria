/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import GlossaryAuthorCRUDContext from "../contexts/GlossaryAuthorCRUDContext";
import Egeria_project_32 from "../../../images/Egeria_project_32";
import Egeria_glossary_32 from "../../../images/Egeria_glossary_32";
import MyNodeView from "./views/MyNodeView";
import GlossaryAuthorNodes from "./GlossaryAuthorNodes";
/**
 * CRUD Glossary author UI
 */
export default function GlossaryAuthorCRUD() {
  return (
    <div>
        <GlossaryAuthorCRUDContext>
          <div className='my-container'>
            <span className='my-item'>
              Current Glossary
              <Egeria_glossary_32 />
              <MyNodeView typeKey="glossary" />
            </span>
            <span className='my-item'>
              Current Project
              <Egeria_project_32 />
              <MyNodeView typeKey="project" />
            </span>
          </div>
          <GlossaryAuthorNodes />
        </GlossaryAuthorCRUDContext>
    </div>
  );
}
