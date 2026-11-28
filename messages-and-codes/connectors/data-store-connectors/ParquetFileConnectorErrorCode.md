<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ParquetFileConnectorErrorCode

The ParquetFileConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Parquet File Connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 8 |
| **Message identifiers begin** | `PARQUET-FILE-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.ffdc.ParquetFileConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/parquet-file-connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/parquet-file-connector) |
| **Source** | [ParquetFileConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/parquet-file-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/datastore/parquetfile/ffdc/ParquetFileConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/digital-resource-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [PARQUET-FILE-CONNECTOR-400-001](#parquet-file-connector-400-001) | 400 | File {0} is not an Apache Parquet file: {1} |
| [PARQUET-FILE-CONNECTOR-400-002](#parquet-file-connector-400-002) | 400 | The footer of Apache Parquet file {0} is encrypted |
| [PARQUET-FILE-CONNECTOR-400-003](#parquet-file-connector-400-003) | 400 | The footer of Apache Parquet file {0} is not valid: {1} |
| [PARQUET-FILE-CONNECTOR-400-004](#parquet-file-connector-400-004) | 400 | File {0} does not have a row {1} |
| [PARQUET-FILE-CONNECTOR-400-005](#parquet-file-connector-400-005) | 400 | The data in Apache Parquet file {0} cannot be read because the DuckDB JDBC driver is not available |
| [PARQUET-FILE-CONNECTOR-400-006](#parquet-file-connector-400-006) | 400 | Connection {0} has been configured without the embedded Parquet File Store connection |
| [PARQUET-FILE-CONNECTOR-500-001](#parquet-file-connector-500-001) | 500 | The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1} |
| [PARQUET-FILE-CONNECTOR-500-002](#parquet-file-connector-500-002) | 500 | The connector was not able to read the data in Apache Parquet file {0}; the error message from DuckDB was: {1} |

----

### PARQUET-FILE-CONNECTOR-400-001

> File {0} is not an Apache Parquet file: {1}

|  |  |
|---|---|
| **Java constant** | `ParquetFileConnectorErrorCode.NOT_PARQUET_FILE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is not able to describe the contents of the file because it does not have the layout of a Parquet file.

**User action**

Check that the file named in the connection's endpoint is the right one, and that it is complete.  If the file is in a different format, catalog it with the type of asset, and connector, that matches the format.


----

### PARQUET-FILE-CONNECTOR-400-002

> The footer of Apache Parquet file {0} is encrypted

|  |  |
|---|---|
| **Java constant** | `ParquetFileConnectorErrorCode.ENCRYPTED_FOOTER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector is not able to describe the contents of the file because the schema, row counts and statistics are all held in the footer and the connector does not support decryption.

**User action**

Use the technology that wrote the file, along with the appropriate encryption keys, to discover the contents of the file.


----

### PARQUET-FILE-CONNECTOR-400-003

> The footer of Apache Parquet file {0} is not valid: {1}

|  |  |
|---|---|
| **Java constant** | `ParquetFileConnectorErrorCode.INVALID_FOOTER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is not able to describe the contents of the file because the footer, which holds the schema, row counts and statistics, can not be decoded.

**User action**

The file is either corrupt or truncated.  Try reading the file with the technology that wrote it.  If that succeeds, raise an issue for the Egeria community, including details of the technology that wrote the file.


----

### PARQUET-FILE-CONNECTOR-400-004

> File {0} does not have a row {1}

|  |  |
|---|---|
| **Java constant** | `ParquetFileConnectorErrorCode.FILE_TOO_SHORT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot retrieve the requested row because the file has fewer rows than that.

**User action**

Ensure the row number requested is within the size of the file.  The first row is row 0 and method getRecordCount returns the number of rows in the file.


----

### PARQUET-FILE-CONNECTOR-400-005

> The data in Apache Parquet file {0} cannot be read because the DuckDB JDBC driver is not available

|  |  |
|---|---|
| **Java constant** | `ParquetFileConnectorErrorCode.DATA_ACCESS_NOT_AVAILABLE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector uses DuckDB to read the rows of data in a Parquet file.  The DuckDB JDBC driver is not on the class path and so the request fails.  The description of the file, which comes from its footer, is still available.

**User action**

Add the DuckDB JDBC driver (org.duckdb:duckdb\_jdbc) to the class path of the process running the connector.  It is included in the OMAG Server Platform.  Method isDataAccessAvailable can be used to test whether data can be read before requesting it.


----

### PARQUET-FILE-CONNECTOR-400-006

> Connection {0} has been configured without the embedded Parquet File Store connection

|  |  |
|---|---|
| **Java constant** | `ParquetFileConnectorErrorCode.NO_EMBEDDED_FILE_STORE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The tabular data set connector cannot start because it does not have the connector that reads the Parquet file.

**User action**

Update the connection so that it is a virtual connection with an embedded connection for the Parquet File Store connector.  The Parquet Tabular Data Set template in the core content pack creates a connection with this structure.


----

### PARQUET-FILE-CONNECTOR-500-001

> The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1}

|  |  |
|---|---|
| **Java constant** | `ParquetFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is not able to describe the contents of the file.

**User action**

Use details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### PARQUET-FILE-CONNECTOR-500-002

> The connector was not able to read the data in Apache Parquet file {0}; the error message from DuckDB was: {1}

|  |  |
|---|---|
| **Java constant** | `ParquetFileConnectorErrorCode.DATA_READ_FAILED` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector uses DuckDB to read the rows of data in a Parquet file and DuckDB reported an error.  The request fails.

**User action**

Check that the request names a column that is in the file, and that the file has not been changed or removed since its footer was read.  Otherwise, use the message from DuckDB to determine the cause of the error.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
