<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenLineageIntegrationConnectorAuditCode

The OpenLineageIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 8 |
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

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
