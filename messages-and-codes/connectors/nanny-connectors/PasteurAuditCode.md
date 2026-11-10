<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# PasteurAuditCode

The PasteurAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 8 |
| **Message identifiers begin** | `PASTEUR-ACTION-HISTORY-CLEANUP-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.pasteur.ffdc.PasteurAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [PasteurAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/pasteur/ffdc/PasteurAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/action/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [PASTEUR-ACTION-HISTORY-CLEANUP-0001](#pasteur-action-history-cleanup-0001) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [PASTEUR-ACTION-HISTORY-CLEANUP-0002](#pasteur-action-history-cleanup-0002) | INFO | The {0} integration connector is starting to clean up the action history through server {1} on platform {2}; the days to clean up are {3}, the completion statuses are {4} and the delete method is {5} |
| [PASTEUR-ACTION-HISTORY-CLEANUP-0003](#pasteur-action-history-cleanup-0003) | INFO | The {0} integration connector has deleted {1} finished actions of type {2} that completed more than {3} days ago using delete method {4} |
| [PASTEUR-ACTION-HISTORY-CLEANUP-0004](#pasteur-action-history-cleanup-0004) | ERROR | The {0} integration connector is unable to delete action {1} of type {2} because of exception {3} with message {4} |
| [PASTEUR-ACTION-HISTORY-CLEANUP-0005](#pasteur-action-history-cleanup-0005) | ERROR | The {0} integration connector is ignoring the entries {1} in its daysToCleanUp configuration property because they do not map a type name to a number of days that is zero or more |
| [PASTEUR-ACTION-HISTORY-CLEANUP-0006](#pasteur-action-history-cleanup-0006) | ERROR | The {0} integration connector is ignoring the values {1} in its completionStatuses configuration property because they are not activity statuses |
| [PASTEUR-ACTION-HISTORY-CLEANUP-0007](#pasteur-action-history-cleanup-0007) | ERROR | The {0} integration connector has nothing to clean up because its configuration properties give no types of action to clean up, or no completion statuses |
| [PASTEUR-ACTION-HISTORY-CLEANUP-0008](#pasteur-action-history-cleanup-0008) | ERROR | The {0} integration connector is ignoring the value {1} of its deleteMethod configuration property because it is not a delete method; it is using {2} |

----

### PASTEUR-ACTION-HISTORY-CLEANUP-0001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `PasteurAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector stops cleaning up the action history for this refresh.  It tries again on the next refresh.

**User action**

Use the details from the error message to determine the cause of the error and correct it.


----

### PASTEUR-ACTION-HISTORY-CLEANUP-0002

> The {0} integration connector is starting to clean up the action history through server {1} on platform {2}; the days to clean up are {3}, the completion statuses are {4} and the delete method is {5}

|  |  |
|---|---|
| **Java constant** | `PasteurAuditCode.STARTING_CONNECTOR` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

On each refresh the connector deletes the actions of each type listed in the days to clean up whose activity status is one of the completion statuses and that completed at least that many days ago, using the delete method.  With the default, LOOK\_FOR\_LINEAGE, actions with lineage relationships are archived and the others are soft-deleted.

**User action**

No action is required.  This message is for monitoring the start up of the action history clean-up.


----

### PASTEUR-ACTION-HISTORY-CLEANUP-0003

> The {0} integration connector has deleted {1} finished actions of type {2} that completed more than {3} days ago using delete method {4}

|  |  |
|---|---|
| **Java constant** | `PasteurAuditCode.ACTIONS_DELETED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The actions, and the elements anchored to them, have been removed.  With the default LOOK\_FOR\_LINEAGE delete method, those with lineage relationships are archived so lineage queries can still find them, and the others are soft-deleted; either way they can be restored if they were removed in error.  ARCHIVE archives them all, SOFT\_DELETE soft-deletes them all and PURGE removes them permanently.

**User action**

No action is required.  This message is for monitoring the progress of the action history clean-up.


----

### PASTEUR-ACTION-HISTORY-CLEANUP-0004

> The {0} integration connector is unable to delete action {1} of type {2} because of exception {3} with message {4}

|  |  |
|---|---|
| **Java constant** | `PasteurAuditCode.DELETE_FAILED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The action is left in place and the connector carries on with the other actions.  It tries to delete this action again on the next refresh.

**User action**

Use the details from the error message to determine why the action could not be deleted - for example, the connector's user may not be permitted to delete elements in the action's governance zones - and correct it.


----

### PASTEUR-ACTION-HISTORY-CLEANUP-0005

> The {0} integration connector is ignoring the entries {1} in its daysToCleanUp configuration property because they do not map a type name to a number of days that is zero or more

|  |  |
|---|---|
| **Java constant** | `PasteurAuditCode.INVALID_DAYS_TO_CLEAN_UP` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The actions of these types are not cleaned up, unless one of their supertypes has a valid entry.

**User action**

Correct the daysToCleanUp configuration property in the connector's connection.  Each key is the name of Action or one of its subtypes and each value is a whole number of days.


----

### PASTEUR-ACTION-HISTORY-CLEANUP-0006

> The {0} integration connector is ignoring the values {1} in its completionStatuses configuration property because they are not activity statuses

|  |  |
|---|---|
| **Java constant** | `PasteurAuditCode.INVALID_COMPLETION_STATUSES` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The actions with these statuses are not cleaned up.

**User action**

Correct the completionStatuses configuration property in the connector's connection.  Each value is the symbolic name of one of the values of the ActivityStatus enum, such as COMPLETED, INVALID or FAILED.


----

### PASTEUR-ACTION-HISTORY-CLEANUP-0007

> The {0} integration connector has nothing to clean up because its configuration properties give no types of action to clean up, or no completion statuses

|  |  |
|---|---|
| **Java constant** | `PasteurAuditCode.NOTHING_TO_CLEAN_UP` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The connector runs, but it does not delete any actions, so the action history grows without bound.

**User action**

Set the daysToCleanUp configuration property in the connector's connection to the types of action to clean up, and check the completionStatuses configuration property.


----

### PASTEUR-ACTION-HISTORY-CLEANUP-0008

> The {0} integration connector is ignoring the value {1} of its deleteMethod configuration property because it is not a delete method; it is using {2}

|  |  |
|---|---|
| **Java constant** | `PasteurAuditCode.INVALID_DELETE_METHOD` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector removes the finished actions using the default delete method.

**User action**

Correct the deleteMethod configuration property in the connector's connection.  It is the symbolic name of one of the values of the DeleteMethod enum: ARCHIVE, SOFT\_DELETE, LOOK\_FOR\_LINEAGE or PURGE.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
