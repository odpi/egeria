<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# BasicFileConnectorErrorCode

The BasicFileConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Basic File Connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 10 |
| **Message identifiers begin** | `BASIC-FILE-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.BasicFileConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/basic-file-connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/basic-file-connector) |
| **Source** | [BasicFileConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/basic-file-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/datastore/basicfile/ffdc/BasicFileConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/digital-resource-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [BASIC-FILE-CONNECTOR-400-001](#basic-file-connector-400-001) | 400 | The file name is null in the Connection object {0} |
| [BASIC-FILE-CONNECTOR-400-002](#basic-file-connector-400-002) | 400 | The file {0} given in Connection object {1} is a directory |
| [BASIC-FILE-CONNECTOR-400-003](#basic-file-connector-400-003) | 400 | The file {0} given in Connection object {1} is not readable |
| [BASIC-FILE-CONNECTOR-400-005](#basic-file-connector-400-005) | 400 | The folder name is null in the Connection object {0} |
| [BASIC-FILE-CONNECTOR-400-006](#basic-file-connector-400-006) | 400 | The folder {0} given in Connection object {1} is a file |
| [BASIC-FILE-CONNECTOR-400-007](#basic-file-connector-400-007) | 400 | The folder {0} given in Connection object {1} is not readable |
| [BASIC-FILE-CONNECTOR-404-001](#basic-file-connector-404-001) | 404 | The folder named {0} in the Connection object {1} does not exist |
| [BASIC-FILE-CONNECTOR-404-002](#basic-file-connector-404-002) | 404 | The file named {0} in the Connection object {1} does not exist |
| [BASIC-FILE-CONNECTOR-500-001](#basic-file-connector-500-001) | 500 | The connector received an unexpected security exception when reading the file named {0}; the error message was: {1} |
| [BASIC-FILE-CONNECTOR-500-002](#basic-file-connector-500-002) | 500 | The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1} |

----

### BASIC-FILE-CONNECTOR-400-001

> The file name is null in the Connection object {0}

|  |  |
|---|---|
| **Java constant** | `BasicFileConnectorErrorCode.FILE_NOT_SPECIFIED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot open the file because the name of the file is not passed in the Connection object.

**User action**

The name of the file should be set up in the address property of the connection's Endpoint object.


----

### BASIC-FILE-CONNECTOR-400-002

> The file {0} given in Connection object {1} is a directory

|  |  |
|---|---|
| **Java constant** | `BasicFileConnectorErrorCode.DIRECTORY_SPECIFIED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot work with a directory.

**User action**

Ensure a valid file name is passed in the address property in the Endpoint object of the Connection object.


----

### BASIC-FILE-CONNECTOR-400-003

> The file {0} given in Connection object {1} is not readable

|  |  |
|---|---|
| **Java constant** | `BasicFileConnectorErrorCode.FILE_NOT_READABLE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot open the file because it does not have sufficient permission.

**User action**

Ensure the name of a readable file is passed in the address property in the Endpoint object of the Connection object.


----

### BASIC-FILE-CONNECTOR-400-005

> The folder name is null in the Connection object {0}

|  |  |
|---|---|
| **Java constant** | `BasicFileConnectorErrorCode.FOLDER_NOT_SPECIFIED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot open the folder because the name of the folder is not passed in the Connection object.

**User action**

The name of the folder should be set up in the address property of the connection's Endpoint object.


----

### BASIC-FILE-CONNECTOR-400-006

> The folder {0} given in Connection object {1} is a file

|  |  |
|---|---|
| **Java constant** | `BasicFileConnectorErrorCode.FILE_NOT_DIRECTORY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot work with a file.

**User action**

Ensure a valid folder name is passed in the address property in the Endpoint object of the Connection object.


----

### BASIC-FILE-CONNECTOR-400-007

> The folder {0} given in Connection object {1} is not readable

|  |  |
|---|---|
| **Java constant** | `BasicFileConnectorErrorCode.FOLDER_NOT_READABLE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot open the folder because it does not have permission to read the file.

**User action**

Ensure a readable folder name is passed in the address property in the Endpoint object of the Connection object.


----

### BASIC-FILE-CONNECTOR-404-001

> The folder named {0} in the Connection object {1} does not exist

|  |  |
|---|---|
| **Java constant** | `BasicFileConnectorErrorCode.FOLDER_NOT_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot open the folder because it does not exist.

**User action**

Add the name of an existing folder to the address property of the connection's Endpoint object.


----

### BASIC-FILE-CONNECTOR-404-002

> The file named {0} in the Connection object {1} does not exist

|  |  |
|---|---|
| **Java constant** | `BasicFileConnectorErrorCode.FILE_NOT_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot locate the file it has been asked to work with.

**User action**

Ensure that the name of the file in the address property of the connection's Endpoint object matches the location of the file that the connector is to access.


----

### BASIC-FILE-CONNECTOR-500-001

> The connector received an unexpected security exception when reading the file named {0}; the error message was: {1}

|  |  |
|---|---|
| **Java constant** | `BasicFileConnectorErrorCode.UNEXPECTED_SECURITY_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot access the file.

**User action**

Use details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### BASIC-FILE-CONNECTOR-500-002

> The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1}

|  |  |
|---|---|
| **Java constant** | `BasicFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot process the file.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
