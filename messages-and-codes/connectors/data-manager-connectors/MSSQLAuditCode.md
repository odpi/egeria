<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# MSSQLAuditCode

The MSSQLAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 6 |
| **Message identifiers begin** | `MSSQL-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.mssql.ffdc.MSSQLAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-manager-connectors/mssql-server-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/mssql-server-connectors) |
| **Source** | [MSSQLAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/mssql-server-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/mssql/ffdc/MSSQLAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [MSSQL-CONNECTOR-0001](#mssql-connector-0001) | EXCEPTION | The Microsoft SQL Server connector {0} received an unexpected {1} exception during method {2}; the error message was: {3} |
| [MSSQL-CONNECTOR-0002](#mssql-connector-0002) | INFO | The {0} survey action service cannot retrieve details of any databases for Microsoft SQL Server {1} ({2}) |
| [MSSQL-CONNECTOR-0003](#mssql-connector-0003) | INFO | The {0} integration connector has catalogued Microsoft SQL Server Database {1} ({2}) |
| [MSSQL-CONNECTOR-0004](#mssql-connector-0004) | INFO | The {0} integration connector is skipping Microsoft SQL Server Database {1} ({2}) because it is already catalogued |
| [MSSQL-CONNECTOR-0007](#mssql-connector-0007) | INFO | The {0} Microsoft SQL Server Connector has been supplied with a friendship connector with GUID {1} |
| [MSSQL-CONNECTOR-0009](#mssql-connector-0009) | INFO | The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Microsoft SQL Server Database Asset {3} for Database {4} |

----

### MSSQL-CONNECTOR-0001

> The Microsoft SQL Server connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `MSSQLAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### MSSQL-CONNECTOR-0002

> The {0} survey action service cannot retrieve details of any databases for Microsoft SQL Server {1} ({2})

|  |  |
|---|---|
| **Java constant** | `MSSQLAuditCode.NO_DATABASES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The survey terminates.

**User action**

This may not be an error if there are no user database on the database server.  If there are, check the permissions associated with the database userId.


----

### MSSQL-CONNECTOR-0003

> The {0} integration connector has catalogued Microsoft SQL Server Database {1} ({2})

|  |  |
|---|---|
| **Java constant** | `MSSQLAuditCode.CATALOGED_DATABASE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector looks for another database.

**User action**

This is an information message showing that the integration connector has found a new database.


----

### MSSQL-CONNECTOR-0004

> The {0} integration connector is skipping Microsoft SQL Server Database {1} ({2}) because it is already catalogued

|  |  |
|---|---|
| **Java constant** | `MSSQLAuditCode.SKIPPING_DATABASE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector continues, looking for another database.

**User action**

This is an information message showing that the integration connector is working, but does not need to do any processing on this database.


----

### MSSQL-CONNECTOR-0007

> The {0} Microsoft SQL Server Connector has been supplied with a friendship connector with GUID {1}

|  |  |
|---|---|
| **Java constant** | `MSSQLAuditCode.FRIENDSHIP_GUID` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The friendship connector is an integration connector that is able to catalog a JDBC database.  Therefore, they will cooperate to synchronize the contents of the Microsoft SQL Server with the open metadata ecosystem.

**User action**

No action is required, this message is just to acknowledge that that the two integration connectors are going to collaborate to catalog the entire contents of the Microsoft SQL Server.


----

### MSSQL-CONNECTOR-0009

> The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Microsoft SQL Server Database Asset {3} for Database {4}

|  |  |
|---|---|
| **Java constant** | `MSSQLAuditCode.NEW_CATALOG_TARGET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector has requested that its friendship connector starts to catalog a new Microsoft SQL Server Database.

**User action**

Verify that the cataloguing starts the next time that the friendship connector refreshes.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
