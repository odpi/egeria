/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { createContext, useState, useContext } from "react";

import PropTypes                                      from "prop-types";

import { RequestContext }                             from "./RequestContext";





export const ResourcesContext = createContext();

export const ResourcesContextConsumer = ResourcesContext.Consumer;


/*
 * The ResourcesContext holds the state for the resources that are retrieved from the
 * topology into the graph.
 * 
 * The ResourcesContext depends on the RequestContext for retrievals and searches.
 */


const ResourcesContextProvider = (props) => {


  const requestContext = useContext(RequestContext);

  /*
   * The resources context remembers the platforms, servers and cohorts that the user visits.
   * When a platform name is selected the platform is loaded, which adds it to the graph.
   * If a platform's known or active servers button is clicked, the servers are retrieved from
   * the platform and added to the graph.
   */


  /*
   * The focus is the resource that is the user's current focus. It can be a platform or server. 
   * It cannot currently be a cohort or service.
   * It's guid is stored in focus.guid and its category is in focus.category.
   */

  /*
   * focus is an object containing the instanceCategory, instanceGUID and the instance itself.
   * 'category' is either "server" or "platform"
   */
  const [focus,             setFocus]               = useState({ category  : "", guid  : ""});
  
  /*
   * This is currently being used only for server configurations - the context will retrieve
   * the stored and active (instance) configurations so they can be compared.
   * The loading property is initialized to "init" and will transition through "loading" to "loaded".
   */
  const [serverConfig,       setServerConfig]         = useState(
    { stored : null , active : null , matching : true, diffs : null, loading : "init" });
  

  /*
   * operationState reflects whether the context is waiting for a response or not.
   * It can have values 'inactive' | 'loading'
   */
  const [operationState,    setOperationState]      = useState({ state  : "inactive", name  : ""});

  /*
   * gens and guidToGenId are the array of gens and the index for rapid object retrieval from the gens.
   */
  const [gens,               setGens]               = useState([]); 
  const [guidToGenId,        setGuidToGenId]        = useState({}); 
  
  
  /*
   * getLatestGenId  - returns the identifier of the most recent gen 
   */
  const getLatestGenId = () => {    
    return gens.length;
  }

  /*
   * getNumGens  - returns the number of gens 
   */
  const getNumGens = () => {    
    return gens.length;
  }

  /*
   * getLatestGen  - returns the most recent gen - this may not be an active gen.
   */
  const getLatestGen = () => {   
    if (gens.length > 0) {
      return gens[gens.length-1];
    }
    else {
      return null;
    }
  }

  const getGens = () => {   
    return gens;
  }


  /*
   * Create an empty generation, including the request summary for the operation
   * responsible for the operation.
   */
  const createEmptyGen = (requestSummary) => {
    let newGen                  = {};

    newGen.requestSummary       = requestSummary;
    newGen.resources            = {};
    newGen.relationships        = {};
    return newGen;
  }

  /*
   * GUID generators
   */

  const genPlatformGUID = (platformRootURL) => {
    let guid = "PLATFORM_"+platformRootURL;
    return guid;
  }

  const genServerGUID = (serverName) => {
    let guid = "SERVER_"+serverName;
    return guid;
  }

  const genServerInstanceGUID = (serverInstanceName) => {
    let guid = "SERVER_INSTANCE"+serverInstanceName;
    return guid;
  }

  const genServiceGUID = (serviceName) => {
    let guid = "SERVICE_"+serviceName;
    return guid;
  }

  const genCohortGUID = (cohortName) => {
    let guid = "COHORT_"+cohortName;
    return guid;
  }

  const genServerCohortGUID = (serverCohortName) => {
    let guid = "SERVER_COHORT"+serverCohortName;
    return guid;
  }

  /* 
   * Always accept the operation name because operation name is needed even in the case where json is null
   */
  const reportFailedOperation = (operation, json) => {
    if (json !== null) {      
      const relatedHTTPCode = json.relatedHTTPCode;
      const exceptionMessage = json.exceptionErrorMessage;
      /*
       * TODO - could be changed to cross-UI means of user notification... for now rely on alerts
       */
      alert("Operation "+operation+" failed with status "+relatedHTTPCode+" and message "+exceptionMessage);
    }
    else {
      alert("Operation "+operation+" did not get a response from the view server");
    }
  }
  



  /*
   * Platforms
   */
  

  /*
   * Load the platform.
   * view service url = dino/user/{userId}/platform/{platformName}
   * Behind the scenes the VS will take the configured platform resource and embellish it with live data
   * to fill in some details - e.g. it will get the origin, services, etc.
   * platform url     = {platformRootURL}/open-metadata/platform-services/users/{userId}/server-platform/origin'
   * 
   */

  const loadPlatform = (platformName) => {
    requestContext.callPOST("platform", platformName,  "platform/"+platformName, null, _loadPlatform);
  };

  const _loadPlatform = (json) => {
    if (json !== null) {
      if (json.relatedHTTPCode === 200 ) {
        let requestSummary = json.requestSummary;
        let platformOverview = json.platformOverview;
        if (requestSummary !== null && platformOverview !== null) {
          processRetrievedPlatform(requestSummary, platformOverview);
          return;
        }
      }
    }
    /*
     * On failure ... json could be null or contain a bad relatedHTTPCode
     */
    reportFailedOperation("loadPlatform",json);
  }

 
  /*
   * A platform query to the VS has returned a platform overview object. 
   * Add the platform to the gens if necessary.
   * platform : {
   *   displayName
   *   description
   *   platformRootURL
   *   platformOrigin
   *   accessServices, etc...
   * }
   */
  const processRetrievedPlatform = (requestSummary, platformOverview) => {

    /*
     * Perform validations on the platform object.
     *
     * Check the platform has a root URL (needed for identity)
     */
    if (!platformOverview.platformRootURL) {
      return;
    }

    /*
     * Generate the GUID for the platform and check if it's already known.
     */ 
    let guid = genPlatformGUID(platformOverview.platformName);
    
    /*
     * Add the meta fields..
     */
    platformOverview.category = "platform";
    platformOverview.guid     = guid;

    let update_objects             = {};
    update_objects.resources       = {};
    update_objects.relationships   = {};
    update_objects.resources[guid] = platformOverview;
    
    updateGens(update_objects, requestSummary);

    /*
     * Set the newly added platform to be the focus...
     */
    setFocus( { category : "platform", guid : guid } );    
    /*
     * Clear any server configuration state
     */
    setServerConfig( { stored : null, active : null , loading : "init"} );
    /*
     * Reset the operation state
     */
    setOperationState( { state : "inactive", name : "" } ); 
  }

  /*
   * update_objects is a map of resources, relationships to be located and updated or added.
   * requestSummary is the request information to associate with a new gen if one is needed
   * Note that older objects may be updated by the request and the requestSummary is unchanged as 
   * it is really to provide an audit trail of when resources and relationships were first added.
   */
  const updateGens = (update_objects, requestSummary) => {
    /*
     * Update the gens. 
     * The update process will always update an existing entry (resource or relationship) to acquire any
     * changed properties; or it will add a new gen to hold any new resources or relationships
     * Start by cloning the gens array.
     * Locate the desired entry (by GUID) in the gens clone and update or add.
     * For new entries (only) also update the guidToGenId map.
     */
    let gens_clone = Object.assign([],gens);
    let map_clone = Object.assign({},guidToGenId);
    /*
     * If a new gen is needed, add all new resources and relationships into the same new gen...
     */
    let addingGen = false;
    /*
     * These may not be used, but that's OK...
     */
    let ad_genId = getLatestGenId() + 1;
    let ad_gen = createEmptyGen(requestSummary);

    /*
     * Process resources - resourceKeys is a list of guids
     */
    let resourceKeys = Object.keys(update_objects.resources);
    resourceKeys.forEach(guid => {

      /*
       * Check whether the GUID is already known
       */
      let ex_genId = guidToGenId[guid];
      if (ex_genId !== undefined) {
        /*
         * Resource is already known
         */
        let ex_gen = gens_clone[ex_genId - 1];
        /*
         * Update the existing resource...
         */
        let existingResource = ex_gen.resources[guid];
        let mergedResource = Object.assign(existingResource, update_objects.resources[guid]);
        ex_gen.resources[guid] = mergedResource;
      }
      else {
        /*
         * The resource was not found in the map, so add it in a new gen.
         */  
        addingGen = true;
     
        /*
         * Add the resource to the new gen.
         */
        ad_gen.resources[guid]   = update_objects.resources[guid];
        /*
         * Set the genId in the resource...
         */
        ad_gen.resources[guid].gen = ad_genId;
        /*
         * Update the guidToGenId map clone
         */
        map_clone[guid] = ad_genId;
      }
    });

    /*
     * Process relationships - relationshipKeys is a list of guids
     */
    let relationshipKeys = Object.keys(update_objects.relationships);
    relationshipKeys.forEach(guid => {
      /*
       * Check whether the GUID is already known
       */
      let ex_genId = guidToGenId[guid];
      if (ex_genId !== undefined) {
        /*
         * GUID is already known
         */
        let ex_gen = gens_clone[ex_genId - 1];
        /*
         * Update the existing relationship...
         *
         * With regard to active staus the desired result from this is that if either the 
         * existing or new relationship is active the merged (updated) relationship should 
         * be active. 
         */
         let existingRelationship = ex_gen.relationships[guid];
         let eitherActive = update_objects.relationships[guid].active || existingRelationship.active;
         let mergedRelationship = Object.assign(existingRelationship, update_objects.relationships[guid]);
         /*
          * Handle active boolean explicity
          */
         mergedRelationship.active = eitherActive;
         ex_gen.relationships[guid] = mergedRelationship;
      }
      else {
        /*
         * The resource was not found in the map, so add it in a new gen.
         */  
        addingGen = true;
       
        /*
         * Add the resource to the new gen.
         */
        ad_gen.relationships[guid]   = update_objects.relationships[guid];
        /*
         * Set the genId in the resource...
         */
        ad_gen.relationships[guid].gen = ad_genId;
        /*
         * Update the guidToGenId map clone
         */
        map_clone[guid] = ad_genId;
      }
    });

    if (addingGen) {
      /*
       * There is at least one new resource or relationship.
       * Add the new gen to the gens clone
       */
      gens_clone[ad_genId -1] = ad_gen;
    }

    /*
     * Regardless of whether a new resource or relationship was added, or 
     * objects were just updated, update the real gens....
     */
    setGens(gens_clone);

    if (addingGen) {
      /*
       * Stamp the new guid map
       */
      setGuidToGenId(map_clone);
    }
  }



 
  /*
   * Get the platform that is the current focus.
   * This function verifies the expectation that there is a focus and that it is a platform
   * It also verifies that it can find a gen containing the guid of the focus platform.
   * If all of these things are true, the platform is returned.
   */
  const getFocusPlatform = () => {
    if (focus.category === "platform") {
      let guid = focus.guid;
      if (guid) {
        let genId = guidToGenId[guid];
        if (genId !== null) {
          let gen = gens[genId-1];
          return gen.resources[guid];
        }
      }
    }
    return null;
  }


  const getActiveServers = (platformGUID) => {
    let platformName = mapPlatformGUIDToPlatformName(platformGUID);
    requestContext.callPOST("platform", platformName, "platform/"+platformName+"/servers/active", null, _getActiveServers);
  };

  const _getActiveServers = (json) => {
    if (json !== null) {
      if (json.relatedHTTPCode === 200 ) {
        let requestSummary = json.requestSummary;
        let serverList = json.serverList;
        if (requestSummary !== null && serverList !== null) {
          loadServersFromPlatformQuery(requestSummary, serverList);
          return;
        }
      }
    }
    /*
     * On failure ... json could be null or contain a bad relatedHTTPCode
     */
    reportFailedOperation("getActiveServers",json);
  }

  /*
   * Get the servers that are known on this platform
   * view service url = dino/user/{userId}/platform/{platformName}/servers/
   * platform url     = {platformRootURL}/open-metadata/platform-services/users/{userId}/server-platform/servers'
   * 
   * getKnownServers is an asynchronous function that issues the request and (in _getKnownServers) retrieves the
   * response.
   */
  const getKnownServers = (platformGUID) => {      
    let platformName = mapPlatformGUIDToPlatformName(platformGUID);
    requestContext.callPOST("platform", platformName, "platform/"+platformName+"/servers", null, _getKnownServers);

  };

  const _getKnownServers = (json) => {
    if (json !== null) {
      if (json.relatedHTTPCode === 200 ) {
        let requestSummary = json.requestSummary;
        let serverList = json.serverList;
        if (requestSummary !== null && serverList !== null) {
          loadServersFromPlatformQuery(requestSummary, serverList);
          return;
        }
      }
    }
    /*
     * On failure ... json could be null or contain a bad relatedHTTPCode
     */
    reportFailedOperation("getKnownServers",json);
  }
  

  const mapPlatformGUIDToPlatformName = (guid) => {
    let platformGenId = guidToGenId[guid];
    if (platformGenId) {
      let platformGen = gens[platformGenId-1];
      if (platformGen) {
        let platform = platformGen.resources[guid];
        if (platform) {
          let platformName = platform.platformName;
          return platformName;
        }
      }
    }
    alert("Could not map supplied platform GUID "+guid+" to a platform!");
    return null;
  }

  const mapServerGUIDToServerName = (guid) => {
    let serverGenId = guidToGenId[guid];
    if (serverGenId) {
      let serverGen = gens[serverGenId-1];
      if (serverGen) {
        let server = serverGen.resources[guid];
        if (server) {
          let serverName = server.serverName;
          return serverName;
        }
      }
    }
    alert("Could not map supplied server GUID "+guid+" to a server!");
    return null;
  }
 


  

  

  /*
   * Loading servers
   * 
   * When loading a server that is known from the configured servers in the server 
   * selector, we supply the platform name from the configuration entry.
   * The server selector can therefore call loadServer directly. This is not the 
   * case when refreshing a server that is already stored in a gen.
   *
   * When loading (refreshing) a server that is already in a gen, we need to find
   * out which platform (i.e. serverInstance) the user wants. If there is only one
   * platform associated with the server it is easy. If there are multiple platforms
   * we need to get the user to click on the corresponding link.
   */

  const loadServerFromSelector = (serverName, platformName, serverInstanceName, description) => {
    setOperationState({state:"loading", name: serverName});
    requestContext.callPOST("server", serverName,  "server/"+serverName, 
      { serverName : serverName, platformName : platformName,
        serverInstanceName : serverInstanceName , description : description
      }, _loadServer);
  }


  const loadServerFromGen = (server) => {
    let platformList   = server.platforms;
    /*
     * If there are no platforms indicate that no further details are avaiable (e.g. the server may have 
     * been discovered through cohort membership and we do not know a platform that hosts it)
     */
    if (!platformList || platformList.length === 0) {
      alert("There are no platforms listed for the server "+server.serverName+" so details cannot be retrieved.");
      return;
    }
    else {
      /* 
        * Check how many platforms the server is running on. If there is one platform, query it.
        * If there is more than one platform ask the user to click the link corresponding to the 
        * instance of the server they wish to load and display.
        */
      if (platformList.length === 1) {
        let platformName = platformList[0];
        loadServer(server.serverName, platformName);
      }
      else {
        /*
          * Multi-platform case. Provide user feedback.
          */
        alert("For a server on multiple platforms, select the link from the platform to the server to indicate which instance of the server to display");
      }
    }
  }


  const loadServer = (serverName, platformName) => {  
    requestContext.callPOST("server", serverName,  "server/"+serverName, 
      { serverName : serverName, platformName : platformName }, _loadServer);
  };

  const _loadServer = (json) => {
    if (json !== null) {
      if (json.relatedHTTPCode === 200 ) {
        let requestSummary = json.requestSummary;
        let serverOverview = json.serverOverview;
        if (requestSummary !== null && serverOverview !== null) {
          processRetrievedServer(requestSummary, serverOverview);
          return;
        }
      }
    }
    /*
     * On failure ... json could be null or contain a bad relatedHTTPCode
     */
    reportFailedOperation("loadServer",json);
  }


  /*
   * User has retrieved the active servers for the focus platform. 
   * Include the servers in the gens.
   * The servers are contained in a list of names (only) and at this stage the name is sufficient.
   * If the user subsequently selects one of the servers (in the platform list of the diagram) 
   * that is the time to retrieve the server overview with nore information to display, similar
   * to addPlatform with the platformOverview.
   *
   * The serverList is a list of DinoServerInstance objects
   */
  const loadServersFromPlatformQuery = (requestSummary, serverList) => {

    /*
     * Check the list is not empty
     */
    if (!serverList) {
      return;
    }


    let update_objects           = {};
    update_objects.resources     = {};
    update_objects.relationships = {};

  
    serverList.forEach(listedServer => {


      /*
       * Perform validations on the serverInstance object.
       */
      if (!listedServer.serverName || !listedServer.platformName) {
        return;
      }


      let serverName     = listedServer.serverName;
      let platformName   = listedServer.platformName;
      let serverGUID     = genServerGUID(serverName);
    
      let server          = {};
      server.category     = "server";
      server.serverName   = serverName;
      server.guid         = serverGUID;      

      /*
       * Find out if the server already exists - and if so augment the platform 
       * list if the platform is not present...
       */
      let serverGenId     = guidToGenId[serverGUID]; 

      if (serverGenId === undefined) {
        /*
         * Since this is a new server, this must be the first platform
         */
        server.platforms  = [ platformName ];
      }
      else {
        /*
         * The server was already known so check if platform present. Add it if not present.
         * The other fields will be updated too.
         */
        let ex_genId      = guidToGenId[serverGUID];
        let ex_gen        = gens[ex_genId - 1];
        let ex_server     = ex_gen.resources[serverGUID];
        if (ex_server.platforms.includes(platformName)) {
          /*
           * This platform is already known, no update needed to new server object
           */
        }
        else {
          /*
           * This platform is not known, need to update new server object with replacement platforms array
           */
          let ex_platforms = Object.assign([],ex_server.platforms);
          ex_platforms.push(platformName);
          server.platforms = ex_platforms;
        }
      }

      update_objects.resources[serverGUID] = server;
    
      /*
       * Synthesize a relationship from the platform to this server...
       */
      let serverInstanceName                  = serverName+"@"+platformName;
      let serverInstanceGUID                  = genServerInstanceGUID(serverInstanceName); 

      let serverInstance                      = {};
      serverInstance.category                 = "server-instance";
      serverInstance.serverInstanceName       = serverInstanceName;
      serverInstance.guid                     = serverInstanceGUID;
      serverInstance.serverName               = serverName;
      serverInstance.platformName             = platformName;
      serverInstance.active                   = listedServer.isActive;
      /*
       * Include graph navigation ids, using platformGUID to identify the source
       */
      let platformGUID                        = genPlatformGUID(platformName);
      serverInstance.source                   = platformGUID;
      serverInstance.target                   = serverGUID;
     
       update_objects.relationships[serverInstanceGUID]=serverInstance; 
    });
      
    updateGens(update_objects, requestSummary);

    /*
     * Do not set the focus to any of the servers - leave it on the platform
     */

  }

  
  /*
   * Get the server that is the current focus.
   * This function verifies the expectation that there is a focus and that it is a server.
   * It also verifies that it can find a gen containing the guid of the focus server.
   * If all of these things are true, the server is returned.
   */
  const getFocusServer = () => {
    if (focus.category === "server") {
      let guid = focus.guid;
      if (guid) {
        let genId = guidToGenId[guid];
        if (genId !== null) {
          let gen = gens[genId-1];
          return gen.resources[guid];
        }
      }
    }
    return null;
  }

  
  /*
   * Check that a resource exists that has the specified guid
   */ 
  const resourceExists = (guid) => {
    let exists = guidToGenId[guid] !== undefined;
    return exists;
  }
  
  

  /*
   * A component has requested that the focus is changed to the entity with the specified GUID.
   */
  const changeFocusResource = (guid) => {
    /*
     * If the resource is the current focus - deselect it.
     */
    if (guid === focus.instanceGUID) {
      clearFocusResource();
    }

    else {
          
      /*
       * Retrieve the new resource.....
       * Every resource has a category field.
       */
      if (guidToGenId[guid] !== undefined) {
        const genId            = guidToGenId[guid];
        const gen              = gens[genId-1];
        const resource         = gen.resources[guid];

        switch(resource.category) {

          case "platform":
            setOperationState({state:"loading", name: resource.platformName});
            loadPlatform(resource.platformName);
            break;

          case "server":
            setOperationState({state:"loading", name: resource.serverName});
            loadServerFromGen(resource);
            break;
              
          case "service":
            /*
             * Not expecting a service to become the focus - if that changes, add code here
             */
            break;

          case "cohort":
            /*
             * Not expecting a cohort to become the focus - if that changes, add code here
             */
            break;

        }
      }
    }
  } 


  /*
   * The resources context has been asked to load the target of a relationship....
   * This is currently only valid if the source is a platform and target is a server.
   */
  const selectTargetResource = (relationshipGUID) => {
    let relGenId        = guidToGenId[relationshipGUID];
    let relGen          = gens[relGenId - 1];
    let relationship    = relGen.relationships[relationshipGUID];
    let sourceGUID      = relationship.source;
    let targetGUID      = relationship.target;
    /*
     * Check that the source resource is a platform
     */
    let srcGenId        = guidToGenId[sourceGUID];
    let srcGen          = gens[srcGenId - 1];
    let src             = srcGen.resources[sourceGUID];
    /*
     * Check that the target resource is a server
     */
    let tgtGenId        = guidToGenId[targetGUID];
    let tgtGen          = gens[tgtGenId - 1];
    let tgt             = tgtGen.resources[targetGUID];
    if (src.category === "platform" && tgt.category === "server") {
      /*
       * Retrieve the server and make it the new focus 
       */
      setOperationState({state:"loading", name: tgt.serverName});
      loadServer(tgt.serverName, src.platformName);
    }
    else {
      /*
       * Provide an alert. This is not very nice and it would be preferable to use a 
       * cross-UI means of advising the user of an error condition. 
       * The need for this warning may be relaxed if we allow other target types to 
       * be selected by link selection.
       */
      alert("The selected link is not a platform-server link so no operation is defined");
    }
  }


  /*
   * clearFocusResource resets the category, guid for the focus resource to a state in which nothing is selected - 
   * i.e. there is no focus.
   * This operation is atomic (all aspects are updated as one state change) to avoid sequencing,
   * e.g. if the category were set first - it would trigger other components to re-render - and if 
   * the category does not match the other aspects, they will be very confused.
   */  
  const clearFocusResource = () => {
    setFocus( { category : "", guid     : ""  });
    /*
     * Ensure any server configuration state is cleared
     */
    setServerConfig( { stored : null, active : null , loading : "init"} );
  }


  /*
   * Servers
   */

  
  /*
   * processRetrievedServer needs to allow for cases when a server skeleton has been added to the graph and the user
   * then selects it - so that the full server overview is retrieved from the view service. The received  
   * server overview should augment or replace the server skeleton. This situation does not occur with 
   * platforms because the 'platform skeletons' used for selection are in the platform selector (only).
   * If servers are configured as resource endpoints (and the user selects one) there will be no 
   * server skeleton in the graph; so processRetrievedServer needs to cover a mixture of cases.
   */

  const processRetrievedServer = (requestSummary, serverOverview) => {


    let platformName             = requestSummary.platformName;
    let serverName               = serverOverview.serverName;

    let update_objects = {};
    update_objects.resources = {};
    update_objects.relationships = {};
    

    /*
     * Generate the GUID for the server.
     * If the server is being loaded as a result of a focus change, the server should already be known.
     * If the server is known any new information will be merged into it.
     * If the server is not known it will be added. 
     */
  
    let serverGUID = genServerGUID(serverOverview.serverName);

    /*
     * Create a server object - same as if the server was returned by a platform 
     * getActiveServers or getKnownServers query.
     */
    
    let server                   = {};
    server.category              = "server";
    server.serverName            = serverName;
    server.guid                  = serverGUID;
    if (serverOverview.description)
      server.description         = "Loaded by "+serverOverview.serverInstanceName+" server link.\\n"+serverOverview.description;
    else
      server.description         = "Loaded by "+platformName+" platform query";
    server.platformRootURL       = serverOverview.platformRootURL;
    server.serverOrigin          = serverOverview.serverOrigin;
    server.serverClassification  = serverOverview.serverClassification;
    server.cohortDetails         = serverOverview.cohortDetails;
    server.serverStatus          = serverOverview.serverStatus;
    server.serverServicesList    = serverOverview.serverServicesList;

    /*
     * Find out if the server already exists - and if so augment the platform list if the platform is not present.
     */
    let serverGenId = guidToGenId[serverGUID];
    if (serverGenId === undefined) {
      /* 
       * Server is new so this must be the first platform
       */
      server.platforms             = [ platformName ];
    }
    else {
      /*
       * Server already known, check if platform present and add if not present.
       * The other fields will be updated too
       */
      let ex_genId = guidToGenId[serverGUID];
      let ex_gen   = gens[ex_genId - 1];
      let ex_server = ex_gen.resources[serverGUID];
      if (ex_server.platforms.includes(platformName)) {
        /*
         * This platform is already known, no update needed to new server object
         */
      }
      else {
        /*
         * This platform is not known, need to update new server object with replacement platforms array
         */
        let ex_platforms = Object.assign([],ex_server.platforms);
        ex_platforms.push(platformName);
        server.platforms = ex_platforms;
      }
    }
    update_objects.resources[serverGUID]=server;

    /*
     * The server may have been loaded by a platform operation or it may have been loaded 
     * directly from a server link in the ServerSelector. In the latter case we do not 
     * necessarily have the server link's platform in the graph so must not attempt to 
     * create a relationship to it. If the platform is in the graph, we need a relationship.
     */
    let platformGUID = genPlatformGUID(platformName);
    if (resourceExists(platformGUID)) {

      /*
       * Synthesize/update relationship from the platform to this server...
       * THe server may already have a relationship to its platform, but it could have 
       * changed state (active -> stopped or vice versa)
       */
      let serverInstanceName                  = serverName+"@"+platformName;
      let serverInstanceGUID                  = "SERVER_INSTANCE_"+serverInstanceName;

      let serverInstance                      = {};
      serverInstance.category                 = "server-instance";
      serverInstance.serverInstanceName       = serverInstanceName;
      serverInstance.guid                     = serverInstanceGUID;
      serverInstance.serverName               = serverName;
      serverInstance.platformName             = platformName;
      serverInstance.active                   = serverOverview.serverStatus.isActive;
      /*
       * Include graph navigation ids, using the platformGUID to identify the source
       */
      serverInstance.source                   = platformGUID;
      serverInstance.target                   = serverGUID;
     
      update_objects.relationships[serverInstanceGUID]=serverInstance; 
    }

    updateGens(update_objects, requestSummary);
  
    /*
     * Set the newly added server to be the focus.
     */
    setFocus({category : "server", guid : serverGUID});   
    setServerConfig( { stored : null, active : null , matching : true, diffs : null, loading : "init" } );
    setOperationState({state:"inactive",name:""}); 
  
  }


  /*
   * Fetch the server configuration via the VS.
   * This will retrieve both the stored and (if the server is running) active configurations
   */
  const loadServerConfiguration = () => {

    if (focus.category !== "server") {
      return;
    }

    setServerConfig( { stored   : null, 
                       active   : null,
                       matching : "true" ,
                       diffs    : null,
                       loading  : "loading" } );

    let guid  = focus.guid;
    let genId = guidToGenId[guid];
    let gen   = gens[genId-1];
    if (gen) {
      let existingServer = gen.resources[guid];
      if (existingServer) {
        let serverName   = existingServer.serverName;
        let platformList = existingServer.platforms;
        if (!platformList || platformList.length === 0) {
          alert("There are no platforms listed for the server "+serverName+" so details cannot be retrieved.");
          return;
        }
        else {
          /* Select the platform we are querying... */
          let platformName = platformList[0];

          /* Retrieve BOTH the stored and running instance configuration for the server */
          requestContext.callPOST("server", serverName,  "server/"+serverName+"/stored-and-active-configuration",  
                                        { platformName : platformName }, 
                                        _loadServerConfiguration);
        }
      }
    }
  }

  const _loadServerConfiguration = (json) => {
  
    if (json !== null) {
      if (json.relatedHTTPCode === 200 ) {

        /*
         * For known (stopped) servers you won't get an active config.
         */
        if (json.storedConfig  && json.activeConfig ) { 

          let differences             = {};
          let mismatchedPropertyNames = [];
          let propertyPath            = [];
          let matched                 = compareConfigurations(json.storedConfig, json.activeConfig, propertyPath, mismatchedPropertyNames);
          
          if (!matched) {
            /*
             * Add them to the differences object - which is a flat rendition of the differences
             * We could make it hierarchical to mimic the config objects but that seems a bit redundant.
             */
            mismatchedPropertyNames.forEach(propName => {
               let storedValue              = propName.split('.').reduce(index, json.storedConfig);
               let activeValue              = propName.split('.').reduce(index, json.activeConfig);
               differences[propName]        = {};
               differences[propName].active = activeValue;
               differences[propName].stored = storedValue;
            });
          }
          
          setServerConfig( { stored   : json.storedConfig, 
                             active   : json.activeConfig,
                             matching : matched ? "true" : "false",
                             diffs    : differences,
                             loading  : "loaded" } );
          return;

        }
        else if (json.storedConfig !== null) {
          /*
           * There is only stored configuration. Nothing to compare, and no differences to report
           */
          setServerConfig( { stored   : json.storedConfig, 
                             active   : null,
                             matching : "true",
                             diffs    : null,
                             loading  : "loaded" } );
          return;

        }
      }
    }
    /*
     * On failure ... json could be null or contain a bad relatedHTTPCode
     */
    reportFailedOperation("loadServerConfiguration",json);
  }

  const index = (obj,i) => {return obj[i]}

  const compareConfigurations = (storedConfig, activeConfig, propertyPath, mpn) => {

    /*
     * Assume a good result, any mismatch will turn it false.
     */
    let matched = true; 

  
    /*
     * The comparison of configurations (stored vs active) compares every field
     * but avoids being prescriptive over the field names in the config, because 
     * this would require significant maintenance (of code) whenever the config is 
     * changed/extended. 
     * An alternative would be to add a differencing method in the config class 
     * (better for maintenance) which would be run in the VS (Java) which would
     * need to be field specific.
     * By doing this in JS then we are able to use a soft approach to extract 
     * the field names.
     *
     * It would (hypotehtically) be possible to compare just the properties that are
     * displayed (in the ServerDisplay module) and code the difference warnings there. 
     * But that would not reveal changes to areas of config that are not displayed in 
     * the subset of properties shown in the details panel, since that is a summary.
     *
     * This approach compares the properties that are physically present in
     * either or both of the active and stored configurations, which is sufficient.
     * We need to omit some (class and auditTrail at least) as these are either 
     * not relevant or do not constitute a mateiral change to the configuration
     * properties.
     */
    

    /*
     * Optionally you could also exclude "auditTrail" but only if the auditTrail 
     * display is NOT wanted in the differences display
     */
    let excludeProps = ["class"];  
   

    /*
     * Generate an aggregated (merged) set of keys - the union across both configs
     */
    let activeConfigPropNames = Object.keys(activeConfig);

    let storedConfigPropNames = Object.keys(storedConfig);

    let configPropNames = Object.assign([],activeConfigPropNames,storedConfigPropNames);

    let configPropNamesSorted = configPropNames.sort();
    configPropNamesSorted.forEach(propName => {
      /*
       * Exclude specified properties.
       */
      if (excludeProps.includes(propName)) {
        return;
      }
      let propType = typeof activeConfig[propName];
      if (propType === "object") {
        /* 
         * Dig deeper
         */
        let localPropertyPath = Object.assign([],propertyPath);
        localPropertyPath.push(propName);
        let match = compareConfigurations(storedConfig[propName], activeConfig[propName], 
          localPropertyPath, mpn);     
        if (!match) {
          matched = false;
        }
      }
      else {
        /*
         * Compare this property value
         */
        let match = storedConfig[propName] === activeConfig[propName];
        if (!match) {
          let localPropertyPath = Object.assign([],propertyPath);
          localPropertyPath.push(propName);
          let propertyLocator = flattenPropertyPath(localPropertyPath);
          mpn.push(propertyLocator);
          matched = false;
        }
       
      }

    })
    return matched;
  }



  const flattenPropertyPath = (propPath) => {
    if (propPath && propPath.length > 0) {
      let flatPath = propPath[0];
      for (let i=1; i<propPath.length; i++) {
        flatPath = flatPath.concat("."+propPath[i]);
      }
      return flatPath;
    }
    return null;
  }





  /*
   * Load a cohort purely from configuration (as opposed to loading it from a server's details)
   */
  const loadConfiguredCohort = (serverName, cohortName) => {

    loadCohort(serverName, cohortName, true);

  }

  /*
   * This function will load the specified cohort from the specified server's serverDetails
   */
  const loadCohortFromServer = (serverName, cohortName) => {

    loadCohort(serverName, cohortName, false);

  }

  /*
   * Load a cohort either from configuration (third parameter true) or from a server's details.
   *
   * The cohort is added into a gen so the cohort exists in its own right. It also creates an 
   * edge from the specified server to the chort. This does not need to retrieve the cohort from the VS
   * because we should already have enough cohort details.
   */
  const loadCohort = (serverName, cohortName, configured) => {


    /*
     * Find the server entry in the gens. If the server is not found the operation will fail.
     */
    let serverGUID  = genServerGUID(serverName);
    let serverGenId = guidToGenId[serverGUID];
    if (serverGenId === undefined) {
      /*
       * Operation cannot proceed - we do not have the specified server.
       */
      alert("Cannot add cohort for unknown server "+serverName);
      return;
    }

  
    /*
     * Create a cohort object
     */
    let cohortGUID = genCohortGUID(cohortName);
  
    let cohort                   = {};
    cohort.category              = "cohort";
    cohort.cohortName            = cohortName;    
    cohort.guid                  = cohortGUID;
  

    /*
     * Create a relationship from the specified server to the cohort - if we do not already have one
     * The relationship will need a guid, a source and target and a gen (which is assigned when the 
     * gen is created)
     */

    let serverCohortName                          = serverName+"@"+cohortName;
    let serverCohortGUID                          = genServerCohortGUID(serverCohortName);

    let serverCohortRelationship                  = {};
    serverCohortRelationship.category             = "server-cohort";
    serverCohortRelationship.serverCohortName     = serverCohortName;
    serverCohortRelationship.guid                 = serverCohortGUID;
    serverCohortRelationship.serverName           = serverName;
    serverCohortRelationship.cohortName           = cohortName;


    /*
     * In both the configured and non-configured cases, indicate whether the server is 
     * actively participating in the cohort.
     * This can be determined by retrieving the server from the gens, look in the serverDetails 
     * and retrieve the cohortDetails to get the connection status.
     */
    let serverGen                                  = gens[serverGenId - 1];
    let server                                     = serverGen.resources[serverGUID];
    let cohortDetails                              = server.cohortDetails[cohortName];
    let connectionDescription                      = cohortDetails.cohortDescription;
    let connectionStatus                           = connectionDescription.connectionStatus;
    let isActive                                   = connectionStatus === "CONNECTED";
    serverCohortRelationship.active                = isActive;

  
    /*
     * Include graph navigation ids.
     */
    serverCohortRelationship.source                = serverGUID;
    serverCohortRelationship.target                = cohortGUID;
  
  
    /*
     * Create a map of the objects to be updated.
     */
    let update_objects                              = {};
    update_objects.resources                        = {};
    update_objects.relationships                    = {};
    update_objects.resources[cohortGUID]            = cohort;
    update_objects.relationships[serverCohortGUID]  = serverCohortRelationship; 

    /*
     * Include a request summary - since this was a local operation there is no request information 
     * to be returned from the VS
     */
    let requestSummary             = {};
    requestSummary.serverName      = serverName;
    requestSummary.platformName    = null;
    if (configured)
      requestSummary.operation     = "Expansion of server configuration for cohort "+cohortName;
    else
      requestSummary.operation     = "Expansion of cohort "+cohortName;
    
    
    /*
     * Update the gens
     */
    updateGens(update_objects, requestSummary);
    
    /*
     * Although we're adding a cohort, leave the focus as it was... so there is no need
     * to setFocus (since there is no change) nor to setOperationState (since there was no
     * remote operation)
     */ 
  
  };




  /*
   * This function will load the specified service from the specified server's serverDetails
   * into a gen so that the service exists in its own right. It also creates an edge from the 
   * specified server to the service. This does not need to retrieve the service from the VS
   * because we should already have enough service details.
   */
  const loadService = (serverName, serviceName) => {


    /*
     * If the server is not found the operation will fail.
     */
  
    let serverGUID = genServerGUID(serverName);

    /*
     * Find the server entry in the gens
     */
    let serverGenId = guidToGenId[serverGUID];
    if (serverGenId === undefined) {
      /*
       * Operation cannot proceed - we do not have the specified server.
       */
      alert("Cannot add service for unknown server "+serverName);
      return;
    }

    /*
     * Create a service object
     */
    let serviceGUID = genServiceGUID(serviceName);

    let service                   = {};
    service.category              = "service";
    service.serviceName           = serviceName;    
    service.guid                  = serviceGUID;

    /*
     * Create a relationship from the specified server to the cohort - if we do not already have one
     * The relationship will need a guid, a source and target and a gen (which is assigned when the 
     * gen is created)
     */

    let serverServiceName                         = serviceName+"@"+serverName;
    let serverServiceGUID                         = "SERVER_SERVICE"+serverServiceName;

    let serverServiceRelationship                 = {};
    serverServiceRelationship.category            = "server-service";
    serverServiceRelationship.serverCohortName    = serverServiceName;
    serverServiceRelationship.guid                = serverServiceGUID;
    serverServiceRelationship.serverName          = serverName;
    serverServiceRelationship.cohortName          = serviceName;
    /* 
     * Server-Service relationships are always active - this is driven from the active server list.
     */
    serverServiceRelationship.active              = true;

    /*
     * Include graph navigation ids.
     */
    serverServiceRelationship.source              = serverGUID;
    serverServiceRelationship.target              = serviceGUID;

  
    /*
     * Create a map of the objects to be updated.
     */
    let update_objects                               = {};
    update_objects.resources                         = {};
    update_objects.relationships                     = {};
    update_objects.resources[serviceGUID]            = service;
    update_objects.relationships[serverServiceGUID]  = serverServiceRelationship; 

    /*
     * Include a request summary - since this was a local operation there is no request information 
     * to be returned from the VS
     */
    let requestSummary             = {};
    requestSummary.serverName      = serverName;
    requestSummary.operation       = "Expansion of service "+serviceName;
    requestSummary.platformName    = null;
      
    updateGens(update_objects, requestSummary);
  
    /*
     * Although we're adding a cohort, leave the focus as it was... so there is no need
     * to setFocus (since there is no change) nor to setOperationState (since there was no
     * remote operation)
     */ 

  };

  /*
   * Clear the state of the session - this includes the gens, the focus and the guidToGenId map.
   */
  const clear = () => {   

    /*
     * Reset the focus
     */
    clearFocusResource();
    
    /*
     * Empty the graph
     */
    const emptyArray = [];
    setGens(emptyArray);

    /*
     * Empty the map
     */
    const emptymap = {};
    setGuidToGenId(emptymap);
    
  }


  /*
   * Remove a generation from the graph
   */
  const removeGen = () => {
    /*
     * Remove the most recent gen from the active gens. This should be noticed by the DiagramManager
     * which will update the diagram data, and callback to the removeGenComplete callback.
     * 
     * If the focus resource is in the gen being removed then clear the focus.
     */    
    
 
    /*
     * Do not mutate the current array - replace for state update to register
     */
    let newList = Object.assign([],gens);
    const removedGen = newList.pop();
    if (removedGen.resources[focus.guid] !== undefined) {
      /*
       * Clear the focus
       */
      setFocus({ category  : "", guid  : "" } );
      /*
       * Clear any server configuration state
       */
      setServerConfig( { stored : null, active : null , loading : "init"} );
      /*
       * Reset any operation state
       */
      setOperationState( { state : "inactive", name : "" } ); 
    }
    setGens( newList );

    /*
     * Look for resources that were added in the removedGen, and remove them from the guidToGenId map.
     * Because the map is immutable, corral the changes in a cloned map and apply them in one replace operation
     */
    
    let newGUIDMap = Object.assign({},guidToGenId);
    const eKeys = Object.keys(removedGen.resources);
    eKeys.forEach(r => {
      delete newGUIDMap[r];
    });
    const rKeys = Object.keys(removedGen.relationships);
    rKeys.forEach(r => {
      delete newGUIDMap[r];
    });
    /*
     * Now replace the map...
     */
    setGuidToGenId(newGUIDMap);

  }

  


  return (
    <ResourcesContext.Provider
      value={{      
        /*
         * Local state
         */
        focus,
        serverConfig,
        operationState,
        gens,
        /*
         * Getters
         */
        getFocusPlatform,
        getFocusServer,
        getLatestGenId,
        getNumGens,
        getLatestGen,
        getGens,
        resourceExists,
        /*
         * Operations
         */
        loadPlatform,
        loadServer,   
        loadServerConfiguration,
        loadServerFromSelector,
        loadServersFromPlatformQuery,
        changeFocusResource,
        selectTargetResource,
        loadCohort,
        loadCohortFromServer,
        loadService,
        loadConfiguredCohort,
        clear,
        removeGen,
        getActiveServers,
        getKnownServers,
        genServiceGUID,
        genCohortGUID        
      }}
    >      
    {props.children}
    </ResourcesContext.Provider>
  );
};

ResourcesContextProvider.propTypes = {
  children: PropTypes.node  
};

export default ResourcesContextProvider;

