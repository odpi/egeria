/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext, useEffect, useRef, useState } from "react";
import {
  Button,
  CodeSnippet,
  Column,
  Form,
  FormGroup,
  Grid,
  InlineNotification,
  Loading,
  ProgressIndicator,
  ProgressStep,
  Row,
  TextInput,
  TileGroup,
  RadioTile,
  SelectableTile
} from "carbon-components-react";
import JSONPretty from 'react-json-pretty';
import axios from "axios";

import { IdentificationContext } from "../../contexts/IdentificationContext";
import generateServerConfig from "./generateServerConfig";
import accessServices from "./accessServices";
import serverTypes from "./serverTypes";

export default function ServerAuthor() {

  const { userId, serverName: tenantId, user } = useContext(IdentificationContext);
  const newServerOrganizationName = user.organizationName;
  const [newServerName, setNewServerName] = useState("");
  const [newServerLocalURLRoot, setNewServerLocalURLRoot] = useState("https://localhost:9443");
  const [newServerLocalServerType, setNewServerLocalServerType] = useState(serverTypes[0].label);
  // const [newServerOrganizationName, setNewServerOrganizationName] = useState("");
  const [newServerLocalUserId, setNewServerLocalUserId] = useState("");
  const [newServerLocalPassword, setNewServerLocalPassword] = useState("");
  const [newServerAccessServices, setNewServerAccessServices] = useState(accessServices.map((s) => s.id));
  const [newServerRepository, setNewServerRepository] = useState("in-memory-repository");
  const [newServerMaxPageSize, setNewServerMaxPageSize] = useState(1000);
  const [notificationKind, setNotificationKind] = useState("");
  const [notificationTitle, setNotificationTitle] = useState("");
  const [notificationSubtitle, setNotificationSubtitle] = useState("");
  const [progressIndicatorIndex, setProgressIndicatorIndex] = useState(0);
  const [loadingText, setLoadingText] = useState("Loading...");
  const [newServerConfig, setNewServerConfig] = useState("");

  const formStartRef = useRef(null);

  useEffect(() => {
    formStartRef.current.focus();
  }, []);

  const fetchServerConfig = async () => {
    const fetchServerConfigURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/configuration`;
    try {
      const fetchServerConfigResponse = await axios.get(fetchServerConfigURL, {
        params: {
          tenantId,
        }
      });
      console.debug({fetchServerConfigResponse});
      if (fetchServerConfigResponse.data.relatedHTTPCode == 200) {
        return fetchServerConfigResponse.data.omagserverConfig;
      } else {
        throw new Error("error in fetchServerConfigResponse");
      }
    } catch(error) {
      console.error("error fetching config from platform", {error});
      throw error;
    }
  }

  const handleServerTypeSelection = async e => {
    e.preventDefault();
    document.getElementById("server-type-container").style.display = "none";
    document.getElementById("config-basic-container").style.display = "block";
    setProgressIndicatorIndex(1);
  }

  const handleBackToServerTypeSelection = e => {
    e.preventDefault();
    document.getElementById("config-basic-container").style.display = "none";
    document.getElementById("server-type-container").style.display = "block";
    setProgressIndicatorIndex(0);
  }

  const handleBasicConfig = async e => {
    e.preventDefault();
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
      serverConfig = generateServerConfig(configOptions);
    } catch(error) {
      console.error("error generating server config", {error});
      alert(error);
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
      });
      if (setServerConfigResponse.data.relatedHTTPCode == 200) {
        setNewServerConfig(serverConfig);
      } else {
        console.error(setServerConfigResponse.data);
        throw new Error("error in setServerConfigResponse")
      }
    } catch(error) {
      console.error("error sending config to platform", {error});
      setNotificationKind("error");
      setNotificationTitle("Configuration Error")
      setNotificationSubtitle("Error sending server configuration to the platform.");
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-basic-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
    // Enable chosen repository
    setLoadingText("Enabling chosen repository...");
    const enableRepositoryURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/local-repository/mode/${newServerRepository}`;
    try {
      const enableRepositoryURLResponse = await axios.post(enableRepositoryURL, {
        config: '',
        tenantId,
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
      });
      if (enableRepositoryURLResponse.data.relatedHTTPCode != 200) {
        console.error(enableRepositoryURLResponse.data);
        throw new Error("error in enableRepositoryURLResponse")
      }
    } catch(error) {
      console.error("error enabling chosen repository", {error});
      setNotificationKind("error");
      setNotificationTitle("Configuration Error")
      setNotificationSubtitle(`Error enabling ${newServerRepository} repository for the server.`);
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-basic-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
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
      });
      if (configureEventBusURLResponse.data.relatedHTTPCode == 200) {
        setProgressIndicatorIndex(2);
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("access-services-container").style.display = "block";
      } else {
        console.error(configureEventBusURLResponse.data);
        throw new Error("error in configureEventBusURLResponse")
      }
    } catch(error) {
      console.error("error configuring event bus", {error});
      setNotificationKind("error");
      setNotificationTitle("Configuration Error")
      setNotificationSubtitle(`Error configuring event bus.`);
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-basic-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
      return;
    }
  }

  const handleBackToBasicConfig = e => {
    e.preventDefault();
    document.getElementById("access-services-container").style.display = "none";
    document.getElementById("config-basic-container").style.display = "block";
    setProgressIndicatorIndex(1);
  }

  const handleDeselectAllAccessServices = () => {
    const boxes = document.getElementsByName('access-services');
    for (let b=0; boxes.length - 1; b++) {
      const isChecked = boxes[b].checked;
      if (isChecked)
        boxes[b].click();
    };
  }

  const handleSelectAllAccessServices = () => {
    const boxes = document.getElementsByName('access-services');
    for (let b=0; boxes.length - 1; b++) {
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
        });
        if (enableServiceAccessResponse.data.relatedHTTPCode != 200) {
          console.error(enableServiceAccessResponse.data);
          throw new Error("error in enableServiceAccessResponse");
        }
      } catch(error) {
        console.error("error enabling service access", {error});
        setNotificationKind("error");
        setNotificationTitle("Configuration Error")
        setNotificationSubtitle("Error enabling service access to the server.");
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
      setNewServerConfig(serverConfig);
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-preview-container").style.display = "block";
      setProgressIndicatorIndex(3);
    } catch(error) {
      console.error("error fetching server config", {error});
      setNotificationKind("error");
      setNotificationTitle("Configuration Error")
      setNotificationSubtitle(`Error fetching configuration for the server.`);
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("access-services-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  const handleBackToAccessServicesConfig = e => {
    e.preventDefault();
    document.getElementById("config-preview-container").style.display = "none";
    document.getElementById("access-services-container").style.display = "block";
    setProgressIndicatorIndex(2);
  }

  const handleDeployConfig = async e => {
    e.preventDefault();
    setLoadingText("Deploying OMAG server from stored configuration...")
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
      });
      if (startServerResponse.data.relatedHTTPCode == 200) {
        setNotificationKind("success");
        setNotificationTitle("Success!")
        setNotificationSubtitle(`Server instance deployed from configuration.`);
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("notification-container").style.display = "block";
      } else {
        console.error(startServerResponse.data);
        throw new Error("error in startServerResponse");
      }
    } catch(error) {
      console.error("error starting server from stored config", {error});
      setNotificationKind("error");
      setNotificationTitle("Deployment Error")
      setNotificationSubtitle(`Error starting server from stored configuration file.`);
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-preview-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
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

  const accessServiceTiles = accessServices.map((service, i) => {
    return (
      <SelectableTile
        id={service.id}
        light={false}
        name={'access-services'}
        // onChange={handleAccessServiceClick}
        selected={true}
        tabIndex={i}
        title={service.label}
        value={service.id}
      >
        {service.label}
      </SelectableTile>
    )
  })

  return (
    <Grid>

      <Row id="server-config-container">

        <Column
          id="server-config-forms"
          sm={{ span: 4 }}
          md={{ span: 6 }}
          lg={{ span: 10, offset: 2 }}
        >
        
          <div id="server-type-container">
            <h1>Create New OMAG Server</h1>
            <p style={{marginBottom: "24px"}}>Logged in as <CodeSnippet type="inline" >{userId}</CodeSnippet></p>
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
            <h1>Create New OMAG Server</h1>
            <p style={{marginBottom: "24px"}}>Logged in as <CodeSnippet type="inline" >{userId}</CodeSnippet></p>
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
                  style={{marginBottom: "16px"}}
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
                    tabIndex={0}
                    value="local-graph-repository"
                  >
                    Janus Graph
                  </RadioTile>
                </TileGroup>
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
            <h1>Create New OMAG Server</h1>
            <p style={{marginBottom: "32px"}}>Logged in as <CodeSnippet type="inline" >{userId}</CodeSnippet></p>
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
                  {accessServiceTiles}
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
        
          <div id="config-preview-container" style={{display: "none"}}>
            <h1>Create New OMAG Server</h1>
            <p style={{marginBottom: "32px"}}>Logged in as <CodeSnippet type="inline" >{userId}</CodeSnippet></p>
            <h4 style={{textAlign: "left", marginBottom: "24px"}}>Preview Configuration</h4>
            <div style={{
              backgroundColor: "#d3d3d3",
              textAlign: "left",
              overflow: "scroll",
              height: "400px"
            }}>
              <JSONPretty id="config-preview" data={newServerConfig}></JSONPretty>
            </div>
            <FormGroup class="bx--btn-set">
              <Button
                kind="secondary"
                style={{margin: "16px auto"}}
                onClick={handleBackToAccessServicesConfig}
              >
                Back to access services
              </Button>
              <Button
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
              kind={notificationKind}
              title={notificationTitle}
              subtitle={notificationSubtitle}
            />
          </div>

        </Column>
        
        <Column
          id="config-progress-container"
          sm={{ span: 4 }}
          md={{ span: 2 }}
          lg={{ span: 4 }}
        >
          <ProgressIndicator
            id="config-progress-indicator"
            vertical={false}
            currentIndex={progressIndicatorIndex}
            spaceEqually={false}
            vertical={true}
            style={{marginTop: "98px"}}
          >
            <ProgressStep
              label="Select server type"
              // secondaryLabel=""
              description="Step 1: OMAG server wizard"
            />
            <ProgressStep
              label="Basic configuration"
              // secondaryLabel=""
              description="Step 2: OMAG server wizard"
            />
            <ProgressStep
              label="Select access services"
              description="Step 3: OMAG server wizard"
            />
            <ProgressStep
              label="Preview configuration and deploy instance"
              description="Step 4: OMAG server wizard"
            />
          </ProgressIndicator>
        </Column>

      </Row>

    </Grid>
  )

}