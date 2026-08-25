<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# PostgresAuditCode

The PostgresAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 6 |
| **Message identifiers begin** | `POSTGRES-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors) |
| **Source** | [PostgresAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/postgres/ffdc/PostgresAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-postgres/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [POSTGRES-CONNECTOR-0001](#postgres-connector-0001) | EXCEPTION | The PostgreSQL connector {0} received an unexpected {1} exception during method {2}; the error message was: {3} |
| [POSTGRES-CONNECTOR-0002](#postgres-connector-0002) | INFO | The {0} survey action service cannot retrieve details of any databases for PostgreSQL Database Server {1} ({2}) |
| [POSTGRES-CONNECTOR-0003](#postgres-connector-0003) | INFO | The {0} integration connector has catalogued PostgreSQL Database {1} ({2}) |
| [POSTGRES-CONNECTOR-0004](#postgres-connector-0004) | INFO | The {0} integration connector is skipping PostgreSQL Database {1} ({2}) because it is already catalogued |
| [POSTGRES-CONNECTOR-0007](#postgres-connector-0007) | INFO | The {0} PostgreSQL Server Connector has been supplied with a friendship connector with GUID {1} |
| [POSTGRES-CONNECTOR-0009](#postgres-connector-0009) | INFO | The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to PostgreSQL Database Asset {3} for Database {4} |

----

### POSTGRES-CONNECTOR-0001

> The PostgreSQL connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### POSTGRES-CONNECTOR-0002

> The {0} survey action service cannot retrieve details of any databases for PostgreSQL Database Server {1} ({2})

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.NO_DATABASES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The survey terminates.

**User action**

This may not be an error if there are no user database on the database server.  If there are, check the permissions associated with the database userId.


----

### POSTGRES-CONNECTOR-0003

> The {0} integration connector has catalogued PostgreSQL Database {1} ({2})

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.CATALOGED_DATABASE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector looks for another database.

**User action**

This is an information message showing that the integration connector has found a new database.


----

### POSTGRES-CONNECTOR-0004

> The {0} integration connector is skipping PostgreSQL Database {1} ({2}) because it is already catalogued

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.SKIPPING_DATABASE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector continues, looking for another database.

**User action**

This is an information message showing that the integration connector is working, but does not need to do any processing on this database.


----

### POSTGRES-CONNECTOR-0007

> The {0} PostgreSQL Server Connector has been supplied with a friendship connector with GUID {1}

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.FRIENDSHIP_GUID` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The friendship connector is an integration connector that is able to catalog a JDBC database.  Therefore, they will cooperate to synchronize the contents of the PostgreSQL Server with the open metadata ecosystem.

**User action**

No action is required, this message is just to acknowledge that that the two integration connectors are going to collaborate to catalog the entire contents of the PostgreSQL Server.


----

### POSTGRES-CONNECTOR-0009

> The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to PostgreSQL Database Asset {3} for Database {4}

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.NEW_CATALOG_TARGET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector has requested that its friendship connector starts to catalog a new PostgreSQL Database.

**User action**

Verify that the cataloguing starts the next time that the friendship connector refreshes.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
