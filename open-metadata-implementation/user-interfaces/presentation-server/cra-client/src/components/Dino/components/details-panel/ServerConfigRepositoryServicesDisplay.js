/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext }               from "react";

import { ResourcesContext }                from "../../contexts/ResourcesContext";

import PropTypes                           from "prop-types";

import "./details-panel.scss";


export default function ServerConfigRepositoryServicesDisplay(props) {

  const resourcesContext = useContext(ResourcesContext);

  const inConfig         = props.config;
  const serverName       = props.serverName;

  let outConfig;


  
  /*
   * Handler for flopping a collapsible
   */
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

  /*
   * As the user flips a cohort section, expand the cohort details display and if the cohort has not 
   * already been added to the gens, add the cohort to the gens so that it appears in the topology diagram.
   * A cohort is only added if either a) it is in the active server registrations section or b) is in the 
   * configuration and (in either case) the user clicks to expand the cohort. The user may expand either 
   * or both sections: expanding the config should result in a dotted edge to the cohort; if the active  
   * cohort entry is expanded this should result in a solid edge. If both are expanded show the solid edge
   * regardless of the order that they are expanded. i.e. we may display dtted then solid, or solid and 
   * retain solid when the configuration entry is expanded. That is all taken care of in the ResourcesContext
   * and DiagramManager.
   */
  const flipSectionAndLoadConfiguredCohort = (evt) => {

    let cohortName = evt.target.id;
    resourcesContext.loadConfiguredCohort(serverName, cohortName);

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


  const formatRepositoryServicesConfig = (cfg) => {
    return (
      <div>
        <ul>
          <li className="details-sublist-item">
            <button className="collapsible" onClick={flipSection}> Configured Cohorts : </button>
            <div className="content">
              {cfg.cohortConfigList ? formatCohortConfigList(cfg.cohortConfigList) : <i>blank</i>}
            </div>
          </li>
          <li className="details-sublist-item">
            <button className="collapsible" onClick={flipSection}> Enterprise Access Config : </button>
            <div className="content">
              {cfg.enterpriseAccessConfig ? formatEnterpriseAccessConfig(cfg.enterpriseAccessConfig) : <i>blank</i>}
            </div>
          </li> 
          <li className="details-sublist-item">
            <button className="collapsible" onClick={flipSection}> Local Repository Config : </button>
            <div className="content">
              {cfg.localRepositoryConfig ? formatLocalRepositoryConfig(cfg.localRepositoryConfig) : <i>blank</i>}
            </div>
          </li>
        </ul>
      </div>
    );
  }

  const formatCohortConfigList = (ccl) => {
    return (
        <ul>
          {formattedCohortConfigs(ccl)}
        </ul>
    );
  }
  
  /*
   * Bootstrap servers is a comma-separated list of host and port pairs formatted as host:port.
   */
  const formattedCohortConfigs = (cohortConfigList) => {
    let listEntries = null;
    listEntries = cohortConfigList.map( (cc) =>   
        <li className="details-sublist-item" key={cc}> 
          <button className="collapsible" id={cc.cohortName} onClick={flipSectionAndLoadConfiguredCohort}> {cc.cohortName} : </button>
            <div className="content">
            {formattedCohortConfig(cc)}
            </div>
        </li>
    );
    return listEntries;
  }

  const formattedCohortConfig = (cc) => { 
    return (
      <div>
        <ul>
          <li className="details-sublist-item">Cohort Name : {cc.cohortName}</li>
          <li className="details-sublist-item">Cohort OMRS Topic Connection : {formatTopicConnection(cc.cohortOMRSTopicConnection)}</li>
          <li className="details-sublist-item">Cohort OMRS Topic Connection Protocol Version : {cc.cohortOMRSTopicProtocolVersion}</li>
          <li className="details-sublist-item">Event To Process Rule : {cc.eventsToProcessRule}</li>
          <li className="details-sublist-item">Cohort Registry Connection : {formatCohortRegistryConnection(cc)}</li>
        </ul>
      </div>
    );
  }

  const formatTopicConnection = (tc) => {
    return (
      <div>
      <div>
        Connector Provider : {tc.connectorType.connectorProviderClassName}
      </div>
      <div>
        Embedded Connections : 
        <ul>
          {formatEmbeddedConnections(tc.embeddedConnections)}
        </ul>
      </div>
      </div>
    );
    
  }

  const formatEmbeddedConnections = (embCons) => {
    return (
      embCons.map( (embCon) =>
        <li className="details-sublist-item" key={embCon.position}> 
          {formatEmbeddedConnection(embCon)}       
        </li>
      )
    );
  }

  const formatEmbeddedConnection = (ec) => {
    let ret = (
      <div>
      <div>
        Display Name : {ec.displayName}
      </div>
      <div>
        Connector Type : {ec.embeddedConnection.connectorType.displayName}
      </div>
      <div>
        Endpoint : {ec.embeddedConnection.endpoint.address}
      </div>
      <div>
        Configuration Properties : {ec.embeddedConnection.configurationProperties ? 
           formatEmbeddedConnectionConfigurationProperties(ec.embeddedConnection.configurationProperties) :
           <i>blank</i>}
      </div>
      </div>
    );
    return ret;
  }

  const formatEmbeddedConnectionConfigurationProperties = (cfgProps) => {
    return (
      <div>
      <div>
        Local Server Id : {cfgProps["local.server.id"]}
      </div>
      <div>
        Producer : { (cfgProps.producer && cfgProps.producer["bootstrap.servers"]) ? 
                        cfgProps.producer["bootstrap.servers"] : <i>blank</i>}
      </div>
      <div>
        Consumer : { (cfgProps.consumer && cfgProps.consumer["bootstrap.servers"]) ? 
                        cfgProps.consumer["bootstrap.servers"] : <i>blank</i>}
      </div>
      </div>
    )
  }

  const formatCohortRegistryConnection = (rc) => {
    return (
      <div>
      <div>
        Cohort Name : {rc.cohortName}
      </div>
      <div>
        Cohort OMRS Topic Connection : {formatTopicConnection(rc.cohortOMRSTopicConnection)}
      </div>
      <div>
        Cohort OMRS Topic Protocol Version : {rc.cohortOMRSTopicProtocolVersion}
      </div>
      <div>
        Cohort Registry Connection : {formatInnerCohortRegistryConnection(rc.cohortRegistryConnection)}
      </div>
      <div>
        Events To Process Rule : {rc.eventsToProcessRule}
      </div>
      </div>
    )
  }

  const formatInnerCohortRegistryConnection = (crc) => {
    return (
      <div>
      <div>
        Connector Type : {crc.connectorType.displayName}
      </div>
      <div>
        Endpoint : {crc.endpoint.address}
      </div>
      </div>
    );
  }


  const formatEnterpriseAccessConfig = (eac) => {
    return (
      <div>
      <div>
        Enterprise Metadata Collection Name : {eac.enterpriseMetadataCollectionName}
      </div>
      <div>
        Enterprise Metadata Collection Id : {eac.enterpriseMetadataCollectionId}
      </div>
      <div>
        Enterprise OMRS Topic Connection : {formatTopicConnection(eac.enterpriseOMRSTopicConnection)}
      </div>
      <div>
        Enterprise OMRS Topic Protocol Version : {eac.enterpriseOMRSTopicProtocolVersion}
      </div>
      </div>
    )
  }


  const formatLocalRepositoryConfig = (lrc) => {
    let ret = (
      <div>
      <div>
        Metadata Collection Name : {lrc.metadataCollectionName}
      </div>
      <div>
        Metadata Collection Id : {lrc.metadataCollectionId}
      </div>
      <div>
        Local Repository Mode : {lrc.localRepositoryMode}
      </div>
      <div>
        Local Repository Local Connection : {formatLocalRepositoryLocalConnection(lrc.localRepositoryLocalConnection)}
      </div>
      <div>
        Local Repository Remote Connection : {formatLocalRepositoryRemoteConnection(lrc.localRepositoryRemoteConnection)}
      </div>
      <div>
        Events To Send Rule : {lrc.eventsToSendRule}
      </div>
      <div>
        Events To Save Rule : {lrc.eventsToSaveRule}
      </div>
      </div>
    );
    return ret;
  }

  const formatLocalRepositoryLocalConnection = (lrc) => {
    let ret = (
      <div>
      <div>
        Connector Type : {lrc.connectorType.displayName}
      </div>
      </div>
    );
    return ret;
  }

  const formatLocalRepositoryRemoteConnection = (rrc) => {
    let ret = (
      <div>
      <div>
        Connector Type : {rrc.connectorType.displayName}
      </div>
      <div>
        Endpoint : {rrc.endpoint.address}
      </div>
      </div>
    );
    return ret;
  }
 

  /*
   * Render repository services configuration
   */
  if (!inConfig) {
    outConfig = (
      <div>
        repository services configuration is empty
      </div>
    )
  }
  else {
   
    outConfig = (              
      <div>
       {formatRepositoryServicesConfig(inConfig)}
       </div>
    );
  }
 
  return outConfig;
}

ServerConfigRepositoryServicesDisplay.propTypes = {
  serverName : PropTypes.string,
  config: PropTypes.object 
};
