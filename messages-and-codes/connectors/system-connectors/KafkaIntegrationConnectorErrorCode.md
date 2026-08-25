<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# KafkaIntegrationConnectorErrorCode

The KafkaIntegrationConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Kafka monitor integration connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `APACHE-KAFKA-INTEGRATION-CONNECTOR-400-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.apachekafka.integration.ffdc.KafkaIntegrationConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors) |
| **Source** | [KafkaIntegrationConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/apachekafka/integration/ffdc/KafkaIntegrationConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [APACHE-KAFKA-INTEGRATION-CONNECTOR-400-001](#apache-kafka-integration-connector-400-001) | 400 | The {0} integration connector cannot proceed processing catalog target {0} ({1}) because it has no event broker capability attached. |

----

### APACHE-KAFKA-INTEGRATION-CONNECTOR-400-001

> The {0} integration connector cannot proceed processing catalog target {0} ({1}) because it has no event broker capability attached.

|  |  |
|---|---|
| **Java constant** | `KafkaIntegrationConnectorErrorCode.MISSING_EVENT_BROKER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot catalog one or more topics because it has no event broker to connect it to.

**User action**

Add an event broker to the server definition.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
