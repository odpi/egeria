<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# MendelAuditCode

The MendelAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 16 |
| **Message identifiers begin** | `MENDEL-DUPLICATE-MANAGER-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.mendel.ffdc.MendelAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [MendelAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/mendel/ffdc/MendelAuditCode.java) |
| **Further reading** | <https://egeria-project.org/features/duplicate-management/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [MENDEL-DUPLICATE-MANAGER-0001](#mendel-duplicate-manager-0001) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [MENDEL-DUPLICATE-MANAGER-0002](#mendel-duplicate-manager-0002) | INFO | The {0} integration connector is starting to manage the duplicates in server {1} on platform {2}; validated duplicates are consolidated once there are {3} of them linked together |
| [MENDEL-DUPLICATE-MANAGER-0003](#mendel-duplicate-manager-0003) | INFO | The {0} integration connector has created the {1} person role ({2}) to receive the to dos raised for duplicates that it can not resolve |
| [MENDEL-DUPLICATE-MANAGER-0004](#mendel-duplicate-manager-0004) | INFO | The {0} integration connector has validated the duplicate link ({1}) between elements {2} and {3} |
| [MENDEL-DUPLICATE-MANAGER-0005](#mendel-duplicate-manager-0005) | INFO | The {0} integration connector has created to do {1} to request that a steward reviews the duplicate link ({2}) between elements {3} and {4} |
| [MENDEL-DUPLICATE-MANAGER-0006](#mendel-duplicate-manager-0006) | INFO | The {0} integration connector has removed the KnownDuplicate classification from element {1} |
| [MENDEL-DUPLICATE-MANAGER-0007](#mendel-duplicate-manager-0007) | INFO | The {0} integration connector has created consolidated element {1} from {2} duplicate {3} elements |
| [MENDEL-DUPLICATE-MANAGER-0011](#mendel-duplicate-manager-0011) | DECISION | The {0} integration connector is discarding the value ({1}) that element {2} supplies for the {3} property, because the more recently updated element {4} in the same cluster of duplicates supplies ({5}) |
| [MENDEL-DUPLICATE-MANAGER-0012](#mendel-duplicate-manager-0012) | DECISION | The {0} integration connector is discarding the {1} property ({2}) supplied by element {3} because it is not a property of {4}, the type of the consolidated element |
| [MENDEL-DUPLICATE-MANAGER-0016](#mendel-duplicate-manager-0016) | DECISION | The {0} integration connector is discarding the {1} property ({2}) of the {3} classification supplied by element {4} because it is not a property of that classification |
| [MENDEL-DUPLICATE-MANAGER-0013](#mendel-duplicate-manager-0013) | DECISION | The {0} integration connector is discarding the {1} classification ({2}) from element {3} because the more recently updated element {4} in the same cluster of duplicates carries the same classification with different properties ({5}) |
| [MENDEL-DUPLICATE-MANAGER-0014](#mendel-duplicate-manager-0014) | DECISION | The {0} integration connector is discarding the {1} classification from element {2} because it can not be attached to {3}, the type of the consolidated element |
| [MENDEL-DUPLICATE-MANAGER-0015](#mendel-duplicate-manager-0015) | DECISION | The {0} integration connector is not copying the {1} relationship between elements {2} and {3} onto consolidated element {4}, because the type only permits one relationship of this kind at the consolidated element's end and a more recently updated member of the cluster has supplied it |
| [MENDEL-DUPLICATE-MANAGER-0009](#mendel-duplicate-manager-0009) | INFO | The {0} integration connector has registered a listener for open metadata events |
| [MENDEL-DUPLICATE-MANAGER-0010](#mendel-duplicate-manager-0010) | ERROR | The {0} integration connector is unable to register a listener for open metadata events due to a {1} exception with message {2} |
| [MENDEL-DUPLICATE-MANAGER-0008](#mendel-duplicate-manager-0008) | INFO | The {0} integration connector has stopped managing the duplicates in server {1} on platform {2} and is shutting down |

----

### MENDEL-DUPLICATE-MANAGER-0001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The service is unable to complete the management of one or more duplicate links.

**User action**

Use the details from the error message to determine the cause of the error and correct it.  The duplicate links that were not processed are picked up the next time they are updated, or they can be resolved by a steward.


----

### MENDEL-DUPLICATE-MANAGER-0002

> The {0} integration connector is starting to manage the duplicates in server {1} on platform {2}; validated duplicates are consolidated once there are {3} of them linked together

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.STARTING_CONNECTOR` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

