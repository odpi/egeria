/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, {useEffect, useRef, useState}     from "react";


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
 * Import the DEFAULT export from the FocusContext module - which is actually the FocusContextProvider
 * Naming it explicitly for clarity that this is the provider not the context.
 */
import FocusContextProvider            from "./contexts/FocusContext";


import ConnectionDetails               from "./components/connection-details/ConnectionDetails";
import FocusControls                   from "./components/focus-controls/FocusControls";
import DetailsPanel                    from "./components/details-panel/DetailsPanel";
import DiagramManager                  from "./components/diagram/DiagramManager";


import "./tex.scss";



export default function TypeExplorer() {

  const containerDiv = useRef();

  // Make this stateful to cause a re-render..... TODO did it work???  SAME FOR WIDTH
  const [cltHeight, setCltHeight] = useState(document.documentElement.clientHeight);  
  const [cltWidth, setCltWidth]   = useState(document.documentElement.clientWidth);  

  let workingHeight = cltHeight - 50;
  let workingWidth  = cltWidth - 265;

  /* You cannot set the containerDiv dimensions until AFTER the cpt has rendered, as this creates the containerDiv */
  const updateSize = () => {    
    /* determine client height, width and set container dimensions */    
    setCltHeight(document.documentElement.clientHeight);
    console.log("cltHeight is "+cltHeight);
    workingHeight = cltHeight - 50;
    console.log("workingHeight is "+workingHeight);
    containerDiv.current.style.height=""+workingHeight+"px";

    //const cltWidth = document.documentElement.clientWidth;  // TODO ?? 
    setCltWidth(document.documentElement.clientWidth);
    console.log("ClientWidth is "+cltWidth);
    workingWidth = cltWidth - 265;
    console.log("workingWidth is "+workingWidth);
    containerDiv.current.style.width=""+workingWidth+"px";
  }


  /* useEffect to set size of container... */
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
  

  return (
    
    <div className="tex-container" ref={containerDiv}>

      <InteractionContextProvider>
        <RepositoryServerContextProvider>
          <TypesContextProvider>
            <FocusContextProvider>
   
              <div className="tex-top">

                <div className="title">
                  <p>Type Explorer</p>
                </div>
                
                <div className="tex-top-left">
                  <ConnectionDetails />
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
        </RepositoryServerContextProvider>
      </InteractionContextProvider>
    </div>


  );
}

// TODO - it would be better to not get the document size and then deduct, but to get the tex-container (for height)
// and the rhs (for diagram manager width) if possible...
