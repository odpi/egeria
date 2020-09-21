/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext, useState, useEffect }    from "react";

import PropTypes                                     from "prop-types";

import TopologyDiagram                               from "./TopologyDiagram";

import { ResourcesContext }                          from "../../contexts/ResourcesContext";



export default function DiagramManager(props) {

  let height = props.height;
  let width = props.width;


  /*
   * Access resourcesContext to get access to gens
   */
  const resourcesContext = useContext(ResourcesContext);

  /*
   * nodeArray is stateful contiguous array of nodes - which resemble entity digests.
   * linkArray is a stateful contiguous array of links - which resemble relationship digests.
   * A contiguous arays are needed for D3 to databind.
   * nodeArray and linkArray are updated when the gens change - this re-renders DiagramManager and 
   * it picks up the change in gens - see below. It's important that nodeArray and linkArray are updated 
   * to reflect gens in this component because it is then possible for Diagram (a child cpt) to depend 
   * on nodeArray and linkArray and to be re-rendered when they change - it the child were to depend on 
   * gens (as a property) and do the gen parsing and update of nodeArray or linkArray itself it does not 
   * get re-rendered.
   */
  const [nodeArray, setNodeArray] = useState( [] );
  const [linkArray, setLinkArray] = useState( [] );

  /*
   * allNodes is a map from guid to node - it provides an index for fast retrieval of a node based on 
   * the id (guid) found in the links.
   */
  const [allNodes, setAllNodes]   = useState( {} );

  /*
   * Get all gens and inspect them, copying each present resource or relationship into fresh (empty)
   * nodesArray and linksArray arrays. Add an entry into an initially empty node map too. When each
   * node is processed, check if it already exists in existing node map and node array and if so 
   * preserve its current position in the new array. Finally stamp the new map and arrays into the 
   * state.
   */
  const parseAllGens = () => {

    /*
     * Create empty node and link arrays and an empty node map.
     */
    let newNodesArray = [];
    let newLinksArray = [];
    let newNodesMap   = {};

    /*
     * Get te gens and iterate over them
     */
    let gens = resourcesContext.getGens();
    for (let i=0; i<gens.length; i++) {
      let gen = gens[i];

      /*
       * Iterate over the resources in this gen
       */
      let resources = gen.resources;
      let resourceGUIDs = Object.keys(resources);
      resourceGUIDs.forEach(resGUID => {
        let resource = resources[resGUID];
        let newNode = {};
        let category = resource.category;
        newNode.category = category;
  
        switch(category) {
  
          case "platform":
            newNode.id                     = resource.guid;
            newNode.label                  = resource.platformName;
            newNode.gen                    = resource.gen;
            break;
  
          case "server":
            newNode.id         = resource.guid;
            newNode.label      = resource.serverName;
            newNode.gen        = resource.gen;
            break;
  
          case "service":
            newNode.id         = resource.guid;
            newNode.label      = resource.serviceName;
            newNode.gen        = resource.gen;
            break;
            
          case "cohort":
            newNode.id         = resource.guid;
            newNode.label      = resource.cohortName;
            newNode.gen        = resource.gen;
            break;
          
        }
      
        /*
         * Check if node already exists and has position...
         */
        if (allNodes[resGUID]) {
          let exNode = allNodes[resGUID];
          newNode.x = exNode.x;
          newNode.y = exNode.y;
          /*
           * Retain the node's pinned state, if applicable
           */
          newNode.fx = exNode.fx;
          newNode.fy = exNode.fy;
        }
        else {
          /*
           * Initialise position to null so that node is given appropriate starting posiiton 
           * by the diagram
           */            
          newNode.x                      = null;
          newNode.y                      = null;
        }

        newNodesArray.push(newNode);     
        newNodesMap[newNode.id] = newNode;     
        
      });

      /*
       * Iterate over the resources in this gen
       */
      let relationships = gen.relationships;
      let relationshipGUIDs = Object.keys(relationships);
      relationshipGUIDs.forEach(relGUID => {
        let relationship = relationships[relGUID];
        var newLink = {};
        newLink.id                     = relationship.guid;
        
        /*
         * Indicate whether the relationship link is active or not; it will be displayed differently
         */
        newLink.active                 = relationship.active; 

        /*
         * Need to get each node from its GUID...it must already be in the gens but you would need to 
         * ask resourcesContext to map the guid to the gen and then again to look up the guid in that gen
         * 
         * The asynchronous state update to allNodes will not have happened yet.
         */
        newLink.source                 = newNodesMap[relationship.source];  
        newLink.target                 = newNodesMap[relationship.target];
        newLink.gen                    = relationship.gen;
      
        /*
         * Graph is not a multigraph, so set idx to 0
         */
        newLink.idx = 0;
       
        newLinksArray.push(newLink);     
        
      });   
    }   
  
   
    /*
     *  Update the states of nodeArray and allNodes ...
     */
    setNodeArray(newNodesArray);
    setAllNodes(newNodesMap);  
    /*
     *  Update the state of linkArray ...
     */
    setLinkArray(newLinksArray);

  }
 

  

  /*
   * Request that the ResourcesContext re-loads the resource and makes it the focus.
   */
  const onNodeClick = (guid) => {
    resourcesContext.changeFocusResource(guid);
  };


  /*
   * Request that the ResourcesContext re-loads the target resource and makes it the focus.
   * This is only supported for a platform -> server link.
   */
  const onLinkClick = (guid) => {
    resourcesContext.selectTargetResource(guid);
  };


  /*
   * When gens has changed - update the nodeArray and linkArray
   * Request the number of gens from ResourcesContext and perform a numeric comparison to assess whether 
   * a new gen has been added since this component was last rendered. If so, request the latest gen
   * and parse it into the nodes and links arrays.
   */

  const numGens = resourcesContext.getNumGens();
  
  /*
   * Parse generations when a change is detected. This needs to parse all the gens
   * because there may be an additional gen, removal of a gen or updates within 
   * existing gens.
   */
  useEffect(
    () => {
      parseAllGens();
    },
    [resourcesContext.gens]
  )
  

  return (
    <div>
      <TopologyDiagram nodes={nodeArray} 
               links={linkArray} 
               numGens={numGens} 
               onNodeClick={onNodeClick}  
               onLinkClick={onLinkClick}  
               outerHeight={height}
               outerWidth={width}             
               />
    </div>     
  );

}


DiagramManager.propTypes = {
  children : PropTypes.node,
  height   : PropTypes.number,
  width    : PropTypes.number
};
