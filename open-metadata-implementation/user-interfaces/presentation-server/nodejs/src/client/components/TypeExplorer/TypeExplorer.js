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

import DeprecatedTypeControl                    from "./components/resource-selection/DeprecatedTypeControl";

import DeprecatedAttributeControl               from "./components/resource-selection/DeprecatedAttributeControl";

import FocusControls                            from "./components/focus-controls/FocusControls";

import DetailsPanel                             from "./components/details-panel/DetailsPanel";

import DiagramManager                           from "./components/diagram/DiagramManager";

import HelpHandler                              from "./HelpHandler";

import QuestionMarkImage                        from "./question-mark-32.png";

import HelpMarkdown                             from './HELP.md';


import "./tex.scss";



export default function TypeExplorer() {

  

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
      /* Get the content of the markdown file and save it in 'help' */
      fetch(HelpMarkdown).then(res => res.text()).then(text => setHelp({ markdown: text }));
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
                       onClick = { () => displayHelp() }  >
                    </input>

                    <HelpHandler   status              = { helpStatus }
                                     help                = { help }
                                     onCancel            = { cancelHelpModal }
                                     onSubmit            = { submitHelpModal } />

                    <EnterpriseControl/>

                    <DeprecatedTypeControl/>

                    <DeprecatedAttributeControl/>
                 
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
                    <DiagramManager height={workingHeight-270} width={workingWidth-450}/>
                  </div>

                </div>

              </FocusContextProvider>
            </TypesContextProvider>
          </RequestContextProvider>
      </InteractionContextProvider>
    </div>


  );
}

