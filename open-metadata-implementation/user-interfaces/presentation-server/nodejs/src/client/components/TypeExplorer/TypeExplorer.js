/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React     from "react";


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




import ConnectionDetails               from "./components/connection-details/ConnectionDetails";
//import FocusControls                   from "./components/focus-controls/FocusControls";
import DetailsPanel                    from "./components/details-panel/DetailsPanel";
import DiagramManager                  from "./components/diagram/DiagramManager";


import "./tex.scss";



export default function TypeExplorer() {

  return (
    
    <div className="tex-container">

    <InteractionContextProvider>
      <RepositoryServerContextProvider>
        <TypesContextProvider>

            <div className="top">
              <h2>Type Explorer</h2>
              <div className="top-left">
                <ConnectionDetails />
              </div>
              <div className="top-middle">
                Focus Controls should go here...
              </div>
            </div>
            <div className="lhs">
              <hr />
              <DetailsPanel />
            </div>
            <div className="rhs">
              <DiagramManager />
            </div>

        </TypesContextProvider>
      </RepositoryServerContextProvider>
    </InteractionContextProvider>

    </div>
  );
}
