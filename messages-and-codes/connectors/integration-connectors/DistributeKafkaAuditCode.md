<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# DistributeKafkaAuditCode

The DistributeKafkaAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `DISTRIBUTE-KAFKA-AUDIT-LOG-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.ffdc.DistributeKafkaAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/kafka-audit-integration-connector](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/kafka-audit-integration-connector) |
| **Source** | [DistributeKafkaAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/kafka-audit-integration-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/kafkaaudit/ffdc/DistributeKafkaAuditCode.java) |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [DISTRIBUTE-KAFKA-AUDIT-LOG-0001](#distribute-kafka-audit-log-0001) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [DISTRIBUTE-KAFKA-AUDIT-LOG-0002](#distribute-kafka-audit-log-0002) | ERROR | The {0} integration connector has been given a catalog target accessed through connector {1}, which is not a supported audit log destination |

----

### DISTRIBUTE-KAFKA-AUDIT-LOG-0001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `DistributeKafkaAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/concepts/audit-log-destination-connector/> |

**System action**

The connector cannot pass one or more audit log records to the audit log destination.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### DISTRIBUTE-KAFKA-AUDIT-LOG-0002

> The {0} integration connector has been given a catalog target accessed through connector {1}, which is not a supported audit log destination

|  |  |
|---|---|
| **Java constant** | `DistributeKafkaAuditCode.UNSUPPORTED_DESTINATION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |
| **Further reading** | <https://egeria-project.org/features/metadata-observability/overview/> |

**System action**

Audit log records are not sent to this catalog target.  Database destinations are no longer supported; the connector can send audit log records to a folder, or to the console.

**User action**

Remove this catalog target from the integration connector.  To observe the activity recorded in the audit log, use the Pepys Activity Observer and the Lovelace services.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