On each refresh, the connector reviews the duplicate links in the open metadata ecosystem.  It confirms the ones where the linked elements are a close enough match, requests a steward's decision on the rest, removes the duplicate classifications from elements whose duplicate links have all been retired, and consolidates the clusters of validated duplicates that have reached the configured size.

**User action**

No action is required.  This message is for monitoring the start up of the automated duplicate manager.


----

### MENDEL-DUPLICATE-MANAGER-0003

> The {0} integration connector has created the {1} person role ({2}) to receive the to dos raised for duplicates that it can not resolve

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.NEW_STEWARD_ROLE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The role is created because there was no existing role with this name.  All of the to dos raised by this service are assigned to it.

**User action**

Appoint one or more people to this role so that the to dos raised for potential duplicates are acted on.


----

### MENDEL-DUPLICATE-MANAGER-0004

> The {0} integration connector has validated the duplicate link ({1}) between elements {2} and {3}

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.DUPLICATES_VALIDATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The elements are a close enough match to be combined without a steward's involvement.  The status of the duplicate link is set to VALIDATED and the KnownDuplicate classification is added to both elements.  This means the retrieval processing combines the elements from this point on.

**User action**

Review the linked elements if the combined element is not as expected.  Removing the KnownDuplicate classifications, or moving the status of the link away from VALIDATED, separates them again.


----

### MENDEL-DUPLICATE-MANAGER-0005

> The {0} integration connector has created to do {1} to request that a steward reviews the duplicate link ({2}) between elements {3} and {4}

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.STEWARD_ACTION_REQUESTED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The elements are not a close enough match for the service to combine them on its own authority, so the decision is passed to a steward.  The elements are not combined during retrieval until the steward moves the status of the link to VALIDATED and adds the KnownDuplicate classification to both elements.

**User action**

Review the to do and decide whether the linked elements represent the same thing.


----

### MENDEL-DUPLICATE-MANAGER-0006

> The {0} integration connector has removed the KnownDuplicate classification from element {1}

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.RETIRED_DUPLICATE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

All of the element's duplicate links have been retired by a steward, so there is nothing left for it to be combined with.  Removing the classification stops the retrieval processing treating it as a duplicate.

**User action**

No action is required.  This message is for monitoring the resolution of duplicates.


----

### MENDEL-DUPLICATE-MANAGER-0007

> The {0} integration connector has created consolidated element {1} from {2} duplicate {3} elements

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.DUPLICATES_CONSOLIDATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The cluster of validated duplicates has reached the size at which they are combined into a single element.  The consolidated element carries the properties and relationships of its members, and is returned by the retrieval processing in their place.

**User action**

Review the consolidated element.  If it is not as expected, the survivorship rules can be adjusted by correcting the properties of the members, or the consolidation can be reversed by removing the consolidated element.


----

### MENDEL-DUPLICATE-MANAGER-0011

> The {0} integration connector is discarding the value ({1}) that element {2} supplies for the {3} property, because the more recently updated element {4} in the same cluster of duplicates supplies ({5})

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.CONFLICTING_PROPERTY` |
| **Severity** | DECISION - A decision has been made related to the operation of the system. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The consolidated element can only hold one value for a property, so it takes the value from the most recently updated member of the cluster.  The discarded value is still held by the member that supplied it, which is unchanged by the consolidation.

**User action**

Review the two values.  If the discarded value is the correct one, correct the member that supplied the surviving value, and delete the consolidated element so that it is rebuilt.  If the members should not have been combined at all, retire the duplicate links between them.


----

### MENDEL-DUPLICATE-MANAGER-0012

> The {0} integration connector is discarding the {1} property ({2}) supplied by element {3} because it is not a property of {4}, the type of the consolidated element

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.INCOMPATIBLE_PROPERTY` |
| **Severity** | DECISION - A decision has been made related to the operation of the system. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The consolidated element takes its type from the most recently updated member of the cluster.  A property that only an earlier member's type defines has nowhere to go on the consolidated element, and storing it anyway would have the repository reject the consolidation.  The property is still held by the member that supplied it, which is unchanged by the consolidation.

