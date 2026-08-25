<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# DuckDBAuditCode

The DuckDBAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 14 |
| **Message identifiers begin** | `DUCKDB-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.duckdb.ffdc.DuckDBAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-manager-connectors/duckdb-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/duckdb-connectors) |
| **Source** | [DuckDBAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/duckdb-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/duckdb/ffdc/DuckDBAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [DUCKDB-CONNECTOR-0001](#duckdb-connector-0001) | EXCEPTION | The DuckDB connector {0} received an unexpected {1} exception during method {2}; the error message was: {3} |
| [DUCKDB-CONNECTOR-0002](#duckdb-connector-0002) | INFO | The {0} integration connector has catalogued DuckDB Database {1} ({2}) |
| [DUCKDB-CONNECTOR-0003](#duckdb-connector-0003) | INFO | The {0} integration connector is skipping DuckDB Database {1} ({2}) because it is already catalogued |
| [DUCKDB-CONNECTOR-0004](#duckdb-connector-0004) | INFO | The {0} DuckDB Connector has been supplied with a friendship connector with GUID {1} |
| [DUCKDB-CONNECTOR-0005](#duckdb-connector-0005) | INFO | The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to DuckDB Database Asset {3} for Database {4} |
| [DUCKDB-CONNECTOR-0006](#duckdb-connector-0006) | INFO | The {0} connector discovered that DuckDB Database {1} has an attached source called {2} of type {3} |
| [DUCKDB-CONNECTOR-0007](#duckdb-connector-0007) | INFO | The {0} connector discovered that DuckDB Database {1} has a view called {2} that scans an external {3} resource at {4} |
| [DUCKDB-CONNECTOR-0008](#duckdb-connector-0008) | INFO | The {0} connector was unable to query DuckDB's {1} federation metadata for database {2}; the error message was: {3} |
| [DUCKDB-CONNECTOR-0009](#duckdb-connector-0009) | INFO | The {0} connector was unable to catalog the federation relationship for {1} discovered in DuckDB Database {2}; the error message was: {3} |
| [DUCKDB-CONNECTOR-0010](#duckdb-connector-0010) | ERROR | The {0} connector was unable to run attachStatements entry "{1}" for DuckDB Database {2}; the error message was: {3} |
| [DUCKDB-CONNECTOR-0011](#duckdb-connector-0011) | INFO | The {0} connector has catalogued the schema for external file source {1} ({2} columns) |
| [DUCKDB-CONNECTOR-0012](#duckdb-connector-0012) | INFO | The {0} connector could not find a DuckDB-side RelationalTable for view {1} (tried qualified names {2} and {3}); classification and lineage linking for this view will be attempted again on a subsequent refresh |
| [DUCKDB-CONNECTOR-0013](#duckdb-connector-0013) | INFO | The {0} connector has catalogued attached table {1} from data source {2} as a TabularDataSet ({3} columns) |
| [DUCKDB-CONNECTOR-0014](#duckdb-connector-0014) | ERROR | The {0} connector found {1} elements with qualified name {2} when only one was expected; the element is skipped for this refresh |

----

### DUCKDB-CONNECTOR-0001

> The DuckDB connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### DUCKDB-CONNECTOR-0002

> The {0} integration connector has catalogued DuckDB Database {1} ({2})

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.CATALOGED_DATABASE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector continues to catalog the contents of the DuckDB database.

**User action**

This is an information message showing that the integration connector has found a new DuckDB database.


----

### DUCKDB-CONNECTOR-0003

> The {0} integration connector is skipping DuckDB Database {1} ({2}) because it is already catalogued

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.SKIPPING_DATABASE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector continues, using the existing catalogued database.

**User action**

This is an information message showing that the integration connector is working, but does not need to create a new asset for this database.


----

### DUCKDB-CONNECTOR-0004

> The {0} DuckDB Connector has been supplied with a friendship connector with GUID {1}

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.FRIENDSHIP_GUID` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The friendship connector is an integration connector that is able to catalog a JDBC database.  Therefore, they will cooperate to synchronize the contents of the DuckDB database with the open metadata ecosystem.

**User action**

No action is required, this message is just to acknowledge that that the two integration connectors are going to collaborate to catalog the entire contents of the DuckDB database.


----

### DUCKDB-CONNECTOR-0005

> The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to DuckDB Database Asset {3} for Database {4}

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.NEW_CATALOG_TARGET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector has requested that its friendship connector starts to catalog the schemas, tables and columns of the DuckDB Database.

