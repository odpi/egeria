<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# DistributeKafkaAuditCode

The DistributeKafkaAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `DISTRIBUTE-KAFKA-AUDIT-LOG-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.ffdc.DistributeKafkaAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/kafka-audit-integration-connector](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/kafka-audit-integration-connector) |
| **Source** | [DistributeKafkaAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/kafka-audit-integration-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/kafkaaudit/ffdc/DistributeKafkaAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/audit-log-destination-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [DISTRIBUTE-KAFKA-AUDIT-LOG-0001](#distribute-kafka-audit-log-0001) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### DISTRIBUTE-KAFKA-AUDIT-LOG-0001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `DistributeKafkaAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot catalog one or more metadata elements in the observations database.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
