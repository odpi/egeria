<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# CSVFileConnectorErrorCode

The CSVFileConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the CSV File Connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `CSV-FILE-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.datastore.csvfile.ffdc.CSVFileConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector) |
| **Source** | [CSVFileConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/datastore/csvfile/ffdc/CSVFileConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/digital-resource-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [CSV-FILE-CONNECTOR-400-004](#csv-file-connector-400-004) | 400 | File {0} does not have {1} rows |
| [CSV-FILE-CONNECTOR-500-001](#csv-file-connector-500-001) | 500 | The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1} |
| [CSV-FILE-CONNECTOR-500-002](#csv-file-connector-500-002) | 500 | The connector cannot change its column names because they are fixed in the connector's configuration |
| [CSV-FILE-CONNECTOR-500-003](#csv-file-connector-500-003) | 500 | Connection {0} has been configured without the embedded CSV File Store connection |
| [CSV-FILE-CONNECTOR-500-004](#csv-file-connector-500-004) | 500 | The {0} CSV File connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### CSV-FILE-CONNECTOR-400-004

> File {0} does not have {1} rows

|  |  |
|---|---|
| **Java constant** | `CSVFileConnectorErrorCode.FILE_TOO_SHORT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot retrieve the requested record because the file is too short.

**User action**

Ensure the record number requested is within the size of the file.  Method getRecordCount will provide information on the number of data records in the file


----

### CSV-FILE-CONNECTOR-500-001

> The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1}

|  |  |
|---|---|
| **Java constant** | `CSVFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot process the structure file.

**User action**

Use details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### CSV-FILE-CONNECTOR-500-002

> The connector cannot change its column names because they are fixed in the connector's configuration

|  |  |
|---|---|
| **Java constant** | `CSVFileConnectorErrorCode.FIXED_COLUMN_NAMES` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | none |

**System action**

The connector cannot process the new column descriptions.

**User action**

Remove the column names definition from the configuration properties to enable new column names to be specified.


----

### CSV-FILE-CONNECTOR-500-003

> Connection {0} has been configured without the embedded CSV File Store connection

|  |  |
|---|---|
| **Java constant** | `CSVFileConnectorErrorCode.NO_EMBEDDED_FILE_STORE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

The connector cannot start because it does not have the connector that manages the file.

**User action**

Update the connection to include the embedded connection needed to work with CSV files.


----

### CSV-FILE-CONNECTOR-500-004

> The {0} CSV File connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `CSVFileConnectorErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
