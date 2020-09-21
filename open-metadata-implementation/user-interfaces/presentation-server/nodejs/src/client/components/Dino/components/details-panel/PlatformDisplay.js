/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }      from "react";

import { ResourcesContext }       from "../../contexts/ResourcesContext";

import PlatformServicesDisplay    from "./PlatformServicesDisplay";

import PropTypes                  from "prop-types";

import "./details-panel.scss";


export default function PlatformDisplay() {


  const resourcesContext = useContext(ResourcesContext);


  /*
   * This component is only displayed when there is a platform in focus
   */

  
  
  /*
   * Get the servers that are active on this platform
   * view service url = dino/user/{userId}/platform/{platformName}/servers/active
   * platform url     = {platformRootURL}/open-metadata/platform-services/users/{userId}/server-platform/servers/active'
   * 
   * getActiveServers is an asynchronous function that issues the request and (in _getActiveServers) retrieves the
   * response.
   */
  const getActiveServers = (platformName) => {
    resourcesContext.getActiveServers(platformName);
  }

  /*
   * Get the servers that are known on this platform
   * view service url = dino/user/{userId}/platform/{platformName}/servers/
   * platform url     = {platformRootURL}/open-metadata/platform-services/users/{userId}/server-platform/servers'
   * 
   * getKnownServers is an asynchronous function that issues the request and (in _getKnownServers) retrieves the
   * response.
   */
  const getKnownServers = (platformName) => {
    resourcesContext.getKnownServers(platformName);
  }
 


  const flipSection = (evt) => {
    /*
     * Use currentTarget (not target) - because we need to operate relative to the button,
     * which is where the handler is defined, in order for the content section to be the sibling.
     */
    const element = evt.currentTarget;
    element.classList.toggle("active");
    const content = element.nextElementSibling;
    if (content.style.display === "block") {
      content.style.display = "none";
    }
    else {
      content.style.display = "block";
    }
  };


  let focus = resourcesContext.focus;
  let platformDetails;
  if (focus.category === "platform") {
    platformDetails = resourcesContext.getFocusPlatform();
    if (!platformDetails) {
      return null;
    }
  }
  else {
    /*
     * focus is not a platform do nothing...
     */
    return null;
  }
  

  return (

    <div className="type-details-container">
      <div className="type-details-item-bold">Platform : {platformDetails.platformName}</div>
      
      <div className="type-details-item-bold">Description : </div>
      <div className="type-details-item">{platformDetails.description}</div>
      <div className="type-details-item-bold">PlatformRootURL : </div>
      <div className="type-details-item">{platformDetails.platformRootURL}</div>
      <div className="type-details-item-bold">Platform Origin : </div>
      <div className="type-details-item">{platformDetails.platformOrigin}</div>

      <div className="type-details-item-bold">Servers:</div>
      <div>
      <button className="server-control-button"
              onClick = { () => getActiveServers(platformDetails.guid) }  >
          Get Active Servers
      </button>
      <button className="server-control-button"
              onClick = { () => getKnownServers(platformDetails.guid) }  >
          Get All Servers
      </button>
      </div>

      <div className="type-details-item-bold">Registered Services:</div>
      <div>
      <button className="collapsible" onClick={flipSection}> Access Services: </button>
        <div className="content">
        <ul>
          <li>
          <PlatformServicesDisplay serviceList={platformDetails.accessServices} />
          </li>
        </ul>
        </div>
      </div>
      <div>
      <button className="collapsible" onClick={flipSection}> Common Services: </button>
        <div className="content">
        <ul>
          <li>
          <PlatformServicesDisplay serviceList={platformDetails.commonServices} />
          </li>
        </ul>
        </div>
      </div>
      <div>
      <button className="collapsible" onClick={flipSection}> Governance Services: </button>
        <div className="content">
        <ul>
          <li>
          <PlatformServicesDisplay serviceList={platformDetails.governanceServices} />
          </li>
        </ul>
        </div>
      </div>
      <div>
      <button className="collapsible" onClick={flipSection}> View Services: </button>
        <div className="content">
        <ul>
          <li>
          <PlatformServicesDisplay serviceList={platformDetails.viewServices} />
          </li>
        </ul>
        </div>
      </div>
    </div>
  )
  
}


PlatformDisplay.propTypes = {
  
};
