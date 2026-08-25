<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# KafkaIntegrationConnectorAuditCode

The KafkaIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `APACHE-KAFKA-INTEGRATION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.apachekafka.integration.ffdc.KafkaIntegrationConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors) |
| **Source** | [KafkaIntegrationConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/apachekafka/integration/ffdc/KafkaIntegrationConnectorAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [APACHE-KAFKA-INTEGRATION-CONNECTOR-0001](#apache-kafka-integration-connector-0001) | INFO | The {0} integration connector is cataloguing event broker {1} at URL {2} with template={3} |
| [APACHE-KAFKA-INTEGRATION-CONNECTOR-0004](#apache-kafka-integration-connector-0004) | EXCEPTION | The {0} integration connector received an unexpected {2} exception when retrieving topics from event broker at {1}.  The error message was {3} |
| [APACHE-KAFKA-INTEGRATION-CONNECTOR-0005](#apache-kafka-integration-connector-0005) | INFO | The {0} integration connector has retrieved {2} topics from {1} |
| [APACHE-KAFKA-INTEGRATION-CONNECTOR-0016](#apache-kafka-integration-connector-0016) | INFO | The {0} integration connector created the Topic {1} ({2}) for a new real-world topic |
| [APACHE-KAFKA-INTEGRATION-CONNECTOR-0019](#apache-kafka-integration-connector-0019) | INFO | The {0} integration connector has deleted the Topic {1} ({2}) because the real-world topic is no longer defined in the event broker |

----

### APACHE-KAFKA-INTEGRATION-CONNECTOR-0001

> The {0} integration connector is cataloguing event broker {1} at URL {2} with template={3}

|  |  |
|---|---|
| **Java constant** | `KafkaIntegrationConnectorAuditCode.CONNECTOR_CONFIGURATION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector monitors changes to the topics managed by the event broker and catalogs them in open metadata.

**User action**

No specific action is required.  This message is to confirm the configuration for a specific catalog target.


----

### APACHE-KAFKA-INTEGRATION-CONNECTOR-0004

> The {0} integration connector received an unexpected {2} exception when retrieving topics from event broker at {1}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `KafkaIntegrationConnectorAuditCode.UNABLE_TO_RETRIEVE_TOPICS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.

**User action**

Use the message in the nested exception to determine the root cause of the error. Once this is resolved, follow the instructions in the messages produced by the integration daemon to restart this connector.


----

### APACHE-KAFKA-INTEGRATION-CONNECTOR-0005

> The {0} integration connector has retrieved {2} topics from {1}

|  |  |
|---|---|
| **Java constant** | `KafkaIntegrationConnectorAuditCode.RETRIEVED_TOPICS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector will maintain these topics as assets.

**User action**

No action is required unless there are errors that follow indicating that the topics can not be maintained.


----

### APACHE-KAFKA-INTEGRATION-CONNECTOR-0016

> The {0} integration connector created the Topic {1} ({2}) for a new real-world topic

|  |  |
|---|---|
| **Java constant** | `KafkaIntegrationConnectorAuditCode.TOPIC_CREATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector created the Topic as part of its monitoring of the topics in the event broker.

**User action**

No action is required.  This message is to record the reason why the Topic was created.


----

### APACHE-KAFKA-INTEGRATION-CONNECTOR-0019

> The {0} integration connector has deleted the Topic {1} ({2}) because the real-world topic is no longer defined in the event broker

|  |  |
|---|---|
| **Java constant** | `KafkaIntegrationConnectorAuditCode.TOPIC_DELETED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector removed the Topic as part of its monitoring of the topics in the event broker.

**User action**

No action is required.  This message is to record the reason why the Topic was removed.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
