<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# KafkaOpenMetadataTopicConnectorAuditCode

The KafkaOpenMetadataTopicConnectorAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 19 |
| **Message identifiers begin** | `OCF-KAFKA-TOPIC-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/kafka-open-metadata-topic-connector](../../../open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/kafka-open-metadata-topic-connector) |
| **Source** | [KafkaOpenMetadataTopicConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/kafka-open-metadata-topic-connector/src/main/java/org/odpi/openmetadata/adapters/eventbus/topic/kafka/KafkaOpenMetadataTopicConnectorAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/open-metadata-topic-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OCF-KAFKA-TOPIC-CONNECTOR-0001](#ocf-kafka-topic-connector-0001) | STARTUP | Connecting to Apache Kafka Topic {0} with a server identifier of {1} and bootstrap server location of {2} |
| [OCF-KAFKA-TOPIC-CONNECTOR-0002](#ocf-kafka-topic-connector-0002) | STARTUP | {0} properties passed to the Apache Kafka Producer for topic {1} |
| [OCF-KAFKA-TOPIC-CONNECTOR-0003](#ocf-kafka-topic-connector-0003) | STARTUP | {0} properties passed to the Apache Kafka Consumer for topic {1} |
| [OCF-KAFKA-TOPIC-CONNECTOR-0004](#ocf-kafka-topic-connector-0004) | SHUTDOWN | The Apache Kafka connector for topic {0} is shutting down |
| [OCF-KAFKA-TOPIC-CONNECTOR-0005](#ocf-kafka-topic-connector-0005) | ERROR | The Apache Kafka connector for topic {0} has been set up with no additional properties |
| [OCF-KAFKA-TOPIC-CONNECTOR-0006](#ocf-kafka-topic-connector-0006) | ERROR | The Apache Kafka connector for topic {0} has been set up with configuration properties that produced the {1} exception when read.  This is the error message: {2} |
| [OCF-KAFKA-TOPIC-CONNECTOR-0007](#ocf-kafka-topic-connector-0007) | ERROR | The Apache Kafka connector has been set up with no topic name |
| [OCF-KAFKA-TOPIC-CONNECTOR-0008](#ocf-kafka-topic-connector-0008) | ERROR | The connector listening on topic {0} received an unexpected exception {1} from Apache Kafka.  The message in the exception was {2} |
| [OCF-KAFKA-TOPIC-CONNECTOR-0009](#ocf-kafka-topic-connector-0009) | ERROR | The Apache Kafka connector listening on topic {0} received an unexpected exception {1} distributing an event to components within the server.  The event was {2} and the message in the exception was {3} |
| [OCF-KAFKA-TOPIC-CONNECTOR-0010](#ocf-kafka-topic-connector-0010) | STARTUP | The Apache Kafka producer for topic {0} is starting up with {1} buffered messages |
| [OCF-KAFKA-TOPIC-CONNECTOR-0011](#ocf-kafka-topic-connector-0011) | SHUTDOWN | The Apache Kafka producer for topic {0} is shutting down after sending {2} messages and with {1} unsent messages |
| [OCF-KAFKA-TOPIC-CONNECTOR-0012](#ocf-kafka-topic-connector-0012) | ERROR | Unable to send event on topic {0}.  {1} events successfully sent; {2} events buffered. Latest error message is {3} |
| [OCF-KAFKA-TOPIC-CONNECTOR-0013](#ocf-kafka-topic-connector-0013) | ERROR | Property {0} is missing from the Kafka Event Bus configuration |
| [OCF-KAFKA-TOPIC-CONNECTOR-0014](#ocf-kafka-topic-connector-0014) | ERROR | Connecting to bootstrap Apache Kafka Broker {0} |
| [OCF-KAFKA-TOPIC-CONNECTOR-0015](#ocf-kafka-topic-connector-0015) | STARTUP | The local server is attempting to connect to Kafka brokers at {0} [ attempt {1} of {2} ] |
| [OCF-KAFKA-TOPIC-CONNECTOR-0016](#ocf-kafka-topic-connector-0016) | SHUTDOWN | An unexpected error {0} was encountered while closing the kafka topic connector for {1}: action {2} and error message {3} |
| [OCF-KAFKA-TOPIC-CONNECTOR-0017](#ocf-kafka-topic-connector-0017) | EXCEPTION | An unexpected error {0} was encountered while committing consumed event offsets to topic {1}: error message is {2} |
| [OCF-KAFKA-TOPIC-CONNECTOR-0018](#ocf-kafka-topic-connector-0018) | INFO | The Egeria client was rebalanced by Kafka and failed to commit already consumed events |
| [OCF-KAFKA-TOPIC-CONNECTOR-0019](#ocf-kafka-topic-connector-0019) | EXCEPTION | Egeria encountered an exception when attempting to connect the message producer to the kafka topic {0} |

----

### OCF-KAFKA-TOPIC-CONNECTOR-0001

> Connecting to Apache Kafka Topic {0} with a server identifier of {1} and bootstrap server location of {2}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server has started up the Apache Kafka connector.

**User action**

No action is required.  This is part of the normal operation of the server.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0002

> {0} properties passed to the Apache Kafka Producer for topic {1}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_PRODUCER_PROPERTIES` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server is configuring its Apache Kafka producer using the properties associated with this log record.

**User action**

No action is required, but these properties are useful when diagnosing problems sending events to this topic.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0003

