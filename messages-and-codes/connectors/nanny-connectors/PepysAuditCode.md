<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# PepysAuditCode

The PepysAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 7 |
| **Message identifiers begin** | `PEPYS-ACTIVITY-OBSERVER-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.pepys.ffdc.PepysAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [PepysAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/pepys/ffdc/PepysAuditCode.java) |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [PEPYS-ACTIVITY-OBSERVER-0001](#pepys-activity-observer-0001) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [PEPYS-ACTIVITY-OBSERVER-0002](#pepys-activity-observer-0002) | INFO | The {0} integration connector is starting to record the usage activity of the anchor elements in server {1} on platform {2}; the activity of users matching {3} is ignored |
| [PEPYS-ACTIVITY-OBSERVER-0003](#pepys-activity-observer-0003) | INFO | The {0} integration connector is listening for audit log records on topic {1} (connection {2}) |
| [PEPYS-ACTIVITY-OBSERVER-0004](#pepys-activity-observer-0004) | INFO | The {0} integration connector has added {1} observed activities to the usage activity counts of {2} elements |
| [PEPYS-ACTIVITY-OBSERVER-0005](#pepys-activity-observer-0005) | ERROR | The {0} integration connector is unable to update the usage activity counts of element {1} because of exception {2} with message {3}; {4} observed activities will be retried on the next refresh |
| [PEPYS-ACTIVITY-OBSERVER-0006](#pepys-activity-observer-0006) | INFO | The {0} integration connector has discarded {1} observed activities for element {2} because it can not be updated: exception {3} with message {4} |
| [PEPYS-ACTIVITY-OBSERVER-0007](#pepys-activity-observer-0007) | ERROR | The {0} integration connector has skipped ignored userId pattern {1} because it is not a valid regular expression: {2} |

----

### PEPYS-ACTIVITY-OBSERVER-0001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `PepysAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector is unable to complete the recording of some of the usage activity it has observed.

**User action**

Use the details from the error message to determine the cause of the error and correct it.


----

### PEPYS-ACTIVITY-OBSERVER-0002

> The {0} integration connector is starting to record the usage activity of the anchor elements in server {1} on platform {2}; the activity of users matching {3} is ignored

|  |  |
|---|---|
| **Java constant** | `PepysAuditCode.STARTING_CONNECTOR` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector listens for the anchor element activity messages written to the audit log topics that are registered as its catalog targets.  On each refresh it adds the activity it has observed to the counts in the UsageActivity classification of each anchor element.  The activity of the users whose userIds match the patterns in the ignoredUserIdPatterns configuration property, and of the connector's own user, is not counted.

**User action**

No action is required.  This message is for monitoring the start up of the activity observer.


----

### PEPYS-ACTIVITY-OBSERVER-0003

> The {0} integration connector is listening for audit log records on topic {1} (connection {2})

|  |  |
|---|---|
| **Java constant** | `PepysAuditCode.LISTENING_TO_TOPIC` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The topic is one of the connector's catalog targets.  Each audit log record received from it that reports activity against an anchor element is counted.

**User action**

No action is required.  This message records the topics that the activity observer is listening to.


----

### PEPYS-ACTIVITY-OBSERVER-0004

> The {0} integration connector has added {1} observed activities to the usage activity counts of {2} elements

|  |  |
|---|---|
| **Java constant** | `PepysAuditCode.USAGE_ACTIVITY_RECORDED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The UsageActivity classification of each of these elements has been created or updated with the activity observed since the previous refresh.

**User action**

No action is required.  This message is for monitoring the progress of the activity observer.


----

### PEPYS-ACTIVITY-OBSERVER-0005

> The {0} integration connector is unable to update the usage activity counts of element {1} because of exception {2} with message {3}; {4} observed activities will be retried on the next refresh

|  |  |
|---|---|
| **Java constant** | `PepysAuditCode.USAGE_ACTIVITY_DEFERRED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The counts are kept in memory and added to the element's UsageActivity classification on the next refresh.  They are lost if the integration daemon is shut down before then.

**User action**

Use the details from the error message to determine why the metadata access server could not be updated, and correct it.


----

### PEPYS-ACTIVITY-OBSERVER-0006

> The {0} integration connector has discarded {1} observed activities for element {2} because it can not be updated: exception {3} with message {4}

|  |  |
|---|---|
| **Java constant** | `PepysAuditCode.USAGE_ACTIVITY_DISCARDED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The element is no longer available - typically because it has been deleted since the activity was observed - or it is not one that a UsageActivity classification can be attached to, or the connector's user is not permitted to classify it.

**User action**

No action is required unless the element is expected to exist.  If the exception is a UserNotAuthorizedException, grant the connector's user permission to classify elements in the element's governance zones so that its usage activity can be recorded.


----

### PEPYS-ACTIVITY-OBSERVER-0007

> The {0} integration connector has skipped ignored userId pattern {1} because it is not a valid regular expression: {2}

|  |  |
|---|---|
| **Java constant** | `PepysAuditCode.INVALID_USER_ID_PATTERN` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector continues to run with the other patterns, so the activity of the users that this pattern was meant to match is counted.

**User action**

Correct the pattern in the ignoredUserIdPatterns configuration property and restart the connector.  The patterns use Java regular expression syntax and are matched against the whole userId - for example, .\*npa matches every userId that ends in npa.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
