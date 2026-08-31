<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# PostgresErrorCode

The PostgresErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with PostgreSQL as an OMRS Metadata Repository. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 8 |
| **Message identifiers begin** | `POSTGRES-REPOSITORY-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/postgres-repository-connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/postgres-repository-connector) |
| **Source** | [PostgresErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/postgres-repository-connector/src/main/java/org/odpi/openmetadata/adapters/repositoryservices/postgres/repositoryconnector/ffdc/PostgresErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/repository-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [POSTGRES-REPOSITORY-CONNECTOR-400-001](#postgres-repository-connector-400-001) | 400 | The {0} postgreSQL repository connector is running in read-only mode; updates are not allowed |
| [POSTGRES-REPOSITORY-CONNECTOR-400-002](#postgres-repository-connector-400-002) | 400 | The {0} postgreSQL repository connector has detected an incompatible search property with operator {1}: {2} |
| [POSTGRES-REPOSITORY-CONNECTOR-500-001](#postgres-repository-connector-500-001) | 500 | The {0} postgreSQL repository connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [POSTGRES-REPOSITORY-CONNECTOR-500-002](#postgres-repository-connector-500-002) | 500 | The postgreSQL repository connector is missing {0} value during method {1} in mapper {2} |
| [POSTGRES-REPOSITORY-CONNECTOR-500-004](#postgres-repository-connector-500-004) | 500 | The {0} postgreSQL repository connector detected an invalid value for column {1} during method {2} in mapper {3}; row values are: {4} |
| [POSTGRES-REPOSITORY-CONNECTOR-500-005](#postgres-repository-connector-500-005) | 500 | The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because this repository is for server {2} |
| [POSTGRES-REPOSITORY-CONNECTOR-500-006](#postgres-repository-connector-500-006) | 500 | The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because this repository is for metadata collection id {2} rather than the configured value of {3} |
| [POSTGRES-REPOSITORY-CONNECTOR-500-007](#postgres-repository-connector-500-007) | 500 | The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because it does not support schema version {2} |

----

### POSTGRES-REPOSITORY-CONNECTOR-400-001

> The {0} postgreSQL repository connector is running in read-only mode; updates are not allowed

|  |  |
|---|---|
| **Java constant** | `PostgresErrorCode.READ_ONLY_MODE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector is not able to to perform any changes to the repository.

**User action**

Read-only mode is enabled through the repositoryMode configuration property for this repository.  If read-only mode is set in error then change the repository's configuration properties and restart the server.


----

### POSTGRES-REPOSITORY-CONNECTOR-400-002

> The {0} postgreSQL repository connector has detected an incompatible search property with operator {1}: {2}

|  |  |
|---|---|
| **Java constant** | `PostgresErrorCode.BAD_SEARCH_PROPERTY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The search method cannot match the supplied property with the supplied operator.

**User action**

Correct the values supplied on the search so that single values are supplied with single value operators such as 'Equal' and multiple values are supplied on multi-value operators such as 'In'.


----

### POSTGRES-REPOSITORY-CONNECTOR-500-001

> The {0} postgreSQL repository connector received an unexpected exception {1} during method {2}; the error message was: {3}

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

### POSTGRES-REPOSITORY-CONNECTOR-500-002

> The postgreSQL repository connector is missing {0} value during method {1} in mapper {2}

|  |  |
|---|---|
| **Java constant** | `PostgresErrorCode.MISSING_MAPPING_VALUE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector cannot process the current request because of an internal sequencing error.

**User action**

Use a trace to determine why one of the mapper was called in the wrong sequence.


----

### POSTGRES-REPOSITORY-CONNECTOR-500-004

> The {0} postgreSQL repository connector detected an invalid value for column {1} during method {2} in mapper {3}; row values are: {4}

|  |  |
|---|---|
| **Java constant** | `PostgresErrorCode.INVALID_REPOSITORY_VALUE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector cannot process the current request because of an incorrect value in the database.

**User action**

This is a logic error since only valid values should make it into the database.  Investigate the contents of the database and the SQL requests used to populate it.


----

### POSTGRES-REPOSITORY-CONNECTOR-500-005

> The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because this repository is for server {2}

|  |  |
|---|---|
| **Java constant** | `PostgresErrorCode.CONTROL_SERVER_MISMATCH` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector will not use a repository designated to another server.  The server is shutdown.

**User action**

This is a configuration error since each repository should only be used by one server.  If you want to have multiple server instances using this repository then each should run on a different OMAG Server Platform and have the same server name.


----

### POSTGRES-REPOSITORY-CONNECTOR-500-006

> The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because this repository is for metadata collection id {2} rather than the configured value of {3}

|  |  |
|---|---|
| **Java constant** | `PostgresErrorCode.CONTROL_MC_ID_MISMATCH` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will not use a repository designated to another metadata collection.  The server is shutdown.

**User action**

This is a configuration error since each repository is assigned a unique metadata collection id when it is first configured.  This value is broadcast across the cohort and so it should not change.  Use the administration services to change the repository's metadata collection id back to its original value.


----

### POSTGRES-REPOSITORY-CONNECTOR-500-007

> The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because it does not support schema version {2}

|  |  |
|---|---|
| **Java constant** | `PostgresErrorCode.CONTROL_SCHEMA_VERSION_MISMATCH` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is not able to understand the structure of the schema.  The server is shutdown.

**User action**

This is caused by using an older version of Egeria than the one used to create the repository.  Upgrade your Egeria installation to the latest level.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
