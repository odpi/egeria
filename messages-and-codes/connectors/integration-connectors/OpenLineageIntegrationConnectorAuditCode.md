<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenLineageIntegrationConnectorAuditCode

The OpenLineageIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 2 |
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

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