**User action**

Verify that the cataloguing starts the next time that the friendship connector refreshes.


----

### DUCKDB-CONNECTOR-0006

> The {0} connector discovered that DuckDB Database {1} has an attached source called {2} of type {3}

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.ATTACHED_SOURCE_FOUND` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector records this as a federation relationship for the DuckDB database.

**User action**

No specific action is required.  This is an information message describing a database that has been ATTACH-ed to the surveyed/catalogued DuckDB database.


----

### DUCKDB-CONNECTOR-0007

> The {0} connector discovered that DuckDB Database {1} has a view called {2} that scans an external {3} resource at {4}

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.EXTERNAL_FILE_SOURCE_FOUND` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector records this as a federation relationship for the DuckDB database.

**User action**

No specific action is required.  This is an information message describing an external file, or object-store, resource that is scanned by a view in the surveyed/catalogued DuckDB database.


----

### DUCKDB-CONNECTOR-0008

> The {0} connector was unable to query DuckDB's {1} federation metadata for database {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.FEDERATION_QUERY_FAILED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector skips federation discovery processing for this database and continues with the rest of its processing.

**User action**

This may not be an error - it is expected for a version of DuckDB that does not support this table function, or for a database that does not use DuckDB's federation capabilities.  If federation metadata is expected, verify that the DuckDB version in use supports it.


----

### DUCKDB-CONNECTOR-0009

> The {0} connector was unable to catalog the federation relationship for {1} discovered in DuckDB Database {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.FEDERATION_LINK_FAILED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector skips this particular federation relationship and continues cataloguing the remaining federation relationships and the rest of the DuckDB database.

**User action**

Review the error message to determine whether any configuration change is needed to catalog this federation relationship on a subsequent refresh.


----

### DUCKDB-CONNECTOR-0010

> The {0} connector was unable to run attachStatements entry "{1}" for DuckDB Database {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.ATTACH_STATEMENT_FAILED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector skips this attach statement and continues to run any remaining attach statements, then continues with the rest of its processing.  Since this statement did not run, any federation relationship it was meant to (re)establish will not be found by federation discovery on this connection.

**User action**

Review the error message - typically this means the statement's syntax is invalid, a required DuckDB extension has not been installed, or the credentials/network address in the statement are no longer correct.  Correct the attachStatements configuration property and try again.


----

### DUCKDB-CONNECTOR-0011

> The {0} connector has catalogued the schema for external file source {1} ({2} columns)

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.FILE_SCHEMA_CATALOGED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector records the real column structure of the external file as a TabularSchemaType attached to the file asset, so that lineage can be traced back to it from the DuckDB view that scans it.

**User action**

No specific action is required.  This is an information message confirming that the schema of an externally-scanned file has been catalogued.


----

### DUCKDB-CONNECTOR-0012

> The {0} connector could not find a DuckDB-side RelationalTable for view {1} (tried qualified names {2} and {3}); classification and lineage linking for this view will be attempted again on a subsequent refresh

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.DUCKDB_TABLE_NOT_YET_CATALOGUED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector skips the CalculatedValue classification and DerivedSchemaTypeQueryTarget lineage linking for this view on this refresh, but the underlying file asset and its schema have still been catalogued.

**User action**

This is expected on an early refresh, before the friendship connector (the generic JDBC integration connector) has had a chance to catalog this DuckDB database's own tables and views.  No action is required unless this message persists across multiple refreshes.


----

### DUCKDB-CONNECTOR-0013

> The {0} connector has catalogued attached table {1} from data source {2} as a TabularDataSet ({3} columns)

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.ATTACHED_TABLE_CATALOGED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector records the table's structure as a TabularDataSet asset, linked to the target RelationalDatabase asset via DataSetContent, so that the table is separately queryable/governable from open metadata even though it is not catalogued by the friendship connector.

**User action**

No specific action is required.  This is an information message describing a table made available through a DuckDB ATTACH-ed network-backed data source.


----

### DUCKDB-CONNECTOR-0014

> The {0} connector found {1} elements with qualified name {2} when only one was expected; the element is skipped for this refresh

|  |  |
|---|---|
| **Java constant** | `DuckDBAuditCode.AMBIGUOUS_ELEMENT_FOUND` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector cannot safely determine which of the matching elements to use, so it skips creating or updating this element for the current refresh.

**User action**

This should not normally happen - qualified names are expected to be unique.  Investigate how the duplicate elements were created and consider removing the redundant one(s).


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
