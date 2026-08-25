<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# BasicFilesIntegrationConnectorsAuditCode

The BasicFilesIntegrationConnectorsAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 19 |
| **Message identifiers begin** | `BASIC-FILES-INTEGRATION-CONNECTORS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/files-integration-connectors](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/files-integration-connectors) |
| **Source** | [BasicFilesIntegrationConnectorsAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/files-integration-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/basicfiles/ffdc/BasicFilesIntegrationConnectorsAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/integration-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [BASIC-FILES-INTEGRATION-CONNECTORS-0001](#basic-files-integration-connectors-0001) | INFO | The {0} integration connector has been initialized with directoryToMonitor={1}, waitForDirectory={2}, toDoTemplateQualifiedName={3}, incidentReportTemplateQualifiedName={4}, fileSystemName={5}, localMountPoint={6}, canonicalMountPoint={7} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0002](#basic-files-integration-connectors-0002) | ERROR | The {0} integration connector encountered an {1} exception when opening directory {2} sourced from {3} during the {4} method.  The exception message included was {5} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0004](#basic-files-integration-connectors-0004) | ERROR | An unexpected {0} exception was returned to the {1} integration connector by the Integration context {2} method when trying to retrieve the FileFolder asset for directory {3} (absolute path {4}).  The error message was {5} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0009](#basic-files-integration-connectors-0009) | INFO | The {0} integration connector has stopped its file monitoring and is shutting down |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0011](#basic-files-integration-connectors-0011) | INFO | The {0} integration connector has updated the last updated time in the DataFolder {1} to {2} because of changes to file {3} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0012](#basic-files-integration-connectors-0012) | ERROR | An unexpected {0} exception was returned to the {1} integration connector when it tried to update the DataFolder {2} in the metadata repositories for directory {3}.  The error message was {4} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0013](#basic-files-integration-connectors-0013) | ERROR | The {0} integration connector retrieved an incomplete DataFile asset: {1} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0014](#basic-files-integration-connectors-0014) | ERROR | An unexpected {0} exception was returned to the {1} integration connector when it tried to update the DataFile in the metadata repositories for file {2}.  The error message was {3} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0016](#basic-files-integration-connectors-0016) | INFO | The {0} integration connector created the DataFile {1} ({2}) for a new real-world file |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0017](#basic-files-integration-connectors-0017) | INFO | The {0} integration connector created the DataFile {1} ({2}) for a new real-world file using template {3} ({4}) |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0018](#basic-files-integration-connectors-0018) | INFO | The {0} integration connector has updated the DataFile {1} ({2}) because the real-world file changed |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0019](#basic-files-integration-connectors-0019) | INFO | The {0} integration connector has deleted the DataFile {1} ({2}) because the real-world file is no longer stored in the directory |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0020](#basic-files-integration-connectors-0020) | INFO | The {0} integration connector has archived the DataFile {1} ({2}) because the real-world file is no longer stored in the directory |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0021](#basic-files-integration-connectors-0021) | ERROR | An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the FileFolder asset {3}.  The error message was {4} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0022](#basic-files-integration-connectors-0022) | ERROR | An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the catalog targets for connector {3}.  The error message was {4} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0023](#basic-files-integration-connectors-0023) | ERROR | The directory named {0} does not exist.  Connector {1} retrieved this path name from source {2} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0024](#basic-files-integration-connectors-0024) | EXCEPTION | The {0} connector received an unexpected {1} exception during method {2}; the error message was: {3} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0025](#basic-files-integration-connectors-0025) | SECURITY | The {0} connector detected an unknown list name {1} nested in list {2} in secrets collection {3} ({4}); the associated security list element is: {5} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-0026](#basic-files-integration-connectors-0026) | SECURITY | The {0} connector detected that catalog target {1} has a metadataSourceQualifiedName {2} that is either unknown, or not of type FileSystem.  This value should be providing details of the file system where the files are located. |

----

### BASIC-FILES-INTEGRATION-CONNECTORS-0001

> The {0} integration connector has been initialized with directoryToMonitor={1}, waitForDirectory={2}, toDoTemplateQualifiedName={3}, incidentReportTemplateQualifiedName={4}, fileSystemName={5}, localMountPoint={6}, canonicalMountPoint={7}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.CONNECTOR_CONFIGURATION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}` |

**System action**

The connector is designed to monitor changes to the content of directories (folders).  The directoryToMonitor is an initial directory to monitor that is supplied in the connector's endpoint.  It is optional, and can be supplemented with catalog targets associated with the connector.  By default, if any of the directories to monitor do not exist, the connector fails.  The waitForDirectory flag overrides this behaviour, so directories that do not exist are skipped.  If the toDoTemplateQualifiedName is set, the connector will create ToDos using the named template if there are any problems in cataloguing a file.  Similarly, if the incidentReportTemplateQualifiedName is set, the connector will create IncidentReports using the named template if there are any problems in cataloguing a file.

**User action**

No specific action is required.  This message is to confirm the configuration for the integration connector.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0002

> The {0} integration connector encountered an {1} exception when opening directory {2} sourced from {3} during the {4} method.  The exception message included was {5}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.BAD_CONFIGURATION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The exception is passed back to the integration daemon that is hosting this connector to enable it to perform error handling.  More messages are likely to follow describing the error handling that was performed.  These can help to determine how to recover from this error.

**User action**

This message contains the exception that was the original cause of the problem. Use the information from the exception stack trace to determine why the connector is not able to access the directory and resolve that issue.  Use the messages that where subsequently logged during the error handling to discover how to restart the connector in the integration daemon once the original cause of the error has been corrected.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0004

