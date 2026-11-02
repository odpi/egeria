<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# SimonAuditCode

The SimonAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 7 |
| **Message identifiers begin** | `SIMON-CONTRIBUTION-RECORDER-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.simon.ffdc.SimonAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [SimonAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/simon/ffdc/SimonAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/karma-point/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [SIMON-CONTRIBUTION-RECORDER-0001](#simon-contribution-recorder-0001) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [SIMON-CONTRIBUTION-RECORDER-0002](#simon-contribution-recorder-0002) | INFO | The {0} integration connector is starting to award karma points for the changes made through server {1} on platform {2}; users matching {3} are not awarded karma points |
| [SIMON-CONTRIBUTION-RECORDER-0003](#simon-contribution-recorder-0003) | INFO | The {0} integration connector has added {1} karma points to the contribution records of {2} users |
| [SIMON-CONTRIBUTION-RECORDER-0004](#simon-contribution-recorder-0004) | ERROR | The {0} integration connector is unable to update the contribution record of user {1} because of exception {2} with message {3}; {4} karma points will be retried on the next refresh |
| [SIMON-CONTRIBUTION-RECORDER-0005](#simon-contribution-recorder-0005) | INFO | The {0} integration connector has discarded {1} karma points for user {2} because their contribution record can not be updated: exception {3} with message {4} |
| [SIMON-CONTRIBUTION-RECORDER-0006](#simon-contribution-recorder-0006) | INFO | The {0} integration connector has discarded {1} karma points for users {2} because they do not have an actor profile |
| [SIMON-CONTRIBUTION-RECORDER-0007](#simon-contribution-recorder-0007) | ERROR | The {0} integration connector has skipped ignored userId pattern {1} because it is not a valid regular expression: {2} |

----

### SIMON-CONTRIBUTION-RECORDER-0001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `SimonAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector is unable to complete the recording of some of the karma points it has awarded.

**User action**

Use the details from the error message to determine the cause of the error and correct it.


----

### SIMON-CONTRIBUTION-RECORDER-0002

> The {0} integration connector is starting to award karma points for the changes made through server {1} on platform {2}; users matching {3} are not awarded karma points

|  |  |
|---|---|
| **Java constant** | `SimonAuditCode.STARTING_CONNECTOR` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector listens for the open metadata events and awards a karma point to the user responsible for each change.  On each refresh it adds the karma points awarded since the previous refresh to the ContributionRecord attached to each user's actor profile.  The users whose userIds match the patterns in the ignoredUserIdPatterns configuration property, and the connector's own user, are not awarded karma points.

**User action**

No action is required.  This message is for monitoring the start up of the contribution recorder.


----

### SIMON-CONTRIBUTION-RECORDER-0003

> The {0} integration connector has added {1} karma points to the contribution records of {2} users

|  |  |
|---|---|
| **Java constant** | `SimonAuditCode.KARMA_POINTS_RECORDED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The ContributionRecord of each of these users has been created or updated with the karma points awarded since the previous refresh.

**User action**

No action is required.  This message is for monitoring the progress of the contribution recorder.


----

### SIMON-CONTRIBUTION-RECORDER-0004

> The {0} integration connector is unable to update the contribution record of user {1} because of exception {2} with message {3}; {4} karma points will be retried on the next refresh

|  |  |
|---|---|
| **Java constant** | `SimonAuditCode.KARMA_POINTS_DEFERRED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The karma points are kept in memory and added to the user's ContributionRecord on the next refresh.  They are lost if the integration daemon is shut down before then.

**User action**

Use the details from the error message to determine why the metadata access server could not be updated, and correct it.


----

### SIMON-CONTRIBUTION-RECORDER-0005

> The {0} integration connector has discarded {1} karma points for user {2} because their contribution record can not be updated: exception {3} with message {4}

|  |  |
|---|---|
| **Java constant** | `SimonAuditCode.KARMA_POINTS_DISCARDED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The user's actor profile or contribution record can not be retrieved or changed by the connector's user - for example, it is in a governance zone that the connector's user can not update.  Trying again would fail in the same way, so the karma points are not kept.

**User action**

No action is required unless the user's karma points are expected to be recorded.  If the exception is a UserNotAuthorizedException, grant the connector's user permission to update the actor profiles and their contribution records.


----

### SIMON-CONTRIBUTION-RECORDER-0006

> The {0} integration connector has discarded {1} karma points for users {2} because they do not have an actor profile

|  |  |
|---|---|
| **Java constant** | `SimonAuditCode.NO_ACTOR_PROFILE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

Karma points are recorded in the ContributionRecord attached to a user's actor profile.  These users have no UserIdentity linked to an actor profile, so there is nowhere to record their contributions.

**User action**

If the users are people whose contributions should be recognized, create a UserIdentity for each one and link it to their actor profile.  If they are not, add a pattern matching their userIds to the ignoredUserIdPatterns configuration property so that their changes are not counted.


----

### SIMON-CONTRIBUTION-RECORDER-0007

> The {0} integration connector has skipped ignored userId pattern {1} because it is not a valid regular expression: {2}

|  |  |
|---|---|
| **Java constant** | `SimonAuditCode.INVALID_USER_ID_PATTERN` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector continues to run with the other patterns, so the users that this pattern was meant to match are awarded karma points.

**User action**

Correct the pattern in the ignoredUserIdPatterns configuration property and restart the connector.  The patterns use Java regular expression syntax and are matched against the whole userId - for example, .\*npa matches every userId that ends in npa.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
