/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext, useEffect, useRef, useState } from "react";
import {
  // Accordion,
  // AccordionItem,
  Button,
  // Checkbox,
  CodeSnippet,
  Column,
  // DataTable, 
  Form,
  FormGroup,
  Grid,
  InlineNotification,
  Loading,
  // ProgressIndicator,
  // ProgressStep,
  Row,
  TextInput,
  TileGroup,
  RadioTile,
  SelectableTile,
  // StructuredListBody,
  // StructuredListCell,
  // StructuredListHead,
  // StructuredListRow,
  // StructuredListWrapper,
} from "carbon-components-react";
// import JSONPretty from 'react-json-pretty';
import axios from "axios";

import { IdentificationContext } from "../../contexts/IdentificationContext";
import generateBasicServerConfig from "./generateBasicServerConfig";
import accessServices from "./accessServices";
import serverTypes from "./serverTypes";
import ConfigureAuditLog from "./ConfigureAuditLog";
import ConfigPreview from "./ConfigPreview";
import ConfigurationSteps from "./ConfigurationSteps";

export default function ServerAuthor() {

  const { userId, serverName: tenantId, user } = useContext(IdentificationContext);
  const newServerOrganizationName = user.organizationName;
  const [newServerName, setNewServerName] = useState("");
  const [newServerLocalURLRoot, setNewServerLocalURLRoot] = useState("https://localhost:9443");
  const [newServerLocalServerType, setNewServerLocalServerType] = useState(serverTypes[0].label);
  // const [newServerOrganizationName, setNewServerOrganizationName] = useState("");
  const [newServerLocalUserId, setNewServerLocalUserId] = useState("");
  const [newServerLocalPassword, setNewServerLocalPassword] = useState("");
  const [newServerSecurityConnector, setNewServerSecurityConnector] = useState("");
  const [availableAccessServices, setAvailableAccessServices] = useState(accessServices);
  const [availableAccessServicesTiles, setAvailableAccessServicesTiles] = useState(null);
  const [newServerAccessServices, setNewServerAccessServices] = useState(accessServices.map((s) => s.serviceURLMarker));
  const [newServerRepository, setNewServerRepository] = useState("in-memory-repository");
  const [newServerMaxPageSize, setNewServerMaxPageSize] = useState(1000);
  const [notificationType, setNotificationType] = useState("");
  const [notificationTitle, setNotificationTitle] = useState("");
  const [notificationSubtitle, setNotificationSubtitle] = useState("");
  const [progressIndicatorIndex, setProgressIndicatorIndex] = useState(0);
  const [loadingText, setLoadingText] = useState("Loading...");
  const [newServerConfig, setNewServerConfig] = useState(null);

  const formStartRef = useRef(null);

  useEffect(() => {
    formStartRef.current.focus();
  }, []);

  const preventDeployment = () => {
    document.getElementById("deploy-button").setAttribute("disabled", true);
    // document.getElementById("deploy-button").setAttribute("kind", "tertiary");
    // document.getElementById("deploy-button").innerHTML = "Update configuration";
    // document.getElementById("deploy-button").setAttribute("onClick", "() => updateConfig()");
  }

  const allowDeployment = () => {
    document.getElementById("deploy-button").removeAttribute("disabled");
    // document.getElementById("deploy-button").setAttribute("kind", "primary");
    // document.getElementById("deploy-button").innerHTML = "Deploy instance";
    // document.getElementById("deploy-button").setAttribute("onClick", "() => handleDeployConfig()");
  }

  const setServerAttribute = async (attrEndpoint, value) => {
    // setLoadingText("Updating server configuration on OMAG server platform...");
    console.log("called setServerAttribute", { attrEndpoint, value });
    const setServerAttrURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/${attrEndpoint}="${value}"`;
    try {
      const setServerAttrResponse = await axios.post(setServerAttrURL, {
        tenantId,
      }, {
        timeout: 30000,
      });
      if (setServerAttrResponse.data.relatedHTTPCode != 200) {
        console.error(setServerAttrResponse.data);
        throw new Error("Error in setServerAttrResponse");
      }
    } catch(error) {
      // Note: this error doesn't prevent user from trying again
      // TODO: Determine what to do next when an edit fails
      console.error("Error configuring/updating property of OMAG Server", { error });
      setNotificationType("error");
      if (error.code && error.code == 'ECONNABORTED') {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle("Error updating server configuration.");
      }
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Fetch Server Config
    // setLoadingText("Fetching final stored server configuration...");
    try {
      const serverConfig = await fetchServerConfig();
      console.log({serverConfig});
      setNewServerConfig(<ConfigPreview config={serverConfig} options={{ accessServices: availableAccessServices, editable: true, allowDeployment, preventDeployment, setServerAttribute }} />);
      // document.getElementById("loading-container").style.display = "none";
      // document.getElementById("config-preview-container").style.display = "block";
      setProgressIndicatorIndex(3);
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
      // document.getElementById("loading-container").style.display = "none";
      // document.getElementById("access-services-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  const fetchServerConfig = async () => {
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

  // Step 1: Server Type Form

  const handleServerTypeSelection = async e => {
    e.preventDefault();
    document.getElementById("server-type-container").style.display = "none";
    document.getElementById("config-basic-container").style.display = "block";
    setProgressIndicatorIndex(progressIndicatorIndex + 1);
  }

  // Step 2: Basic Config Form

  const handleBackToServerTypeSelection = e => {
    e.preventDefault();
    document.getElementById("config-basic-container").style.display = "none";
    document.getElementById("server-type-container").style.display = "block";
    setProgressIndicatorIndex(progressIndicatorIndex - 1);
  }

  const handleBasicConfig = async e => {
    e.preventDefault();
    // Generate server config
    setLoadingText("Generating server configuration...");
    document.getElementById("config-basic-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    const configOptions = {
      userId,
      newServerName,
      newServerLocalURLRoot,
      newServerLocalServerType,
      newServerOrganizationName,
      newServerLocalUserId,
      newServerLocalPassword,
      newServerMaxPageSize,
    }
    let serverConfig;
    try {
      serverConfig = generateBasicServerConfig(configOptions);
    } catch(error) {
      console.error("Error generating server config", { error });
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
        console.log({ setServerConfigResponse });
        setNewServerConfig(<ConfigPreview config={serverConfig} options={{ accessServices: availableAccessServices, editable: true, allowDeployment, preventDeployment, setServerAttribute }} />);
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
    // Fetch Access Services
    setLoadingText("Fetching access services...");
    try {
      const accessServices = await fetchAccessServices();
      setAvailableAccessServices(accessServices);
      setAvailableAccessServicesTiles(buildAccessServiceTiles(accessServices));
      setNewServerAccessServices(accessServices.map((s) => s.serviceURLMarker));
    } catch(error) {
      console.error("error fetching access services", { error });
      setNotificationType("error");
      if (error.code && error.code == 'ECONNABORTED') {
        setNotificationTitle("Connection Error");
        setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");  
      } else {
        setNotificationTitle("Configuration Error");
        setNotificationSubtitle(`Error fetching access services.`);
      }
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-basic-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Direct User to Next Step
    setProgressIndicatorIndex(progressIndicatorIndex + 1);
    document.getElementById("loading-container").style.display = "none";
    if (newServerLocalServerType == "Metadata Server") {
      document.getElementById("access-services-container").style.display = "block";
    } else {
      document.getElementById("audit-log-container").style.display = "block";
    }
  }

  // Step 3

  const handleBackToBasicConfig = e => {
    e.preventDefault();
    document.getElementById("access-services-container").style.display = "none";
    document.getElementById("config-basic-container").style.display = "block";
    setProgressIndicatorIndex(progressIndicatorIndex - 1);
  }

  // Step 3a: Access Services Form (optional)

  const handleDeselectAllAccessServices = () => {
    const boxes = document.getElementsByName('access-services');
    for (let b = 0; b <= boxes.length - 1; b++) {
      const isChecked = boxes[b].checked;
      if (isChecked)
        boxes[b].click();
    };
  }

  const handleSelectAllAccessServices = () => {
    const boxes = document.getElementsByName('access-services');
    for (let b = 0; b <= boxes.length - 1; b++) {
      const isChecked = boxes[b].checked;
      if (!isChecked)
        boxes[b].click();
    };
  }

  const handleAccessServicesConfig = async e => {
    e.preventDefault();
    const boxes = [...document.getElementsByName('access-services')];
    const checkedAccessServices = boxes.filter((box) => { return box.checked }).map((box) => { return box.value });
    setNewServerAccessServices(checkedAccessServices);
    setLoadingText("Enabling access services...");
    document.getElementById("access-services-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Enable Access Services
    const accessServiceURLs = [];
    // check if all
    if (checkedAccessServices.length == accessServices.length) {
      accessServiceURLs.push(`/open-metadata/admin-services/users/${userId}/servers/${newServerName}/access-services`);
    } else {
      checkedAccessServices.forEach((accessService) => {
        accessServiceURLs.push(`/open-metadata/admin-services/users/${userId}/servers/${newServerName}/access-services/${accessService}`);
      })
    }
    for (const enableServiceAccessURL of accessServiceURLs) {
      try {
        const urlArray = enableServiceAccessURL.split("/")
        const serviceType = urlArray[urlArray.length - 1];
        if (serviceType != 'access-services') {
          setLoadingText(`Enabling ${serviceType} access service...`);
        }
        const enableServiceAccessResponse = await axios.post(enableServiceAccessURL, {
          config: '',
          tenantId,
        }, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 30000,
        });
        if (enableServiceAccessResponse.data.relatedHTTPCode != 200) {
          console.error(enableServiceAccessResponse.data);
          throw new Error("Error in enableServiceAccessResponse");
        }
      } catch(error) {
        console.error("Error enabling service access", { error });
        setNotificationType("error");
        if (error.code && error.code == "ECONNABORTED") {
          setNotificationTitle("Connection Error");
          setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
        } else {
          setNotificationTitle("Configuration Error");
          setNotificationSubtitle("Error enabling service access to the server.");
        }
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("access-services-container").style.display = "block";
        document.getElementById("notification-container").style.display = "block";
        return;
      }
    }
    // Fetch Server Config
    setLoadingText("Fetching final stored server configuration...");
    try {
      const serverConfig = await fetchServerConfig();
      console.log({serverConfig});
      setNewServerConfig(<ConfigPreview config={serverConfig} options={{ accessServices: availableAccessServices, editable: true, allowDeployment, preventDeployment, setServerAttribute }} />);
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("audit-log-container").style.display = "block";
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

  // Step 3b: Audit Log Destionations Form

  const handleBackToAccessServicesConfig = e => {
    e.preventDefault();
    document.getElementById("audit-log-container").style.display = "none";
    document.getElementById("access-services-container").style.display = "block";
    setProgressIndicatorIndex(progressIndicatorIndex - 1);
  }

  const configureAuditLogDestinations = async (auditLogDestinations) => {
    setLoadingText("Configuring audit log destinations...");
    document.getElementById("audit-log-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Enable Audit Log Destinations
    const chosenAuditLogDestinations = auditLogDestinations.map((destination) => destination.selected)
    for (const destination of chosenAuditLogDestinations) {
      try {
        const url = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/audit-log-destinations/${destination.id}`;
        const data = {
          tenantId
        }
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
      setNewServerConfig(<ConfigPreview config={serverConfig} options={{ accessServices: availableAccessServices, editable: true, allowDeployment, preventDeployment, setServerAttribute }} />);
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-preview-container").style.display = "block";
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

  // Step 4: Config Preview

  const handleBackToAuditLogDestinationsConfig = e => {
    e.preventDefault();
    document.getElementById("config-preview-container").style.display = "none";
    document.getElementById("audit-log-container").style.display = "block";
    setProgressIndicatorIndex(progressIndicatorIndex - 1);
  }

  const updateConfig = async (serverConfig) => {
    setLoadingText("Updating OMAG server configuration...");
    document.getElementById("config-preview-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Issue the configuration call to update the server configuration
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
        setNewServerConfig(<ConfigPreview config={serverConfig} options={{ accessServices: availableAccessServices, editable: true, allowDeployment, preventDeployment, setServerAttribute }} />);
        allowDeployment();
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("config-preview-container").style.display = "block";
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
  }

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

  const buildAccessServiceTiles = (services) => {
    return services.map((s, i) => (
      <SelectableTile
        id={s.serviceURLMarker}
        light={false}
        name={'access-services'}
        selected={true}
        tabIndex={i}
        title={s.serviceName}
        value={s.serviceURLMarker}
      >
        {s.serviceName}
      </SelectableTile>
    ))
  }

  const serverTypeTiles = serverTypes.map((serverType, i) => {
    return (
      <RadioTile
        id={serverType.id}
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

      <Row id="server-config-container">

        {/* Form Column */}

        <Column
          id="server-config-forms"
          sm={{ span: 4 }}
          md={{ span: 6 }}
          lg={{ span: 11, offset: 1 }}
        >
          
          <h1>Create New OMAG Server</h1>
          <p style={{marginBottom: "24px"}}>Logged in as <CodeSnippet type="inline" >{userId}</CodeSnippet></p>
        
          <div id="server-type-container">
            <h4 style={{textAlign: "left", marginBottom: "32px"}}>Select Server Type</h4>
            <TileGroup
              defaultSelected={serverTypes[0].label}
              name="server-types"
              valueSelected=""
              onChange={value => setNewServerLocalServerType(value)}
            >
              {serverTypeTiles}
            </TileGroup>
            <Button
              kind="primary"
              style={{margin: "16px auto"}}
              onClick={handleServerTypeSelection}
            >
              Proceed to basic configuration
            </Button>
          </div>

          <div id="config-basic-container" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "16px"}}>Basic Configuration</h4>
            <Form id="server-author-basic-config-form" onSubmit={handleBasicConfig}>
              <FormGroup>
                <TextInput
                  type="text"
                  labelText="Server name"
                  name="new-server-name"
                  defaultValue={newServerName}
                  onChange={e => setNewServerName(e.target.value)}
                  placeholder="cocoMDS1"
                  required
                  style={{marginBottom: "16px", width: "100%"}}
                  ref={formStartRef}
                />
                <TextInput
                  type="text"
                  labelText="Local URL root"
                  name="new-server-local-url-root"
                  defaultValue={newServerLocalURLRoot}
                  onChange={e => setNewServerLocalURLRoot(e.target.value)}
                  placeholder="https://localhost:9443"
                  required
                  style={{marginBottom: "16px"}}
                />
                <TextInput
                  type="text"
                  labelText="Local user ID"
                  name="new-server-local-user-id"
                  defaultValue={newServerLocalUserId}
                  onChange={e => setNewServerLocalUserId(e.target.value)}
                  placeholder="myMetadataServerUserId"
                  required
                  style={{marginBottom: "16px"}}
                />
                <TextInput.PasswordInput
                  labelText="Local password"
                  name="new-server-local-password"
                  defaultValue={newServerLocalPassword}
                  onChange={e => setNewServerLocalPassword(e.target.value)}
                  placeholder="myMetadataServerPassword"
                  required
                  style={{marginBottom: "16px"}}
                />
                <TextInput
                  type="text"
                  labelText="Max page size"
                  name="new-server-max-page-size"
                  defaultValue={newServerMaxPageSize}
                  onChange={e => setNewServerMaxPageSize(e.target.value)}
                  placeholder="1000"
                  style={{marginBottom: "16px"}}
                />
                <TextInput
                  type="text"
                  labelText="Security Connector (Class Name)"
                  name="new-server-security-connector"
                  defaultValue={newServerSecurityConnector}
                  onChange={e => setNewServerSecurityConnector(e.target.value)}
                  placeholder="Fully Qualified Java Class Name"
                  helperText="Note: This field is optional. Leave blank to skip."
                  style={{marginBottom: "16px"}}
                />
                {
                  // If server type is Metadata Server, show local repository tiles
                  (newServerLocalServerType == "Metadata Server") &&
                  <TileGroup
                    defaultSelected="in-memory-repository"
                    name="repository-types"
                    valueSelected=""
                    legend="Server repository"
                    onChange={value => setNewServerRepository(value)}
                  >
                    <RadioTile
                      id="in-memory-repository"
                      light={false}
                      name="in-memory-repository"
                      tabIndex={0}
                      value="in-memory-repository"
                    >
                      In Memory
                    </RadioTile>
                    <RadioTile
                      id="local-graph-repository"
                      light={false}
                      name="local-graph-repository"
                      tabIndex={1}
                      value="local-graph-repository"
                    >
                      Janus Graph
                    </RadioTile>
                    <RadioTile
                      id="read-only-repository"
                      light={false}
                      name="read-only-repository"
                      tabIndex={2}
                      value="read-only-repository"
                    >
                      Read Only
                    </RadioTile>
                  </TileGroup>
                }
              </FormGroup>
              <div className="bx--btn-set">
                <Button
                  kind="secondary"
                  style={{margin: "16px auto"}}
                  onClick={handleBackToServerTypeSelection}
                >
                  Back to server type selection
                </Button>
                <Button
                  kind="primary"
                  type="submit"
                  style={{margin: "16px auto"}}
                  // disabled={!validateForm()}
                >
                  Proceed to access services
                </Button>
              </div>
            </Form>
          </div>
          
          <div id="access-services-container" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Select Access Services</h4>
            <div className="bx--btn-set">
              <Button
                kind="secondary"
                style={{margin: "4px auto"}}
                onClick={handleDeselectAllAccessServices}
              >
                Deselect All
              </Button>
              <Button
                kind="secondary"
                style={{margin: "4px auto"}}
                onClick={handleSelectAllAccessServices}
              >
                Select All
              </Button>
            </div>
            <Form id="server-author-access-services-form" onSubmit={(e) => e.preventDefault()}>
              <FormGroup>
                <div
                  aria-label="selectable tiles"
                  role="group"
                >
                  {availableAccessServicesTiles}
                </div>
              </FormGroup>
              <div className="bx--btn-set">
                <Button
                  kind="secondary"
                  style={{margin: "16px auto"}}
                  onClick={handleBackToBasicConfig}
                >
                  Back to basic configuration
                </Button>
                <Button
                  type="submit"
                  kind="primary"
                  style={{margin: "16px auto"}}
                  onClick={handleAccessServicesConfig}
                  // disabled={!validateForm()}
                >
                  Proceed to preview configuration
                </Button>
              </div>
            </Form>
          </div>
        
          <div id="audit-log-container" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Configure Audit Log Destinations</h4>
            <ConfigureAuditLog
              nextAction={{
                label: "Configure audit log destinations",
                action: (state) => configureAuditLogDestinations(state)
              }}
              previousAction={{
                label: (newServerLocalServerType == "Metadata Server") ? "Back to access services" : "Back to basic configuration",
                action: () => {
                  (newServerLocalServerType == "Metadata Server") ? handleBackToAccessServicesConfig() : handleBackToBasicConfig();
                }
              }}
            />
          </div>

          <div id="config-preview-container" style={{display: "none"}}>
            <h4 style={{textAlign: "left", marginBottom: "4px", marginLeft: "1rem"}}>Preview Configuration</h4>
            {newServerConfig}
            <FormGroup class="bx--btn-set">
              <Button
                kind="secondary"
                style={{margin: "16px auto"}}
                onClick={handleBackToAuditLogDestinationsConfig}
              >
                Back to audit log destinations
              </Button>
              <Button
                id="deploy-button"
                type="submit"
                kind="primary"
                style={{margin: "16px auto"}}
                onClick={handleDeployConfig}
              >
                Deploy instance
              </Button>
            </FormGroup>
          </div>
        
          <div id="loading-container" style={{display: "none"}}>
            <Loading
              description="Active loading indicator"
              withOverlay={false}
              style={{margin: 'auto'}}
            />
            <p>{loadingText}</p>
          </div>

          <div id="notification-container" style={{display: "none"}}>
            <InlineNotification
              kind={notificationType}
              title={notificationTitle}
              subtitle={notificationSubtitle}
            />
          </div>

        </Column>

        {/* Progress Indicator Column */}
        
        <Column
          id="config-progress-container"
          sm={{ span: 4 }}
          md={{ span: 2 }}
          lg={{ span: 4 }}
        >
          <ConfigurationSteps serverType={newServerLocalServerType} progressIndicatorIndex={progressIndicatorIndex} />
        </Column>

      </Row>

    </Grid>
  )

}