<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OracleAuditCode

The OracleAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 6 |
| **Message identifiers begin** | `ORACLE-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.oracle.ffdc.OracleAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-manager-connectors/oracle-server-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/oracle-server-connectors) |
| **Source** | [OracleAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/oracle-server-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/oracle/ffdc/OracleAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-oracle/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [ORACLE-CONNECTOR-0001](#oracle-connector-0001) | EXCEPTION | The Oracle Database connector {0} received an unexpected {1} exception during method {2}; the error message was: {3} |
| [ORACLE-CONNECTOR-0002](#oracle-connector-0002) | INFO | The {0} survey action service cannot retrieve details of any pluggable databases for Oracle Database Server {1} ({2}) |
| [ORACLE-CONNECTOR-0003](#oracle-connector-0003) | INFO | The {0} integration connector has catalogued Oracle Pluggable Database {1} ({2}) |
| [ORACLE-CONNECTOR-0004](#oracle-connector-0004) | INFO | The {0} integration connector is skipping Oracle Pluggable Database {1} ({2}) because it is already catalogued |
| [ORACLE-CONNECTOR-0007](#oracle-connector-0007) | INFO | The {0} Oracle Database Connector has been supplied with a friendship connector with GUID {1} |
| [ORACLE-CONNECTOR-0009](#oracle-connector-0009) | INFO | The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Oracle Pluggable Database Asset {3} for Database {4} |

----

### ORACLE-CONNECTOR-0001

> The Oracle Database connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `OracleAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### ORACLE-CONNECTOR-0002

> The {0} survey action service cannot retrieve details of any pluggable databases for Oracle Database Server {1} ({2})

|  |  |
|---|---|
| **Java constant** | `OracleAuditCode.NO_DATABASES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The survey terminates.

**User action**

This may not be an error if there are no user pluggable databases on the database server.  If there are, check the permissions associated with the database userId.


----

### ORACLE-CONNECTOR-0003

> The {0} integration connector has catalogued Oracle Pluggable Database {1} ({2})

|  |  |
|---|---|
| **Java constant** | `OracleAuditCode.CATALOGED_DATABASE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector looks for another pluggable database.

**User action**

This is an information message showing that the integration connector has found a new pluggable database.


----

### ORACLE-CONNECTOR-0004

> The {0} integration connector is skipping Oracle Pluggable Database {1} ({2}) because it is already catalogued

|  |  |
|---|---|
| **Java constant** | `OracleAuditCode.SKIPPING_DATABASE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector continues, looking for another pluggable database.

**User action**

This is an information message showing that the integration connector is working, but does not need to do any processing on this pluggable database.


----

### ORACLE-CONNECTOR-0007

> The {0} Oracle Database Connector has been supplied with a friendship connector with GUID {1}

|  |  |
|---|---|
| **Java constant** | `OracleAuditCode.FRIENDSHIP_GUID` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The friendship connector is an integration connector that is able to catalog a JDBC database.  Therefore, they will cooperate to synchronize the contents of the Oracle Database Server with the open metadata ecosystem.

**User action**

No action is required, this message is just to acknowledge that that the two integration connectors are going to collaborate to catalog the entire contents of the Oracle Database Server.


----

### ORACLE-CONNECTOR-0009

> The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Oracle Pluggable Database Asset {3} for Database {4}

|  |  |
|---|---|
| **Java constant** | `OracleAuditCode.NEW_CATALOG_TARGET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector has requested that its friendship connector starts to catalog a new Oracle Pluggable Database.

**User action**

Verify that the cataloguing starts the next time that the friendship connector refreshes.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
