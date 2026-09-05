<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# KafkaOpenMetadataTopicConnectorErrorCode

The KafkaOpenMetadataTopicConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Apache Kafka connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 3 |
| **Message identifiers begin** | `OCF-KAFKA-TOPIC-CONNECTOR-400-` |
| **Java class** | `org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/kafka-open-metadata-topic-connector](../../../open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/kafka-open-metadata-topic-connector) |
| **Source** | [KafkaOpenMetadataTopicConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/kafka-open-metadata-topic-connector/src/main/java/org/odpi/openmetadata/adapters/eventbus/topic/kafka/KafkaOpenMetadataTopicConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/open-metadata-topic-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OCF-KAFKA-TOPIC-CONNECTOR-400-001](#ocf-kafka-topic-connector-400-001) | 400 | An unexpected {0} exception was caught while sending an event to topic {1}.  The message in the exception was: {2} |
| [OCF-KAFKA-TOPIC-CONNECTOR-400-002](#ocf-kafka-topic-connector-400-002) | 400 | Egeria was unable to initialize a connection to a Kafka cluster.  The message in the exception was: {0} |
| [OCF-KAFKA-TOPIC-CONNECTOR-400-003](#ocf-kafka-topic-connector-400-003) | 400 | Egeria encountered an exception while attempting to connect a message producer to a Kafka.  The message in the exception was: {0} |

----

### OCF-KAFKA-TOPIC-CONNECTOR-400-001

> An unexpected {0} exception was caught while sending an event to topic {1}.  The message in the exception was: {2}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorErrorCode.ERROR_SENDING_EVENT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot send the event.

**User action**

Review the exception that was returned from the send.


----

### OCF-KAFKA-TOPIC-CONNECTOR-400-002

> Egeria was unable to initialize a connection to a Kafka cluster.  The message in the exception was: {0}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorErrorCode.ERROR_ATTEMPTING_KAFKA_INITIALIZATION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system is unable initialize.

**User action**

Ensure that Kafka is available


----

### OCF-KAFKA-TOPIC-CONNECTOR-400-003

> Egeria encountered an exception while attempting to connect a message producer to a Kafka.  The message in the exception was: {0}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorErrorCode.ERROR_CONNECTING_KAFKA_PRODUCER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

Egeria cannot produce events

**User action**

Ensure that the Kafka service is available and that the connection properties are valid.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
