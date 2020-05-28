/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { createContext, useContext, useState } from "react";

import PropTypes                                      from "prop-types";

import { RepositoryServerContext }                    from "./RepositoryServerContext";


/*
 * The InstancesContext holds the state for the instances that are retrieved from the 
 * repository and loaded into the graph. It also holds transient state that exists
 * following the completion of a search and the selection by the user of which searched
 * instances they want to add to the graph.
 * 
 * The InstancesContext depends on the RepositoryServerContext for retrievals and searches.
 */
export const InstancesContext         = createContext();

export const InstancesContextConsumer = InstancesContext.Consumer;

const InstancesContextProvider = (props) => {


  const repositoryServerContext = useContext(RepositoryServerContext);

  //console.log("InstancesContext: being rendered, serverName: "+repositoryServerContext.repositoryServerName);
 

  /*
   * The focusInstance is the instance (entity or relationship) that is the user's current
   * focus. It's GUID is stored in focusInstanceGUID and its category is in focusInstanceCategory.
   * These things all move together - as demonstrated in functions such as setFocusEntity().
   * When the focus instance is changed (e.g. by clicking on an instance in the diagram) the 
   * whole set of focus fields is updated.
   * When the user types/pastes a GUID into the GUID field in the browser it updates the guidToLoad
   * field in the InstanceRetriever. Same for categoryToLoad. When the user presses "Get" in the 
   * InstanceRetriever, the instance is retrieved from the repository and all the focus fields 
   * are updated (together) as described above.   
   */

   /*
    * focus is an object containing the instanceCategory, instanceGUID and the instance itself.
    */
  //const [focusInstanceCategory, setFocusInstanceCategory] = useState(""); 
  //const [focusInstanceGUID,     setFocusInstanceGUID]     = useState(""); 
  //const [focusInstance,         setFocusInstance]         = useState(null); 
  const [focus, setFocus] = useState({ instanceCategory : "", 
                                       instanceGUID     : "",
                                       instance         : null });


 
  /*
   * gens
   * ----
   * This is where the previously retrieved instances are stored. 
   * This is structured as an array of traversal objects. A traversal may contain a single instance
   * such as an entity that was retrieved using Get or Search, or it may contain a set of neighbours
   * that were retrieved as the result of an Explore traversal starting from an existing entity.
   * A gen (traversal) has the following fields:
   *   entities       - a map from entity GUID to entity digest containing the instances
   *   relationships  - a map from relaitonship GUID to relationship digest containing the instances
   *   operation      - the operation that was performed to retrieve this traversal, e.g:
   *                      "entitySearch", "relationshipSearch"
   *                    This is recorded in the traversal, to provide an informative summary in history
   *   serverName     - the name of the server whose repository returned the instances in the traversal
   *   searchText     - the searchText that was used to find the set of instance in the traversal
   *                    (The search category is implicit from the operaton and the nature of the instances)
   * 
   * Initial State and Progress:
   * At the start of a session, or following a clear operation (or successive undo operations) gens
   * is empty.
   * A gen is added (to the end of the array) when a retrieval (get, search-completion  or explore) 
   * completes and returns one or more instances that were not already 'known' (i.e. in a previous gen).
   * The most recent gen can be removed by an Undo operation. It is removed in the second phase of Undo.
   * Gens are identified by numeric identifiers which start at 1 - they are NOT the gens array index.
   * The number of gens (previously stored as a separate field) can be found by getting the current genId.
   * 
   * The gens are accessed by other componenets using the functions:
   *   getLatestActiveGenId  - returns the most recent gen number - (previously called getCurrentGen)
   *   getLatestGen          - returns the most recent gen (not necessarily an active gen)
   *   addGen                - this encapsulates extension of a gen
   *                         - this updates the gens array and the guidToGenId map too.
   *   removeGen             - this encapsulates removal of a gen
   *                         - this updates the gens array and the guidToGenId map too.
   *                         - when a gen is removed, this method also checks whether it contained the focus - in which
   *                           case the focus is cleared.
   * 
   * 
   * guidToGenId
   * -----------
   * In addition to the gens themselves there is a map for quick existence checking and retrieval:
   *   guidToGenId     - maps the instance (entity or relationship) to the identifier of the gen it is in (if any)
   * 
   * Initial State and Progress:
   * At the start of a session, or following a clear operation (or successive undo operations) guidToGenId
   * is empty. An entry is added to the map when a gen is added and an entry is removed when the gen removed.
   */
  const [gens,         setGens]           = useState([]); 
  const [guidToGenId , setGuidToGenId]    = useState({}); 

  // The latestGenId is not just the length of the gens array - it indicates the id of the most recent
  // active generation. When the array is growing it will always be the last gen in the array. But when 
  // undo or clear is used, it will be either one less than the array length, or even zero (on clear).
  const [latestActiveGenId,  setLatestActiveGenId]    = useState(0);




  /*
   * getLatestActiveGenId  - returns the most recent gen number that is active
   */
  const getLatestActiveGenId = () => {    
    return latestActiveGenId;
  }

   /*
   * getLatestGen  - returns the most recent gen - this may not be an active gen.
   */
  const getLatestGen = () => {   
    return gens[gens.length-1];
  }
  
  /*
   * getAllGens  - returns all the gens
   */
  //const getAllGens = () => {
  //  return gens;
  //}

  /*
   * addGen - updates the gens array and the guidToGenId map too.
   */
  const addGen = (traversal) => {
    // The traversal contains new instances (entities and/or relationships) - that do 
    // not already existing in previous gens.

    // Do not mutate the current array - must replace for state update to register
    const newList = gens.concat(traversal);
    setGens( newList );   
    setLatestActiveGenId(newList.length);


    // Look for new instances in the traversal, and add them to the new gen.
    // If it was an entity that was processed, this function will only have been called if the entity
    // needs adding to the new gen - so no checking would be required. However, if it was a relationship
    // that was processed it carries a pair of entity digests and they may or may not have been already
    // known; the relationship processor will have already checked for pre-existence and set their genIds 
    // accordingly.This function needs to check each entity's genId - and only add the ones for the new 
    // gen.
    // If an entity is from the new gen, record it in the guidToGenId map. Relationships will always be 
    // from the new gen.
    // Because the map is immutable, corral the changes in a cloned map and apply them in one replace operation
    const newGen = newList.length; 
    let newEntries = Object.assign({},guidToGenId);
    const eKeys = Object.keys(traversal.entities);
    eKeys.forEach(e => {     
      newEntries[e] = newGen;
    });
    const rKeys = Object.keys(traversal.relationships);
    rKeys.forEach(r => {
      newEntries[r] = newGen;
    });
    // Now replace the map...
    setGuidToGenId(newEntries);
  
  }

 


  /*
   * Functions to process retrieved instances
   */

  /*
   * processRetrievedEntity accepts an expEntity, checks whether it is already known and if not,
   * creates a traversal to add the entity to a new gen
   */
  const processRetrievedEntity = (expEntity) => {

    const serverName = expEntity.serverName;
    const entityGUID = expEntity.entityDigest.entityGUID;

    
    let genId;
    if (guidToGenId[entityGUID] !== undefined) {
      // Entity is already known
      genId = guidToGenId[entityGUID];
      expEntity.entityDigest.gen = genId;
    }
    else {
      // Entity is not already known

      // Construct a traversal for the entity and add it to the gens.
      // The genId is in the digest and will be one beyond the current latest gen     
      genId = getLatestActiveGenId() + 1;
      expEntity.entityDigest.gen = genId;

      let rexTraversal                  = {};
      rexTraversal.entities             = {};
      rexTraversal.relationships        = {};
      rexTraversal.entities[entityGUID] = expEntity.entityDigest;
      rexTraversal.serverName           = serverName;
      rexTraversal.operation            = "getEntity";

      guidToGenId[entityGUID] = genId;
      
      // Add the traversal to the sequence of gens in the graph.
      addGen(rexTraversal);
    }
    
    /*
     * Because this is processing the retrieval of a single entity, that entity becomes the focus
     */
    setFocusEntity(expEntity);

  }

  /*
   * processRetrievedRelationship accepts an expRelationship, checks whether it is already known and if not,
   * creates a traversal to add the relationship to a new gen
   */
  const processRetrievedRelationship = (expRelationship) => {  

    const serverName       = expRelationship.serverName;
    const relationshipGUID = expRelationship.relationshipDigest.relationshipGUID;

    
    let genId;
    if (guidToGenId[relationshipGUID] !== undefined) {
      // Relationship is already known
      genId = guidToGenId[relationshipGUID];
      expRelationship.relationshipDigest.gen = genId;
    }
    else {
      // Relationship is not already known

      // Construct a traversal for the relationship and add it to the gens.
      // The genId is in the digest and will be one beyond the current latest gen     
      genId = getLatestActiveGenId() + 1;
      expRelationship.relationshipDigest.gen = genId;

      let rexTraversal                              = {};
      rexTraversal.entities                         = {};
      rexTraversal.relationships                    = {};
      rexTraversal.relationships[relationshipGUID]  = expRelationship.relationshipDigest;
      rexTraversal.serverName                       = serverName;
      rexTraversal.operation                        = "getRelationship";


      guidToGenId[relationshipGUID] = genId;

      /*
       * We need to retrieve the end entity digests from the expRelationship and find out
       * whether each end entity is new or known, so they can either keep their gens or be
       * assigned the next gen...
       */

      /*
       * entityOne
       */
      const entityOneDigest = expRelationship.entityOneDigest;
      const entityOneGUID = entityOneDigest.entityGUID;

      /*
       * Determine whether entityOne is already known. This could loop through the gens
       * but it is slightly more efficient to use the guidToGen map as a direct index.
       */
      let entityOneKnown = false;
      let e1gen;
      if (guidToGenId[entityOneGUID] !== undefined) {
          entityOneKnown = true;
          e1gen          = guidToGenId[entityOneGUID];
      }
      if (entityOneKnown === false) {
          e1gen = genId;
      }
      entityOneDigest.gen = e1gen;

      /*
       * entityTwo
       */
      const entityTwoDigest = expRelationship.entityTwoDigest;
      const entityTwoGUID   = entityTwoDigest.entityGUID;

      /*
       * Determine whether entityTwo is already known. This could loop through the gens
       * but it is slightly more efficient to use the guidToGen map as a direct index.
       */
      let entityTwoKnown = false;
      let e2gen;
      if (guidToGenId[entityTwoGUID] !== undefined) {
          entityTwoKnown = true;
          e2gen          = guidToGenId[entityTwoGUID];
      }
      if (entityTwoKnown === false) {
          e2gen = genId;
      }
      entityTwoDigest.gen = e2gen;

      /*
       * Add the entity digests to the traversal ONLY if they are new in this gen
       */
      if (!entityOneKnown) {
        rexTraversal.entities[entityOneGUID] = entityOneDigest;
        guidToGenId[entityOneGUID]           = e1gen;
      }
      if (!entityTwoKnown) {
        rexTraversal.entities[entityTwoGUID] = entityTwoDigest;
        guidToGenId[entityTwoGUID]           = e2gen;
      }
      
      // Add the traversal to the sequence of gens in the graph.
      addGen(rexTraversal);
    }
    
    /*
     * Because this is processing the retrieval of a single relationship, that relationship becomes the focus
     */
    setFocusRelationship(expRelationship);
    console.log("Hmmm...");
  }


  /*
   * processRetrievedTraversal accepts a RexTraversal, and parses it, checking whether each instance it contains is
   * already known. If there are any new instances, creates a traversal to add the instances to a new gen.
   */
  const processRetrievedTraversal = (rexTraversal) => {
    

      /*
       * If this is a traversal from an Explore, the traversal results should have been formatted by the VS 
       * into the form needed by Rex.
       * This means that it should have:
       *   a map of entityGUID       --> { entityGUID, label, gen }
       *   a map of relationshipGUID --> { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
       *
       * Alternatively this could be a traversal object resulting from a search and subsequent user 
       * selection of search results. 
       * 
       * For each entity and relationship in the traversal response we need to determine whether it
       * is known or new. Anything known is dropped from the traversal, which is then pushed to the
       * logically next gen, setting the gen in each digest accordingly.
       */



      /* Assume initially that the traversal contains new information; if so it will be added to
       * the next gen, calculated below. If nothing new is learned from the traversal then we
       * will not update the current gen.
       * 
       * Notice that the currentGen is NOT advanced at this stage - this will only happen if the
       * traversal contains new metadata objects.
       */
      
      const genId = getLatestActiveGenId() + 1;

      /*
       * Process entities...
       * Anything that is known should be removed from the traversal.
       * Anything new can remain and should be assigned the next gen.
       */
      const entities = rexTraversal.entities;
      const eKeys = Object.keys(entities);
      eKeys.forEach(eKey => {
          const entity = entities[eKey];        

          const entityGUID = entity.entityGUID;
          /*
           * Determine whether entity is already known ...
           */
          let entityKnown = false;
          if (guidToGenId[entityGUID] !== undefined) {
              entityKnown = true;
          }
          if (entityKnown === true) {
              // Remove the entity from the traversal
              delete rexTraversal.entities[entityGUID];
          }
          else {
              // Update the new entity's gen
              rexTraversal.entities[entityGUID].gen = genId;
              guidToGenId[entityGUID] = genId;
          }
      });


      /*
       * Process relationships...
       * Anything that is known should be removed from the traversal.
       * Anything new can remain and should be assigned the next gen.
       */
      const relationships = rexTraversal.relationships;
      const rKeys = Object.keys(relationships);
      rKeys.forEach(rKey => {
        const relationship = relationships[rKey];         

        const relationshipGUID = relationship.relationshipGUID;

          /*
           * Determine whether relationship is already known ...
           */
          let relationshipKnown = false;
          if (guidToGenId[relationshipGUID] !== undefined) {
              relationshipKnown = true;
          }
          if (relationshipKnown === true) {
              // Remove the relationship from the traversal
              delete rexTraversal.relationships[relationshipGUID];
          }
          else {
              // Relationship is new.
              // Update the new relationship's gen
              rexTraversal.relationships[relationshipGUID].gen = genId;
              guidToGenId[relationshipGUID] = genId;
          }
      });


      /*
       * If there is anything new still in the traversal,
       *   -  each new instance has been assigned (above) the next gen; advance the value of currentGen.
       *   -  push the traversal into the appropriate position in gens
       *   -  generate a graph-changed event
       * else
       *   -  display a message saying that nothing new was learned.
       */
      const no_entities      = rexTraversal.entities      === undefined || Object.keys(rexTraversal.entities).length      === 0;
      const no_relationships = rexTraversal.relationships === undefined || Object.keys(rexTraversal.relationships).length === 0;

      if (no_entities && no_relationships) {
          /*
           * This is not an error - it just means everything in the traversal was already known,
           * which can happen.
           * However, it is desirable to advise the user that nothing new was returned, which should explain why
           * there will be no visible change to the display.
           */
          alert("No additional objects were returned in the traversal");
      }

      else {
        
          /*
           * For what it's worth - set the gen at the traversal level (the contained objects already have
           * gen set)
           */
          rexTraversal.gen = genId;

          /*
           * Add the traversal to the sequence of gens in the graph. Then generate the graph-changed event.
           */
          addGen(rexTraversal);

      }
  }


  /*
   * A component has requested that the focus is changed to the entity with the specified GUID.
   */
  const changeFocusEntity = (entityGUID) => {
    console.log("InstancesContext: changeFocusEntity called with guid "+entityGUID);
    /*
     * If the entity is the current focus - deselect it.
     */
    if (entityGUID === focus.instanceGUID) {
      console.log("InstancesContext: changeFocusEntity will clear focus");
      clearFocusInstance();         
    }

    else {
          
      /*
       * Check whether the entity is homed by the selected server (repo) - if so we know it
       * can be retrieved. If not, then check enterprise flag is on and if not provide an alert
       * to advise the user that the entity will only be retrieved if enterprise is set.
       */
      if (guidToGenId[entityGUID] !== undefined) {
        const genId = guidToGenId[entityGUID];
        const gen = gens[genId-1];
        const entityDigest = gen.entities[entityGUID];
        const home = entityDigest.metadataCollectionName;
        const serverName = repositoryServerContext.repositoryServerName;            
        // TODO - clean up experiments!
        console.log("InstancesContext: changeFocusEntity see repo context serverName as "+serverName);  
        console.log("InstancesContext: changeFocusEntity call repo context getRepositoryServerName");
        const serverName2 = repositoryServerContext.getRepositoryServerName();
        console.log("InstancesContext: changeFocusEntity got back "+serverName2);
        const enterpriseOption = repositoryServerContext.getRepositoryServerEnterpriseOption();
        if (enterpriseOption === true || serverName === home ) {
          loadEntity(entityGUID);                  
        }
        else {
          alert("The selected object is not local to the server being used and the enterprise option is not checked. You could retrieve it either by setting server to "+home+" or by enabling the enterprise option");
        }
      }
    }
  } 

  /*
   * A component has requested that the focus is changed to the relationship with the specified GUID.
   */
  const changeFocusRelationship = (relationshipGUID) => {
    /*
     * If the entity is the current focus - deselect it.
     */
    if (relationshipGUID === focus.instanceGUID) {
      clearFocusInstance();         
    }

    else {
        
      /*
       * Check whether the relationship is homed by the selected server (repo) - if so we know it
       * can be retrieved. If not, then check enterprise flag is on and if not provide an alert
       * to advise the user that the relationship will only be retrieved if enterprise is set.
       */
      if (guidToGenId[relationshipGUID] !== undefined) {
        const genId = guidToGenId[relationshipGUID];
        const gen = gens[genId-1];
       const relationshipDigest = gen.relationships[relationshipGUID];
        const home = relationshipDigest.metadataCollectionName;
        const serverName = repositoryServerContext.repositoryServerName;              
        const enterpriseOption = repositoryServerContext.repositoryServerEnterpriseOption;
        if (enterpriseOption === true || serverName === home ) {
          loadRelationship(relationshipGUID);                  
        }
        else {
          alert("The selected object is not local to the server being used and the enterprise option is not checked. You could retrieve it either by setting server to "+home+" or by enabling the enterprise option");
        }
      }
    }
  }

  /*
   * setFocusEntity sets the category, instance, guid for the focus instance.
   * This operation is atomic (all three aspects are updated as one state change) to avoid sequqncing,
   * e.g. if the category were set first - it would trigger other components to re-render - and if 
   * the category does not match the other aspects, they will be very confused.
   */  
  const setFocusEntity = (expEntity) => {
    console.log("InstancesContext: Setting focus to entity..."+expEntity.entityDetail.guid);
    const newFocus = { instanceCategory : "Entity", instanceGUID : expEntity.entityDetail.guid, instance : expEntity };
   setFocus( newFocus );
    
   
  }

  /*
   * setFocusRelationship sets the category, instance, guid for the focus instance.
   * This operation is atomic (all three aspects are updated as one state change) to avoid sequqncing,
   * e.g. if the category were set first - it would trigger other components to re-render - and if 
   * the category does not match the other aspects, they will be very confused.
   */  
  const setFocusRelationship = (expRelationship) => {
    console.log("InstancesContext: Setting focus to relationship..."+expRelationship.relationship.guid);
    setFocus( { instanceCategory : "Relationship", 
                instanceGUID     : expRelationship.relationship.guid,
                instance         : expRelationship });
  
   
  }

  /*
   * Get the GUID of the focus instance
   */
  const getFocusGUID = () => {
    //console.log("InstancesContext: getFocusGUID finds "+focus.instanceGUID);
    return focus.instanceGUID;
  };

  /*
   * clearFocusInstance resets the category, instance, guid for the focus instance
   * to a state in which nothing is selected - there is no focus.
   * This operation is atomic (all three aspects are updated as one state change) to avoid sequqncing,
   * e.g. if the category were set first - it would trigger other components to re-render - and if 
   * the category does not match the other aspects, they will be very confused.
   */  
  const clearFocusInstance = () => {
    console.log("InstancesContext: Clearing focus instance...");
    setFocus( { instanceCategory : "Entity", 
                instanceGUID     : "",
                instance         : null });
   
  }


  /*
   * Function to get entity by GUID from the repository
   */
  const loadEntity = (entityGUID) => {
    console.log("InstanceRetriever: try to get (expanded) entity using instanceGUID : "+entityGUID);
   
    repositoryServerContext.repositoryPOST("instances/entity", { entityGUID : entityGUID }, _loadEntity); 
  };

  const _loadEntity = (json) => {  
    if (json !== null) {
      if (json.relatedHTTPCode === 200) {
        /*
         * Should have an expandedEntityDetail
         */
        let expEntity = json.expandedEntityDetail;
        if (expEntity !== null) {
          console.log("_loadEntity: got back expEntity..."+expEntity);
          processRetrievedEntity(expEntity);        
          return;
        }
      }  
      else {
        /*
         * Request failed
         */     
        alert("Attempt to retrieve entity from repository server returned status code "+json.relatedHTTPCode+" exception "+json.exceptionErrorMessage);
      }
    }   
    // On failure of any of the above...  
    alert("Could not get entity from repository server");
  };

  /*
   * Function to get relationship by GUID from the repository
   */
  const loadRelationship = (relationshipGUID) => {
    console.log("InstanceRetriever: try to get (expanded) relationship using instanceGUID : "+relationshipGUID);
   
    repositoryServerContext.repositoryPOST("instances/relationship", { relationshipGUID : relationshipGUID }, _loadRelationship); 
  };

  const _loadRelationship = (json) => {  
    if (json !== null) {
      if (json.relatedHTTPCode === 200) {
        /*
         * Should have an expandedRelationship
         */
        let expRelationship = json.expandedRelationship;
        if (expRelationship !== null) {
          console.log("_loadRelationship: got back expRelationship..."+expRelationship);
          processRetrievedRelationship(expRelationship);        
          return;
        }
      }  
      else {
        /*
         * Request failed
         */
        //console.log("_loadRelationship: got back status code "+json.relatedHTTPCode+" exception "+json.exceptionErrorMessage);        
        alert("Attempt to load a relationship from the repository  got back status code "+json.relatedHTTPCode+" exception "+json.exceptionErrorMessage);
      }
    }   
    // On failure of any of the above... 
    //console.log("Could not get relationship from repository server");  
    alert("Attempt to load a relationship from the repository got back nothing");
  };






/*
   * Helper function to retrieve focus entity
   */
  const getFocusCategory = () => {
    if (focus.instanceCategory === "Entity" || focus.instanceCategory === "Relationship" ) {      
      return focus.instanceCategory;
    }
    console.log("InstancesContext: no focus category is set");
    return null;
  };



  /*
   * Helper function to retrieve focus entity
   */
  const getFocusEntity = () => {
    if (focus.instanceCategory === "Entity") {
      if (focus.instance !== null) {
        return focus.instance;
      }
    }
    return null;
  };

   /*
   * Helper function to retrieve focus entity
   */
  const getFocusRelationship = () => {
    if (focus.instanceCategory === "Relationship") {
      if (focus.instance !== null) {
        return focus.instance;
      }
    }
    return null;
  };



  /*
   * Function to explore the neighborhood around the current focus entity
   * Parmeters: list of typeGUIDs for each of te three categories.
   */
  const explore = ( selectedEntityTypes, selectedRelationshipTypes, selectedClassificationTypes ) => {

    console.log("InstanceRetriever: explore starting at the focus entity");
   
    repositoryServerContext.repositoryPOST(
      "instances/rex-traversal", 
      { entityGUID             :  getFocusGUID(),
        depth                  :  1,
        entityTypeGUIDs        :  selectedEntityTypes,
        relationshipTypeGUIDs  :  selectedRelationshipTypes,
        classificationNames    :  selectedClassificationTypes
      },  // depth is always limited to 1
      _explore); 
  }

  const _explore = (json) => {  
    if (json !== null) {
      if (json.relatedHTTPCode === 200) {
        /*
         * Should have a traversal object        
         */
        let rexTraversal = json.rexTraversal;
        if (rexTraversal !== null) {
          console.log("_explore: got back rexTraversal..."+rexTraversal);
          rexTraversal.operation = "traversal";
          processRetrievedTraversal(rexTraversal);
          return;
        }
      }  
      else {
        /*
         * Request failed
         */     
        alert("Traversal request to repository server returned status code "+json.relatedHTTPCode+" exception "+json.exceptionErrorMessage);
      }
    }   
    // On failure of any of the above...   
    alert("Traversal request to repository server failed to get a response");  
  };



  /*
   * Remove a generation from the graph
   */
  const removeGen = () => {
    /*
     * Remove the most recent gen from the active gens. his should be noticed by the DiagramManager
     * which will update the diagram data, and callback to the removeGenComplete callback.
     */    
    
 
    // Do not mutate the current array - must replace for state update to register
    let newList = Object.assign(gens);
    const removedGen = newList.pop();
    setGens( newList );
    setLatestActiveGenId(newList.length);

    // Look for new instances in the removedGen, and remove then from the guidToGenId map.
    // Because the map is immutable, corral the changes in a cloned map and apply them in one replace operation
    
    let newGUIDMap = Object.assign({},guidToGenId);
    const eKeys = Object.keys(removedGen.entities);
    eKeys.forEach(e => {
      delete newGUIDMap[e];
    });
    const rKeys = Object.keys(removedGen.relationships);
    rKeys.forEach(r => {
      delete newGUIDMap[r];
    });
    // Now replace the map...
    setGuidToGenId(newGUIDMap);

  }

  
 


  /*
   * Clear the state of the session - this includes the gens, the focus and the guidToGenId map.
   * Reset the searchCategory to Entity and the CategoryToLoad to Entity.
   */
  const clear = () => {

    console.log("InstancesContext: clear everything");

    // Reset the focusInstance
    clearFocusInstance();
    
    // Empty the graph
    const emptyArray = [];
    setGens(emptyArray);
    setLatestActiveGenId(0);

    // Empty the map
    const emptymap = {};
    setGuidToGenId(emptymap);

    
    
  }


  /*
   * getHistory compiles a history list describing the exploration from gen 1 onwards.
   */
  const getHistory = () => {

    // Build a history as a list of gens with summaries...
    let historyList = [];

    const testData = false;

    if (testData) {

      for (let i=0; i<10 ; i++) {
        let item = {}
        item.gen = i+1;
        item.query = "Operation "+item.gen;
        item.instances = [];
        item.instances.push( { category : "First", label : "FirstLabel", guid : "1111-1111" } );
        item.instances.push( { category : "Next", label : "NextLabel", guid : "2222-2222" } );
        item.instances.push( { category : "Almost", label : "AlmostLabel", guid : "3333-3333" } );
        item.instances.push( { category : "Last", label : "LastLabel", guid : "4444-4444" } );
      
        historyList.push(item);
      }

    }

    else {

      /* Each gen consists of the following:
       *
       *   private String               entityGUID;               // must be non-null
       *   private List<String>         entityTypeGUIDs;          // a list of type guids or null
       *   private List<String>         relationshipTypeGUIDs;    // a list of type guids or null
       *   private List<String>         classificationNames;      // a list of names or null
       *   private Integer              depth;                    // the depth used to create the subgraph
       *   private Integer              gen;                      // which generation this subgraph pertains to
       *
       *   There are also fields that contain maps of instance summaries.
       *   An instance summary is much smaller than the full instance.
       *   The entities map is keyed by entityGUID and the value part consists of
       *       { entityGUID, label, gen }
       *   The relationships map is keyed by relationshipGUID and the value part consists of
       *       { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
       *   The above value types are described by the RexEntityDigest and RexRelationshipDigest Java classes.
       *   private Map<String,RexEntityDigest>         entities;
       *   private Map<String,RexRelationshipDigest>   relationships;
       *
       *   The traversal is augmented in the client by the addition of an operation field. This is only meaningful in the
       *   client code.
       *   private String                operation  - has values { 'getEntity' | 'getRelationship' | 'traversal' }
       */

      for (let i=0; i<gens.length; i++) {
        const gen = i+1;
        const genContent = gens[i];

        /*
         *  Build the query description
         */

        /*
         * The querySummary always starts with the Repository Server's name
         */

        const serverName = genContent.serverName;
        let querySummary = "["+serverName+"]";

        switch (genContent.operation) {

          case "getEntity":
            /*
            * Format querySummary as "Entity retrieval \n GUID: <guid>"
            */
            querySummary = querySummary.concat(" Entity retrieval using GUID");
            break;

          case "getRelationship":
            /*
            * Format querySummary as "Relationship retrieval \n GUID: <guid>"
            */
            querySummary = querySummary.concat(" Relationship retrieval using GUID");
            break;

          case "traversal":
            /*
             * Format querySummary as "Traversal"
             * Would like to present user with label rather than guid. genContent.root contains GUID.
             * The reason for the lookup is that the root of the traversal will not be in the same gen
             * as the traversal results. It is not known which gen it is (except it must have existed prior
             * to traversal).
             */
            {
              const entityGUID    = genContent.entityGUID;
              const rootGenNumber = guidToGenId[entityGUID];
              const rootGen       = gens[rootGenNumber-1];
              const rootDigest    = rootGen.entities[entityGUID];
              const rootLabel     = rootDigest.label;
              querySummary        = querySummary.concat(" Traversal from entity "+rootLabel);
              querySummary        = querySummary.concat(" Depth: "+genContent.depth);
              // Entity Type Filters - show type names rather than type GUIDs
              querySummary        = querySummary.concat(" Entity Type Filters: ");
              var entityTypeNames = genContent.entityTypeNames;
              if (entityTypeNames != undefined && entityTypeNames != null) {
                let first = true;
                entityTypeNames.forEach(function(etn){
                  if (first) {
                    first = false;
                    querySummary = querySummary.concat(etn);
                  }
                  else {
                    querySummary = querySummary.concat(", "+etn);
                  }
                });
              }            
              else
                querySummary = querySummary.concat("none");
            }

            // Relationship Type Filters - show type names rather than type GUIDs
            querySummary = querySummary.concat(" Relationship Type Filters: ");
            var relationshipTypeNames = genContent.relationshipTypeNames;
            if (relationshipTypeNames != undefined && relationshipTypeNames != null) {
              let first = true;
              relationshipTypeNames.forEach(function(rtn){
                if (first) {
                  first = false;
                  querySummary = querySummary.concat(rtn);
                }
                else {
                  querySummary = querySummary.concat(", "+rtn);
                }
              });
            }
            else
              querySummary = querySummary.concat("none"); 

            // Classification Filters - shown as names
            querySummary = querySummary.concat(" Classification Filters: ");
            var ClassificationNames = genContent.ClassificationNames;
            if (ClassificationNames != undefined && ClassificationNames != null) {
              let first = true;
              ClassificationNames.forEach(function(rtn){
                if (first) {
                  first = false;
                  querySummary = querySummary.concat(rtn);
                }
                else {
                  querySummary = querySummary.concat(", "+rtn);
                }
              });
            }
            else
              querySummary = querySummary.concat("none");
  
            break;


          case "entitySearch":
            /*
             * Format querySummary as "Entity retrieval \n GUID: <guid>"
             */
            querySummary = querySummary.concat(" Entity Search");
            querySummary = querySummary.concat(" expression ["+genContent.searchText+"]");
            break;

          default:
            /*
             *  Found a gen result with no operation type.
             *  Put message to console and add error message to gen so this is noticed in history.
             */
            querySummary = "Operation not recognised!";
            break;
        }

        /*
         *  Build the instances section
         */
        const instanceList = [];
        const entities = genContent.entities;
        for (let guid in entities) {
          const ent = entities[guid];
          instanceList.push( { "category" : "Entity" , "label" : ent.label , "guid" : ent.entityGUID } );
        }

        const relationships = genContent.relationships;
        for (let guid in relationships) {
          const rel = relationships[guid];
          instanceList.push( { "category" : "Relationship" , "label" : rel.label , "guid" : rel.relationshipGUID } );
        }
        var historyItem = {  "gen" : gen , "query" : querySummary , "instances" : instanceList};

        historyList.push(historyItem);
      }
    }

    return historyList;


  }

 
  return (
    <InstancesContext.Provider
      value={{      
        focus,
        setFocus,       
        getFocusGUID,
        setFocusEntity,
        getFocusEntity,
        changeFocusEntity,
        getFocusRelationship,
        setFocusRelationship,
        changeFocusRelationship,
        getFocusCategory,
        clearFocusInstance,
        clear,    
        getHistory, 
        loadEntity,
        _loadEntity,
        loadRelationship,
        _loadRelationship,
        processRetrievedTraversal,
        explore,
        _explore,       
        gens,
        setGens,
        getLatestActiveGenId,
        removeGen,       
        getLatestGen,        
        guidToGenId              // only added this for debug in i-r - should be encapsulated in InstancesContext
      }}
    >      
     {props.children}
    </InstancesContext.Provider>
  );
};

InstancesContextProvider.propTypes = {
  children: PropTypes.node  
};

export default InstancesContextProvider;

