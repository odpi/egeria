<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# BaudotAuditCode

The BaudotAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 3 |
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

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
