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
 * Import the DEFAULT export from the ResourcesContext module - which is actually the ResourcesContextProvider
 * Naming it explicitly for clarity that this is the provider not the context.
 */
import ResourcesContextProvider                 from "./contexts/ResourcesContext";

import ServerSelector                           from "./components/resource-selection/ServerSelector";

import PlatformSelector                         from "./components/resource-selection/PlatformSelector";

import GraphControls                            from "./components/graph-controls/GraphControls";

import DetailsPanel                             from "./components/details-panel/DetailsPanel";

import DiagramManager                           from "./components/diagram/DiagramManager";

import HelpHandler                              from "./HelpHandler";

import QuestionMarkImage                        from "./question-mark-32.png";

import HelpMarkdown                             from './HELP.md';

import "./dino.scss";




export default function Dino() {


  const containerDiv = useRef();

  /*
   * Height and width are stateful, so will cause a re-render.
   */
  const [dimensions, setDimensions] = useState({cltWidth  : document.documentElement.clientWidth,
                                                cltHeight : document.documentElement.clientHeight });

  const [help, setHelp]             = useState( { markdown : '' } );
  const [helpStatus, setHelpStatus] = useState("idle");

  let workingHeight = dimensions.cltHeight - 50;
  let workingWidth  = dimensions.cltWidth - 265;

  /*
   * Do not set the containerDiv dimensions until AFTER the cpt has first rendered, as this creates the containerDiv
   */
  if (containerDiv.current) {
    containerDiv.current.style.width=""+workingWidth+"px";
    containerDiv.current.style.height=""+workingHeight+"px";
  }


  /*
   * Window resize event handler
   */
  const updateSize = () => {

    /*
     * Determine client height, width and set container and diagram dimensions then set dimensions.
     * The setDimensions is to ensure that we trigger a re-render.
     */
    let newClientWidth  = document.documentElement.clientWidth;
    let newClientHeight = document.documentElement.clientHeight;

    let workingWidth  = newClientWidth - 265;
    let workingHeight = newClientHeight - 50;

    containerDiv.current.style.width=""+workingWidth+"px";
    containerDiv.current.style.height=""+workingHeight+"px";

    let newDimensions = {cltWidth  : newClientWidth,
                         cltHeight : newClientHeight };

    setDimensions(newDimensions);

  }

  const displayHelp = () => {
    setHelpStatus("complete");
  }

  const cancelHelpModal = () => {
    setHelpStatus("idle");
  };

  const submitHelpModal = () => {
    setHelpStatus("idle");
  };


  /*
   * useEffect to set size of container... 
   */
  useEffect(
    () => {

      /* Attach event listener for resize events */
      window.addEventListener('resize', updateSize);

      /* On unmount, remove the event listener. */
      return () => window.removeEventListener('resize', updateSize);
    },
    [] /* run effect once only */
  )

  /*
   * useEffect to load markdown help file
   */
  useEffect(
    () => {
      // Get the content of the markdown file and save it in 'help'.
      fetch(HelpMarkdown).then(res => res.text()).then(text => setHelp({ markdown: text }));
    }, 
    [] /* run effect once only */
  )


  return (

    <div className="dino-container" ref={containerDiv}>

      <InteractionContextProvider>
        <RequestContextProvider>
          <ResourcesContextProvider>
          
              <div className="dino-top">

                <div className="title">
                  <p>Dino</p>
             
                  <input type="image"  src={QuestionMarkImage}
                     onClick = { () => displayHelp() }  >
                  </input>

                  <HelpHandler   status              = { helpStatus }
                                 help                = { help }
                                 onCancel            = { cancelHelpModal }
                                 onSubmit            = { submitHelpModal } />

                </div>

                <div className="dino-top-left">
                  <PlatformSelector />
                </div>

                <div className="dino-top-middle">
                  <ServerSelector />
                </div>
                 
              </div>

              <div className="dino-content">

                <div className="dino-lhs">
                  <hr />
                  <GraphControls />
                  <hr />
                  <DetailsPanel />
                </div>

                <div className="dino-rhs">
                  <DiagramManager height={workingHeight-210} width={workingWidth-500}/>
                </div>

              </div>

          
          </ResourcesContextProvider>
        </RequestContextProvider>
      </InteractionContextProvider>
    </div>


  );
}

