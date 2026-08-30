<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# LovelaceInsightErrorCode

The LovelaceInsightErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Lovelace services. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `LOVELACE-INSIGHTS-500-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/lovelace-insights](../../../open-metadata-implementation/adapters/open-connectors/lovelace-insights) |
| **Source** | [LovelaceInsightErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/lovelace-insights/src/main/java/org/odpi/openmetadata/adapters/connectors/lovelaceinsight/ffdc/LovelaceInsightErrorCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/organization-insight/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [LOVELACE-INSIGHTS-500-003](#lovelace-insights-500-003) | 500 | The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception message is: {2} |
| [LOVELACE-INSIGHTS-500-004](#lovelace-insights-500-004) | 500 | The {0} governance action service received an unexpected exception {1} during its processing; the error message was: {2} |

----

### LOVELACE-INSIGHTS-500-003

> The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception message is: {2}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightErrorCode.UNABLE_TO_REGISTER_LISTENER` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action throws a GovernanceServiceException in the hope that the .

**User action**

This is likely to be a configuration error.  Review the description of the exception's message to understand what is not set up correctly and and follow its instructions.


----

### LOVELACE-INSIGHTS-500-004

> The {0} governance action service received an unexpected exception {1} during its processing; the error message was: {2}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action returns an exception to the Governance Action Engine.

**User action**

Use details from the error message to determine the cause of the error and retry the service call once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
