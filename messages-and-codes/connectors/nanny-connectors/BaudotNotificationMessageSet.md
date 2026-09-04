<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# BaudotNotificationMessageSet

The BaudotNotificationMessageSet is used to define the message content for the notifications from Baudot.

|  |  |
|---|---|
| **Type of message** | Notification messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `BAUDOT-SUBSCRIPTION-MANAGEMENT-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.baudot.BaudotNotificationMessageSet` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [BaudotNotificationMessageSet.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/baudot/BaudotNotificationMessageSet.java) |
| **Further reading** | <https://egeria-project.org/concepts/notification/> |


## Messages

| Message Id | Message |
|---|---|
| [BAUDOT-SUBSCRIPTION-MANAGEMENT-0001](#baudot-subscription-management-0001) | Welcome to your subscription for product subscription type: {0} ({1}) |
| [BAUDOT-SUBSCRIPTION-MANAGEMENT-0002](#baudot-subscription-management-0002) | The subscription for the following subscription type has been terminated: {0} ({1}) |
| [BAUDOT-SUBSCRIPTION-MANAGEMENT-0003](#baudot-subscription-management-0003) | The monitored {0} resource {1} ({2}) has changed for subscription type: {3} ({4}) |
| [BAUDOT-SUBSCRIPTION-MANAGEMENT-0004](#baudot-subscription-management-0004) | Your subscription to {0} ({1}) has been triggered |
| [BAUDOT-SUBSCRIPTION-MANAGEMENT-0005](#baudot-subscription-management-0005) | A regular notification for subscription type {0} ({1}) has been triggered.  It will trigger again no sooner than {2} minutes from now |

----

### BAUDOT-SUBSCRIPTION-MANAGEMENT-0001

> Welcome to your subscription for product subscription type: {0} ({1})

|  |  |
|---|---|
| **Java constant** | `BaudotNotificationMessageSet.NEW_SUBSCRIBER` |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The subscription manager has received your subscription request.  New notifications will be sent each time the monitored resources associated with your subscription have changed until you unsubscribe.

**User action**

No specific action is required.  This message is to confirm that the subscription is in place.  You can unsubscribe from this subscription type using the subscription type guid supplied in the welcome message.


----

### BAUDOT-SUBSCRIPTION-MANAGEMENT-0002

> The subscription for the following subscription type has been terminated: {0} ({1})

|  |  |
|---|---|
| **Java constant** | `BaudotNotificationMessageSet.CANCELLED_SUBSCRIBER` |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The subscription manager has removed you from the subscriber list and no further notifications will be sent.

**User action**

No specific action is required.  This message is to confirm the subscription has been cancelled. You may subscribe again at any time, using the subscription type guid supplied in the cancellation message.


----

### BAUDOT-SUBSCRIPTION-MANAGEMENT-0003

> The monitored {0} resource {1} ({2}) has changed for subscription type: {3} ({4})

|  |  |
|---|---|
| **Java constant** | `BaudotNotificationMessageSet.MONITORED_RESOURCE_CHANGED` |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The subscription manager has detected a change in one of the monitored resources for the subscription type and has sent this notification to inform you.

**User action**

No specific action is required.  This message is to inform you of the change.  If you no longer which to receive these types of notifications, you can unsubscribe from this subscription type using the subscription type guid supplied in the welcome message.


----

### BAUDOT-SUBSCRIPTION-MANAGEMENT-0004

> Your subscription to {0} ({1}) has been triggered

|  |  |
|---|---|
| **Java constant** | `BaudotNotificationMessageSet.ONE_TIME_NOTIFICATION` |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The subscription manager has been requested to send this notification to you.  It is a one-time notification.

**User action**

No specific action is required.  This message is to inform you of the one-time notification.


----

### BAUDOT-SUBSCRIPTION-MANAGEMENT-0005

> A regular notification for subscription type {0} ({1}) has been triggered.  It will trigger again no sooner than {2} minutes from now

|  |  |
|---|---|
| **Java constant** | `BaudotNotificationMessageSet.PERIODIC_NOTIFICATION` |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The subscription manager has been requested to send this notification to you.  It is a periodic notification.

**User action**

No specific action is required.  This message is to inform you of the periodic notification.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
