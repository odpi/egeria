<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OWFAuditCode

The OWFAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 3 |
| **Message identifiers begin** | `OPEN-WATCHDOG-ACTION-` |
| **Java class** | `org.odpi.openmetadata.frameworks.openwatchdog.ffdc.OWFAuditCode` |
| **Module** | [open-metadata-implementation/frameworks/open-watchdog-framework](../../open-metadata-implementation/frameworks/open-watchdog-framework) |
| **Source** | [OWFAuditCode.java](../../open-metadata-implementation/frameworks/open-watchdog-framework/src/main/java/org/odpi/openmetadata/frameworks/openwatchdog/ffdc/OWFAuditCode.java) |
| **Further reading** | <https://egeria-project.org/frameworks/owf/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OPEN-WATCHDOG-ACTION-0003](#open-watchdog-action-0003) | INFO | The watchdog action service {0} linked to request type {1} for engine action {2} is ignoring the following notification type action targets: {3} |
| [OPEN-WATCHDOG-ACTION-0004](#open-watchdog-action-0004) | EXCEPTION | The watchdog action service {0} could not read the action targets of engine action {1}; it is working from the {2} action target(s) it was started with.  The {3} exception message was: {4} |
| [OPEN-WATCHDOG-ACTION-0005](#open-watchdog-action-0005) | INFO | The watchdog action service {0} read {1} action target(s) of engine action {2}, of which {3} are notification types it will monitor |

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

### OPEN-WATCHDOG-ACTION-0004

> The watchdog action service {0} could not read the action targets of engine action {1}; it is working from the {2} action target(s) it was started with.  The {3} exception message was: {4}

|  |  |
|---|---|
| **Java constant** | `OWFAuditCode.UNABLE_TO_READ_ACTION_TARGETS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The watchdog action service continues with the action targets it already has, which may be none.  Work given to it since it started is invisible to it until this call succeeds.

**User action**

This is usually a sign that the metadata access server is unreachable or overloaded.  Check the metadata access server, and check whether this message repeats - a service that reports it on every refresh is monitoring nothing.


----

### OPEN-WATCHDOG-ACTION-0005

> The watchdog action service {0} read {1} action target(s) of engine action {2}, of which {3} are notification types it will monitor

|  |  |
|---|---|
| **Java constant** | `OWFAuditCode.ACTION_TARGETS_READ` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The watchdog action service monitors the notification types listed in its action targets.

**User action**

No action is required.  This message says what the service is monitoring; a count of zero means it has been given nothing to do, which is worth investigating if subscriptions are not being serviced.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
