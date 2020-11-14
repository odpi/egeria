/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { createContext, useContext, useEffect, useRef, useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";

import { IdentificationContext } from "./IdentificationContext";

import accessServices from "../components/ServerAuthor/defaults/accessServices";
import serverTypes from "../components/ServerAuthor/defaults/serverTypes";
import viewServices from "../components/ServerAuthor/defaults/viewServices";
import discoveryEngines from "../components/ServerAuthor/defaults/discoveryEngines";
import stewardshipEngines from "../components/ServerAuthor/defaults/stewardshipEngines";

export const ServerAuthorContext = createContext();
export const ServerAuthorContextConsumer = ServerAuthorContext.Consumer;

const ServerAuthorContextProvider = props => {

  const { userId, serverName: tenantId, user } = useContext(IdentificationContext);

  // Known Servers
  const [knownServers, setKnownServers] = useState([]);
  // Basic Config
  const [newServerName, setNewServerName] = useState("");
  const [newServerLocalURLRoot, setNewServerLocalURLRoot] = useState("https://localhost:9443");
  const [newServerLocalServerType, setNewServerLocalServerType] = useState(serverTypes[0].label);
  const [newServerOrganizationName, setNewServerOrganizationName] = useState(user.organizationName);
  const [newServerLocalUserId, setNewServerLocalUserId] = useState("");
  const [newServerLocalPassword, setNewServerLocalPassword] = useState("");
  const [newServerSecurityConnector, setNewServerSecurityConnector] = useState("");
  const [newServerRepository, setNewServerRepository] = useState("in-memory-repository");
  const [newServerMaxPageSize, setNewServerMaxPageSize] = useState(1000);
  // Access Services
  const [availableAccessServices, setAvailableAccessServices] = useState(accessServices);
  const [selectedAccessServices, setSelectedAccessServices] = useState(accessServices);
  // Cohorts
  const [newServerCohorts, setNewServerCohorts] = useState([]);
  // Archives
  const [newServerOMArchives, setNewServerOMArchives] = useState([]);
  // Proxy
  const [newServerProxyConnector, setNewServerProxyConnector] = useState("");
  const [newServerEventMapperConnector, setNewServerEventMapperConnector] = useState("");
  const [newServerEventSource, setNewServerEventSource] = useState("");
  // View Services
  const [availableViewServices, setAvailableViewServices] = useState(viewServices);
  const [selectedViewServices, setSelectedViewServices] = useState([]);
  const [newServerViewServiceRemoteServerName, setNewServerViewServiceRemoteServerName] = useState("");
  const [newServerViewServiceRemoteServerURLRoot, setNewServerViewServiceRemoteServerURLRoot] = useState("");
  // Discovery Engines
  const [availableDiscoveryEngines, setAvailableDiscoveryEngines] = useState(discoveryEngines);
  const [selectedDiscoveryEngines, setSelectedDiscoveryEngines] = useState([]);
  const [newServerDiscoveryEngineRemoteServerName, setNewServerDiscoveryEngineRemoteServerName] = useState("");
  const [newServerDiscoveryEngineRemoteServerURLRoot, setNewServerDiscoveryEngineRemoteServerURLRoot] = useState("");
  // Stewardship Engines
  const [availableStewardshipEngines, setAvailableStewardshipEngines] = useState(stewardshipEngines);
  const [selectedStewardshipEngines, setSelectedStewardshipEngines] = useState([]);
  const [newServerStewardshipEngineRemoteServerName, setNewServerStewardshipEngineRemoteServerName] = useState("");
  const [newServerStewardshipEngineRemoteServerURLRoot, setNewServerStewardshipEngineRemoteServerURLRoot] = useState("");
  // Notifications
  const [notificationType, setNotificationType] = useState("error");
  const [notificationTitle, setNotificationTitle] = useState("");
  const [notificationSubtitle, setNotificationSubtitle] = useState("");
  // Progress
  const [progressIndicatorIndex, setProgressIndicatorIndex] = useState(0);
  const [loadingText, setLoadingText] = useState("Loading...");
  // Preview & Deploy
  const [newServerConfig, setNewServerConfig] = useState(null);
  const [preventDeployment, setPreventDeployment] = useState(false);

  const basicConfigFormStartRef = useRef(null);
  const discoveryEnginesFormStartRef = useRef(null);
  const stewardshipEnginesFormStartRef = useRef(null);
  
  useEffect(() => {
    const fetchLists = async () => {
      const serverList = await fetchKnownServers();
      setKnownServers(serverList.map((v) => { return { id: v, serverName: v, status: "known" } }));
      const accessServices = await fetchAccessServices();
      setAvailableAccessServices(accessServices);
      setSelectedAccessServices(accessServices);
    }
    fetchLists();
  }, []);


  const fetchAccessServices = async () => {
    const fetchAccessServicesURL = `/open-metadata/platform-services/users/${userId}/server-platform/registered-services/access-services`;
    try {
      const fetchAccessServicesResponse = await axios.get(fetchAccessServicesURL, {
        params: {
          tenantId,
        },
        timeout: 30000,
      });
      console.debug({fetchAccessServicesResponse});
      if (fetchAccessServicesResponse.data.relatedHTTPCode == 200) {
        return fetchAccessServicesResponse.data.services;
      } else {
        throw new Error("Error in fetchAccessServicesResponse");
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        console.error("Error fetching access services from platform", { error });
      }
      throw error;
    }
  }

  const fetchKnownServers = async () => {
    console.log("called fetchKnownServers()");
    const fetchKnownServersURL = `/open-metadata/platform-services/users/${userId}/server-platform/servers`;
    try {
      const fetchKnownServersResponse = await axios.get(fetchKnownServersURL, {
        params: {
          tenantId,
        },
        timeout: 30000,
      });
      if (fetchKnownServersResponse.data.relatedHTTPCode == 200) {
        return fetchKnownServersResponse.data.serverList || [];
      } else {
        throw new Error(fetchKnownServersResponse.data.exceptionErrorMessage);
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        console.error("Error fetching known servers.", { error });
      }
      throw error;
    }
  }

  const fetchServerConfig = async () => {
    console.log("called fetchServerConfig");
    const fetchServerConfigURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/configuration`;
    try {
      const fetchServerConfigResponse = await axios.get(fetchServerConfigURL, {
        params: {
          tenantId,
        },
        timeout: 30000,
      });
      console.debug({fetchServerConfigResponse});
      if (fetchServerConfigResponse.data.relatedHTTPCode == 200) {
        return fetchServerConfigResponse.data.omagserverConfig;
      } else {
        throw new Error("Error in fetchServerConfigResponse");
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        console.error("Error fetching config from platform", { error });
      }
      throw error;
    }
  }

  const generateBasicServerConfig = () => {

    if (!newServerName || newServerName == "") {
      throw new Error(`Cannot create OMAG server configuration without Server Name`);
    }
  
    if (!newServerLocalURLRoot || newServerLocalURLRoot == "") {
      throw new Error(`Cannot create OMAG server configuration without Local Server URL Root`);
    }
  
    if (!newServerLocalServerType || newServerLocalServerType == "") {
      throw new Error(`Cannot create OMAG server configuration without Local Server Type`);
    }
  
    if (!newServerOrganizationName || newServerOrganizationName == "") {
      throw new Error(`Cannot create OMAG server configuration without Organization Name`);
    }
  
    if (!newServerLocalUserId || newServerLocalUserId == "") {
      throw new Error(`Cannot create OMAG server configuration without Local Server User ID`);
    }
  
    if (!newServerLocalPassword || newServerLocalPassword == "") {
      throw new Error(`Cannot create OMAG server configuration without Local Server Password`);
    }
  
    return {
      "class": "OMAGServerConfig",
      "versionId": "V2.0",
      "localServerName": newServerName,
      "localServerType": newServerLocalServerType,
      "organizationName": newServerOrganizationName,
      "localServerURL": newServerLocalURLRoot,
      "localServerUserId": newServerLocalUserId,
      "localServerPassword": newServerLocalPassword,
      "maxPageSize": newServerMaxPageSize,
    }
  
  }

  const registerCohort = async (cohortName) => {
    console.log("called registerCohort", { cohortName });
    const registerCohortURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/cohorts/${cohortName}`;
    try {
      const registerCohortResponse = await axios.post(registerCohortURL, {
        tenantId,
      }, {
        timeout: 30000,
      });
      if (registerCohortResponse.data.relatedHTTPCode != 200) {
        throw new Error("Error in registerCohortResponse");
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        console.error("Error registering OMAG Server with cohort", { error });
      }
      throw error;
    }
  }

  const configureAccessServices = async (serviceURLMarker) => {
    console.log("called configureAccessServices", { serviceURLMarker });
    let configureAccessServicesURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/access-services`;
    if (serviceURLMarker && serviceURLMarker != "") {
      configureAccessServicesURL += `/${serviceURLMarker}`;
    }
    try {
      const configureAccessServicesURLResponse = await axios.post(configureAccessServicesURL, {
        tenantId,
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
        timeout: 30000,
      });
      if (configureAccessServicesURLResponse.data.relatedHTTPCode != 200) {
        throw new Error(configureAccessServicesURLResponse.data.exceptionErrorMessage);
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        console.error("Error configuring access service(s).", error.message);
      }
      throw error;
    }
  }

  const configureArchiveFile = async (archiveName) => {
    console.log("called configureArchive", { archiveName });
    const configureArchiveURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/open-metadata-archives/file`;
    try {
      const configureArchiveResponse = await axios.post(configureArchiveURL, {
        tenantId,
        config: archiveName
      }, {
        timeout: 30000,
      });
      if (configureArchiveResponse.data.relatedHTTPCode != 200) {
        throw new Error("Error in configureArchiveResponse");
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        console.error("Error registering OMAG Server with cohort", { error });
      }
      throw error;
    }
  }

  const configureRepositoryProxyConnector = async (className) => {
    console.log("called configureRepositoryProxyConnector", { className });
    if (className != "") {
      const configureRepositoryProxyConnectorURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/local-repository/mode/repository-proxy/details?connectorProvider=${className}`;
      try {
        const configureRepositoryProxyConnectorURLResponse = await axios.post(configureRepositoryProxyConnectorURL, {
          tenantId,
        }, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 30000,
        });
        if (configureRepositoryProxyConnectorURLResponse.data.relatedHTTPCode != 200) {
          throw new Error(configureRepositoryProxyConnectorURLResponse.data.exceptionErrorMessage);
        }
      } catch(error) {
        if (error.code && error.code == 'ECONNABORTED') {
          console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
        } else {
          console.error("Error configuring repository proxy connector.", error.message);
        }
        throw error;
      }
    }
  }

  const configureRepositoryEventMapperConnector = async (className, eventSource) => {
    console.log("called configureRepositoryEventMapperConnector", { className });
    if (className != "" && eventSource != "") {
      const configureRepositoryEventMapperConnectorURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/local-repository/event-mapper-details?connectorProvider=${className}&eventSource=${eventSource}`;
      try {
        const configureRepositoryEventMapperConnectorURLResponse = await axios.post(configureRepositoryEventMapperConnectorURL, {
          tenantId,
        }, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 30000,
        });
        if (configureRepositoryEventMapperConnectorURLResponse.data.relatedHTTPCode != 200) {
          throw new Error(configureRepositoryProxyConnectorURLResponse.data.exceptionErrorMessage);
        }
      } catch(error) {
        if (error.code && error.code == 'ECONNABORTED') {
          console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
        } else {
          console.error("Error configuring repository event mapper connector.", error.message);
        }
        throw error;
      }
    }
  }

  const configureViewServices = async (remoteServerURLRoot, remoteServerName, serviceURLMarker) => {
    console.log("called configureViewServices", { serviceURLMarker });
    let configureViewServicesURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/view-services`;
    if (serviceURLMarker && serviceURLMarker != "") {
      configureViewServicesURL += `/${serviceURLMarker}`;
    }
    try {
      const configureViewServicesURLResponse = await axios.post(configureViewServicesURL, {
        tenantId,
        config: { 
          "class": "ViewServiceConfig",
          "omagserverPlatformRootURL": remoteServerURLRoot,
          "omagserverName": remoteServerName,
        }
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
        timeout: 30000,
      });
      if (configureViewServicesURLResponse.data.relatedHTTPCode != 200) {
        throw new Error(configureViewServicesURLResponse.data.exceptionErrorMessage);
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        console.error("Error configuring view services.", error.message);
      }
      throw error;
    }
  }

  const configureDiscoveryEngineClient = async (remoteServerURLRoot, remoteServerName) => {
    console.log("called configureDiscoveryEngineClient", { remoteServerURLRoot, remoteServerName });
    const configureDiscoveryEngineClientURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/discovery-server/client-config`;
    try {
      const configureDiscoveryEngineClientURLResponse = await axios.post(configureDiscoveryEngineClientURL, {
        tenantId,
        config: { 
          "class": "OMAGServerClientConfig",
          "omagserverPlatformRootURL": remoteServerURLRoot,
          "omagserverName": remoteServerName,
        }
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
        timeout: 30000,
      });
      if (configureDiscoveryEngineClientURLResponse.data.relatedHTTPCode != 200) {
        throw new Error(configureDiscoveryEngineClientURLResponse.data.exceptionErrorMessage);
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        console.error("Error configuring discovery engine client (metadata server).", error.message);
      }
      throw error;
    }
  }

  const configureDiscoveryEngines = async (engines) => {
    console.log("called configureDiscoveryEngines", { engines });
    const configureDiscoveryEnginesURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/discovery-server/set-discovery-engines`;
    try {
      const configureDiscoveryEnginesURLResponse = await axios.post(configureDiscoveryEnginesURL, {
        tenantId,
        config: engines,
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
        timeout: 30000,
      });
      if (configureDiscoveryEnginesURLResponse.data.relatedHTTPCode != 200) {
        throw new Error(configureDiscoveryEnginesURLResponse.data.exceptionErrorMessage);
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        console.error("Error configuring discovery engines.", error.message);
      }
      throw error;
    }
  }

  const configureStewardshipEngineClient = async (remoteServerURLRoot, remoteServerName) => {
    console.log("called configureStewardshipEngineClient", { remoteServerURLRoot, remoteServerName });
    const configureStewardshipEngineClientURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/stewardship-server/client-config`;
    try {
      const configureStewardshipEngineClientURLResponse = await axios.post(configureStewardshipEngineClientURL, {
        tenantId,
        config: { 
          "class": "OMAGServerClientConfig",
          "omagserverPlatformRootURL": remoteServerURLRoot,
          "omagserverName": remoteServerName,
        }
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
        timeout: 30000,
      });
      if (configureStewardshipEngineClientURLResponse.data.relatedHTTPCode != 200) {
        throw new Error(configureStewardshipEngineClientURLResponse.data.exceptionErrorMessage);
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        console.error("Error configuring stewardship engine client (metadata server).", error.message);
      }
      throw error;
    }
  }

  const configureStewardshipEngines = async (engines) => {
    console.log("called configureStewardshipEngines", { engines });
    const configureStewardshipEnginesURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/stewardship-server/set-stewardship-engines`;
    try {
      const configureStewardshipEnginesURLResponse = await axios.post(configureStewardshipEnginesURL, {
        tenantId,
        config: engines,
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
        timeout: 30000,
      });
      if (configureStewardshipEnginesURLResponse.data.relatedHTTPCode != 200) {
        throw new Error(configureStewardshipEnginesURLResponse.data.exceptionErrorMessage);
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        console.error("Error configuring stewardship engines.", error.message);
      }
      throw error;
    }
  }

  const serverConfigurationSteps = (serverType) => {

    const steps = [
      "Select server type",
      "Basic configuration",
      "Configure audit log destinations",
      "Preview configuration and deploy instance"
    ];

    switch(serverType) {
  
      case "Metadata Access Point":
      case "Metadata Server":
        steps.splice(2, 0, "Select access services");
        steps.splice(steps.length - 1, 0, "Register to a cohort");
        steps.splice(steps.length - 1, 0, "Configure the open metadata archives");
        break;
  
      case "Repository Proxy":
        steps.splice(steps.length - 1, 0, "Register to a cohort");
        steps.splice(steps.length - 1, 0, "Configure the open metadata archives");
        steps.splice(steps.length - 1, 0, "Configure the repository proxy connectors");
        break;
  
      case "Conformance Test Server":
        steps.splice(steps.length - 1, 0, "Register to a cohort");
        break;
  
      case "View Server":
        steps.splice(steps.length - 1, 0, "Configure the Open Metadata View Services (OMVS)");
        break;
  
      case "Discovery Server":
        steps.splice(steps.length - 1, 0, "Configure the discovery engine services");
        break;
  
      case "Security Sync Server":
        steps.splice(steps.length - 1, 0, "Configure the security sync services");
        break;
  
      case "Stewardship Server":
        steps.splice(steps.length - 1, 0, "Configure the stewardship engine services");
        break;
  
    }

    return steps;

  }

  const setServerAttribute = async (attrEndpoint, value) => {
    console.log("called setServerAttribute", { attrEndpoint, value });
    const setServerAttrURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/${attrEndpoint}=${value}`;
    try {
      const setServerAttrResponse = await axios.post(setServerAttrURL, {
        tenantId,
      }, {
        timeout: 30000,
      });
      if (setServerAttrResponse.data.relatedHTTPCode != 200) {
        throw new Error("Error in setServerAttrResponse");
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        console.error("Error configuring/updating property of OMAG Server", { error });
      }
      throw error;
    }
  }

  const setServerConfig = async (serverConfig) => {
    const setServerConfigURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/configuration`;
    try {
      const setServerConfigResponse = await axios.post(setServerConfigURL, {
        config: serverConfig,
        tenantId,
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
        timeout: 30000,
      });
      if (setServerConfigResponse.data.relatedHTTPCode != 200) {
        throw new Error("Error in setServerConfigResponse");
      }
    } catch(error) {
      if (error.code && error.code == 'ECONNABORTED') {
        console.error("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        console.error("Error updating configuration of OMAG Server", { error });
      }
      throw error;
    }
  }

  const showConfigForm = () => {
    document.getElementById("server-list-container").style.display = "none";
    document.getElementById("server-config-container").style.display = "flex";
  }

  const hideConfigForm = () => {
    document.getElementById("server-list-container").style.display = "flex";
    document.getElementById("server-config-container").style.display = "none";
    for (let el of document.querySelectorAll('.hideable')) el.style.display = 'none';
    document.getElementById("server-type-container").style.display = "block";
    setProgressIndicatorIndex(0);
  }


  return (
    <ServerAuthorContext.Provider
      value={{
        // States
        knownServers, setKnownServers,
        newServerName, setNewServerName,
        newServerLocalURLRoot, setNewServerLocalURLRoot,
        newServerLocalServerType, setNewServerLocalServerType,
        newServerOrganizationName, setNewServerOrganizationName,
        newServerLocalUserId, setNewServerLocalUserId,
        newServerLocalPassword, setNewServerLocalPassword,
        newServerSecurityConnector, setNewServerSecurityConnector,
        newServerRepository, setNewServerRepository,
        newServerMaxPageSize, setNewServerMaxPageSize,
        availableAccessServices, setAvailableAccessServices,
        selectedAccessServices, setSelectedAccessServices,
        newServerCohorts, setNewServerCohorts,
        newServerOMArchives, setNewServerOMArchives,
        newServerProxyConnector, setNewServerProxyConnector,
        newServerEventMapperConnector, setNewServerEventMapperConnector,
        newServerEventSource, setNewServerEventSource,
        availableViewServices, setAvailableViewServices,
        selectedViewServices, setSelectedViewServices,
        newServerViewServiceRemoteServerName, setNewServerViewServiceRemoteServerName,
        newServerViewServiceRemoteServerURLRoot, setNewServerViewServiceRemoteServerURLRoot,
        availableDiscoveryEngines, setAvailableDiscoveryEngines,
        selectedDiscoveryEngines, setSelectedDiscoveryEngines,
        newServerDiscoveryEngineRemoteServerName, setNewServerDiscoveryEngineRemoteServerName,
        newServerDiscoveryEngineRemoteServerURLRoot, setNewServerDiscoveryEngineRemoteServerURLRoot,
        availableStewardshipEngines, setAvailableStewardshipEngines,
        selectedStewardshipEngines, setSelectedStewardshipEngines,
        newServerStewardshipEngineRemoteServerName, setNewServerStewardshipEngineRemoteServerName,
        newServerStewardshipEngineRemoteServerURLRoot, setNewServerStewardshipEngineRemoteServerURLRoot,
        notificationType, setNotificationType,
        notificationTitle, setNotificationTitle,
        notificationSubtitle, setNotificationSubtitle,
        progressIndicatorIndex, setProgressIndicatorIndex,
        loadingText, setLoadingText,
        newServerConfig, setNewServerConfig,
        preventDeployment, setPreventDeployment,
        // Refs
        basicConfigFormStartRef,
        discoveryEnginesFormStartRef,
        stewardshipEnginesFormStartRef,
        // Functions
        fetchAccessServices,
        fetchKnownServers,
        fetchServerConfig,
        generateBasicServerConfig,
        configureAccessServices,
        registerCohort,
        configureArchiveFile,
        configureRepositoryProxyConnector,
        configureRepositoryEventMapperConnector,
        configureViewServices,
        configureDiscoveryEngineClient,
        configureDiscoveryEngines,
        configureStewardshipEngineClient,
        configureStewardshipEngines,
        serverConfigurationSteps,
        setServerAttribute,
        setServerConfig,
        showConfigForm,
        hideConfigForm,
      }}
    >
      {props.children}
    </ServerAuthorContext.Provider>
  );

};

ServerAuthorContextProvider.propTypes = {
  children: PropTypes.node
};

export default ServerAuthorContextProvider;
