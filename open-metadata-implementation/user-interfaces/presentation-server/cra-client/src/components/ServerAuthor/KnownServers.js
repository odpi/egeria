/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";
import {
  Button,
  DataTable,
  OverflowMenu,
  OverflowMenuItem,
} from "carbon-components-react";
import {
  CheckmarkOutline16,
  MisuseOutline16,
} from "@carbon/icons-react";
import axios from "axios";
import { IdentificationContext } from "../../contexts/IdentificationContext";
import { ServerAuthorContext } from "../../contexts/ServerAuthorContext";

export default function KnownServers() {

  const { userId, serverName: tenantId } = useContext(IdentificationContext);
  const {
    knownServers, setKnownServers,
    setNotificationType,
    setNotificationTitle,
    setNotificationSubtitle,
    fetchKnownServers,
    showConfigForm,
  } = useContext(ServerAuthorContext);

  const startServers = (selectedRows) => async () => {
    console.log('called startServers', { selectedRows });
    // Start servers
    const serverURLs = [];
    selectedRows.forEach((row) => {
      serverURLs.push(`/open-metadata/admin-services/users/${userId}/servers/${row.id}/instance`);
    });
    for (const url of serverURLs) {
      try {
        const startServerResponse = await axios.post(url, {
          tenantId,
        }, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 30000,
        });
        if (startServerResponse.data.relatedHTTPCode != 200) {
          console.error(startServerResponse.data);
          throw new Error(startServerResponse.data.exceptionErrorMessage);
        }
      } catch(error) {
        console.error("Error starting server", { error });
        setNotificationType("error");
        if (error.code && error.code == "ECONNABORTED") {
          setNotificationTitle("Connection Error");
          setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
        } else {
          setNotificationTitle("Deployment Error");
          setNotificationSubtitle("Error starting server(s). " + error.message);
        }
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("notification-container").style.display = "block";
        return;
      }
    }
    // Refresh Server List
    const serverList = await fetchKnownServers();
    setKnownServers(serverList.map((v) => { return { id: v, serverName: v, status: "known" } }));
  }

  const stopServers = (selectedRows, del = false) => async () => {
    console.log('called stopServers', { selectedRows });
    // Stop servers
    const serverURLs = [];
    selectedRows.forEach((row) => {
      let url = `/open-metadata/admin-services/users/${userId}/servers/${row.id}`
      if (!del) {
        url += '/instance';
      }
      serverURLs.push(url);
    });
    for (const url of serverURLs) {
      try {
        const stopServerResponse = await axios.delete(url, {
          data: {
            tenantId,
          },
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 30000,
        });
        if (stopServerResponse.data.relatedHTTPCode != 200) {
          console.error("Error occurred, response:", stopServerResponse.data);
          throw new Error(stopServerResponse.data.exceptionErrorMessage);
        }
      } catch(error) {
        console.error("Error stopping server", { error });
        setNotificationType("error");
        if (error.code && error.code == "ECONNABORTED") {
          setNotificationTitle("Connection Error");
          setNotificationSubtitle("Error connecting to the platform. Please ensure the OMAG server platform is available.");
        } else {
          setNotificationTitle("Deployment Error");
          setNotificationSubtitle("Error stopping server(s). " + error.message);
        }
        document.getElementById("loading-container").style.display = "none";
        document.getElementById("notification-container").style.display = "block";
        return;
      }
    }
    // Refresh Server List
    const serverList = await fetchKnownServers();
    setKnownServers(serverList.map((v) => { return { id: v, serverName: v, status: "known" } }));
  }

  const headers = [
    {
      key: "serverName",
      header: "Server Name"
    },
    {
      key: "status",
      header: "Status"
    }
  ];

  return (

    <div style={{ textAlign: 'left' }}>

      <DataTable rows={knownServers} headers={headers} isSortable>
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
            title="Known OMAG Servers"
            description="List of all of the known OMAG servers"
            {...getTableContainerProps()}
          >
            <DataTable.TableToolbar {...getToolbarProps()}>
              <DataTable.TableBatchActions {...getBatchActionProps()}>
                <DataTable.TableBatchAction
                  tabIndex={getBatchActionProps().shouldShowBatchActions ? 0 : -1}
                  renderIcon={MisuseOutline16}
                  onClick={stopServers(selectedRows)}>
                  Shutdown
                </DataTable.TableBatchAction>
                <DataTable.TableBatchAction
                  tabIndex={getBatchActionProps().shouldShowBatchActions ? 0 : -1}
                  renderIcon={MisuseOutline16}
                  onClick={stopServers(selectedRows, true)}>
                  Shutdown {'&'} Delete
                </DataTable.TableBatchAction>
                <DataTable.TableBatchAction
                  tabIndex={getBatchActionProps().shouldShowBatchActions ? 0 : -1}
                  renderIcon={CheckmarkOutline16}
                  onClick={startServers(selectedRows)}>
                  Start
                </DataTable.TableBatchAction>
              </DataTable.TableBatchActions>
              <DataTable.TableToolbarContent>
                <DataTable.TableToolbarSearch id="known-server-search" onChange={onInputChange} />
              </DataTable.TableToolbarContent>
              <Button
                tabIndex={getBatchActionProps().shouldShowBatchActions ? -1 : 0}
                style={{display: getBatchActionProps().shouldShowBatchActions ? "none" : "inherit"}}
                onClick={showConfigForm}
                size="small"
                kind="primary">
                Create new
              </Button>
            </DataTable.TableToolbar>
            <DataTable.Table {...getTableProps()}>
              <DataTable.TableHead>
                <DataTable.TableRow>
                  <DataTable.TableSelectAll {...getSelectionProps()} />
                  {headers.map((header, i) => (
                    <DataTable.TableHeader key={`known-servers-table-header-${i}`} {...getHeaderProps({ header })}>
                      {header.header}
                    </DataTable.TableHeader>
                  ))}
                  <DataTable.TableHeader />
                </DataTable.TableRow>
              </DataTable.TableHead>
              <DataTable.TableBody>
                {rows.map((row, index) => (
                  <React.Fragment key={index}>
                    <DataTable.TableRow {...getRowProps({ row })}>
                      <DataTable.TableSelectRow {...getSelectionProps({ row })} />
                      {row.cells.map((cell) => {
                          return (<DataTable.TableCell key={cell.id}>{cell.value}</DataTable.TableCell>);
                      })}
                      <DataTable.TableCell className="bx--table-column-menu">
                        <OverflowMenu flipped>
                          <OverflowMenuItem
                            itemText="Start Server"
                            onClick={startServers([row])}
                          />
                          <OverflowMenuItem
                            itemText="Shutdown Server"
                            onClick={stopServers([row])}
                          />
                          <OverflowMenuItem
                            itemText={"Shutdown & Delete Server"}
                            onClick={stopServers([row], true)}
                            isDelete
                            requireTitle
                          />
                        </OverflowMenu>
                      </DataTable.TableCell>
                    </DataTable.TableRow>
                  </React.Fragment>
                ))}
              </DataTable.TableBody>
            </DataTable.Table>
          </DataTable.TableContainer>
        )}
      </DataTable>

    </div>

  )

}