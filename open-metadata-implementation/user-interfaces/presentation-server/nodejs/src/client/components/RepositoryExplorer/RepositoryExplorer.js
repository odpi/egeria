/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, {useEffect, useRef, useState}      from "react";


/* 
 * Import the DEFAULT export from the InteractionContext module - which is actually the InteractionContextProvider
 * Naming it explicitly for clarity that this is the provider not the context.
 */
import InteractionContextProvider      from "./contexts/InteractionContext";

/* 
 * Import the DEFAULT export from the RepositoryContext module - which is actually the RepositoryServerContextProvider
 * Naming it explicitly for clarity that this is the provider not the context.
 */
import RepositoryServerContextProvider from "./contexts/RepositoryServerContext";

/* 
 * Import the DEFAULT export from the TypesContext module - which is actually the TypesContextProvider
 * Naming it explicitly for clarity that this is the provider not the context.
 */
import TypesContextProvider            from "./contexts/TypesContext";

/* 
 * Import the DEFAULT export from the InstancesContext module - which is actually the InstancesContextProvider
 * Naming it explicitly for clarity that this is the provider not the context.
 */
import InstancesContextProvider        from "./contexts/InstancesContext";

import InstanceRetrieval               from "./components/instance-retrieval/InstanceRetrieval";
import InstanceSearch                  from "./components/instance-retrieval/InstanceSearch";
import DetailsPanel                    from "./components/details-panel/DetailsPanel";
import DiagramManager                  from "./components/diagram/DiagramManager";
import GraphControls                   from "./components/graph-controls/GraphControls";
import ServerSelector                  from "./components/resource-selection/ServerSelector";
import EnterpriseControl               from "./components/resource-selection/EnterpriseControl";
import HelpHandler                     from "./HelpHandler";
import QuestionMarkImage               from "./question-mark-32.png";
import HelpMarkdown                    from './HELP.md';

import "./rex.scss";



export default function RepositoryExplorer() {

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
   * useEffect to load markdown readme file
   */
  useEffect(
    () => {
      // Get the content of the markdown file and save it in 'readme'.
      fetch(HelpMarkdown).then(res => res.text()).then(text => setHelp({ markdown: text }));
    },
    [] /* run effect once only */
  )



  return (

    <div className="rex-container" ref={containerDiv}>

      <InteractionContextProvider>
        <RepositoryServerContextProvider>
          <TypesContextProvider>
            <InstancesContextProvider>

              <div className="rex-top">

                <div className="title">
                  <p>Repository Explorer</p>

                  <input type="image"  src={QuestionMarkImage}
                     onClick = { () => displayHelp() }  >
                  </input>

                  <HelpHandler   status              = { helpStatus }
                                   help                = { help }
                                   onCancel            = { cancelHelpModal }
                                   onSubmit            = { submitHelpModal } />

                  <EnterpriseControl/>
                </div>

                <div className="rex-top-left">
                  <ServerSelector />
                </div>

                <div className="rex-top-middle">
                  <InstanceRetrieval />
                </div>

                <div className="rex-top-right">
                  <InstanceSearch />
                </div>

              </div>

              <div className="rex-content">

                <div className="rex-lhs">
                  <hr />
                  <GraphControls />
                  <hr />
                  <DetailsPanel />
                </div>

                <div className="rex-rhs">
                  <DiagramManager height={workingHeight-300} width={workingWidth-500}/>
                </div>

              </div>

            </InstancesContextProvider>
          </TypesContextProvider>
        </RepositoryServerContextProvider>
      </InteractionContextProvider>
    </div>


  );
}

