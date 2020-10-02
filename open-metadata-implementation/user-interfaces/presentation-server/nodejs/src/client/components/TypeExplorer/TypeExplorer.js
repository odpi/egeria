/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, {useEffect, useRef, useState}     from "react";

/* 
 * Import the DEFAULT export from the InteractionContext module - which is actually the InteractionContextProvider
 * Naming it explicitly for clarity that this is the provider not the context.
 */
import InteractionContextProvider               from "./contexts/InteractionContext";

/* 
 * Import the DEFAULT export from the RequestContext module - which is actually the RequestContextProvider
 * Naming it explicitly for clarity that this is the provider not the context.
 */
import RequestContextProvider                   from "./contexts/RequestContext";

/* 
 * Import the DEFAULT export from the TypesContext module - which is actually the TypesContextProvider
 * Naming it explicitly for clarity that this is the provider not the context.
 */
import TypesContextProvider                     from "./contexts/TypesContext";

/* 
 * Import the DEFAULT export from the FocusContext module - which is actually the FocusContextProvider
 * Naming it explicitly for clarity that this is the provider not the context.
 */
import FocusContextProvider                     from "./contexts/FocusContext";

import ServerSelector                           from "./components/resource-selection/ServerSelector";

import EnterpriseControl                        from "./components/resource-selection/EnterpriseControl";

import FocusControls                            from "./components/focus-controls/FocusControls";

import DetailsPanel                             from "./components/details-panel/DetailsPanel";

import DiagramManager                           from "./components/diagram/DiagramManager";

import ReadmeHandler                            from "./ReadmeHandler";

import QuestionMarkImage                        from "./question-mark-32.png";

import ReadmeMarkdown                           from './README.md';


import "./tex.scss";



export default function TypeExplorer() {

  

  const containerDiv = useRef();

  /*
   * Height and width are stateful, so will cause a re-render.
   */
  const [cltHeight, setCltHeight] = useState(document.documentElement.clientHeight);  
  const [cltWidth, setCltWidth]   = useState(document.documentElement.clientWidth);  

  const [readme, setReadme]             = useState( { markdown : '' } );
  const [readmeStatus, setReadmeStatus] = useState("idle");

  

  let workingHeight = cltHeight - 50;
  let workingWidth  = cltWidth - 265;

  /*
   * Do not set the containerDiv dimensions until AFTER the cpt has rendered, as this creates the containerDiv
   */
  const updateSize = () => {

    /*
     * Determine client height, width and set container dimensions 
     */    
    setCltHeight(document.documentElement.clientHeight);
    workingHeight = cltHeight - 50;
    containerDiv.current.style.height=""+workingHeight+"px";

    setCltWidth(document.documentElement.clientWidth);
    workingWidth = cltWidth - 265;
    containerDiv.current.style.width=""+workingWidth+"px";
  }

  const displayReadme = () => {
    setReadmeStatus("complete");
  }

  const cancelReadmeModal = () => {
    setReadmeStatus("idle");
  };

  const submitReadmeModal = () => {
    setReadmeStatus("idle");
  };

  
  /*
   * useEffect to set size of container... 
   */
  useEffect(
    () => {
      /* Attach event listener for resize events */
      window.addEventListener('resize', updateSize);
      /* Ensure the size gets updated on this load */
      updateSize();
      /* On unmount, remove the event listener. */
      return () => window.removeEventListener('resize', updateSize);
    }
  )

   /*
    * useEffect to load markdown readme file
    */
  useEffect(
    () => {
      /* Get the content of the markdown file and save it in 'readme' */
      fetch(ReadmeMarkdown).then(res => res.text()).then(text => setReadme({ markdown: text }));
    }, 
    [] /* run effect once only */
  )



  return (

    <div className="tex-container" ref={containerDiv}>

      <InteractionContextProvider>
        <RequestContextProvider>
            <TypesContextProvider>
              <FocusContextProvider>

                <div className="tex-top">

                  <div className="title">
                    <p>Type Explorer</p>

                    <input type="image"  src={QuestionMarkImage}
                       onClick = { () => displayReadme() }  >
                    </input>

                    <ReadmeHandler   status              = { readmeStatus }
                                     readme              = { readme }
                                     onCancel            = { cancelReadmeModal }
                                     onSubmit            = { submitReadmeModal } />

                    <EnterpriseControl/>
                 
                  </div>

                  <div className="tex-top-left">
                    <ServerSelector />
                  </div>

                  <div className="tex-top-middle">
                    <FocusControls />
                  </div>        
                </div>

                <div className="tex-content">

                  <div className="tex-lhs">
                    <hr />
                    <DetailsPanel />
                  </div>

                  <div className="tex-rhs">
                    <DiagramManager height={workingHeight-270} width={workingWidth-500}/>
                  </div>

                </div>

              </FocusContextProvider>
            </TypesContextProvider>
          </RequestContextProvider>
      </InteractionContextProvider>
    </div>


  );
}

