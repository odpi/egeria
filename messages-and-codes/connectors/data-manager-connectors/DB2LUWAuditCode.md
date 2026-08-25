<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# DB2LUWAuditCode

The DB2LUWAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 6 |
| **Message identifiers begin** | `DB2LUW-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.db2luw.ffdc.DB2LUWAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-manager-connectors/db2luw-server-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/db2luw-server-connectors) |
| **Source** | [DB2LUWAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/db2luw-server-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/db2luw/ffdc/DB2LUWAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-db2luw/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [DB2LUW-CONNECTOR-0001](#db2luw-connector-0001) | EXCEPTION | The Db2 for Linux, UNIX and Windows connector {0} received an unexpected {1} exception during method {2}; the error message was: {3} |
| [DB2LUW-CONNECTOR-0002](#db2luw-connector-0002) | INFO | The {0} survey action service cannot retrieve details of any databases for Db2 for Linux, UNIX and Windows Server {1} ({2}) |
| [DB2LUW-CONNECTOR-0003](#db2luw-connector-0003) | INFO | The {0} integration connector has catalogued Db2 for Linux, UNIX and Windows Database {1} ({2}) |
| [DB2LUW-CONNECTOR-0004](#db2luw-connector-0004) | INFO | The {0} integration connector is skipping Db2 for Linux, UNIX and Windows Database {1} ({2}) because it is already catalogued |
| [DB2LUW-CONNECTOR-0007](#db2luw-connector-0007) | INFO | The {0} Db2 for Linux, UNIX and Windows Connector has been supplied with a friendship connector with GUID {1} |
| [DB2LUW-CONNECTOR-0009](#db2luw-connector-0009) | INFO | The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Db2 for Linux, UNIX and Windows Database Asset {3} for Database {4} |

----

### DB2LUW-CONNECTOR-0001

> The Db2 for Linux, UNIX and Windows connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `DB2LUWAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### DB2LUW-CONNECTOR-0002

> The {0} survey action service cannot retrieve details of any databases for Db2 for Linux, UNIX and Windows Server {1} ({2})

|  |  |
|---|---|
| **Java constant** | `DB2LUWAuditCode.NO_DATABASES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The survey terminates.

**User action**

This may not be an error if there are no user databases catalogued for the database server.  If there are, check the includeDatabaseList configuration property and the permissions associated with the database userId.


----

### DB2LUW-CONNECTOR-0003

> The {0} integration connector has catalogued Db2 for Linux, UNIX and Windows Database {1} ({2})

|  |  |
|---|---|
| **Java constant** | `DB2LUWAuditCode.CATALOGED_DATABASE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector looks for another database.

**User action**

This is an information message showing that the integration connector has found a new database.


----

### DB2LUW-CONNECTOR-0004

> The {0} integration connector is skipping Db2 for Linux, UNIX and Windows Database {1} ({2}) because it is already catalogued

|  |  |
|---|---|
| **Java constant** | `DB2LUWAuditCode.SKIPPING_DATABASE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector continues, looking for another database.

**User action**

This is an information message showing that the integration connector is working, but does not need to do any processing on this database.


----

### DB2LUW-CONNECTOR-0007

> The {0} Db2 for Linux, UNIX and Windows Connector has been supplied with a friendship connector with GUID {1}

|  |  |
|---|---|
| **Java constant** | `DB2LUWAuditCode.FRIENDSHIP_GUID` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The friendship connector is an integration connector that is able to catalog a JDBC database.  Therefore, they will cooperate to synchronize the contents of the Db2 for Linux, UNIX and Windows Server with the open metadata ecosystem.

**User action**

No action is required, this message is just to acknowledge that that the two integration connectors are going to collaborate to catalog the entire contents of the Db2 for Linux, UNIX and Windows Server.


----

### DB2LUW-CONNECTOR-0009

> The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Db2 for Linux, UNIX and Windows Database Asset {3} for Database {4}

|  |  |
|---|---|
| **Java constant** | `DB2LUWAuditCode.NEW_CATALOG_TARGET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector has requested that its friendship connector starts to catalog a new Db2 for Linux, UNIX and Windows Database.

**User action**

Verify that the cataloguing starts the next time that the friendship connector refreshes.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
