/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext, useState } from "react";
import {
  Accordion,
  AccordionItem,
  Button,
  DataTable,
  Link,
  StructuredListBody,
  StructuredListCell,
  StructuredListHead,
  StructuredListRow,
  StructuredListWrapper,
  TextInput,
  Tooltip,
} from "carbon-components-react";
import {
  CheckmarkOutline16,
  Edit16,
  MisuseOutline16,
  Save16,
  TrashCan16,
} from "@carbon/icons-react";
import JSONPretty from 'react-json-pretty';
import axios from "axios";
import { IdentificationContext } from "../../contexts/IdentificationContext";
import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";
import './serverConfig.scss';

export default function ConfigPreview({ options }) {

  const { userId, serverName: tenantId } = useContext(IdentificationContext);
  const {
    availableAccessServices,
    setNotificationType,
    setNotificationTitle,
    setNotificationSubtitle,
    newServerConfig, setNewServerConfig,
    setPreventDeployment,
    fetchServerConfig,
    setServerAttribute,
  } = useContext(ServerAuthorContext);

  const [editing_organizationName, setEditing_organizationName] = useState(false);
  const [editing_localServerUserId, setEditing_localServerUserId] = useState(false);
  const [editing_localServerPassword, setEditing_localServerPassword] = useState(false);
  const [editing_maxPageSize, setEditing_maxPageSize] = useState(false);

  if (!newServerConfig || newServerConfig == {} || newServerConfig == '') {
    return null;
  }

  if (!options)
    options = {
      editable: false,
    };

  const {
    editable,
  } = options;

  const enableAccessServices = (selectedRows) => async () => {
    console.log('called enableAccessServices', { selectedRows });
    // Enable Access Services
    const accessServiceURLs = [];
    selectedRows.forEach((row) => {
      console.log({row});
      // TODO: Add comparison of new access services to enable vs. existing access services from config
      //       to rule out any unneccessary calls
      accessServiceURLs.push(`/open-metadata/admin-services/users/${userId}/servers/${newServerConfig.localServerName}/access-services/${row.id}`);
    });
    for (const enableServiceAccessURL of accessServiceURLs) {
      try {
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
          setNotificationSubtitle("Error enabling access service(s).");
        }
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("notification-container").style.display = "block";
        return;
      }
    }
    // Refresh Server Config
    const serverConfig = await fetchServerConfig();
    setNewServerConfig(serverConfig);
  }

  const disableAccessServices = (selectedRows) => async () => {
    console.log('called disableAccessServices', { selectedRows });
    // Disable Access Services
    const accessServiceURLs = [];
    selectedRows.forEach((row) => {
      console.log({row});
      // TODO: Add comparison of new access services to disable vs. existing access services from config
      //       to rule out any unneccessary calls
      accessServiceURLs.push(`/open-metadata/admin-services/users/${userId}/servers/${newServerConfig.localServerName}/access-services/${row.id}`);
    });
    for (const disableServiceAccessURL of accessServiceURLs) {
      try {
        const disableServiceAccessResponse = await axios.delete(disableServiceAccessURL, {
          data: {
            config: '',
            tenantId,
          },
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 30000,
        });
        if (disableServiceAccessResponse.data.relatedHTTPCode != 200) {
          console.error(disableServiceAccessResponse.data);
          throw new Error("Error in disableServiceAccessResponse");
        }
      } catch(error) {
        console.error("Error disabling access service", { error });
        setNotificationType("error");
        if (error.code && error.code == "ECONNABORTED") {
          setNotificationTitle("Connection Error");
          setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
        } else {
          setNotificationTitle("Configuration Error");
          setNotificationSubtitle("Error disabling access service(s).");
        }
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("notification-container").style.display = "block";
        return;
      }
    }
    // Refresh Server Config
    const serverConfig = await fetchServerConfig();
    setNewServerConfig(serverConfig);
  }

  // Editable Cells

  const makeCellEditable = (field) => {
    console.log("called makeCellEditable", { field });
    eval(`setEditing_${field}(true)`);
  }

  const cancelChange = (field) => {
    console.log("called cancelChange", { field });
    eval(`setEditing_${field}(false)`);
    setPreventDeployment(false);
  }

  const saveChange = async (field) => {
    console.log("called saveChange", { field });
    const value = document.getElementById(`${field}-input`).value;
    try {
      switch(field) {
        case "localServerType":
          await setServerAttribute("server-type?typeName", value);
          break;
        case "organizationName":
          await setServerAttribute("organization-name?name", value);
          break;
        case "localServerUserId":
          await setServerAttribute("server-user-id?id", value);
          break;
        case "localServerPassword":
          await setServerAttribute("server-user-password?password", value);
          break;
        case "maxPageSize":
          await setServerAttribute("max-page-size?limit", value);
          break;
        default:
          break;
      }
      eval(`setEditing_${field}(false)`);
      const serverConfig = await fetchServerConfig();
      console.log({ serverConfig });
      setNewServerConfig(serverConfig);
      setPreventDeployment(false);
    } catch(error) {
      console.error({ error });
    }
  }

  const handleCellChange = (field, originalValue, currentValue) => {
    console.log("called handleCellChange", { field, originalValue, currentValue });
    if (currentValue == originalValue) {
      console.log("value matches original");
      setPreventDeployment(false);
    } else {
      console.log("value has changed");
      setPreventDeployment(true);
    }
  }

  const editableCell = (field, originalValue, editable = false, editing = false) => {
    // console.log("called editableCell", { field, editable, editing });
    if (!editable) {
      return <span>{originalValue}</span>
    }
    if (!editing) {
      return (
        <span>
          {originalValue}
          <Button
            hasIconOnly
            kind="tertiary"
            size="small"
            renderIcon={Edit16}
            iconDescription="Edit"
            tooltipAlignment="start"
            tooltipPosition="right"
            onClick={() => makeCellEditable(field)}
          />
        </span>
      )
    }
    return (
      <span>
        <TextInput
          id={field+"-input"}
          type="text"
          size="sm"
          inline
          labelText=""
          defaultValue={originalValue}
          onChange={e => handleCellChange(field, originalValue, e.target.value)}
          invalidText="Warning: changes aren't saved until save button is clicked" />
        <Button
          hasIconOnly
          kind="tertiary"
          size="small"
          renderIcon={Save16}
          iconDescription="Save"
          tooltipAlignment="start"
          tooltipPosition="right"
          onClick={() => saveChange(field)}
        />
        <Button
          hasIconOnly
          kind="tertiary"
          size="small"
          renderIcon={TrashCan16}
          iconDescription="Discard changes"
          tooltipAlignment="start"
          tooltipPosition="right"
          onClick={() => cancelChange(field)}
        />
      </span>
    )
  }

  // Access Services

  let showAccessServices = false;
  if (newServerConfig.localServerType == "Metadata Server" || newServerConfig.localServerType == "Metadata Access Point") {
    showAccessServices = true;
  }

  const accessServicesHeaders = [
    {
      key: "serviceName",
      header: "Service Name"
    },
    {
      key: "status",
      header: "Status"
    }
  ]
  
  let accessServicesRows = [];

  if (availableAccessServices && availableAccessServices.length) {
    
    availableAccessServices.forEach((accessService) => {
      
      if (newServerConfig.accessServicesConfig && newServerConfig.accessServicesConfig.length) {

        // Check if service is listed in config
        const matchingService = newServerConfig.accessServicesConfig.find((c) => {
          return c.accessServiceURLMarker === accessService.serviceURLMarker;
        });

        if (!matchingService) {
          accessServicesRows.push({
            id: accessService.serviceURLMarker,
            serviceName: accessService.serviceName,
            urlMarker: accessService.serviceURLMarker,
            description: accessService.serviceDescription,
            wiki: accessService.serviceWiki,
            status: false,
            inTopic: 'n/a',
            outTopic: 'n/a',
          });
        } else {
          accessServicesRows.push({
            id: matchingService.accessServiceURLMarker,
            serviceName: matchingService.accessServiceFullName,
            urlMarker: matchingService.accessServiceURLMarker,
            description: matchingService.accessServiceDescription,
            wiki: matchingService.accessServiceWiki,
            status: matchingService.accessServiceOperationalStatus == "ENABLED",
            inTopic: matchingService.accessServiceInTopic.endpoint.address,
            outTopic: matchingService.accessServiceOutTopic.endpoint.address,
          });
        }

      } else {

        accessServicesRows.push({
          id: accessService.serviceURLMarker,
          serviceName: accessService.serviceName,
          urlMarker: accessService.serviceURLMarker,
          description: accessService.serviceDescription,
          wiki: accessService.serviceWiki,
          status: false,
          inTopic: 'n/a',
          outTopic: 'n/a',
        });

      }

    });

  } else {

    if (newServerConfig.accessServicesConfig && newServerConfig.accessServicesConfig.length) {

      accessServicesRows = newServerConfig.accessServicesConfig.map((c) => {
        return {
          id: c.accessServiceURLMarker,
          serviceName: c.accessServiceFullName,
          urlMarker: c.accessServiceURLMarker,
          description: c.accessServiceDescription,
          wiki: c.accessServiceWiki,
          status: c.accessServiceOperationalStatus == "ENABLED",
          inTopic: c.accessServiceInTopic.endpoint.address,
          outTopic: c.accessServiceOutTopic.endpoint.address,
        }
      });

    }

  }

  // Repository Services

  let repoServices = <div><p>n/a</p></div>;
  if (newServerConfig.repositoryServicesConfig) {
    // Audit Log Connections
    let auditLogConnections = [];
    if (newServerConfig.repositoryServicesConfig.auditLogConnections && newServerConfig.repositoryServicesConfig.auditLogConnections.length) {
      auditLogConnections = newServerConfig.repositoryServicesConfig.auditLogConnections.map((c, i) => {
        return (
          <div key={`audit-log-connection-${i}`} style={{ marginBottom: '16px', padding: '16px', border: "1px solid #e0e0e0" }}>
            <div style={{lineHeight: "20px"}}>
              <span style={{fontWeight: "700"}}>Name:</span>
              {' '}
              <span>{c.displayName}</span>
            </div>
            <div style={{lineHeight: "20px"}}>
              <span style={{fontWeight: "700"}}>Connector Type:</span>
              {' '}
              <span>{c.connectorType.displayName}</span>
            </div>
            <div style={{lineHeight: "20px"}}>
              <span style={{fontWeight: "700"}}>Supported Severities:</span>
            </div>
            <div style={{lineHeight: "20px"}}>
              <ul style={{paddingLeft: "36px"}}>
                {c.configurationProperties.supportedSeverities.map((sev, i) => (
                  <li key={i} style={{textDecoration: "none"}}>{sev}</li>
                ))}
              </ul>
            </div>
          </div>
        )
      });
    }
    // Enterprise Connections
    let enterpriseConnections = [];
    if (
      newServerConfig.repositoryServicesConfig.enterpriseAccessConfig &&
      newServerConfig.repositoryServicesConfig.enterpriseAccessConfig.enterpriseOMRSTopicConnection &&
      newServerConfig.repositoryServicesConfig.enterpriseAccessConfig.enterpriseOMRSTopicConnection.embeddedConnections &&
      newServerConfig.repositoryServicesConfig.enterpriseAccessConfig.enterpriseOMRSTopicConnection.embeddedConnections.length
    ) {
      enterpriseConnections = newServerConfig.repositoryServicesConfig.enterpriseAccessConfig.enterpriseOMRSTopicConnection.embeddedConnections.map((c, i) => {
        return (
          <div key={`embedded-connection-${i}`} style={{ marginBottom: '16px', padding: '16px', border: "1px solid #e0e0e0" }}>
            <div style={{lineHeight: "20px"}}>
              <span style={{fontWeight: "700"}}>Name:</span>
              {' '}
              <span>{c.displayName}</span>
            </div>
            <div style={{lineHeight: "20px"}}>
              <span style={{fontWeight: "700"}}>Connector Type:</span>
              {' '}
              <span>
                {c.embeddedConnection.connectorType.displayName}
                <Tooltip direction="right" iconName="Information20" style={{lineHeight: "24px", alignItems: "baseline"}}>
                  <p>{c.embeddedConnection.connectorType.description}</p>
                </Tooltip>
              </span>
            </div>
            <div style={{lineHeight: "20px"}}>
              <span style={{fontWeight: "700"}}>Endpoint:</span>
              {' '}
              <span>{c.embeddedConnection.endpoint.address}</span>
            </div>
          </div>
        )
      })
    }
    // Local Repository Configuration
    let localRepositoryConfig;
    const localRepositoryConfigComponents = [];
    if (newServerConfig.repositoryServicesConfig.localRepositoryConfig) {
      // Local Repository Local Connection
      if (newServerConfig.repositoryServicesConfig.localRepositoryConfig.localRepositoryLocalConnection) {
        localRepositoryConfigComponents.push(
          <div key="local-repository-local-connection" style={{lineHeight: "20px"}}>
            <span style={{fontWeight: "700"}}>Local Repository Local Connection:</span>
            {' '}
            {newServerConfig.repositoryServicesConfig.localRepositoryConfig.localRepositoryLocalConnection.connectorType.displayName}
            <Tooltip direction="right" iconName="Information20" style={{lineHeight: "24px", alignItems: "baseline"}}>
              <p>{newServerConfig.repositoryServicesConfig.localRepositoryConfig.localRepositoryLocalConnection.connectorType.description}</p>
            </Tooltip>
          </div>
        )
      }
      // Local Repository Remote Connection
      if (newServerConfig.repositoryServicesConfig.localRepositoryConfig.localRepositoryRemoteConnection) {
        localRepositoryConfigComponents.push(
          <div key="local-repository-remote-connection" style={{lineHeight: "20px"}}>
            <span style={{fontWeight: "700"}}>Local Repository Remote Connection:</span>
            {' '}
            {newServerConfig.repositoryServicesConfig.localRepositoryConfig.localRepositoryRemoteConnection.connectorType.displayName}
            <Tooltip direction="right" iconName="Information20" style={{lineHeight: "24px", alignItems: "baseline"}}>
              <p>{newServerConfig.repositoryServicesConfig.localRepositoryConfig.localRepositoryRemoteConnection.connectorType.description}</p>
            </Tooltip>
          </div>
        )
      }
      // Local Repository Events to Save
      if (newServerConfig.repositoryServicesConfig.localRepositoryConfig.eventsToSaveRule) {
        localRepositoryConfigComponents.push(
          <div key="local-repository-events-to-save" style={{lineHeight: "20px"}}>
            <span style={{fontWeight: "700"}}>Events to Save:</span>
            {' '}
            {newServerConfig.repositoryServicesConfig.localRepositoryConfig.eventsToSaveRule}
          </div>
        )
      }
      // Local Repository Events to Save
      if (newServerConfig.repositoryServicesConfig.localRepositoryConfig.eventsToSendRule) {
        localRepositoryConfigComponents.push(
          <div key="local-repository-events-to-send" style={{lineHeight: "20px"}}>
            <span style={{fontWeight: "700"}}>Events to Send:</span>
            {' '}
            {newServerConfig.repositoryServicesConfig.localRepositoryConfig.eventsToSendRule}
          </div>
        )
      }
      localRepositoryConfig = <div style={{ marginBottom: '16px', padding: '16px', border: "1px solid #e0e0e0" }}>{localRepositoryConfigComponents}</div>;
    }

    // Repository Services div
    repoServices = <div>
      <h6 style={{ padding: '1rem', color: '#0f62fe', border: "1px solid #e0e0e0" }}>Audit Log Connections</h6>
      {auditLogConnections.map((alc) => (
        alc
      ))}
      <h6 style={{ padding: '1rem', color: '#0f62fe', border: "1px solid #e0e0e0" }}>Local Repository Configuration</h6>
      {localRepositoryConfig}
      <h6 style={{ padding: '1rem', color: '#0f62fe', border: "1px solid #e0e0e0" }}>Enterprise Access Configuration</h6>
      {enterpriseConnections.map((ec) => (
        ec
      ))}
    </div>
  }

  // Audit Trail

  let auditTrailBody;
  if (newServerConfig.auditTrail) {
    if (newServerConfig.auditTrail.length) {
      auditTrailBody = newServerConfig.auditTrail.map((t, i) => {
        return (
          <StructuredListRow key={`audit-trail-item-${i}`}>
            <StructuredListCell>{t.slice(0, 28)}</StructuredListCell>
            <StructuredListCell>{t.slice(29)}</StructuredListCell>
          </StructuredListRow>
        )
      })
    } else {
      auditTrailBody = <StructuredListRow>
        <StructuredListCell>n/a</StructuredListCell>
        <StructuredListCell>No actions found.</StructuredListCell>
      </StructuredListRow>
    }
  }

  return (

    <div style={{ textAlign: 'left' }}>
      
      <p style={{
        marginBottom: '24px',
        marginLeft: '1rem',
        fontSize: 'small',
      }}>
        Configuration Version: {newServerConfig.versionId}
      </p>

      <Accordion>
        
        <AccordionItem open title="Basic Configuration" className="customAccordionItem">
          <StructuredListWrapper style={{ marginBottom: '0' }}>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server ID</StructuredListCell>
              <StructuredListCell>{newServerConfig.localServerId}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server Name</StructuredListCell>
              <StructuredListCell>{newServerConfig.localServerName}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server Type</StructuredListCell>
              <StructuredListCell>{newServerConfig.localServerType}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Organization Name</StructuredListCell>
              <StructuredListCell id="organizationName">{editableCell("organizationName", newServerConfig.organizationName, editable, editing_organizationName)}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server URL</StructuredListCell>
              <StructuredListCell>{newServerConfig.localServerURL}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server User ID</StructuredListCell>
              <StructuredListCell id="localServerUserId">{editableCell("localServerUserId", newServerConfig.localServerUserId, editable, editing_localServerUserId)}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server Password</StructuredListCell>
              <StructuredListCell id="localServerPassword">{editableCell("localServerPassword", newServerConfig.localServerPassword, editable, editing_localServerPassword)}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Max Page Size</StructuredListCell>
              <StructuredListCell id="maxPageSize">{editableCell("maxPageSize", newServerConfig.maxPageSize, editable, editing_maxPageSize)}</StructuredListCell>
            </StructuredListRow>
          </StructuredListWrapper>
        </AccordionItem>
        
        {showAccessServices && <AccordionItem title="Access Services Configuration" className="customAccordionItem">
          <DataTable rows={accessServicesRows} headers={accessServicesHeaders} isSortable>
            {({
              rows,
              headers,
              getHeaderProps,
              getRowProps,
              getSelectionProps,
              getToolbarProps,
              getBatchActionProps,
              onInputChange,
              selectedRows,
              getTableProps,
              getTableContainerProps,
            }) => (
              <DataTable.TableContainer
                title="Access Services"
                description="OMAG Server Access Services"
                {...getTableContainerProps()}
              >
                <DataTable.TableToolbar {...getToolbarProps()}>
                  {editable && 
                    <DataTable.TableBatchActions {...getBatchActionProps()}>
                      <DataTable.TableBatchAction
                        tabIndex={getBatchActionProps().shouldShowBatchActions ? 0 : -1}
                        renderIcon={MisuseOutline16}
                        onClick={disableAccessServices(selectedRows)}>
                        Disable
                      </DataTable.TableBatchAction>
                      <DataTable.TableBatchAction
                        tabIndex={getBatchActionProps().shouldShowBatchActions ? 0 : -1}
                        renderIcon={CheckmarkOutline16}
                        onClick={enableAccessServices(selectedRows)}>
                        Enable
                      </DataTable.TableBatchAction>
                    </DataTable.TableBatchActions>
                  }
                  <DataTable.TableToolbarContent>
                    <DataTable.TableToolbarSearch onChange={onInputChange} defaultExpanded />
                  </DataTable.TableToolbarContent>
                </DataTable.TableToolbar>
                <DataTable.Table {...getTableProps()}>
                  <DataTable.TableHead>
                    <DataTable.TableRow>
                      {editable && <DataTable.TableSelectAll {...getSelectionProps()} />}
                      <DataTable.TableExpandHeader />
                      {headers.map((header, i) => (
                        <DataTable.TableHeader key={`access-server-table-header-${i}`} {...getHeaderProps({ header })}>
                          {header.header}
                        </DataTable.TableHeader>
                      ))}
                    </DataTable.TableRow>
                  </DataTable.TableHead>
                  <DataTable.TableBody>
                    {rows.map((row) => (
                      <React.Fragment key={row.id}>
                        <DataTable.TableExpandRow {...getRowProps({ row })}>
                          {editable && <DataTable.TableSelectRow {...getSelectionProps({ row })} />}
                          {row.cells.map((cell) => {
                            if (cell.info.header === 'status') {
                              return (<DataTable.TableCell key={cell.id}>{cell.value ? 'Enabled' : 'Disabled'}</DataTable.TableCell>);
                            } else {
                              return (<DataTable.TableCell key={cell.id}>{cell.value}</DataTable.TableCell>);
                            }
                          })}
                        </DataTable.TableExpandRow>
                        <DataTable.TableExpandedRow
                          colSpan={headers.length + 2}
                          className="expanded-td"
                        >
                          {accessServicesRows.filter((v) => v.id === row.id).map((r) => (
                            <div key={`expanded-row-${r.id}`}>
                              <h6>{r.description}</h6>
                              <div>More information: <Link href={r.wiki}>{r.wiki}</Link></div>
                              <div>Access Service In Topic (Kafka): {r.inTopic}</div>
                              <div>Access Service Out Topic (Kafka): {r.outTopic}</div>
                            </div>
                          ))}
                        </DataTable.TableExpandedRow>
                      </React.Fragment>
                    ))}
                  </DataTable.TableBody>
                </DataTable.Table>
              </DataTable.TableContainer>
            )}
          </DataTable>
        </AccordionItem>}

        <AccordionItem title="Repository Services Configuration" className="customAccordionItem">
          {repoServices}
        </AccordionItem>

        <AccordionItem title="Audit Trail" className="customAccordionItem">
          <StructuredListWrapper style={{ marginBottom: '0' }}>
            <StructuredListHead>
              <StructuredListRow head>
                <StructuredListCell head>Time</StructuredListCell>
                <StructuredListCell head>Action</StructuredListCell>
              </StructuredListRow>
            </StructuredListHead>
            <StructuredListBody>
              {auditTrailBody}
            </StructuredListBody>
          </StructuredListWrapper>
        </AccordionItem>

        <AccordionItem title="Entire Configuration as JSON" className="customAccordionItem">
          <div style={{
            backgroundColor: "#f4f4f4",
            textAlign: "left",
            overflow: "scroll",
            height: "400px"
          }}>
            <JSONPretty id="config-preview" data={newServerConfig} space="4"></JSONPretty>
          </div>
        </AccordionItem>

      </Accordion>

    </div>

  )

}