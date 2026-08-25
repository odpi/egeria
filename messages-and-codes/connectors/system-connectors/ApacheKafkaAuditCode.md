<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ApacheKafkaAuditCode

The ApacheKafkaAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `APACHE-KAFKA-REST-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ffdc.ApacheKafkaAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors) |
| **Source** | [ApacheKafkaAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/apachekafka/resource/ffdc/ApacheKafkaAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [APACHE-KAFKA-REST-CONNECTOR-0008](#apache-kafka-rest-connector-0008) | EXCEPTION | The {0} Apache Kafka Admin Connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### APACHE-KAFKA-REST-CONNECTOR-0008

> The {0} Apache Kafka Admin Connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `ApacheKafkaAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot catalog one or more metadata elements.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
