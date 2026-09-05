<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# SmartCollectionsAuditCode

The SmartCollectionsAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 4 |
| **Message identifiers begin** | `SMART-COLLECTIONS-INTEGRATION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.smartcollections.ffdc.SmartCollectionsAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/smart-collections-integration-connector](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/smart-collections-integration-connector) |
| **Source** | [SmartCollectionsAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/smart-collections-integration-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/smartcollections/ffdc/SmartCollectionsAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/integration-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0001](#smart-collections-integration-connector-0001) | INFO | Connector {0} is refreshing the membership of results set {1} |
| [SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0002](#smart-collections-integration-connector-0002) | ERROR | Connector {0} found {1} saved queries attached to results set {2}; expecting to find exactly one |
| [SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0003](#smart-collections-integration-connector-0003) | INFO | Connector {0} completed the membership refresh of results set {1}: {2} members added, {3} members removed |
| [SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0004](#smart-collections-integration-connector-0004) | EXCEPTION | The Smart Collections Integration Connector {0} received an unexpected {1} exception during method {2} while refreshing results set {3}; the error message was: {4} |

----

### SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0001

> Connector {0} is refreshing the membership of results set {1}

|  |  |
|---|---|
| **Java constant** | `SmartCollectionsAuditCode.REFRESHING_RESULTS_SET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is about to run the saved query attached to the results set and update its membership to match the results.

**User action**

No action is required.  This message records that a refresh of the results set has begun.


----

### SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0002

> Connector {0} found {1} saved queries attached to results set {2}; expecting to find exactly one

|  |  |
|---|---|
| **Java constant** | `SmartCollectionsAuditCode.WRONG_NUMBER_OF_SAVED_QUERIES` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is unable to determine which saved query to run, so it is leaving the membership of the results set unchanged.

**User action**

Ensure that exactly one SavedQuery entity is linked to the results set via the SmartQuery relationship.


----

### SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0003

> Connector {0} completed the membership refresh of results set {1}: {2} members added, {3} members removed

|  |  |
|---|---|
| **Java constant** | `SmartCollectionsAuditCode.RESULTS_SET_REFRESHED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector has finished comparing the results of the saved query with the current membership of the results set.

**User action**

No action is required, but the counts in this message show how much the membership of the results set changed.


----

### SMART-COLLECTIONS-INTEGRATION-CONNECTOR-0004

> The Smart Collections Integration Connector {0} received an unexpected {1} exception during method {2} while refreshing results set {3}; the error message was: {4}

|  |  |
|---|---|
| **Java constant** | `SmartCollectionsAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector was unable to complete the membership refresh for this results set.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
