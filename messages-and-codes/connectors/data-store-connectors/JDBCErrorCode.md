<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# JDBCErrorCode

The JDBCErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Kafka monitor integration connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 7 |
| **Message identifiers begin** | `JDBC-RESOURCE-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc.JDBCErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-store-connectors/jdbc-resource-connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/jdbc-resource-connector) |
| **Source** | [JDBCErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/jdbc-resource-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/resource/jdbc/ffdc/JDBCErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/digital-resource-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [JDBC-RESOURCE-CONNECTOR-400-001](#jdbc-resource-connector-400-001) | 400 | Connection {0} has been configured without the URL to the database |
| [JDBC-RESOURCE-CONNECTOR-400-002](#jdbc-resource-connector-400-002) | 400 | The JDBC resource connector for database {0} has been configured with an invalid DriverManager class name of {1} in its connection {2}: ClassNotFoundException message is {3} |
| [JDBC-RESOURCE-CONNECTOR-400-003](#jdbc-resource-connector-400-003) | 400 | Connection has been configured without the schema name of the database |
| [JDBC-RESOURCE-CONNECTOR-400-004](#jdbc-resource-connector-400-004) | 400 | The value supplied for column {0} contains a null (U+0000) character at position {1}; the value was being stored by method {2} in mapper {3} |
| [JDBC-RESOURCE-CONNECTOR-500-001](#jdbc-resource-connector-500-001) | 500 | The JDBC resource connector for database {0} received an unexpected exception {1} during method {2}; the error message was: {3} |
| [JDBC-RESOURCE-CONNECTOR-500-002](#jdbc-resource-connector-500-002) | 500 | The JDBC resource connector detected a missing value for column {0} during method {1} in mapper {2} |
| [JDBC-RESOURCE-CONNECTOR-500-003](#jdbc-resource-connector-500-003) | 500 | The JDBC resource connector for database {0} received an unexpected SQL exception from request "{1}" during method {2}; the error message was: {3} |

----

### JDBC-RESOURCE-CONNECTOR-400-001

> Connection {0} has been configured without the URL to the database

|  |  |
|---|---|
| **Java constant** | `JDBCErrorCode.NULL_URL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot start because the endpoint of its connection has a null address property.

**User action**

Update the connection's endpoint to include the connection string needed to connect to the desired database.


----

### JDBC-RESOURCE-CONNECTOR-400-002

> The JDBC resource connector for database {0} has been configured with an invalid DriverManager class name of {1} in its connection {2}: ClassNotFoundException message is {3}

|  |  |
|---|---|
| **Java constant** | `JDBCErrorCode.BAD_DRIVER_MANAGER_CLASS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector fails to start.

**User action**

Update the 'jdbcDriverManagerClassName' configuration property in this connector's connection.  This property is only needed for unusual databases.  It may also be worth trying the connector without this property to see if the driver is well known to your JDBC implementation.


----

### JDBC-RESOURCE-CONNECTOR-400-003

> Connection has been configured without the schema name of the database

|  |  |
|---|---|
| **Java constant** | `JDBCErrorCode.NULL_SCHEMA_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The connector cannot start because the configuration properties have a null databaseSchema property.

**User action**

Update the connection's configuration properties to include the schema name needed to connect to the desired database schema.


----

### JDBC-RESOURCE-CONNECTOR-400-004

> The value supplied for column {0} contains a null (U+0000) character at position {1}; the value was being stored by method {2} in mapper {3}

|  |  |
|---|---|
| **Java constant** | `JDBCErrorCode.NULL_CHARACTER_IN_VALUE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector rejects the request rather than attempting to store the value.  A text column cannot hold a null character - PostgreSQL, for example, refuses the whole statement with "null character not permitted" - so the request would fail in the database anyway, with an error that says nothing about which property was at fault.

**User action**

Remove the null character from the offending property value and retry the request.  A null character in a name, description or other text property is almost always a symptom of a fault further upstream - a C-style null-terminated string copied byte-for-byte, a fixed-width field padded with zero bytes, or binary content mislabelled as text - so it is worth correcting whatever produced the value rather than only the single property.


----

### JDBC-RESOURCE-CONNECTOR-500-001

> The JDBC resource connector for database {0} received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `JDBCErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### JDBC-RESOURCE-CONNECTOR-500-002

> The JDBC resource connector detected a missing value for column {0} during method {1} in mapper {2}

|  |  |
|---|---|
| **Java constant** | `JDBCErrorCode.MISSING_DATABASE_VALUE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector cannot process the current request because of a missing value in the database.

**User action**

Investigate the contents of the database and the SQL requests used to populate it.


----

### JDBC-RESOURCE-CONNECTOR-500-003

> The JDBC resource connector for database {0} received an unexpected SQL exception from request "{1}" during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request because the database returned an unexpected error.

**User action**

Use the details from the SQL error message and the SQL request to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
