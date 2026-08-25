<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ApacheKafkaErrorCode

The ApacheKafkaErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Apache Kafka Admin connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `APACHE-KAFKA-REST-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ffdc.ApacheKafkaErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors) |
| **Source** | [ApacheKafkaErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/apachekafka/resource/ffdc/ApacheKafkaErrorCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [APACHE-KAFKA-REST-CONNECTOR-400-001](#apache-kafka-rest-connector-400-001) | 400 | Apache Kafka Admin connector {0} has been configured without the URL to Apache Kafka |
| [APACHE-KAFKA-REST-CONNECTOR-500-001](#apache-kafka-rest-connector-500-001) | 500 | The {0} Apache Kafka Admin connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### APACHE-KAFKA-REST-CONNECTOR-400-001

> Apache Kafka Admin connector {0} has been configured without the URL to Apache Kafka

|  |  |
|---|---|
| **Java constant** | `ApacheKafkaErrorCode.NULL_URL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot contact the Apache Kafka server.

**User action**

The Apache Kafka URL is configured in the Apache Kafka Admin connector's connection endpoint in the address property.  Typically it is the host name and port where Apache Kafka is listening.  The connection is either found in the Integration Daemon's configuration, or, if the Integration Daemon is configured with integration groups, in the open metadata definition of the appropriate integration group.


----

### APACHE-KAFKA-REST-CONNECTOR-500-001

> The {0} Apache Kafka Admin connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `ApacheKafkaErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot catalog one or more metadata elements.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
