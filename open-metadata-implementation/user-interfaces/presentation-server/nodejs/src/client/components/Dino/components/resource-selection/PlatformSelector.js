/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useState, useEffect }         from "react";

import { ResourcesContext }                               from "../../contexts/ResourcesContext";

import { RequestContext }                                 from "../../contexts/RequestContext";

import "./resource-selector.scss"


/*
 * The PlatformSelector displays the names of the permitted platforms that retrieved from
 * the view service at strartup.  
 * The platform selector control will present platforms by name to the user and allow the user to select
 * one. During platform load, the platformName is passed to the view service, which resolves it to the 
 * configured platformRootURL for that platform. 
 * The platform's details are retrieved and the platform will become the new focus object.
 */
export default function PlatformSelector() {
  
  const resourcesContext                      = useContext(ResourcesContext);

  const requestContext                        = useContext(RequestContext);

  const [platformsLoaded, setPlatformsLoaded] = useState(false);

  /*
   * availablePlatforms is a list of names of the platforms to which requests can be sent.
   * This list comes from the view-service on initialisation.
   * If the configuration of the V-S is changed, refresh the page - or we could provide a
   * list refresh capability behind a button. 
   */
  const [availablePlatforms, setAvailablePlatforms]       = useState({});

  /*
   * Populate the available platform list by retrieving the permitted platform names 
   * from the view-service
   */
  const getPlatforms = () => {
    requestContext.callGET("resource-endpoints", _getPlatforms);
    setPlatformsLoaded(true);
  }

  const _getPlatforms = (json) => {
    if (json !== null) {
      if (json.relatedHTTPCode === 200 ) {
        let platformList = json.platformList;
        if (platformList !== null) {
          let newPlatforms = {};
          platformList.forEach(plt => {
            const newPlatform = { "platformName"    : plt.platformName, 
                                  "description"     : plt.resourceDescription, 
                                  "platformRootURL" : plt.resourceRootURL  };
            newPlatforms[plt.platformName] = newPlatform;
          });
          setAvailablePlatforms(newPlatforms);
          return;
        }
      }
    }
  }



  if (!platformsLoaded) {
    getPlatforms();
  }

  /*
   * Handler for change to platform selector.
   *
   *
   * Get the platform.
   * view service url = dino/user/{userId}/platform/{platformName}
   * Behind the scenes the VS will take the configured platform resource and embellish it with live data
   * to fill in some details - e.g. it will get the origin, registered services, etc.
   * The details will be filled in by calling various backend (platform) URLs, such as:
   * platform url     = {platformRootURL}/open-metadata/platform-services/users/{userId}/server-platform/origin'
   */
  const platformSelected = (evt) => {
    let platformName = evt.target.value;
    resourcesContext.loadPlatform(platformName);
  }


  /*
   * It's important to reset (clear) the selector if the focus changes to anything other than the selected
   * option - including (and especially) if an option from a different selector is chosen. This is to ensure
   * that if/when the user navigates back to this selector, if they click on the same (previously selected) 
   * option then we will see it as a change. Otherwise the user action will be ignored and the focus will 
   * not change.
   */
  let selectorValue;
  useEffect(
    () => {
      const focus = resourcesContext.focus;
      /*
       * If the focus is a platform then we need to ensure we track the current focus
       */
      if (focus.category === "platform") {
        selectorValue = (focus.name === "") ? "none" : focus.name;
        const selector = document.getElementById("platformSelector");
        if (selector)
          selector.value = selectorValue;
      }
      else {
        /*
         * If the focus is not a platform we need to clear the platform selector
         */
        selectorValue = "none";
        const selector = document.getElementById("platformSelector");
        if (selector)
          selector.value = selectorValue;
      }
    },
    [resourcesContext.focus]
  )
  
 
  const platformNameList = Object.keys(availablePlatforms);
  const platformNameListSorted = platformNameList.sort(function (a, b) {
    return a.toLowerCase().localeCompare(b.toLowerCase());
  });
 
  
  return (
    <div className="resource-controls">

      <p>Accessible Platforms</p>

      <label htmlFor="platformSelector">Platforms: </label>
      <select className="platform-selector" id="platformSelector" name="platformSelector"        
              onChange = { platformSelected }  size = "5" >
      {
        platformNameListSorted.length === 0 && 
        ( <option value="dummy" disabled defaultValue>No platforms yet - please add one!</option> )
      }
      {
        platformNameListSorted.length > 0 && 
        (
          platformNameListSorted.map(platformName => 
            ( <option key={platformName} value={platformName}> {platformName} </option> )
          )      
        )                                         
      }      
     </select>

    </div>

  );
}