> {0} properties passed to the Apache Kafka Consumer for topic {1}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_CONSUMER_PROPERTIES` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server is registering to receive events from Apache Kafka using the properties associated with this log record.

**User action**

No action is required, but these properties are useful when diagnosing problems receiving events from this topic.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0004

> The Apache Kafka connector for topic {0} is shutting down

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local server has requested shut down of the Apache Kafka connector.

**User action**

No action is required.  This is part of the normal shut down of the server.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0005

> The Apache Kafka connector for topic {0} has been set up with no additional properties

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.NULL_ADDITIONAL_PROPERTIES` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

Without these properties, the server is not able to send and receive events on the topic.

**User action**

This problem must be fixed before the server can exchange metadata.  The properties are supplied on the event bus admin command.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0006

> The Apache Kafka connector for topic {0} has been set up with configuration properties that produced the {1} exception when read.  This is the error message: {2}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.UNABLE_TO_PARSE_CONFIG_PROPERTIES` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

An exception occurred reading the configuration properties.  This means that the server is not able to send and receive events on the topic.

**User action**

Use the exception message to correct the configuration properties supplied on the event bus admin command.  This problem must be fixed before the server can exchange metadata.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0007

> The Apache Kafka connector has been set up with no topic name

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.NO_TOPIC_NAME` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | none |

**System action**

Without the name of the topic, the server is not able to send and receive events.

**User action**

This problem must be fixed before the server can exchange metadata.  The topic name is supplied in the endpoint object of the connector's connection.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0008

> The connector listening on topic {0} received an unexpected exception {1} from Apache Kafka.  The message in the exception was {2}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_RECEIVING_EVENT` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

A call to receive events from Apache Kafka failed with an exception.

**User action**

Use the exception message, along with the Apache Kafka logs, to determine why the topic could not be read.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0009

> The Apache Kafka connector listening on topic {0} received an unexpected exception {1} distributing an event to components within the server.  The event was {2} and the message in the exception was {3}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_DISTRIBUTING_EVENT` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

An incoming event could not be processed by one or more components in the server.

**User action**

Use the information in the event and the exception message, along with other messages to determine the source of the error.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0010

> The Apache Kafka producer for topic {0} is starting up with {1} buffered messages

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.KAFKA_PRODUCER_START` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local server has started the Apache Kafka connector.

**User action**

No action is required, but a non-zero count of buffered messages shows events that were still waiting to be sent when the connector last stopped.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0011

> The Apache Kafka producer for topic {0} is shutting down after sending {2} messages and with {1} unsent messages

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.KAFKA_PRODUCER_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The Apache Kafka connector is closing its producer.  The counts of sent and unsent messages are in this message.

**User action**

No action is required if the count of unsent messages is zero.  If it is not, check this audit log for send failures on this topic.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0012

> Unable to send event on topic {0}.  {1} events successfully sent; {2} events buffered. Latest error message is {3}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.EVENT_SEND_IN_ERROR_LOOP` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

There is a reoccurring error being returned by the Apache Kafka event bus.  Outbound events are being buffered.

**User action**

Review the operational status of Apache Kafka to ensure it is running and the topic is defined.  If no events have been send, then it may be a configuration error, either in this server or in the event bus itself. Once the error is corrected, the server will send the buffered events.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0013

> Property {0} is missing from the Kafka Event Bus configuration

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.MISSING_PROPERTY` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The system cannot connect to the event bus.

**User action**

Add the missing property to the event bus properties in the server configuration.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0014

> Connecting to bootstrap Apache Kafka Broker {0}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_FAILED_INITIALIZING` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The local server has failed to started up the Apache Kafka connector, Kafka Broker is unavailable

**User action**

Ensure Kafka is running and restart the local Egeria Server


----

### OCF-KAFKA-TOPIC-CONNECTOR-0015

> The local server is attempting to connect to Kafka brokers at {0} [ attempt {1} of {2} ]

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.KAFKA_CONNECTION_RETRY` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system retries the connection after a short wait.

**User action**

Ensure the Kafka Cluster has started


----

### OCF-KAFKA-TOPIC-CONNECTOR-0016

> An unexpected error {0} was encountered while closing the kafka topic connector for {1}: action {2} and error message {3}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.UNEXPECTED_SHUTDOWN_EXCEPTION` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector continues to shutdown.  Some resources may not be released properly.

**User action**

Check the OMAG Server's audit log and Kafka error logs for related messages that may indicate if there are any unreleased resources.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0017

> An unexpected error {0} was encountered while committing consumed event offsets to topic {1}: error message is {2}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_COMMITTING_OFFSETS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

Depending on the nature of the error, events may no longer be exchanged with the topic.

**User action**

Check the OMAG Server's audit log and Kafka error logs for related messages that indicate the cause of this error.  Work to clear the underlying error.  Once fixed, it may be necessary to restart the server to cause a reconnect to Kafka.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0018

> The Egeria client was rebalanced by Kafka and failed to commit already consumed events

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.FAILED_TO_COMMIT_CONSUMED_EVENTS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | none |

**System action**

If this was experienced in a production environment check the kafka heartbeat and batch processing settings.

**User action**

Some events may be delivered to the server more than once.  If this happens regularly, review the Kafka heartbeat and batch processing settings so that this consumer is not rebalanced part way through a batch.


----

### OCF-KAFKA-TOPIC-CONNECTOR-0019

> Egeria encountered an exception when attempting to connect the message producer to the kafka topic {0}

|  |  |
|---|---|
| **Java constant** | `KafkaOpenMetadataTopicConnectorAuditCode.ERROR_CONNECTING_KAFKA_PRODUCER` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

Resolve the kafka service is available and that the kafka producer connection properties are correct

**User action**

Check the  Kafka error logs for related messages that could indicate the cause of this error.  Work to clear the underlying error.  Once fixed, it may be necessary to restart the server to cause a reconnect to Kafka.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