**User action**

This occurs when the members of the cluster are of different types.  Review the members: if the discarded property matters, the cluster should be consolidated into the type that defines it, which means correcting the type of the members, or the members are not duplicates of each other and their duplicate links should be retired.


----

### MENDEL-DUPLICATE-MANAGER-0016

> The {0} integration connector is discarding the {1} property ({2}) of the {3} classification supplied by element {4} because it is not a property of that classification

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.INCOMPATIBLE_CLASSIFICATION_PROPERTY` |
| **Severity** | DECISION - A decision has been made related to the operation of the system. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The classification is still copied to the consolidated element, but without this property.  Storing it anyway would have the repository reject the whole consolidation.  The property is still held by the member that supplied it, which is unchanged by the consolidation.

**User action**

A property that the classification's type does not define means the member was created against a different version of the open metadata types.  Review the member and remove or rename the property so that its classification matches the type in force.


----

### MENDEL-DUPLICATE-MANAGER-0013

> The {0} integration connector is discarding the {1} classification ({2}) from element {3} because the more recently updated element {4} in the same cluster of duplicates carries the same classification with different properties ({5})

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.CONFLICTING_CLASSIFICATION` |
| **Severity** | DECISION - A decision has been made related to the operation of the system. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

Only one classification of each type can be attached to an element, so the consolidated element takes the classification from the most recently updated member of the cluster.  The discarded classification is still attached to the member that supplied it, which is unchanged by the consolidation.

**User action**

Review the two sets of classification properties.  If the discarded classification is the correct one, correct the member that supplied the surviving classification, and delete the consolidated element so that it is rebuilt.


----

### MENDEL-DUPLICATE-MANAGER-0014

> The {0} integration connector is discarding the {1} classification from element {2} because it can not be attached to {3}, the type of the consolidated element

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.INCOMPATIBLE_CLASSIFICATION` |
| **Severity** | DECISION - A decision has been made related to the operation of the system. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The consolidated element takes its type from the most recently updated member of the cluster, and a classification is only valid for the types that its definition names.  Attaching it anyway would have the repository reject the consolidation.  The classification is still attached to the member that supplied it, which is unchanged by the consolidation.

**User action**

This occurs when the members of the cluster are of different types.  Review the members: if the discarded classification matters, the cluster should be consolidated into a type that it can be attached to, which means correcting the type of the members, or the members are not duplicates of each other and their duplicate links should be retired.


----

### MENDEL-DUPLICATE-MANAGER-0015

> The {0} integration connector is not copying the {1} relationship between elements {2} and {3} onto consolidated element {4}, because the type only permits one relationship of this kind at the consolidated element's end and a more recently updated member of the cluster has supplied it

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.CONFLICTING_RELATIONSHIP` |
| **Severity** | DECISION - A decision has been made related to the operation of the system. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The consolidated element keeps the relationship from the most recently updated member of the cluster.  The relationship that is not copied is still in place on the member that supplied it, which is unchanged by the consolidation.

**User action**

Review the relationships of the members.  If the relationship that was not copied is the correct one, correct the member that supplied the surviving relationship, and delete the consolidated element so that it is rebuilt.


----

### MENDEL-DUPLICATE-MANAGER-0009

> The {0} integration connector has registered a listener for open metadata events

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.LISTENER_REGISTERED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The connector has worked through the duplicate links that were waiting for it when it started, and now reviews new and updated duplicate links as they occur rather than waiting for its next refresh.

**User action**

No action is required.  This message is for monitoring the start up of the automated duplicate manager.


----

### MENDEL-DUPLICATE-MANAGER-0010

> The {0} integration connector is unable to register a listener for open metadata events due to a {1} exception with message {2}

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.UNABLE_TO_REGISTER_LISTENER` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector continues to run, but it only reviews duplicate links on each refresh rather than as they occur.  It attempts to register the listener again on its next refresh.

**User action**

Use the details from the error message to determine the cause of the error and correct it.


----

### MENDEL-DUPLICATE-MANAGER-0008

> The {0} integration connector has stopped managing the duplicates in server {1} on platform {2} and is shutting down

|  |  |
|---|---|
| **Java constant** | `MendelAuditCode.CONNECTOR_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is disconnecting.

**User action**

No action is required unless there are errors that follow indicating that there were problems shutting down.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
