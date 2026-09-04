<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# BaudotAuditCode

The BaudotAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 9 |
| **Message identifiers begin** | `BAUDOT-SUBSCRIPTION-MANAGER-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.baudot.ffdc.BaudotAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [BaudotAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/baudot/ffdc/BaudotAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/notification-type/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [BAUDOT-SUBSCRIPTION-MANAGER-0001](#baudot-subscription-manager-0001) | STARTUP | The {0} integration connector is starting; it is connected to metadata access server {1} on platform {2} |
| [BAUDOT-SUBSCRIPTION-MANAGER-0002](#baudot-subscription-manager-0002) | INFO | The {0} integration connector has refreshed notification type {1} ({2}) where notifications are triggered by changes to its monitored resources.  {3} monitored resources are currently registered |
| [BAUDOT-SUBSCRIPTION-MANAGER-0003](#baudot-subscription-manager-0003) | INFO | The {0} integration connector has refreshed notification type {1} ({2}) where only one notification is sent to each subscriber |
| [BAUDOT-SUBSCRIPTION-MANAGER-0004](#baudot-subscription-manager-0004) | SHUTDOWN | The {0} integration connector is stopping |
| [BAUDOT-SUBSCRIPTION-MANAGER-0005](#baudot-subscription-manager-0005) | INFO | The {0} integration connector has refreshed notification type {1} ({2}) where a notification is sent to each subscriber on a regular schedule no more often than every {3} milliseconds.  The connector's next scheduled refresh is at {4} |
| [BAUDOT-SUBSCRIPTION-MANAGER-0006](#baudot-subscription-manager-0006) | EXCEPTION | The {0} integration connector was unable to refresh notification type {1} ({2}); the {3} exception had message {4} |
| [BAUDOT-SUBSCRIPTION-MANAGER-0007](#baudot-subscription-manager-0007) | EXCEPTION | The {0} integration connector received a {1} exception while processing a change event for element {2}; the message was {3} |
| [BAUDOT-SUBSCRIPTION-MANAGER-0011](#baudot-subscription-manager-0011) | ERROR | The {0} integration connector is not monitoring notification type {1} ({2}) because it could not be retrieved, or is not a notification type |
| [BAUDOT-SUBSCRIPTION-MANAGER-0013](#baudot-subscription-manager-0013) | INFO | The {0} integration connector has completed a refresh: {1} notification type(s) are its catalog targets, and {2} resource(s) are being monitored for changes |

----

### BAUDOT-SUBSCRIPTION-MANAGER-0001

> The {0} integration connector is starting; it is connected to metadata access server {1} on platform {2}

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.STARTING_CONNECTOR` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector registers to receive metadata change events, and then waits for the integration daemon to refresh it.  Each refresh notifies the subscribers of every notification type the connector has been given as a catalog target.

**User action**

No action is required.  The notification types this connector looks after are its catalog targets; add one to have its subscribers notified.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0002

> The {0} integration connector has refreshed notification type {1} ({2}) where notifications are triggered by changes to its monitored resources.  {3} monitored resources are currently registered

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.MONITORED_RESOURCE_NOTIFICATION_TYPE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector monitors the events generated when open metadata elements change.  Notifications are sent if one of the monitored resources (or anything anchored from it) changes, unless another notification has been sent out within the minimumNotificationInterval.

**User action**

This notification pattern was selected because the notification type has monitored resources. Verify that this is the intended behaviour and that the correct elements are linked to this notification type using the MonitoredResource relationship.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0003

> The {0} integration connector has refreshed notification type {1} ({2}) where only one notification is sent to each subscriber

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.ONE_TIME_NOTIFICATION_TYPE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector sends a notification to each subscriber that has not yet received one.  New subscribers are noticed on each refresh.

**User action**

This notification pattern was selected because multipleNotificationsPermitted is set to false. Validate that this is the right pattern.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0004

> The {0} integration connector is stopping

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.CONNECTOR_STOPPING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The connector stops listening for metadata change events.  No further notifications are sent until it is restarted.

**User action**

No action is required if the integration daemon is shutting down.  Otherwise check why the connector was stopped.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0005

> The {0} integration connector has refreshed notification type {1} ({2}) where a notification is sent to each subscriber on a regular schedule no more often than every {3} milliseconds.  The connector's next scheduled refresh is at {4}

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.PERIODIC_NOTIFICATION_TYPE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector sends a notification to each new subscriber, and a further notification to each existing subscriber on every refresh that falls outside the notification type's minimum interval.  The refresh interval is part of the connector's configuration in the integration daemon.

**User action**

This notification pattern was selected because multipleNotificationsPermitted is set to true and the notification type has no monitored resources. Validate that this is the intended behaviour and that the connector's refresh interval and the notification type's minimum interval are appropriate together.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0006

> The {0} integration connector was unable to refresh notification type {1} ({2}); the {3} exception had message {4}

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.NOTIFICATION_TYPE_REFRESH_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The notification type is skipped on this refresh and tried again on the next.  The connector's other notification types are unaffected.

**User action**

Use the exception message to determine the cause, and correct it before the next refresh.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0007

> The {0} integration connector received a {1} exception while processing a change event for element {2}; the message was {3}

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.EVENT_PROCESSING_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The event is dropped.  Any notification it should have prompted is not sent.

**User action**

Use the exception message to determine the cause.  If subscribers have missed a change, the next change to the same resource will be notified normally.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0011

> The {0} integration connector is not monitoring notification type {1} ({2}) because it could not be retrieved, or is not a notification type

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.UNREADABLE_NOTIFICATION_TYPE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The notification type is skipped.  Its subscribers receive nothing.

**User action**

Check that the element attached as a catalog target of this connector is a notification type, and that this connector's userId can read it.


----

### BAUDOT-SUBSCRIPTION-MANAGER-0013

> The {0} integration connector has completed a refresh: {1} notification type(s) are its catalog targets, and {2} resource(s) are being monitored for changes

|  |  |
|---|---|
| **Java constant** | `BaudotAuditCode.REFRESH_COMPLETE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector notifies the subscribers of the notification types it has been given as catalog targets.

**User action**

No action is required.  A refresh that reports zero notification types is monitoring nothing, and subscriptions to any product will not be delivered: check that the connector that creates the notification types is adding them to this connector as catalog targets.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
