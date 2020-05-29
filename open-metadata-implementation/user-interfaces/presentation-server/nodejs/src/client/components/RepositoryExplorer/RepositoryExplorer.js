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

/* 
 * Import the DEFAULT export from the InstancesContext module - which is actually the InstancesContextProvider
 * Naming it explicitly for clarity that this is the provider not the context.
 */
import InstancesContextProvider        from "./contexts/InstancesContext";



import ConnectionDetails               from "./components/connection-details/ConnectionDetails";
import InstanceRetrieval               from "./components/instance-retrieval/InstanceRetrieval";
import InstanceSearch                  from "./components/instance-retrieval/InstanceSearch";
import DetailsPanel                    from "./components/details-panel/DetailsPanel";
import DiagramManager                  from "./components/diagram/DiagramManager";
import GraphControls                   from "./components/graph-controls/GraphControls";

import "./rex.scss";



export default function RepositoryExplorer() {

  return (
    
    <div className="rex-container">
    
    <InteractionContextProvider>
      <RepositoryServerContextProvider>
        <TypesContextProvider>
          <InstancesContextProvider>
            <div className="top">
              <h2>Repository Explorer</h2>
              <div className="top-left">
                <ConnectionDetails />    
              </div>       
              <div className="top-middle">                               
                <InstanceRetrieval />  
              </div>
              <div className="top-right">      
                <InstanceSearch />  
              </div>
            </div>
            <div className="lhs">                                                        
              <GraphControls />
              <hr />
              <DetailsPanel />            
            </div>
            <div className="rhs">   
              <DiagramManager />
            </div>
          </InstancesContextProvider>
        </TypesContextProvider>
      </RepositoryServerContextProvider>      
    </InteractionContextProvider>
      
    </div>
  );
}
