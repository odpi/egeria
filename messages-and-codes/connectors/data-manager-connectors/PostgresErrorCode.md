<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# PostgresErrorCode

The PostgresErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Kafka monitor integration connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `POSTGRES-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors) |
| **Source** | [PostgresErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/postgres/ffdc/PostgresErrorCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-postgres/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [POSTGRES-CONNECTOR-400-002](#postgres-connector-400-002) | 400 | Connection {0} has been configured without the embedded JDBC database connection |
| [POSTGRES-CONNECTOR-500-001](#postgres-connector-500-001) | 500 | The {0} postgreSQL connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### POSTGRES-CONNECTOR-400-002

> Connection {0} has been configured without the embedded JDBC database connection

|  |  |
|---|---|
| **Java constant** | `PostgresErrorCode.NO_DATABASE_CONNECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot start because it does not have a connector to the database.

**User action**

Update the connection to include the embedded connection needed to connect to the desired database.


----

### POSTGRES-CONNECTOR-500-001

> The {0} postgreSQL connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `PostgresErrorCode.UNEXPECTED_EXCEPTION` |
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
