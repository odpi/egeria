<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# JDBCAuditCode

The JDBCAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 4 |
| **Message identifiers begin** | `JDBC-RESOURCE-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc.JDBCAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-store-connectors/jdbc-resource-connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/jdbc-resource-connector) |
| **Source** | [JDBCAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/jdbc-resource-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/resource/jdbc/ffdc/JDBCAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/digital-resource-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [JDBC-RESOURCE-CONNECTOR-0001](#jdbc-resource-connector-0001) | EXCEPTION | The JDBC resource connector for database {0} received an unexpected exception {1} during method {2}; the error message was: {3} |
| [JDBC-RESOURCE-CONNECTOR-0003](#jdbc-resource-connector-0003) | INFO | The JDBC resource connector for database {0} has received {1} results from query {2} |
| [JDBC-RESOURCE-CONNECTOR-0009](#jdbc-resource-connector-0009) | INFO | The JDBC resource connector for database {0} is closing all {1} connection(s) to database and is shutting down |
| [JDBC-RESOURCE-CONNECTOR-0010](#jdbc-resource-connector-0010) | INFO | The JDBC resource connector for database {0} has enabled TCP keepalive using driver property {1} |

----

### JDBC-RESOURCE-CONNECTOR-0001

> The JDBC resource connector for database {0} received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `JDBCAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### JDBC-RESOURCE-CONNECTOR-0003

> The JDBC resource connector for database {0} has received {1} results from query {2}

|  |  |
|---|---|
| **Java constant** | `JDBCAuditCode.UNEXPECTED_ROW_COUNT_FROM_DATABASE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector has detected that the row count on the SQL requests is incorrect.

**User action**

Check the code where this error occurred to determine if the connector code is wrong - or the caller.  Correct whichever has the problem.


----

### JDBC-RESOURCE-CONNECTOR-0009

> The JDBC resource connector for database {0} is closing all {1} connection(s) to database and is shutting down

|  |  |
|---|---|
| **Java constant** | `JDBCAuditCode.CONNECTOR_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector has been requested to disconnect from the database and is ensuring all connections are closed.  This message is output by each data source that was created by the connector.  Therefore the number of times that this message is emitted indicates the number of data sources were created by the connector.

**User action**

No action is required unless there are errors that follow indicating that there were problems shutting down.


----

### JDBC-RESOURCE-CONNECTOR-0010

> The JDBC resource connector for database {0} has enabled TCP keepalive using driver property {1}

|  |  |
|---|---|
| **Java constant** | `JDBCAuditCode.CONNECTION_KEEPALIVE_ENABLED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector has switched on socket level keepalive for the connections in its pool.  This stops the pool from filling up with connections whose network peer has disappeared silently, which would otherwise drain the pool to zero without it recovering.

**User action**

No action is required.  If the database is reached through a firewall or load balancer that drops idle connections, check that its idle timeout is longer than the keepalive interval configured in the operating system.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
