<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OGFAuditCode

The OGFAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `OPEN-GOVERNANCE-ACTION-` |
| **Java class** | `org.odpi.openmetadata.frameworks.opengovernance.ffdc.OGFAuditCode` |
| **Module** | [open-metadata-implementation/frameworks/open-governance-framework](../../open-metadata-implementation/frameworks/open-governance-framework) |
| **Source** | [OGFAuditCode.java](../../open-metadata-implementation/frameworks/open-governance-framework/src/main/java/org/odpi/openmetadata/frameworks/opengovernance/ffdc/OGFAuditCode.java) |
| **Further reading** | <https://egeria-project.org/frameworks/ogf/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OPEN-GOVERNANCE-ACTION-0001](#open-governance-action-0001) | INFO | The {0} service is issuing a notification to subscriber {1} of type {2} for notification type {3} (notification count = {4}) |
| [OPEN-GOVERNANCE-ACTION-0002](#open-governance-action-0002) | ERROR | Subscriber {0} for notification type {1} is of a type {2}, but the {3} service only supports the following subscriber type(s): {4} |

----

### OPEN-GOVERNANCE-ACTION-0001

> The {0} service is issuing a notification to subscriber {1} of type {2} for notification type {3} (notification count = {4})

|  |  |
|---|---|
| **Java constant** | `OGFAuditCode.ISSUING_NOTIFICATION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The governance service attempts to notify the subscriber.

**User action**

Verify that this subscriber should be linked to this notification type.  If not, remove the subscriber from the notification type.  If this is a valid subscriber then verify that the notification was successful.  Error messages should be logged if there are any known failures.


----

### OPEN-GOVERNANCE-ACTION-0002

> Subscriber {0} for notification type {1} is of a type {2}, but the {3} service only supports the following subscriber type(s): {4}

|  |  |
|---|---|
| **Java constant** | `OGFAuditCode.WRONG_TYPE_OF_SUBSCRIBER` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The governance service ignores this subscriber.

**User action**

Remove this subscriber from the notification type and replace it with a subscriber type that is supported.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
