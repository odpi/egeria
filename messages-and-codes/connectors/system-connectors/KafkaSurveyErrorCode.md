<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# KafkaSurveyErrorCode

The KafkaSurveyErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Apache Kafka Admin connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `APACHE-KAFKA-SURVEY-ACTION-CONNECTOR-500-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.apachekafka.survey.ffdc.KafkaSurveyErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors) |
| **Source** | [KafkaSurveyErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/apachekafka/survey/ffdc/KafkaSurveyErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/survey-action-service/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [APACHE-KAFKA-SURVEY-ACTION-CONNECTOR-500-001](#apache-kafka-survey-action-connector-500-001) | 500 | The {0} Apache Kafka Survey Action connector received an unexpected {1} exception during method {2}; the error message was: {3} |

----

### APACHE-KAFKA-SURVEY-ACTION-CONNECTOR-500-001

> The {0} Apache Kafka Survey Action connector received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `KafkaSurveyErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot continue to survey the Apache Kafka Server.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
