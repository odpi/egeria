/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext } from "react";
import {
  CodeSnippet,
  Column,
  Grid,
  InlineNotification,
  Loading,
  Row,
  TileGroup,
  RadioTile,
} from "carbon-components-react";
import axios from "axios";

import { IdentificationContext } from "../../contexts/IdentificationContext";
import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";
import serverTypes from "./defaults/serverTypes";

import KnownServers from "./KnownServers";
import ConfigurationSteps from "./ConfigurationSteps";
import NavigationButtons from "./NavigationButtons";
import BasicConfig from "./BasicConfig";
import ConfigureAccessServices from "./ConfigureAccessServices";
import ConfigureAuditLog from "./ConfigureAuditLog";
import RegisterCohorts from "./RegisterCohorts";
import ConfigureOMArchives from "./ConfigureOMArchives";
import ConfigureRepositoryProxyConnectors from "./ConfigureRepositoryProxyConnectors";
import ConfigureViewServices from "./ConfigureViewServices";
import ConfigureDiscoveryEngines from "./ConfigureDiscoveryEngines";
import ConfigureStewardshipEngines from "./ConfigureStewardshipEngines";
import ConfigPreview from "./ConfigPreview";

export default function ServerAuthor() {

  const { userId, serverName: tenantId } = useContext(IdentificationContext);
  const {
    newServerName,
    newServerLocalServerType, setNewServerLocalServerType,
    newServerSecurityConnector,
    availableAccessServices,
    selectedAccessServices,
    newServerRepository,
    newServerCohorts,
    newServerOMArchives,
    newServerProxyConnector,
    newServerEventMapperConnector,
    newServerEventSource,
    availableViewServices,
    selectedViewServices,
    newServerViewServiceRemoteServerURLRoot,
    newServerViewServiceRemoteServerName,
    selectedDiscoveryEngines,
    newServerDiscoveryEngineRemoteServerName,
    newServerDiscoveryEngineRemoteServerURLRoot,
    selectedStewardshipEngines,
    newServerStewardshipEngineRemoteServerName,
    newServerStewardshipEngineRemoteServerURLRoot,
    notificationType, setNotificationType,
    notificationTitle, setNotificationTitle,
    notificationSubtitle, setNotificationSubtitle,
    progressIndicatorIndex, setProgressIndicatorIndex,
    loadingText, setLoadingText,
    setNewServerConfig,
    basicConfigFormStartRef,
    discoveryEnginesFormStartRef,
    stewardshipEnginesFormStartRef,
    fetchServerConfig,
    generateBasicServerConfig,
    registerCohort,
    configureAccessServices,
    configureArchiveFile,
    configureRepositoryProxyConnector,
    configureRepositoryEventMapperConnector,
    configureViewServices,
    configureDiscoveryEngineClient,
    configureDiscoveryEngines,
    configureStewardshipEngineClient,
    configureStewardshipEngines,
    serverConfigurationSteps,
  } = useContext(ServerAuthorContext);

  // Navigation

  const sectionMapping = {
    ["Select server type"]: "server-type-container",
    ["Basic configuration"]: "config-basic-container",
    ["Configure audit log destinations"]: "audit-log-container",
    ["Preview configuration and deploy instance"]: "config-preview-container",
    ["Select access services"]: "access-services-container",
    ["Register to a cohort"]: "cohort-container",
    ["Configure the open metadata archives"]: "archives-container",
    ["Configure the repository proxy connectors"]: "repository-proxy-container",
    ["Configure the Open Metadata View Services (OMVS)"]: "view-services-container",
    ["Configure the discovery engine services"]: "discovery-engines-container",
    ["Configure the security sync services"]: "security-sync-container",
    ["Configure the stewardship engine services"]: "stewardship-engines-container",
  }

  const showPreviousStep = () => {
    const steps = serverConfigurationSteps(newServerLocalServerType);
    if (progressIndicatorIndex == 0) {
      return null;
    }
    const previous = steps[progressIndicatorIndex - 1];
    for (let el of document.querySelectorAll('.hideable')) el.style.display = 'none';
    document.getElementById(sectionMapping[previous]).style.display = "block";
  }

  const showNextStep = () => {
    const steps = serverConfigurationSteps(newServerLocalServerType);
    if (progressIndicatorIndex == steps.length) {
      return null;
    }
    const next = steps[progressIndicatorIndex + 1];
    for (let el of document.querySelectorAll('.hideable')) el.style.display = 'none';
    document.getElementById(sectionMapping[next]).style.display = "block";
    switch (next) {
      case "Basic configuration":
        basicConfigFormStartRef.current.focus();
      case "Configure the discovery engine services":
        discoveryEnginesFormStartRef.current.focus();
      case "Configure the stewardship engine services":
        stewardshipEnginesFormStartRef.current.focus();
    }
  }

  const handleBackToPreviousStep = e => {
    e.preventDefault();
    showPreviousStep();
    setProgressIndicatorIndex(progressIndicatorIndex - 1);
  }

  // Server Type

  const handleServerTypeSelection = async e => {
    e.preventDefault();
    showNextStep();
    setProgressIndicatorIndex(progressIndicatorIndex + 1);
  }

  // Basic Config

  const handleBasicConfig = async e => {
    e.preventDefault();
    // Generate server config
    setLoadingText("Generating server configuration...");
    document.getElementById("config-basic-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    let serverConfig;
    try {
      serverConfig = generateBasicServerConfig();
    } catch(error) {
      console.error("Error generating server config", { error });
      setNotificationType("error");
      setNotificationTitle("Configuration Error");
      setNotificationSubtitle("Error generating OMAG server configuration file. " + error.message);
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-basic-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Post server config
    setLoadingText("Storing basic server configuration on OMAG server platform...");
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
      if (setServerConfigResponse.data.relatedHTTPCode == 200) {
        setNewServerConfig(serverConfig);
        console.log("Finished updating config preview");
      } else {
        console.error(setServerConfigResponse.data);
        throw new Error("Error in setServerConfigResponse");
      }
    } catch(error) {
      console.error("Error sending config to platform", { error });
      setNewServerConfig(null);
      setNotificationType("error");
      if (error.code && error.code == 'ECONNABORTED') {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle("Error sending server configuration to the platform.");
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-basic-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Enable chosen repository
    if (newServerLocalServerType == "Metadata Server") {
      setLoadingText("Enabling chosen local repository...");
      const enableRepositoryURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/local-repository/mode/${newServerRepository}`;
      try {
        const enableRepositoryURLResponse = await axios.post(enableRepositoryURL, {
          config: '',
          tenantId,
        }, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 30000,
        });
        if (enableRepositoryURLResponse.data.relatedHTTPCode != 200) {
          console.error(enableRepositoryURLResponse.data);
          throw new Error("Error in enableRepositoryURLResponse");
        }
      } catch(error) {
        console.error("Error enabling chosen repository", { error });
        setNotificationType("error");
        if (error.code && error.code == 'ECONNABORTED') {
          setNotificationTitle("Connection Error");
          setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
        } else {
          setNotificationTitle("Configuration Error");
          setNotificationSubtitle(`Error enabling ${newServerRepository} repository for the server.`);
        }
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("config-basic-container").style.display = "block";
        document.getElementById("notification-container").style.display = "block";
        return;
      }
    }
    // Configure event bus
    setLoadingText("Configuring event bus...");
    const configureEventBusURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/event-bus?topicURLRoot=egeriaTopics`;
    try {
      const configureEventBusURLResponse = await axios.post(configureEventBusURL, {
        config: '',
        tenantId,
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
        timeout: 30000,
      });
      if (configureEventBusURLResponse.data.relatedHTTPCode != 200) {
        console.error(configureEventBusURLResponse.data);
        throw new Error("Error in configureEventBusURLResponse");
      }
    } catch(error) {
      console.error("error configuring event bus", { error });
      setNotificationType("error");
      if (error.code && error.code == 'ECONNABORTED') {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error configuring event bus.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-basic-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Configure security connector
    if (newServerSecurityConnector != "") {
      setLoadingText("Configuring security connector...");
      const configureSecurityConnectorURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/security/connection`;
      try {
        const configureSecurityConnectorURLResponse = await axios.post(configureSecurityConnectorURL, {
          config: {
            "class": "Connection",
            "connectorType": {
                "class": "ConnectorType",
                "connectorProviderClassName": newServerSecurityConnector
            }
          },
          tenantId,
        }, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 30000,
        });
        if (configureSecurityConnectorURLResponse.data.relatedHTTPCode != 200) {
          console.error(configureSecurityConnectorURLResponse.data);
          throw new Error("Error in configureSecurityConnectorURLResponse");
        }
      } catch(error) {
        console.error("error configuring security connector", { error });
        setNotificationType("error");
        if (error.code && error.code == 'ECONNABORTED') {
          setNotificationTitle("Connection Error");
          setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
        } else {
          setNotificationTitle("Configuration Error");
          setNotificationSubtitle(`Error configuring security connector. Please ensure the fully qualified name is correct.`);
        }
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("config-basic-container").style.display = "block";
        document.getElementById("notification-container").style.display = "block";
        return;
      }
    }
    // Direct User to Next Step
    showNextStep();
    setProgressIndicatorIndex(progressIndicatorIndex + 1);
  }

  // Access Services (optional)

  const handleAccessServicesConfig = async () => {
    setLoadingText("Enabling access services...");
    document.getElementById("access-services-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Enable Access Services
    try {
      if (selectedAccessServices.length == availableAccessServices.length) {
        configureAccessServices();
      } else {
        for (const service of selectedAccessServices) {
          setLoadingText(`Enabling ${service} access service...`);
          configureAccessServices(service);
        }
      }
    } catch(error) {
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error enabling the access service(s).`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("access-services-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Fetch Server Config
    setLoadingText("Fetching final stored server configuration...");
    try {
      const serverConfig = await fetchServerConfig();
      setNewServerConfig(serverConfig);
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
    } catch(error) {
      console.error("error fetching server config", {error});
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error fetching configuration for the server.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("access-services-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  // Audit Log Destionations

  const configureAuditLogDestinations = async (auditLogDestinations) => {
    setLoadingText("Configuring audit log destinations...");
    document.getElementById("audit-log-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Enable Audit Log Destinations
    console.log({auditLogDestinations});
    const chosenAuditLogDestinations = auditLogDestinations.filter((destination) => destination.selected);
    for (const destination of chosenAuditLogDestinations) {
      try {
        const url = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/audit-log-destinations/${destination.id}`;
        const data = {
          tenantId
        };
        if (destination.id != "default") {
          data.config = destination.severities.map((s) => s.id);
        }
        setLoadingText(`Enabling the ${destination.label} audit log destination...`);
        const enableAuditLogDestinationResponse = await axios.post(url, data, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 30000,
        });
        if (enableAuditLogDestinationResponse.data.relatedHTTPCode != 200) {
          console.error(enableAuditLogDestinationResponse.data);
          throw new Error("Error in enableAuditLogDestinationResponse");
        }
      } catch(error) {
        console.error(`Error enabling the ${destination.label} audit log destination`, { error });
        setNotificationType("error");
        if (error.code && error.code == "ECONNABORTED") {
          setNotificationTitle("Connection Error");
          setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
        } else {
          setNotificationTitle("Configuration Error");
          setNotificationSubtitle(`Error enabling the ${destination.label} audit log destination`);
        }
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("audit-log-container").style.display = "block";
        document.getElementById("notification-container").style.display = "block";
        return;
      }
    }
    // Fetch Server Config
    setLoadingText("Fetching final stored server configuration...");
    try {
      const serverConfig = await fetchServerConfig();
      setNewServerConfig(serverConfig);
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
    } catch(error) {
      console.error("error fetching server config", {error});
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error fetching configuration for the server.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("audit-log-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  // Optional Steps

  // Register to a cohort

  const handleRegisterCohorts = async () => {
    setLoadingText("Registering cohort(s)...");
    document.getElementById("cohort-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Register Cohorts
    for (const cohortName of newServerCohorts) {
      try {
        setLoadingText(`Registering the OMAG Server to the ${cohortName} cohort...`);
        await registerCohort(cohortName);
      } catch(error) {
        console.error(`Error registering the OMAG Server to the ${cohortName} cohort`, { error });
        setNotificationType("error");
        if (error.code && error.code == "ECONNABORTED") {
          setNotificationTitle("Connection Error");
          setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
        } else {
          setNotificationTitle("Configuration Error");
          setNotificationSubtitle(`Error registering the OMAG Server to the ${cohortName} cohort`);
        }
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("cohort-container").style.display = "block";
        document.getElementById("notification-container").style.display = "block";
        return;
      }
    }
    // Fetch Server Config
    setLoadingText("Fetching final stored server configuration...");
    try {
      const serverConfig = await fetchServerConfig();
      setNewServerConfig(serverConfig);
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
    } catch(error) {
      console.error("error fetching server config", {error});
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error fetching configuration for the server.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("cohort-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  // Configure open metadata archives to load on server startup

  const handleConfigureArchives = async () => {
    setLoadingText("Configuring archives(s) to load on server startup...");
    document.getElementById("archives-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Register Cohorts
    for (const archiveName of newServerOMArchives) {
      try {
        setLoadingText(`Configuring the OMAG Server to load the ${archiveName} archive upon startup...`);
        await configureArchiveFile(archiveName); // TODO
      } catch(error) {
        console.error(`Error configuring the OMAG Server to load the ${archiveName} archive upon startup`, { error });
        setNotificationType("error");
        if (error.code && error.code == "ECONNABORTED") {
          setNotificationTitle("Connection Error");
          setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
        } else {
          setNotificationTitle("Configuration Error");
          setNotificationSubtitle(`Error configuring the OMAG Server to load the ${archiveName} archive upon startup.`);
        }
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("archives-container").style.display = "block";
        document.getElementById("notification-container").style.display = "block";
        return;
      }
    }
    // Fetch Server Config
    setLoadingText("Fetching final stored server configuration...");
    try {
      const serverConfig = await fetchServerConfig();
      setNewServerConfig(serverConfig);
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
    } catch(error) {
      console.error("error fetching server config", {error});
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error fetching configuration for the server.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("archives-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  // Configure the Repository Proxy Connectors

  const handleConfigureRepositoryProxyConnectors = async () => {
    // If all three fields are blank, skip to next step
    if (
      (!newServerProxyConnector || newServerProxyConnector == "") &&
      (!newServerEventMapperConnector || newServerEventMapperConnector == "") &&
      (!newServerEventSource || newServerEventSource == "")
    ) {
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
      return;
    }
    // If one or two fields are blank, show notification
    if (
      (!newServerProxyConnector || newServerProxyConnector == "") ||
      (!newServerEventMapperConnector || newServerEventMapperConnector == "") ||
      (!newServerEventSource || newServerEventSource == "")
    ) {
      setNotificationType("error");
      setNotificationTitle("Input Error");
      setNotificationSubtitle(`All three fields are required to configure the repository proxy connector. Leave all three fields blank to skip this step.`);
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    setLoadingText("Configuring repository proxy connector...");
    document.getElementById("repository-proxy-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Configure the repository proxy connector
    try {
      await configureRepositoryProxyConnector(newServerProxyConnector);
    } catch(error) {
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error configuring the repository proxy connector. Please ensure the fully qualified repository proxy connectory name is correct.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("repository-proxy-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Configure the repository event mapper connector
    setLoadingText("Configuring repository event mapper connector...");
    try {
      await configureRepositoryEventMapperConnector(newServerEventMapperConnector, newServerEventSource);
    } catch(error) {
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error configuring the repository event mapper connector. Please ensure the fully qualified repository event mapper connector name and event source are correct.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("repository-proxy-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Fetch Server Config
    setLoadingText("Fetching final stored server configuration...");
    try {
      const serverConfig = await fetchServerConfig();
      setNewServerConfig(serverConfig);
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
    } catch(error) {
      console.error("error fetching server config", {error});
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error fetching configuration for the server.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("repository-proxy-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  // Configure the open metadata view services

  const handleConfigureViewServices = async () => {
    // If all three fields are blank, skip to next step
    if (
      (!newServerViewServiceRemoteServerURLRoot || newServerViewServiceRemoteServerURLRoot == "") &&
      (!newServerViewServiceRemoteServerName || newServerViewServiceRemoteServerName == "") &&
      (!selectedViewServices || !selectedViewServices.length)
    ) {
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
      return;
    }
    // If one or two fields are blank, show notification
    if (
      (!newServerViewServiceRemoteServerURLRoot || newServerViewServiceRemoteServerURLRoot == "") ||
      (!newServerViewServiceRemoteServerName || newServerViewServiceRemoteServerName == "") ||
      (!selectedViewServices || !selectedViewServices.length)
    ) {
      setNotificationType("error");
      setNotificationTitle("Input Error");
      setNotificationSubtitle(`All three fields are required to configure the repository proxy connector. Leave all three fields blank to skip this step.`);
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    setLoadingText("Enabling view services...");
    document.getElementById("view-services-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Enable View Services
    try {
      if (selectedViewServices.length == availableViewServices.length) {
        configureViewServices(newServerViewServiceRemoteServerURLRoot, newServerViewServiceRemoteServerName);
      } else {
        for (const service of selectedViewServices) {
          setLoadingText(`Enabling ${service} view service...`);
          configureViewServices(newServerViewServiceRemoteServerURLRoot, newServerViewServiceRemoteServerName, service);
        }
      }
    } catch(error) {
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error enabling the view service(s).`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("view-services-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Fetch Server Config
    setLoadingText("Fetching final stored server configuration...");
    try {
      const serverConfig = await fetchServerConfig();
      setNewServerConfig(serverConfig);
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
    } catch(error) {
      console.error("error fetching server config", {error});
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error fetching configuration for the server.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("view-services-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  // Configure the Discovery Engines

  const handleConfigureDiscoveryEngines = async () => {
    // If all three fields are blank, skip to next step
    if (
      (!newServerDiscoveryEngineRemoteServerURLRoot || newServerDiscoveryEngineRemoteServerURLRoot == "") &&
      (!newServerDiscoveryEngineRemoteServerName || newServerDiscoveryEngineRemoteServerName == "") &&
      (!selectedDiscoveryEngines || !selectedDiscoveryEngines.length)
    ) {
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
      return;
    }
    // If one or two fields are blank, show notification
    if (
      (!newServerDiscoveryEngineRemoteServerURLRoot || newServerDiscoveryEngineRemoteServerURLRoot == "") ||
      (!newServerDiscoveryEngineRemoteServerName || newServerDiscoveryEngineRemoteServerName == "") ||
      (!selectedDiscoveryEngines || !selectedDiscoveryEngines.length)
    ) {
      setNotificationType("error");
      setNotificationTitle("Input Error");
      setNotificationSubtitle(`All three fields are required to configure the discovery engine. Leave all three fields blank to skip this step.`);
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    setLoadingText("Configuring discovery engine client...");
    document.getElementById("discovery-engines-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Configure the discovery engines client
    try {
      await configureDiscoveryEngineClient(newServerDiscoveryEngineRemoteServerURLRoot, newServerDiscoveryEngineRemoteServerName);
    } catch(error) {
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error configuring the discovery engine client. Please ensure the metadata server root URL and name are correct.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("discovery-engines-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Configure the discovery engines
    setLoadingText("Configuring discovery engines...");
    try {
      await configureDiscoveryEngines(selectedDiscoveryEngines);
    } catch(error) {
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error configuring the discovery engines. ${error.message}`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("discovery-engines-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Fetch Server Config
    setLoadingText("Fetching final stored server configuration...");
    try {
      const serverConfig = await fetchServerConfig();
      setNewServerConfig(serverConfig);
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
    } catch(error) {
      console.error("error fetching server config", {error});
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error fetching configuration for the server.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("discovery-engines-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  // Configure the Stewardship Engines

  const handleConfigureStewardshipEngines = async () => {
    console.log({
      newServerStewardshipEngineRemoteServerURLRoot,
      newServerStewardshipEngineRemoteServerName,
      selectedStewardshipEngines
    });
    // If all three fields are blank, skip to next step
    if (
      (!newServerStewardshipEngineRemoteServerURLRoot || newServerStewardshipEngineRemoteServerURLRoot == "") &&
      (!newServerStewardshipEngineRemoteServerName || newServerStewardshipEngineRemoteServerName == "") &&
      (!selectedStewardshipEngines || !selectedStewardshipEngines.length)
    ) {
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
      return;
    }
    // If one or two fields are blank, show notification
    if (
      (!newServerStewardshipEngineRemoteServerURLRoot || newServerStewardshipEngineRemoteServerURLRoot == "") ||
      (!newServerStewardshipEngineRemoteServerName || newServerStewardshipEngineRemoteServerName == "") ||
      (!selectedStewardshipEngines || !selectedStewardshipEngines.length)
    ) {
      setNotificationType("error");
      setNotificationTitle("Input Error");
      setNotificationSubtitle(`All three fields are required to configure the stewardship engine. Leave all three fields blank to skip this step.`);
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    setLoadingText("Configuring stewardship engine client...");
    document.getElementById("stewardship-engines-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Configure the stewardship engines client
    try {
      await configureStewardshipEngineClient(newServerStewardshipEngineRemoteServerURLRoot, newServerStewardshipEngineRemoteServerName);
    } catch(error) {
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error configuring the stewardship engine client. Please ensure the metadata server root URL and name are correct.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("stewardship-engines-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Configure the stewardship engines
    setLoadingText("Configuring stewardship engines...");
    try {
      await configureStewardshipEngines(selectedStewardshipEngines);
    } catch(error) {
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error configuring the stewardship engines. ${error.message}`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("stewardship-engines-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Fetch Server Config
    setLoadingText("Fetching final stored server configuration...");
    try {
      const serverConfig = await fetchServerConfig();
      setNewServerConfig(serverConfig);
      showNextStep();
      setProgressIndicatorIndex(progressIndicatorIndex + 1);
    } catch(error) {
      console.error("error fetching server config", {error});
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error fetching configuration for the server.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("stewardship-engines-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  // Config Preview

  const handleDeployConfig = async e => {
    e.preventDefault();
    setLoadingText("Deploying OMAG server from stored configuration...");
    document.getElementById("config-preview-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Issue the instance call to start the new server
    const startServerURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/instance`;
    try{
      const startServerResponse = await axios.post(startServerURL, {
        config: '',
        tenantId,
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
        timeout: 30000
      });
      if (startServerResponse.data.relatedHTTPCode == 200) {
        setNotificationType("success");
        setNotificationTitle("Success!")
        setNotificationSubtitle(`Server instance deployed from configuration.`);
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("notification-container").style.display = "block";
        document.getElementById("server-list-container").style.display = "flex";
      } else {
        console.error(startServerResponse.data);
        throw new Error("Error in startServerResponse");
      }
    } catch(error) {
      console.error("Error starting server from stored config", { error });
      setNotificationType("error");
      if (error.code && error.code == "ECONNABORTED") {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
      } else {
        setNotificationTitle("Deployment Error");
        setNotificationSubtitle(`Error starting server from stored configuration file.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-preview-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  const serverTypeTiles = serverTypes.map((serverType, i) => {
    return (
      <RadioTile
        id={serverType.id}
        key={`server-type-${i}`}
        light={false}
        name={`server-serverType-${i}`}
        tabIndex={i}
        value={serverType.label}
      >
        {serverType.label}
      </RadioTile>
    )
  });

  return (

    <Grid>

      <Row id="server-list-container">

        <Column
          id="server-list"
          sm={{ span: 8 }}
          md={{ span: 8 }}
          lg={{ span: 16 }}
        >

          <h1>Known OMAG Servers</h1>
          <p style={{marginBottom: "24px"}}>Logged in as <CodeSnippet type="inline" >{userId}</CodeSnippet></p>

          <KnownServers />

        </Column>

      </Row>

      <Row id="server-config-container" style={{ display: "none" }}>

        {/* Form Column */}

        <Column
          id="server-config-forms"
          sm={{ span: 4 }}
          md={{ span: 6 }}
          lg={{ span: 11, offset: 1 }}
        >
          
          <h1>Create New OMAG Server</h1>
          <p style={{marginBottom: "24px"}}>Logged in as <CodeSnippet type="inline" >{userId}</CodeSnippet></p>

          <div id="notification-container" className="hideable" style={{display: "none"}}>
            <InlineNotification
              kind={notificationType}
              title={notificationTitle}
              subtitle={notificationSubtitle}
              hideCloseButton={true}
              timeout={10}
            />
          </div>
        
          <div id="server-type-container" className="hideable">
            <h4 style={{textAlign: "left", marginBottom: "32px"}}>Select Server Type</h4>
            <TileGroup
              defaultSelected={serverTypes[0].label}
              name="server-types"
              valueSelected=""
              onChange={value => setNewServerLocalServerType(value)}
            >
              {serverTypeTiles}
            </TileGroup>
            <NavigationButtons handleNextStep={handleServerTypeSelection} />
          </div>

          <div id="config-basic-container" className="hideable" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "16px"}}>Basic Configuration</h4>
            <BasicConfig />
            <NavigationButtons
              handlePreviousStep={handleBackToPreviousStep}
              handleNextStep={handleBasicConfig}
            />
          </div>
          
          <div id="access-services-container" className="hideable" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Select Access Services</h4>
            <ConfigureAccessServices />
            <NavigationButtons
              handlePreviousStep={handleBackToPreviousStep}
              handleNextStep={handleAccessServicesConfig}
            />
          </div>
        
          <div id="audit-log-container" className="hideable" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Configure Audit Log Destinations</h4>
            <ConfigureAuditLog
              nextAction={(destinations) => configureAuditLogDestinations(destinations)}
              previousAction={handleBackToPreviousStep}
            />
          </div>

          <div id="cohort-container" className="hideable" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Register to the following cohort(s):</h4>
            <RegisterCohorts />
            <NavigationButtons
              handlePreviousStep={handleBackToPreviousStep}
              handleNextStep={handleRegisterCohorts}
            />
          </div>

          <div id="archives-container" className="hideable" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Configure the Open Metadata Archives that are loaded on server startup</h4>
            <ConfigureOMArchives />
            <NavigationButtons
              handlePreviousStep={handleBackToPreviousStep}
              handleNextStep={handleConfigureArchives}
            />
          </div>

          <div id="repository-proxy-container" className="hideable" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Configure the Repository Proxy Connectors</h4>
            <ConfigureRepositoryProxyConnectors />
            <NavigationButtons
              handlePreviousStep={handleBackToPreviousStep}
              handleNextStep={handleConfigureRepositoryProxyConnectors}
            />
          </div>

          <div id="view-services-container" className="hideable" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Configure the Open Metadata View Services (OMVSs)</h4>
            <ConfigureViewServices />
            <NavigationButtons
              handlePreviousStep={handleBackToPreviousStep}
              handleNextStep={handleConfigureViewServices}
            />
          </div>

          <div id="discovery-engines-container" className="hideable" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Configure the discovery engine services</h4>
            <ConfigureDiscoveryEngines />
            <NavigationButtons
              handlePreviousStep={handleBackToPreviousStep}
              handleNextStep={handleConfigureDiscoveryEngines}
            />
          </div>

          <div id="security-sync-container" className="hideable" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Configure the security sync services</h4>
            <p>Coming Soon!</p>
            <NavigationButtons
              handlePreviousStep={handleBackToPreviousStep}
              handleNextStep={showNextStep}
            />
          </div>

          <div id="stewardship-engines-container" className="hideable" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Configure the stewardship engine services</h4>
            <ConfigureStewardshipEngines />
            <NavigationButtons
              handlePreviousStep={handleBackToPreviousStep}
              handleNextStep={handleConfigureStewardshipEngines}
            />
          </div>

          <div id="config-preview-container" className="hideable" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "4px", marginLeft: "1rem"}}>Preview Configuration</h4>
            <ConfigPreview options={{ editable: true }} />
            <NavigationButtons
              handlePreviousStep={handleBackToPreviousStep}
              handleNextStep={handleDeployConfig}
            />
          </div>
        
          <div id="loading-container" className="hideable" style={{display: "none"}}>
            <Loading
              description="Active loading indicator"
              withOverlay={false}
              style={{margin: 'auto'}}
            />
            <p>{loadingText}</p>
          </div>

        </Column>

        {/* Progress Indicator Column */}
        
        <Column
          id="config-progress-container"
          sm={{ span: 4 }}
          md={{ span: 2 }}
          lg={{ span: 4 }}
        >
          <ConfigurationSteps />
        </Column>

      </Row>

    </Grid>

  )

}