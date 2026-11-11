<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenLineageIntegrationConnectorAuditCode

The OpenLineageIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 14 |
| **Message identifiers begin** | `OPEN-LINEAGE-INTEGRATION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors) |
| **Source** | [OpenLineageIntegrationConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/openlineage/ffdc/OpenLineageIntegrationConnectorAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0001](#open-lineage-integration-connector-0001) | INFO | The {0} integration connector is monitoring Apache Kafka topic {1} with connection: {2} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0010](#open-lineage-integration-connector-0010) | EXCEPTION | The {0} integration connector received an unexpected {1} exception in method {2} when working with open lineage events; the error message was: {3} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0020](#open-lineage-integration-connector-0020) | INFO | The {0} integration connector has catalogued {1} {2} ({3}) from open lineage {4} {5} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0021](#open-lineage-integration-connector-0021) | INFO | The {0} integration connector has added a {1} lineage relationship from {2} to {3} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0022](#open-lineage-integration-connector-0022) | INFO | The {0} integration connector is ignoring open lineage event {1} because {2} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0024](#open-lineage-integration-connector-0024) | INFO | The {0} integration connector has renamed asset {1} from {2} to {3} following open lineage {4} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0025](#open-lineage-integration-connector-0025) | INFO | The {0} integration connector has removed asset {1} ({2}) using delete method {3} following open lineage {4} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0023](#open-lineage-integration-connector-0023) | ERROR | The {0} integration connector was unable to record {1} for open lineage event {2}; the {3} exception message was: {4} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0026](#open-lineage-integration-connector-0026) | INFO | The {0} integration connector has applied the run events received since its last refresh to the RunMetrics and DataScope classifications of {1} elements |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0030](#open-lineage-integration-connector-0030) | INFO | The {0} integration connector is storing open lineage events in database schema {1} of {2}; records older than {3} days are deleted on each refresh |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0031](#open-lineage-integration-connector-0031) | INFO | The {0} integration connector has deleted {1} open lineage events, {2} runs, {3} data quality assertions and {4} tests recorded before {5} from database schema {6} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0032](#open-lineage-integration-connector-0032) | ERROR | The {0} integration connector is not able to store open lineage events in catalog target {1} because {2} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0033](#open-lineage-integration-connector-0033) | ERROR | The {0} integration connector was unable to store the {1} open lineage event for run {2} of job {3} in database schema {4}; the {5} exception message was: {6} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-0034](#open-lineage-integration-connector-0034) | ERROR | The {0} integration connector is ignoring the value {1} of its daysToCleanUp configuration property because it is not a whole number of days that is zero or more |

----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0001

> The {0} integration connector is monitoring Apache Kafka topic {1} with connection: {2}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.KAFKA_RECEIVER_CONFIGURATION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is designed to monitor open lineage events published to an Apache Kafka topic.

**User action**

No specific action is required.  This message is to confirm the configuration for the Kafka Open Lineage Receiver integration connector.  It is output for each unique embedded connector and KafkaTopic catalog target


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0010

> The {0} integration connector received an unexpected {1} exception in method {2} when working with open lineage events; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process one or more lineage events.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0020

> The {0} integration connector has catalogued {1} {2} ({3}) from open lineage {4} {5}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.ELEMENT_CATALOGUED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The connector has created a new open metadata element to represent a job, run or dataset found in an open lineage event.

**User action**

No specific action is required.  This message is to record the new element in the open metadata ecosystem.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0021

> The {0} integration connector has added a {1} lineage relationship from {2} to {3}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.LINEAGE_CATALOGUED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector has linked two elements together to represent a data flow, control flow, process hierarchy or column mapping found in an open lineage event.

**User action**

No specific action is required.  This message is to record the new relationship in the open metadata ecosystem.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0022

> The {0} integration connector is ignoring open lineage event {1} because {2}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.EVENT_IGNORED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector cannot map the event to open metadata because it is missing a mandatory value, or its job or dataset matches multiple existing elements.

**User action**

Review the event and, if it is expected to be catalogued, correct the producer or remove the duplicate elements from the open metadata repository.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0024

> The {0} integration connector has renamed asset {1} from {2} to {3} following open lineage {4}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.DATASET_RENAMED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

An open lineage event reported a RENAME lifecycle state change for a dataset, so the names of the asset that represents it have been updated.  The previous names remain visible in the history of the element.

**User action**

No specific action is required.  Use the element's history to see the names it had before the rename.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0025

> The {0} integration connector has removed asset {1} ({2}) using delete method {3} following open lineage {4}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.DATASET_DROPPED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

An open lineage event reported a DROP lifecycle state change for a dataset, so the asset that represents it has been archived or deleted according to the delete method configured for the connector.

**User action**

No specific action is required.  Change the connector's delete method if archived (Memento) assets are wanted instead of deleted ones, or vice versa.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0023

> The {0} integration connector was unable to record {1} for open lineage event {2}; the {3} exception message was: {4}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.OPTIONAL_METADATA_FAILED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The lineage from the event has been catalogued, but the optional metadata (run details, schema, statistics, data quality or data scope) could not be recorded.

**User action**

Use the details from the error message to determine the cause of the error.  The optional metadata will be captured from subsequent events once the problem is resolved.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0026

> The {0} integration connector has applied the run events received since its last refresh to the RunMetrics and DataScope classifications of {1} elements

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.CLASSIFICATIONS_WRITTEN` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The run metrics of processes and the data scope of datasets are gathered from the run events as they arrive and written once per element on each refresh, rather than once per event.

