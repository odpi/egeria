<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# JDBCIntegrationConnectorAuditCode

The JDBCIntegrationConnectorAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 11 |
| **Message identifiers begin** | `JDBC-INTEGRATION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/jdbc-integration-connector](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/jdbc-integration-connector) |
| **Source** | [JDBCIntegrationConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/jdbc-integration-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/jdbc/ffdc/JDBCIntegrationConnectorAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/integration-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [JDBC-INTEGRATION-CONNECTOR-0001](#jdbc-integration-connector-0001) | INFO | Connector {0} is preparing to extract metadata from database {1} |
| [JDBC-INTEGRATION-CONNECTOR-0002](#jdbc-integration-connector-0002) | ERROR | Connector {0} cannot connect to database {1}; the {2} exception returned a message of {3} |
| [JDBC-INTEGRATION-CONNECTOR-0003](#jdbc-integration-connector-0003) | INFO | Connector {0} has successfully extracted metadata from database {1} |
| [JDBC-INTEGRATION-CONNECTOR-0016](#jdbc-integration-connector-0016) | EXCEPTION | The JDBC Integration Connector {0} received an unexpected {1} exception during method {2} while working with database {3}; the error message was: {4} |
| [JDBC-INTEGRATION-CONNECTOR-0005](#jdbc-integration-connector-0005) | EXCEPTION | An {0} exception while connecting to database {1}. Exception message is: {2} |
| [JDBC-INTEGRATION-CONNECTOR-0006](#jdbc-integration-connector-0006) | EXCEPTION | An {0} exception was received by method {1}. Exception message is: {2} |
| [JDBC-INTEGRATION-CONNECTOR-0007](#jdbc-integration-connector-0007) | ERROR | Exiting from method {0} as a result of a failed database transfer |
| [JDBC-INTEGRATION-CONNECTOR-0008](#jdbc-integration-connector-0008) | EXCEPTION | Error reading data from Metadata Access Server in method {0}. Possible message is {1} |
| [JDBC-INTEGRATION-CONNECTOR-0010](#jdbc-integration-connector-0010) | EXCEPTION | Unknown error when removing element from Metadata Access Server with guid {0} and qualified name {1}. |
| [JDBC-INTEGRATION-CONNECTOR-0012](#jdbc-integration-connector-0012) | INFO | Transfer complete for {0} |
| [JDBC-INTEGRATION-CONNECTOR-0015](#jdbc-integration-connector-0015) | ERROR | Connector {0} found {1} elements in the metadata access server with a qualified name of {2}; expecting to find at most one |

----

### JDBC-INTEGRATION-CONNECTOR-0001

> Connector {0} is preparing to extract metadata from database {1}

|  |  |
|---|---|
| **Java constant** | `JDBCIntegrationConnectorAuditCode.STARTING_METADATA_TRANSFER` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is about to connect to the named database to extract details of its schemas tables and columns.

**User action**

Check that this is an appropriate database for the connector to be accessing.


----

### JDBC-INTEGRATION-CONNECTOR-0002

> Connector {0} cannot connect to database {1}; the {2} exception returned a message of {3}

|  |  |
|---|---|
| **Java constant** | `JDBCIntegrationConnectorAuditCode.CONNECTION_FAILED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector requested a connection to the database and the exception occurred.

**User action**

Check the set up of the open metadata connection attached to the database asset, or directly to this connector.  Are the userId and password correct?  Is the jdbc connection string specified in the endpoint's address correct?  Is the database server set up correctly to receive the connection request?


----

### JDBC-INTEGRATION-CONNECTOR-0003

> Connector {0} has successfully extracted metadata from database {1}

|  |  |
|---|---|
| **Java constant** | `JDBCIntegrationConnectorAuditCode.EXITING_ON_COMPLETE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector has completed its refresh of this database.

**User action**

No user actions are necessary.  The connector will connect again with this database after the next refresh interval.


----

### JDBC-INTEGRATION-CONNECTOR-0016

> The JDBC Integration Connector {0} received an unexpected {1} exception during method {2} while working with database {3}; the error message was: {4}

|  |  |
|---|---|
| **Java constant** | `JDBCIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### JDBC-INTEGRATION-CONNECTOR-0005

> An {0} exception while connecting to database {1}. Exception message is: {2}

|  |  |
|---|---|
| **Java constant** | `JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_JDBC` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector issued a SQL call to the database and the exception occurred.

**User action**

Use the message from the exception to guide you in locating the error.


----

### JDBC-INTEGRATION-CONNECTOR-0006

> An {0} exception was received by method {1}. Exception message is: {2}

|  |  |
|---|---|
| **Java constant** | `JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

Upserting an entity into the Metadata Access Server failed.

**User action**

Investigate OMAS availability. If it is available then contact the Egeria team for support


----

### JDBC-INTEGRATION-CONNECTOR-0007

> Exiting from method {0} as a result of a failed database transfer

|  |  |
|---|---|
| **Java constant** | `JDBCIntegrationConnectorAuditCode.EXITING_ON_DATABASE_TRANSFER_FAIL` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

Stopping execution

**User action**

Consult logs for further details


----

### JDBC-INTEGRATION-CONNECTOR-0008

> Error reading data from Metadata Access Server in method {0}. Possible message is {1}

|  |  |
|---|---|
| **Java constant** | `JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

Reading open metadata from the repository.

**User action**

Consult logs for further details


----

### JDBC-INTEGRATION-CONNECTOR-0010

> Unknown error when removing element from Metadata Access Server with guid {0} and qualified name {1}.

|  |  |
|---|---|
| **Java constant** | `JDBCIntegrationConnectorAuditCode.EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

Removing element in OMAS

**User action**

Consult logs for further details


----

### JDBC-INTEGRATION-CONNECTOR-0012

> Transfer complete for {0}

|  |  |
|---|---|
| **Java constant** | `JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

Continue execution

**User action**

None


----

### JDBC-INTEGRATION-CONNECTOR-0015

> Connector {0} found {1} elements in the metadata access server with a qualified name of {2}; expecting to find at most one

|  |  |
|---|---|
| **Java constant** | `JDBCIntegrationConnectorAuditCode.MULTIPLE_ELEMENTS_FOUND` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is unable to determine whether this element already exists, so it is skipping it for this refresh rather than risk creating a duplicate.

**User action**

Investigate why more than one element has this qualified name and remove the duplicates.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
