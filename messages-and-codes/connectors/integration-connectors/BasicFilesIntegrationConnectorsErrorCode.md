<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# BasicFilesIntegrationConnectorsErrorCode

The BasicFilesIntegrationConnectorsErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Basic File Connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 7 |
| **Message identifiers begin** | `BASIC-FILES-INTEGRATION-CONNECTORS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/files-integration-connectors](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/files-integration-connectors) |
| **Source** | [BasicFilesIntegrationConnectorsErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/files-integration-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/basicfiles/ffdc/BasicFilesIntegrationConnectorsErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/integration-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [BASIC-FILES-INTEGRATION-CONNECTORS-400-002](#basic-files-integration-connectors-400-002) | 400 | The file location {0} is not a directory |
| [BASIC-FILES-INTEGRATION-CONNECTORS-400-003](#basic-files-integration-connectors-400-003) | 400 | The directory (folder) {0} is not readable |
| [BASIC-FILES-INTEGRATION-CONNECTORS-400-004](#basic-files-integration-connectors-400-004) | 400 | An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the FileFolder asset for directory {3} (absolute path {4}).  The error message was {5} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-400-005](#basic-files-integration-connectors-400-005) | 400 | An unexpected {0} exception was returned to the {1} integration connector when it tried to update the DataFile in the metadata repositories for file {2}.  The error message was {3} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-500-003](#basic-files-integration-connectors-500-003) | 500 | An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the FileFolder asset {3}.  The error message was {4} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-500-004](#basic-files-integration-connectors-500-004) | 500 | An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the catalog targets for connector {3}.  The error message was {4} |
| [BASIC-FILES-INTEGRATION-CONNECTORS-500-005](#basic-files-integration-connectors-500-005) | 500 | The connector {0} received an unexpected {1} exception when processing the file named {2} in method {3}; the error message was: {4} |

----

### BASIC-FILES-INTEGRATION-CONNECTORS-400-002

> The file location {0} is not a directory

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_DIRECTORY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot work with this location since it is not a directory (folder).

**User action**

Ensure a valid directory name is passed in the address property in the Endpoint object of the Connection object.  This connection object is part of he Files Integration integration service configuration which is part of the configuration of the Integration Daemon OMAG server where this connector is running.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-400-003

> The directory (folder) {0} is not readable

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_READABLE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot open the file because it does not have sufficient permission.

**User action**

Ensure the name of a readable file is passed in the address property in the Endpoint object of the Connection object.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-400-004

> An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the FileFolder asset for directory {3} (absolute path {4}).  The error message was {5}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXC_RETRIEVING_FOLDER_BY_PATH_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.

**User action**

Use the message in the nested exception to determine the root cause of the error. Once this is resolved, follow the instructions in the messages produced by the integration daemon to restart the connector.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-400-005

> An unexpected {0} exception was returned to the {1} integration connector when it tried to update the DataFile in the metadata repositories for file {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXC_DATA_FILE_UPDATE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The exception is logged and the integration connector continues to synchronize metadata.  This file is not catalogued at this time but may succeed later.

**User action**

Use the message in the unexpected exception to determine the root cause of the error and fix it.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-500-003

> An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the FileFolder asset {3}.  The error message was {4}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXC_RETRIEVING_FOLDER_BY_GUID` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling since this is likely to be a logic error.

**User action**

Use the message in the nested exception to determine the root cause of the error. Report the situation to the Egeria community.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-500-004

> An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the catalog targets for connector {3}.  The error message was {4}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXC_RETRIEVING_CATALOG_TARGETS` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling since this is likely to be a set up error. This exception is not expected if there are no catalog targets.

**User action**

Use the message in the nested exception to determine the root cause of the error. Fix the configuration error and restart the connector.


----

### BASIC-FILES-INTEGRATION-CONNECTORS-500-005

> The connector {0} received an unexpected {1} exception when processing the file named {2} in method {3}; the error message was: {4}

|  |  |
|---|---|
| **Java constant** | `BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector cannot process the file.  The associated catalog entry may be out of date

**User action**

Use the details from the error message to determine the cause of the error and fix it. Retry the connector once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
