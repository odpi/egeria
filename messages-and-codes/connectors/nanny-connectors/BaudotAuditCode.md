<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# BaudotAuditCode

The BaudotAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 6 |
| **Message identifiers begin** | `BAUDOT-SUBSCRIPTION-MANAGER-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.baudot.ffdc.BaudotAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [BaudotAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/baudot/ffdc/BaudotAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/notification-type/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [BAUDOT-SUBSCRIPTION-MANAGER-0002](#baudot-subscription-manager-0002) | INFO | The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where notifications are triggered by changes to its monitored resources.   {3} monitored resources are currently registered |
| [BAUDOT-SUBSCRIPTION-MANAGER-0003](#baudot-subscription-manager-0003) | INFO | The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where only one notification is sent to each subscriber |
| [BAUDOT-SUBSCRIPTION-MANAGER-0005](#baudot-subscription-manager-0005) | INFO | The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where a notification is sent to each subscriber on a regular schedule every {3} minutes.  The next scheduled notification will be sent at {4} |
| [BAUDOT-SUBSCRIPTION-MANAGER-0011](#baudot-subscription-manager-0011) | ERROR | The {0} governance service is not monitoring notification type {1} ({2}) because it could not be retrieved, or is not a notification type |
| [BAUDOT-SUBSCRIPTION-MANAGER-0012](#baudot-subscription-manager-0012) | INFO | The {0} governance service is not yet monitoring notification type {1} because it is planned to start at {2} |
| [BAUDOT-SUBSCRIPTION-MANAGER-0013](#baudot-subscription-manager-0013) | INFO | The {0} governance service refreshed its caches: {1} notification type(s) configured, {2} being monitored for the first time by this service |

----

### BAUDOT-SUBSCRIPTION-MANAGER-0002

> The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where notifications are triggered by changes to its monitored resources.   {3} monitored resources are currently registered

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.MONITORED_RESOURCE_NOTIFICATION_TYPE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The governance service monitors the events generated when open metadata elements change.  Notifications are sent if one of the monitored resources (or anything anchored from it) changes, unless another notification has been sent out within the minimumNotificationInterval.

**User action**

This notification pattern was selected because multipleNotificationsPermitted is set to true and notificationInterval is set to 0. Verify that this is the intended behaviour and that the correct elements are linked to this notification type using the MonitoredResource relationship.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0003

> The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where only one notification is sent to each subscriber

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.ONE_TIME_NOTIFICATION_TYPE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance service sends a notification to each subscriber registered when the service starts up and then monitors for new subscribers in order to send them a notification.

**User action**

This notification pattern was selected because multipleNotificationsPermitted is set to false. Validate that this is the right pattern.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0005

> The {0} governance service is refreshing its cache for configured notification type {1} ({2}) where a notification is sent to each subscriber on a regular schedule every {3} minutes.  The next scheduled notification will be sent at {4}

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.PERIODIC_NOTIFICATION_TYPE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The governance service sends a notification to each subscriber registered when the service starts up and then monitors for new subscribers in order to send them notifications.  Additional notifications are sent to each active subscriber every notification interval.

**User action**

This notification pattern was selected because multipleNotificationsPermitted is set to true and notificationInterval is greater than 0. Validate that this is the intended behaviour and the notification interval is appropriate.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0011

> The {0} governance service is not monitoring notification type {1} ({2}) because it could not be retrieved, or is not a notification type

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.UNREADABLE_NOTIFICATION_TYPE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The notification type is skipped.  Its subscribers receive nothing.

**User action**

Check that the element named as an action target of this service is a notification type, and that this service's userId can read it.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0012

> The {0} governance service is not yet monitoring notification type {1} because it is planned to start at {2}

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.NOTIFICATION_TYPE_NOT_STARTED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The notification type is skipped until its planned start date has passed.

**User action**

No action is required if the start date is intended.  A notification type whose subscribers are waiting for data has the wrong start date.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0013

> The {0} governance service refreshed its caches: {1} notification type(s) configured, {2} being monitored for the first time by this service

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.CACHE_REFRESHED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The service monitors the notification types it has been given and notifies their subscribers.

**User action**

No action is required.  A refresh that reports zero notification types is monitoring nothing, and subscriptions to any product will not be delivered.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
