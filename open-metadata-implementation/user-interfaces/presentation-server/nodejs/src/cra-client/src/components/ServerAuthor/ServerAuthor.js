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
  MultiSelect,
  ProgressIndicator,
  ProgressStep,
  Row,
  Select,
  SelectItem,
  TextInput,
} from "carbon-components-react";
import axios from "axios";

import { IdentificationContext } from "../../contexts/IdentificationContext";
import generateServerConfig from "./generateServerConfig";
import accessServices from "./accessServices";

export default function ServerAuthor() {

  const { userId: originalUserId } = useContext(IdentificationContext);
  const [userId] = useState(originalUserId);
  const [newServerName, setNewServerName] = useState("");
  const [newServerLocalURLRoot, setNewServerLocalURLRoot] = useState("https://localhost:19443");
  const [newServerLocalServerType, setNewServerLocalServerType] = useState("Open Metadata and Governance Server");
  const [newServerOrganizationName, setNewServerOrganizationName] = useState("");
  const [newServerLocalUserId, setNewServerLocalUserId] = useState("");
  const [newServerLocalPassword, setNewServerLocalPassword] = useState("");
  const [newServerAccessServices, setNewServerAccessServices] = useState([]);
  const [newServerRepository, setNewServerRepository] = useState("");
  const [newServerMaxPageSize, setNewServerMaxPageSize] = useState(1000);
  const [notificationKind, setNotificationKind] = useState("");
  const [notificationTitle, setNotificationTitle] = useState("");
  const [notificationSubtitle, setNotificationSubtitle] = useState("");
  const [progressIndicatorIndex, setProgressIndicatorIndex] = useState(0);

  const formStartRef = useRef(null);

  useEffect(() => {
    formStartRef.current.focus();
  }, []);

  const fetchServerConfig = async () => {
    const fetchServerConfigURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/configuration`;
    try {
      const fetchServerConfigResponse = await axios.get(fetchServerConfigURL, {
        params: {
          platformURL: newServerLocalURLRoot
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

  const handleBasicConfig = async e => {
    e.preventDefault();
    console.log("server-author-basic-config-form submitted");
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
    const setServerConfigURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/configuration`;
    try {
      const setServerConfigResponse = await axios.post(setServerConfigURL, {
        config: serverConfig,
        platformURL: newServerLocalURLRoot,
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
      });
      if (setServerConfigResponse.data.relatedHTTPCode == 200) {
        document.getElementById("config-preview").innerHTML = JSON.stringify(serverConfig, null, 2);
        setProgressIndicatorIndex(1);
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("config-advanced-container").style.display = "block";
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
    }
  }

  const handleBackToBasicConfig = e => {
    e.preventDefault();
    console.log("back to basic config");
    document.getElementById("config-advanced-container").style.display = "none";
    document.getElementById("config-basic-container").style.display = "block";
    setProgressIndicatorIndex(0);
  }

  const handleAdvancedConfig = async e => {
    e.preventDefault();
    console.log("server-author-advanced-config-form submitted");
    document.getElementById("config-advanced-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Enable Access Services
    setNewServerAccessServices("asset-owner"); // TODO: use array of access services
    const enableServiceAccessURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/access-services/${newServerAccessServices}`;
    try {
      const enableServiceAccessResponse = await axios.post(enableServiceAccessURL, {
        config: '',
        platformURL: newServerLocalURLRoot,
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
      });
      if (enableServiceAccessResponse.data.relatedHTTPCode == 200) {
        // Enable chosen repository
        const enableRepositoryURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/local-repository/mode/${newServerRepository}`;
        try {
          const enableRepositoryURLResponse = await axios.post(enableRepositoryURL, {
            config: '',
            platformURL: newServerLocalURLRoot,
          }, {
            headers: {
              'Content-Type': 'application/json'
            },
          });
          if (enableRepositoryURLResponse.data.relatedHTTPCode == 200) {
            try {
              const serverConfig = await fetchServerConfig();
              document.getElementById("config-preview").innerHTML = JSON.stringify(serverConfig, null, 2);
              document.getElementById("loading-container").style.display = "none";
              document.getElementById("config-preview-container").style.display = "block";
              setProgressIndicatorIndex(2);
            } catch(error) {
              console.error("error fetching server config", {error});
              setNotificationKind("error");
              setNotificationTitle("Configuration Error")
              setNotificationSubtitle(`Error fetching configuration for the server.`);
              document.getElementById("loading-container").style.display = "none";
              document.getElementById("config-advanced-container").style.display = "block";
              document.getElementById("notification-container").style.display = "block";
            }
          }
        } catch(error) {
          console.error("error enabling chosen repository", {error});
          setNotificationKind("error");
          setNotificationTitle("Configuration Error")
          setNotificationSubtitle(`Error enabling ${newServerRepository} repository for the server.`);
          document.getElementById("loading-container").style.display = "none";
          document.getElementById("config-advanced-container").style.display = "block";
          document.getElementById("notification-container").style.display = "block";
        }

      }
    } catch(error) {
      console.error("error enabling service access", {error});
      setNotificationKind("error");
      setNotificationTitle("Configuration Error")
      setNotificationSubtitle("Error enabling service access to the server.");
      document.getElementById("loading-container").style.display = "none";
      document.getElementById("config-advanced-container").style.display = "block";
      document.getElementById("notification-container").style.display = "block";
    }
  }

  const handleBackToAdvancedConfig = e => {
    e.preventDefault();
    console.log("back to advanced config");
    document.getElementById("config-preview-container").style.display = "none";
    document.getElementById("config-advanced-container").style.display = "block";
    setProgressIndicatorIndex(1);
  }

  const handleDeployConfig = async e => {
    e.preventDefault();
    console.log("store config and deploy server");
    document.getElementById("config-preview-container").style.display = "none";
    document.getElementById("loading-container").style.display = "block";
    // Issue the instance call to start the new server
    const startServerURL = `/open-metadata/admin-services/users/${userId}/servers/${newServerName}/instance`;
    try{
      const startServerResponse = await axios.post(startServerURL, {
        config: '',
        platformURL: newServerLocalURLRoot,
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
      }
    } catch(error) {
      console.log("error starting server from stored config", {error});
    }
  }

  return (
    <Grid>

      <Row id="config-basic-container">
        <Column
          sm={{ span: 4 }}
          md={{ span: 8 }}
          lg={{ span: 8, offset: 4 }}
        >
          <h1>Create New OMAG Server</h1>
          <p style={{marginBottom: "24px"}}>Logged in as <CodeSnippet type="inline" >{userId}</CodeSnippet></p>
          <h4 style={{textAlign: "left", marginBottom: "32px"}}>Basic Configuration</h4>
          <Form id="server-author-basic-config-form" onSubmit={handleBasicConfig}>
            <FormGroup>
              <TextInput
                type="text"
                labelText="Server name"
                name="new-server-name"
                defaultValue={newServerName}
                onChange={e => setNewServerName(e.target.value)}
                placeholder="e.g., cocoMDS1"
                required
                style={{marginBottom: "32px"}}
                ref={formStartRef}
              />
              <TextInput
                type="text"
                labelText="Local URL root"
                name="new-server-local-url-root"
                defaultValue={newServerLocalURLRoot}
                onChange={e => setNewServerLocalURLRoot(e.target.value)}
                placeholder="e.g., https://localhost:19443"
                required
                style={{marginBottom: "32px"}}
              />
              <TextInput
                type="text"
                labelText="Local server type"
                name="new-server-local-server-type"
                defaultValue={newServerLocalServerType}
                onChange={e => setNewServerLocalServerType(e.target.value)}
                placeholder="e.g., Open Metadata and Governance Server"
                required
                style={{marginBottom: "32px"}}
              />
              <TextInput
                type="text"
                labelText="Organization name"
                name="new-server-organization-name"
                defaultValue={newServerOrganizationName}
                onChange={e => setNewServerOrganizationName(e.target.value)}
                placeholder="e.g., myOrg"
                required
                style={{marginBottom: "32px"}}
              />
              <TextInput
                type="text"
                labelText="Local user ID"
                name="new-server-local-user-id"
                defaultValue={newServerLocalUserId}
                onChange={e => setNewServerLocalUserId(e.target.value)}
                placeholder="e.g., myMetadataServerUserId"
                required
                style={{marginBottom: "32px"}}
              />
              <TextInput.PasswordInput
                labelText="Local password"
                name="new-server-local-password"
                defaultValue={newServerLocalPassword}
                onChange={e => setNewServerLocalPassword(e.target.value)}
                placeholder="e.g., myMetadataServerPassword"
                required
              />
            </FormGroup>
            <FormGroup class="bx--btn-set">
              <Button
                type="submit"
                style={{marginTop: "48px", marginBottom: "16px"}}
                // disabled={!validateForm()}
              >
                Proceed to advanced configuration
              </Button>
            </FormGroup>
          </Form>
        </Column>
      </Row>
      
      <Row id="config-advanced-container" style={{display: "none"}}>
        <Column
          sm={{ span: 4 }}
          md={{ span: 8 }}
          lg={{ span: 8, offset: 4 }}
        >
          <h1>Create New OMAG Server</h1>
          <p style={{marginBottom: "32px"}}>Logged in as <CodeSnippet type="inline" >{userId}</CodeSnippet></p>
          <h4 style={{textAlign: "left", marginBottom: "24px"}}>Advanced Configuration (Optional)</h4>
          <Form id="server-author-advanced-config-form" onSubmit={(e) => e.preventDefault()}>
            <FormGroup>
              <MultiSelect
                id="new-server-access-services"
                items={accessServices}
                label="Access Services"
                onChange={e => setNewServerAccessServices(e && e.selectedItems.length ? e.selectedItems.map((item) => item.id) : [])}
              >
                <SelectItem
                  disabled
                  hidden
                  value="placeholder-option"
                  text="Choose an option"
                />
                <SelectItem
                  value="asset-owner"
                  text="Asset Owner"
                />
              </MultiSelect>
              <Select
                name="new-server-repository"
                defaultValue="placeholder-option"
                // onChange={e => console.log(e)} // setNewServerRepository(e.options[e.selectedIndex].value)}
              >
                <SelectItem
                  disabled
                  hidden
                  value="placeholder-option"
                  text="Choose an option"
                />
                <SelectItem
                  value="in-memory-repository"
                  text="In Memory"
                  onClick={() => setNewServerRepository("in-memory-repository")}
                />
                <SelectItem
                  value="local-graph-repository"
                  text="Janus Graph"
                  onClick={() => setNewServerRepository("in-memory-repository")}
                />
              </Select>
              <TextInput
                type="text"
                labelText="Max page size"
                name="new-server-max-page-size"
                defaultValue={newServerMaxPageSize}
                onChange={e => setNewServerMaxPageSize(e.target.value)}
                placeholder="e.g., 1000"
                style={{marginBottom: "32px"}}
              />
            </FormGroup>
            <FormGroup class="bx--btn-set">
              <Button
                kind="secondary"
                onClick={handleBackToBasicConfig}
              >
                Back to basic configuration
              </Button>
              <Button
                type="submit"
                kind="primary"
                style={{marginTop: "48px", marginBottom: "16px"}}
                onClick={handleAdvancedConfig}
                // disabled={!validateForm()}
              >
                Proceed to preview configuration
              </Button>
            </FormGroup>
          </Form>
        </Column>
      </Row>
      
      <Row id="config-preview-container" style={{display: "none"}}>
        <Column
          sm={{ span: 4 }}
          md={{ span: 6, offset: 1 }}
          lg={{ span: 12, offset: 2 }}
        >
          <h1>Create New OMAG Server</h1>
          <p style={{marginBottom: "32px"}}>Logged in as <CodeSnippet type="inline" >{userId}</CodeSnippet></p>
          <h4 style={{textAlign: "left", marginBottom: "24px"}}>Preview Configuration</h4>
          <CodeSnippet id="config-preview" type="multi"></CodeSnippet>
          <FormGroup class="bx--btn-set">
            <Button
              kind="secondary"
              onClick={handleBackToAdvancedConfig}
            >
              Back to advanced configuration
            </Button>
            <Button
              type="submit"
              kind="primary"
              style={{marginTop: "48px", marginBottom: "16px"}}
              onClick={handleDeployConfig}
            >
              Deploy instance
            </Button>
          </FormGroup>
        </Column>
      </Row>
      
      <Row id="loading-container" style={{display: "none"}}>
        <Column
          sm={{ span: 4 }}
          md={{ span: 8 }}
          lg={{ span: 8, offset: 4 }}
        >
          <Loading
            description="Active loading indicator"
            withOverlay={false}
          />
        </Column>
      </Row>

      <Row id="notification-container" style={{display: "none"}}>
        <Column
          sm={{ span: 4 }}
          md={{ span: 8 }}
          lg={{ span: 8, offset: 4 }}
        >
          <InlineNotification
            kind={notificationKind}
            title={notificationTitle}
            subtitle={notificationSubtitle}
          />
        </Column>
      </Row>

      <Row id="config-progress-container">
        <Column
          sm={{ span: 4 }}
          md={{ span: 6, offset: 1 }}
          lg={{ span: 8, offset: 4 }}
        >
          <ProgressIndicator
            id="config-progress-indicator"
            vertical={false}
            currentIndex={progressIndicatorIndex}
            spaceEqually={false}
          >
            <ProgressStep
              label="First step"
              description="Step 1: OMAG server wizard"
              secondaryLabel="Basic configuration"
            />
            <ProgressStep
              label="Second step"
              description="Step 2: OMAG server wizard"
              secondaryLabel="Advanced configuration"
              /* Advanced config to include:
                  Max page size,
                  Enabling Janus graph repository,
                  Enabling access services (asset-owner by default)
              */
            />
            <ProgressStep
              label="Third step"
              description="Step 3: OMAG server wizard"
              secondaryLabel="Preview configuration and deploy instance"
            />
          </ProgressIndicator>
        </Column>
      </Row>

    </Grid>
  )

}