<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OWFAuditCode

The OWFAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `OPEN-WATCHDOG-ACTION-` |
| **Java class** | `org.odpi.openmetadata.frameworks.openwatchdog.ffdc.OWFAuditCode` |
| **Module** | [open-metadata-implementation/frameworks/open-watchdog-framework](../../open-metadata-implementation/frameworks/open-watchdog-framework) |
| **Source** | [OWFAuditCode.java](../../open-metadata-implementation/frameworks/open-watchdog-framework/src/main/java/org/odpi/openmetadata/frameworks/openwatchdog/ffdc/OWFAuditCode.java) |
| **Further reading** | <https://egeria-project.org/frameworks/owf/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OPEN-WATCHDOG-ACTION-0003](#open-watchdog-action-0003) | INFO | The watchdog action service {0} linked to request type {1} for engine action {2} is ignoring the following notification type action targets: {3} |

----

### OPEN-WATCHDOG-ACTION-0003

> The watchdog action service {0} linked to request type {1} for engine action {2} is ignoring the following notification type action targets: {3}

|  |  |
|---|---|
| **Java constant** | `OWFAuditCode.IGNORING_NOTIFICATION_TYPES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The watchdog action service is only processing notification types connected to this service as action types if the activity status is either null, REQUESTED, APPROVED, IN\_PROGRESS, or WAITING.  The other notification types are ignored.

**User action**

Validate that the activity status of the ignored notification types is correct.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
