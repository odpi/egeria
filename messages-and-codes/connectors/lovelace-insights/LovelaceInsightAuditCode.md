<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# LovelaceInsightAuditCode

The LovelaceInsightAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `LOVELACE-INSIGHTS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/lovelace-insights](../../../open-metadata-implementation/adapters/open-connectors/lovelace-insights) |
| **Source** | [LovelaceInsightAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/lovelace-insights/src/main/java/org/odpi/openmetadata/adapters/connectors/lovelaceinsight/ffdc/LovelaceInsightAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/organization-insight/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [LOVELACE-INSIGHTS-0001](#lovelace-insights-0001) | EXCEPTION | The organization insight service {0} received an unexpected exception {1} during method {2}; the error message was: {3} |
| [LOVELACE-INSIGHTS-0016](#lovelace-insights-0016) | INFO | The {0} governance action service received a {1} exception when it registered its completion status.  The exception message is: {2} |
| [LOVELACE-INSIGHTS-0017](#lovelace-insights-0017) | INFO | The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception message is: {2} |
| [LOVELACE-INSIGHTS-0018](#lovelace-insights-0018) | INFO | The {0} governance service has processed the {1} governance zone ({2}) |
| [LOVELACE-INSIGHTS-0038](#lovelace-insights-0038) | INFO | The {0} governance action service has completed successfully |

----

### LOVELACE-INSIGHTS-0001

> The organization insight service {0} received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### LOVELACE-INSIGHTS-0016

> The {0} governance action service received a {1} exception when it registered its completion status.  The exception message is: {2}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.UNABLE_TO_SET_COMPLETION_STATUS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action throws a GovernanceServiceException in the hope that the hosting server is able to clean up.

**User action**

Review the exception messages that are logged about the same time as one of them will point to the root cause of the error.


----

### LOVELACE-INSIGHTS-0017

> The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception message is: {2}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.UNABLE_TO_REGISTER_LISTENER` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action service throws a GovernanceServiceException.

**User action**

This is likely to be a configuration error.  Review the description of the exception's message to understand what is not set up correctly and and follow its instructions.


----

### LOVELACE-INSIGHTS-0018

> The {0} governance service has processed the {1} governance zone ({2})

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.GOVERNANCE_ZONE_PROCESSED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The service will move on to the next governance zone until all have been processed.

**User action**

No action is required except to validate that each of the governance zones have been processed.


----

### LOVELACE-INSIGHTS-0038

> The {0} governance action service has completed successfully

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.SERVICE_COMPLETED_SUCCESSFULLY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The service is shutting down.

**User action**

No action is required except to validate that the shutdown is occurring at an appropriate time.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
