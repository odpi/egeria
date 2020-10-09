/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext, useEffect, useRef, useState } from "react";
import {
  Accordion,
  AccordionItem,
  Button,
  // Checkbox,
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

export default function ConfigPreview({ config, options }) {

  if (!config || config == {} || config == '') {
    return null;
  }

  if (!options)
    options = {};

  const {
    accessServices,
    editable,
    allowDeployment,
    preventDeployment,
    setServerAttribute,
  } = options;

  const enableAccessServices = (selectedRows) => async () => {
    console.log('called enableAccessServices', { selectedRows });
    // Enable Access Services
    const accessServiceURLs = [];
    selectedRows.forEach((row) => {
      console.log({row});
      // TODO: Add comparison of new access services to enable vs. existing access services from config
      //       to rule out any unneccessary calls
      accessServiceURLs.push(`/open-metadata/admin-services/users/${userId}/servers/${newServerName}/access-services/${row.id}`);
    })
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
  }

  const disableAccessServices = (selectedRows) => async () => {
    // TODO: Add comparison of new access services to disable vs. existing access services from config
    //       to rule out any unneccessary calls
    //       Most likely will need to disable all access services, then re-enable the remaining services (dangerous operation)
    console.log('called disableAccessServices', { selectedRows });
  }

  // Editable Cells

  const makeCellEditable = (field, originalValue) => {
    console.log("called makeCellEditable", { field });
    eval(`set_${field}(editableCell(field, "${originalValue}", true, true))`);
  }

  const cancelChange = (field, originalValue) => {
    console.log("called cancelChange", { field });
    eval(`set_${field}(editableCell(field, "${originalValue}", true, false))`);
    allowDeployment();
  }

  const saveChange = async (field) => {
    console.log("called saveChange", { field });
    const value = document.getElementById(`${field}-input`).value;
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
    allowDeployment();
  }

  const handleCellChange = (field, originalValue, currentValue) => {
    console.log("called handleCellChange", { field, originalValue, currentValue });
    if (currentValue == originalValue) {
      console.log("value matches original");
      allowDeployment();
    } else {
      console.log("value has changed");
      preventDeployment();
    }
  }

  const editableCell = (field, originalValue, editable = false, editing = false) => {
    console.log("called editableCell", { field, editable, editing });
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
            onClick={() => makeCellEditable(field, originalValue)}
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
          onClick={() => cancelChange(field, originalValue)}
        />
      </span>
    )
  }

  const [organizationName, set_organizationName] = useState(editableCell("organizationName", config.organizationName, true));
  const [localServerUserId, set_localServerUserId] = useState(editableCell("localServerUserId", config.localServerUserId, true));
  const [localServerPassword, set_localServerPassword] = useState(editableCell("localServerPassword", config.localServerPassword, true));
  const [maxPageSize, set_maxPageSize] = useState(editableCell("maxPageSize", config.maxPageSize, true));

  // Access Services

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
  
  if (accessServices && accessServices.length) {
    
    accessServices.forEach((accessService) => {
      
      if (config.accessServicesConfig && config.accessServicesConfig.length) {

        // Check if service is listed in config
        const matchingService = config.accessServicesConfig.find((c) => {
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

      }

    });

  } else {

    if (config.accessServicesConfig && config.accessServicesConfig.length) {

      accessServicesRows = config.accessServicesConfig.map((c) => {
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
  if (config.repositoryServicesConfig) {
    let auditLogConnections = [];
    if (config.repositoryServicesConfig.auditLogConnections && config.repositoryServicesConfig.auditLogConnections.length) {
      auditLogConnections = config.repositoryServicesConfig.auditLogConnections.map((c) => {
        return (
          <StructuredListWrapper style={{ marginBottom: '16px', border: "1px solid #e0e0e0" }}>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Name</StructuredListCell>
              <StructuredListCell>{c.displayName}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Connector Type</StructuredListCell>
              <StructuredListCell>{c.connectorType.displayName}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Supported Severities</StructuredListCell>
              <StructuredListCell>
                {c.configurationProperties.supportedSeverities.map((sev, i) => (
                  <p key={i}>{sev}</p>
                ))}
              </StructuredListCell>
            </StructuredListRow>
          </StructuredListWrapper>
        )
      });
    }
    let enterpriseConnections = [];
    if (config.repositoryServicesConfig.enterpriseAccessConfig.enterpriseOMRSTopicConnection.embeddedConnections && config.repositoryServicesConfig.enterpriseAccessConfig.enterpriseOMRSTopicConnection.embeddedConnections.length) {
      enterpriseConnections = config.repositoryServicesConfig.enterpriseAccessConfig.enterpriseOMRSTopicConnection.embeddedConnections.map((c) => {
        return (
          <StructuredListWrapper style={{ marginBottom: '16px', border: "1px solid #e0e0e0" }}>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Name</StructuredListCell>
              <StructuredListCell>{c.displayName}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Connector Type</StructuredListCell>
              <StructuredListCell>
                {c.embeddedConnection.connectorType.displayName}
                <Tooltip direction="right">
                  <p>{c.embeddedConnection.connectorType.description}</p>
                </Tooltip>
              </StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Endpoint</StructuredListCell>
              <StructuredListCell>{c.embeddedConnection.endpoint.address}</StructuredListCell>
            </StructuredListRow>
          </StructuredListWrapper>
        )
      })
    }
    repoServices = <div>
      <h6 style={{ padding: '1rem', color: '#0f62fe', border: "1px solid #e0e0e0" }}>Audit Log Connections</h6>
      {auditLogConnections.map((alc) => (
        alc
      ))}
      <h6 style={{ padding: '1rem', color: '#0f62fe', border: "1px solid #e0e0e0" }}>Local Repository Configuration</h6>
      <StructuredListWrapper style={{ marginBottom: '16px', border: "1px solid #e0e0e0" }}>
        <StructuredListRow>
          <StructuredListCell head={true} className="customStructuredListHeader">Local Repository Local Connection</StructuredListCell>
          <StructuredListCell>
            {config.repositoryServicesConfig.localRepositoryConfig.localRepositoryLocalConnection.connectorType.displayName}
            <Tooltip direction="right">
              <p>{config.repositoryServicesConfig.localRepositoryConfig.localRepositoryLocalConnection.connectorType.description}</p>
            </Tooltip>
          </StructuredListCell>
        </StructuredListRow>
        <StructuredListRow>
          <StructuredListCell head={true} className="customStructuredListHeader">Local Repository Remote Connection</StructuredListCell>
          <StructuredListCell>
            {config.repositoryServicesConfig.localRepositoryConfig.localRepositoryRemoteConnection.connectorType.displayName}
            <Tooltip direction="right">
              <p>{config.repositoryServicesConfig.localRepositoryConfig.localRepositoryRemoteConnection.connectorType.description}</p>
            </Tooltip>
          </StructuredListCell>
        </StructuredListRow>
        <StructuredListRow>
          <StructuredListCell head={true} className="customStructuredListHeader">Events to Save</StructuredListCell>
          <StructuredListCell>{config.repositoryServicesConfig.localRepositoryConfig.eventsToSaveRule}</StructuredListCell>
        </StructuredListRow>
        <StructuredListRow>
          <StructuredListCell head={true} className="customStructuredListHeader">Events to Send</StructuredListCell>
          <StructuredListCell>{config.repositoryServicesConfig.localRepositoryConfig.eventsToSendRule}</StructuredListCell>
        </StructuredListRow>
      </StructuredListWrapper>
      <h6 style={{ padding: '1rem', color: '#0f62fe', border: "1px solid #e0e0e0" }}>Enterprise Access Configuration</h6>
      {enterpriseConnections.map((ec) => (
        ec
      ))}
    </div>
  }

  // Audit Trail

  let auditTrailBody;
  if (config.auditTrail) {
    if (config.auditTrail.length) {
      auditTrailBody = config.auditTrail.map((t) => {
        return (
          <StructuredListRow>
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
        Configuration Version: {config.versionId}
      </p>

      <Accordion>
        
        <AccordionItem open title="Basic Configuration" className="customAccordionItem">
          <StructuredListWrapper style={{ marginBottom: '0' }}>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server ID</StructuredListCell>
              <StructuredListCell>{config.localServerId}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server Name</StructuredListCell>
              <StructuredListCell>{config.localServerName}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server Type</StructuredListCell>
              {/* <StructuredListCell>{config.localServerType}</StructuredListCell> */}
              <StructuredListCell>{config.localServerType}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Organization Name</StructuredListCell>
              {/* <StructuredListCell>{config.organizationName}</StructuredListCell> */}
              <StructuredListCell id="organizationName">{organizationName}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server URL</StructuredListCell>
              <StructuredListCell>{config.localServerURL}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server User ID</StructuredListCell>
              {/* <StructuredListCell>{config.localServerUserId}</StructuredListCell> */}
              <StructuredListCell id="localServerUserId">{localServerUserId}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Local Server Password</StructuredListCell>
              {/* <StructuredListCell>{config.localServerPassword}</StructuredListCell> */}
              <StructuredListCell id="localServerPassword">{localServerPassword}</StructuredListCell>
            </StructuredListRow>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Max Page Size</StructuredListCell>
              {/* <StructuredListCell>{config.maxPageSize}</StructuredListCell> */}
              <StructuredListCell id="maxPageSize">{maxPageSize}</StructuredListCell>
            </StructuredListRow>
          </StructuredListWrapper>
        </AccordionItem>
        
        <AccordionItem title="Event Bus Configuration" className="customAccordionItem">
          <StructuredListWrapper style={{ marginBottom: '0' }}>
            <StructuredListRow>
              <StructuredListCell head={true} className="customStructuredListHeader">Topic URL Root</StructuredListCell>
              <StructuredListCell>{config.eventBusConfig ? config.eventBusConfig.topicURLRoot : 'n/a'}</StructuredListCell>
            </StructuredListRow>
          </StructuredListWrapper>
        </AccordionItem>
        
        <AccordionItem title="Access Services Configuration" className="customAccordionItem">
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
                        <DataTable.TableHeader key={i} {...getHeaderProps({ header })}>
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
                            <div>
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
        </AccordionItem>

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
            <JSONPretty id="config-preview" data={config} space="4"></JSONPretty>
          </div>
        </AccordionItem>

      </Accordion>

    </div>

  )

}