**User action**

No specific action is required.  The interval between refreshes sets how up to date these classifications are.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0030

> The {0} integration connector is storing open lineage events in database schema {1} of {2}; records older than {3} days are deleted on each refresh

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.DATABASE_LOG_STORE_READY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector has created the schema and its tables, if they did not exist, and stores each open lineage event it receives, along with a digest of its run, datasets, data quality assertions and tests.

**User action**

No specific action is required.  The number of days is set by the daysToCleanUp configuration property: if it is not set, nothing is deleted and the log store grows without bound.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0031

> The {0} integration connector has deleted {1} open lineage events, {2} runs, {3} data quality assertions and {4} tests recorded before {5} from database schema {6}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.DATABASE_LOG_STORE_PURGED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The records older than the number of days in the daysToCleanUp configuration property are deleted on each refresh.  The datasets of each deleted run are deleted with it.

**User action**

No specific action is required.  The analysis services that read the log store can only look back as far as the records that are kept, so their analysis window should not be longer than daysToCleanUp.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0032

> The {0} integration connector is not able to store open lineage events in catalog target {1} because {2}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.DATABASE_LOG_STORE_UNUSABLE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector ignores this catalog target.  The events it receives are still stored in its other catalog targets.

**User action**

The catalog target must be a PostgreSQL database schema asset whose connection uses the JDBC resource connector.  The name of the schema is taken from the databaseSchema configuration property of the catalog target relationship or, if it is not set there, of the asset's connection.  Correct the catalog target and the connector will use it on its next refresh.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0033

> The {0} integration connector was unable to store the {1} open lineage event for run {2} of job {3} in database schema {4}; the {5} exception message was: {6}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.DATABASE_LOG_STORE_WRITE_FAILED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

None of the event is stored: the raw event and its digest are written in one transaction.

**User action**

Use the details from the error message to determine the cause of the error.  Events that arrive once the problem is resolved are stored as usual.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-0034

> The {0} integration connector is ignoring the value {1} of its daysToCleanUp configuration property because it is not a whole number of days that is zero or more

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorAuditCode.INVALID_DAYS_TO_CLEAN_UP` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

No records are deleted from the log store, so it grows without bound.

**User action**

Correct the daysToCleanUp configuration property in the connector's connection, or in the catalog target relationship of the database schema.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