> An unexpected {0} exception was returned to the {1} integration connector by the Integration context {2} method when trying to retrieve the FileFolder asset for directory {3} (absolute path {4}).  The error message was {5}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_RETRIEVING_FOLDER_BY_PATH_NAME` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.

**User action**

Use the message in the nested exception to determine the root cause of the error. Once this is resolved, follow the instructions in the messages produced by the integration daemon to restart this connector.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0009

> The {0} integration connector has stopped its file monitoring and is shutting down

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.CONNECTOR_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The connector is disconnecting.

**User action**

No action is required unless there are errors that follow indicating that there were problems shutting down.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0011

> The {0} integration connector has updated the last updated time in the DataFolder {1} to {2} because of changes to file {3}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.DATA_FOLDER_UPDATED_FOR_FILE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector updated the DataFolder as part of its monitoring of the files in the file directory.

**User action**

No action is required.  This message is to record the reason why the DataFolder was updated.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0012

> An unexpected {0} exception was returned to the {1} integration connector when it tried to update the DataFolder {2} in the metadata repositories for directory {3}.  The error message was {4}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_FOLDER_UPDATE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The exception is logged and the integration connector continues to synchronize metadata.

**User action**

Use the message in the unexpected exception to determine the root cause of the error and restart the connector once it is resolved.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0013

> The {0} integration connector retrieved an incomplete DataFile asset: {1}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.BAD_FILE_ELEMENT` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The metadata element for the file that was retrieved from the open metadata repositories has missing information.  This is likely to be a logic error in the Open Integration Framework or Open Metadata Store.

**User action**

Look for errors in the audit logs for the integration daemon where the connector is running and the metadata access server where the Open Metadata Store is running.  Collect these diagnostics and ask the Egeria community for help to determine why the DataFile element is incomplete.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0014

> An unexpected {0} exception was returned to the {1} integration connector when it tried to update the DataFile in the metadata repositories for file {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_DATA_FILE_UPDATE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The exception is logged and the integration connector continues to synchronize metadata.  This file is not catalogued at this time but may succeed later.

**User action**

Use the message in the unexpected exception to determine the root cause of the error and fix it.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0016

> The {0} integration connector created the DataFile {1} ({2}) for a new real-world file

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_CREATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector created the DataFile as part of its monitoring of the files in the file directory.

**User action**

No action is required.  This message is to record the reason why the DataFolder was created.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0017

> The {0} integration connector created the DataFile {1} ({2}) for a new real-world file using template {3} ({4})

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_CREATED_FROM_TEMPLATE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector created the DataFile as part of its monitoring of the files in the file directory.  The template provides details of additional metadata that should also be attached to the new DataFile element.  It was specified in the templateQualifiedName configuration property of the connector.

**User action**

No action is required.  This message is to record the reason why the DataFile was created with the template.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0018

> The {0} integration connector has updated the DataFile {1} ({2}) because the real-world file changed

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_UPDATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector updated the DataFile as part of its monitoring of the files in the file directory.

**User action**

No action is required.  This message is to record the reason why the DataFile was updated.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0019

> The {0} integration connector has deleted the DataFile {1} ({2}) because the real-world file is no longer stored in the directory

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_DELETED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector removed the DataFile as part of its monitoring of the files in the file directory.

**User action**

No action is required.  This message is to record the reason why the DataFile was removed.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0020

> The {0} integration connector has archived the DataFile {1} ({2}) because the real-world file is no longer stored in the directory

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_ARCHIVED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector updated the DataFile to reflect that is is now just a placeholder for an asset that no longer exists.  Its presence is still needed in the metadata repository for lineage reporting.

**User action**

No action is required.  This message is to record the reason why the DataFile was archived.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0021

> An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the FileFolder asset {3}.  The error message was {4}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_RETRIEVING_FOLDER_BY_GUID` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling since this is likely to be a logic error.

**User action**

Use the message in the nested exception to determine the root cause of the error. Report the situation to the Egeria community.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0022

> An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the catalog targets for connector {3}.  The error message was {4}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_RETRIEVING_CATALOG_TARGETS` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling since this is likely to be a set up error. This exception is not expected if there are no catalog targets.

**User action**

Use the message in the nested exception to determine the root cause of the error. Fix the configuration error and restart the connector.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0023

> The directory named {0} does not exist.  Connector {1} retrieved this path name from source {2}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.FILES_LOCATION_NOT_FOUND` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector cannot locate the directory (folder) it has been asked to work with.

**User action**

Ensure that the path name of the folder matches the location of the directory that the connector is to monitor.  Correct it if necessary, otherwise, once the directory has been created, the connector will start monitoring it on the next refresh.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0024

> The {0} connector received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0025

> The {0} connector detected an unknown list name {1} nested in list {2} in secrets collection {3} ({4}); the associated security list element is: {5}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.UNKNOWN_LIST_NAME` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The connector skips the unknown name in the list and continues processing the list.

**User action**

Use the details from the error message to either remove, or define the named list.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-0026

> The {0} connector detected that catalog target {1} has a metadataSourceQualifiedName {2} that is either unknown, or not of type FileSystem.  This value should be providing details of the file system where the files are located.

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsAuditCode.BAD_METADATA_SOURCE` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector skips the unknown name continues processing.

**User action**

Update the catalog target properties so it is identifying a correct metadata source.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
