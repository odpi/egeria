<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# PostgresAuditCode

The PostgresAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 7 |
| **Message identifiers begin** | `POSTGRES-REPOSITORY-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/postgres-repository-connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/postgres-repository-connector) |
| **Source** | [PostgresAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/postgres-repository-connector/src/main/java/org/odpi/openmetadata/adapters/repositoryservices/postgres/repositoryconnector/ffdc/PostgresAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/repository-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [POSTGRES-REPOSITORY-CONNECTOR-0001](#postgres-repository-connector-0001) | EXCEPTION | The PostgreSQL repository connector {0} received an unexpected {1} exception during method {2}; the error message was: {3} |
| [POSTGRES-REPOSITORY-CONNECTOR-0002](#postgres-repository-connector-0002) | STARTUP | The PostgreSQL repository connector {0} is connecting to database {1} |
| [POSTGRES-REPOSITORY-CONNECTOR-0003](#postgres-repository-connector-0003) | STARTUP | The PostgreSQL repository connector {0} is validating the schema definitions for schema {1} |
| [POSTGRES-REPOSITORY-CONNECTOR-0007](#postgres-repository-connector-0007) | STARTUP | The PostgreSQL repository connector {0} has is using a default asOfTime for queries of: {1} |
| [POSTGRES-REPOSITORY-CONNECTOR-0008](#postgres-repository-connector-0008) | STARTUP | The PostgreSQL repository connector {0} is using a repository mode of: {1} |
| [POSTGRES-REPOSITORY-CONNECTOR-0009](#postgres-repository-connector-0009) | STARTUP | The PostgreSQL repository connector {0} has brought {1} stored supertype chain(s) into line with the current open metadata types |
| [POSTGRES-REPOSITORY-CONNECTOR-0010](#postgres-repository-connector-0010) | ERROR | The PostgreSQL repository connector {0} could not check its stored supertype chains; the {1} exception was: {2} |

----

### POSTGRES-REPOSITORY-CONNECTOR-0001

> The PostgreSQL repository connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}

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

### POSTGRES-REPOSITORY-CONNECTOR-0002

> The PostgreSQL repository connector {0} is connecting to database {1}

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.STARTING_REPOSITORY` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is testing its connection to the database.

**User action**

Check for errors following this message.


----

### POSTGRES-REPOSITORY-CONNECTOR-0003

> The PostgreSQL repository connector {0} is validating the schema definitions for schema {1}

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.CONFIRMING_REPOSITORY_SCHEMA` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is testing the table and column definitions for the database schema.  If they are missing, they are created automatically.

**User action**

Check for errors in configuring the schema.


----

### POSTGRES-REPOSITORY-CONNECTOR-0007

> The PostgreSQL repository connector {0} has is using a default asOfTime for queries of: {1}

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.DEFAULT_AS_OF_TIME` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

All queries that do not explicitly specify an asOfTime will use this value.  A value of null means it will use the current time.  This value is changed using the 'defaultAsOfTime' configuration property.

**User action**

Check that this is the intended value.  Typically it is only changed from its default value of null for audits that are focused on a particular moment in time.


----

### POSTGRES-REPOSITORY-CONNECTOR-0008

> The PostgreSQL repository connector {0} is using a repository mode of: {1}

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.REPOSITORY_MODE` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The repository mode is used to switch the repository into a read-only mode.  The default mode is read-write.  This value is changed using the 'repositoryMode' configuration property.  If it is set to 'readOnly' then repositoryMode=read-only; if it is set to anything else (or not set) then repositoryMode=read-write.

**User action**

Check that this is the intended value.  Typically it is only changed from its default value of read-write for situations where you do not want any changes to be made to the metadata in the repository.


----

### POSTGRES-REPOSITORY-CONNECTOR-0009

> The PostgreSQL repository connector {0} has brought {1} stored supertype chain(s) into line with the current open metadata types

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.SUPERTYPE_CHAINS_REPAIRED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

Each instance carries its type's supertype chain, written when the instance was stored, and searches for a supertype are answered from it.  Instances written before a type was moved in the hierarchy still held the hierarchy as it used to be, so they did not answer searches for the type's new supertypes.  They have been rewritten and now do.

**User action**

No action is required.  This message is expected the first time a server starts after a type has been given a new supertype, and is not issued by a repository that is already up to date.


----

### POSTGRES-REPOSITORY-CONNECTOR-0010

> The PostgreSQL repository connector {0} could not check its stored supertype chains; the {1} exception was: {2}

|  |  |
|---|---|
| **Java constant** | `PostgresAuditCode.SUPERTYPE_CHAIN_CHECK_FAILED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The server continues to start.  Any instance whose stored supertype chain is out of date stays invisible to searches for the supertypes it gained, until a later start completes this check.

**User action**

Use the details in the message to work out why the repository could not be read or written, then restart the server.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
