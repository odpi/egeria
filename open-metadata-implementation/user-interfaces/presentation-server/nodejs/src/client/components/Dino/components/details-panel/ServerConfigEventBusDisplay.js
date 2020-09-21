/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React          from "react";

import PropTypes      from "prop-types";

import "./details-panel.scss";


export default function ServerConfigEventBusDisplay(props) {

  const inConfig  = props.config;

  let outConfig;

  

  const formatEventBusConfig = (cfg) => {
    return (
      <div>
        <ul>
          <li className="details-sublist-item">Topic URL Root : {cfg.topicURLRoot}</li>
          <li className="details-sublist-item">Producer : 
            {cfg.configurationProperties.producer ? formatBootstrapEndpoints(cfg.configurationProperties.producer) : <i>blank</i>}</li>
          <li className="details-sublist-item">Consumer : 
            {cfg.configurationProperties.consumer ? formatBootstrapEndpoints(cfg.configurationProperties.consumer) : <i>blank</i>}</li> 
          <li className="details-sublist-item">Additional Properties : {cfg.additionalProperties ? formatAdditionalProperties(cfg.additionalProperties) : <i>blank</i>}</li>
        </ul>
      </div>
    );
  }

  const formatBootstrapEndpoints = (be) => {
    let ret = (
      <div>Bootstrap Servers :
        <ul>
          {formattedEndpoints(be["bootstrap.servers"])}
        </ul>
      </div>
    );
    return ret;
  }
  
  /*
   * Bootstrap servers is a comma-separated list of host and port pairs formatted as host:port.
   */
  const formattedEndpoints = (endpointList) => {
    let listEntries = null;
    let endpoints = endpointList.split(',');
    listEntries = endpoints.map( (ept) =>   
        <li className="details-sublist-item" key={ept}> 
          {ept}
        </li>
    );
    return listEntries;
  }


  const formatAdditionalProperties = (ap) => {
    let ret = (
      <div>
        <div>
          Producer bootstrap endpoints : 
          <ul>
          {formattedEndpoints(ap.producer["bootstrap.servers"])}
          </ul>
        </div>
        <div>
          Consumer bootstrap endpoints : 
          <ul>
          {formattedEndpoints(ap.consumer["bootstrap.servers"])}
          </ul>
        </div>
      </div>

    );

    return ret;
  }

 


  /*
   * Render event bus configuration
   */
  if (!inConfig) {
    outConfig = (
      <div>
        event bus configuration is empty
      </div>
    )
  }
  else {
   
    outConfig = (              
      <div>
        {formatEventBusConfig(inConfig)}          
      </div>   
      
    );
  }

  return outConfig;
}

ServerConfigEventBusDisplay.propTypes = {
  config: PropTypes.object 
};
