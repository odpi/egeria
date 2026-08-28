<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# GenericHandlersAuditCode

The GenericHandlersAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 15 |
| **Message identifiers begin** | `OMAG-GENERIC-HANDLERS-` |
| **Java class** | `org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersAuditCode` |
| **Module** | [open-metadata-implementation/common-services/generic-handlers](../../open-metadata-implementation/common-services/generic-handlers) |
| **Source** | [GenericHandlersAuditCode.java](../../open-metadata-implementation/common-services/generic-handlers/src/main/java/org/odpi/openmetadata/commonservices/generichandlers/ffdc/GenericHandlersAuditCode.java) |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMAG-GENERIC-HANDLERS-0001](#omag-generic-handlers-0001) | ERROR | The Open Metadata Service {0} is not able to set the Anchors classification on entity {1} of type {2} ({3}) during method {4}. The resulting exception was {5} with error message {6} |
| [OMAG-GENERIC-HANDLERS-0007](#omag-generic-handlers-0007) | EXCEPTION | The Open Metadata Service {0} has deleted a relationship, resulting in entity {1} of type {2} ({3}) losing its anchor.An attempt was made to delete this unanchored entity, which failed.  The resulting exception was {5} with error message {6} |
| [OMAG-GENERIC-HANDLERS-0008](#omag-generic-handlers-0008) | TRACE | Ignoring unnecessary update to {0} entity with unique identifier {1} through method {2} by user {3} |
| [OMAG-GENERIC-HANDLERS-0009](#omag-generic-handlers-0009) | TRACE | Ignoring unnecessary update to {0} relationship with unique identifier {1} through method {2} by user {3} |
| [OMAG-GENERIC-HANDLERS-0010](#omag-generic-handlers-0010) | TRACE | Ignoring unnecessary update to {0} classification attached to entity with unique identifier {1} through method {2} by user {3} |
| [OMAG-GENERIC-HANDLERS-0011](#omag-generic-handlers-0011) | INFO | Template {0} was used to create new {1} element {2} with additional mapping to the following entities {3} and relationships {4} |
| [OMAG-GENERIC-HANDLERS-0020](#omag-generic-handlers-0020) | STARTUP | Initializing a new engine action {0} for request type {1} to run on governance engine {2} with receivedGuards of {3}, mandatoryGuards of {4}, supplied with request parameters {5} and a start time of {6} at the request of {7} |
| [OMAG-GENERIC-HANDLERS-0021](#omag-generic-handlers-0021) | STARTUP | Initializing a new engine action {0} from governance action process step {1} for request type {2} to run on governance engine {3} with receivedGuards of {4}, mandatoryGuards of {5}, supplied with request parameters {6} and a start time of {7} as part of process {8} |
| [OMAG-GENERIC-HANDLERS-0022](#omag-generic-handlers-0022) | STARTUP | Adding action target {0} ({1}) to engine action {2} ({3}) |
| [OMAG-GENERIC-HANDLERS-0024](#omag-generic-handlers-0024) | STARTUP | Governance engine with {0} userId has successfully claimed engine action {1} |
| [OMAG-GENERIC-HANDLERS-0025](#omag-generic-handlers-0025) | INFO | Status changed from {0} to {1} for engine action {2} by governance engine with {3} userId |
| [OMAG-GENERIC-HANDLERS-0027](#omag-generic-handlers-0027) | SHUTDOWN | Engine action {0} has been cancelled by user {1}, it was in {2} status before the cancel request |
| [OMAG-GENERIC-HANDLERS-0028](#omag-generic-handlers-0028) | INFO | Method {0} was unable to receive list of entities due to a {1} exception with message {2} |
| [OMAG-GENERIC-HANDLERS-0029](#omag-generic-handlers-0029) | INFO | Method {0} detected multiple {1} entities with a {2} of {3}; they have been linked with PeerDuplicateLink relationships with a status of DISCOVERED.  The entities are {4} |
| [OMAG-GENERIC-HANDLERS-0030](#omag-generic-handlers-0030) | ERROR | Method {0} was unable to link the duplicate entities {1} with PeerDuplicateLink relationships due to a {2} exception with message {3} |

----

### OMAG-GENERIC-HANDLERS-0001

> The Open Metadata Service {0} is not able to set the Anchors classification on entity {1} of type {2} ({3}) during method {4}. The resulting exception was {5} with error message {6}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.UNABLE_TO_SET_ANCHORS` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

The server was attempting to add Anchors classifications to a collection of metadata instances that are logically part of the same object.  This classification is used to optimize the retrieval and maintenance of complex objects.  It is optional function.  The server continues to process the original request which will complete successfully unless something else goes wrong.

**User action**

No specific action is required.  This message is to highlight that the retrieval and management of metadata is not optimalbecause none of the repositories in the cohort support the Anchors classification.  To enable the optimization provided through the Anchors classification, add an Egeria native metadata server to the cohort.  This will provide the support for the Anchors classification.


----

### OMAG-GENERIC-HANDLERS-0007

> The Open Metadata Service {0} has deleted a relationship, resulting in entity {1} of type {2} ({3}) losing its anchor.An attempt was made to delete this unanchored entity, which failed.  The resulting exception was {5} with error message {6}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.UNABLE_TO_DELETE_UNANCHORED_BEAN` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{5}`, `{6}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

The server attempted to delete an entity that had lost its anchor. As the relationship was successfully deleted, the call succeeds.

**User action**

This message is to highlight that an entity has lost its anchor, and a delete was attempted on it, but failed. An administrator should assess what is required for the entity, and either delete it or supply a new anchor for it.


----

### OMAG-GENERIC-HANDLERS-0008

> Ignoring unnecessary update to {0} entity with unique identifier {1} through method {2} by user {3}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.IGNORING_UNNECESSARY_ENTITY_UPDATE` |
| **Severity** | TRACE - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

No update is made to the entity in the repository because the new properties are the same as the old. In order to determine that the update is unnecessary, Egeria has retrieved the existing entity from the repository and compared it to the new values.

**User action**

Determine if the processing by Egeria is the most efficient way to detect if an update is required to the entity and make adjustments to the caller's logic if appropriate.


----

### OMAG-GENERIC-HANDLERS-0009

> Ignoring unnecessary update to {0} relationship with unique identifier {1} through method {2} by user {3}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.IGNORING_UNNECESSARY_RELATIONSHIP_UPDATE` |
| **Severity** | TRACE - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

No update is made to the relationship in the repository because the new properties are the same as the old. In order to determine that the update is unnecessary, Egeria has retrieved the existing relationship from the repository and compared it to the new values.

**User action**

Determine if the processing by Egeria is the most efficient way to detect if an update is required to the relationship and make adjustments to the caller's logic if appropriate.


----

### OMAG-GENERIC-HANDLERS-0010

> Ignoring unnecessary update to {0} classification attached to entity with unique identifier {1} through method {2} by user {3}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.IGNORING_UNNECESSARY_CLASSIFICATION_UPDATE` |
| **Severity** | TRACE - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

No update is made to the classification in the repository because the new properties are the same as the old. In order to determine that the update is unnecessary, Egeria has retrieved the existing entity from the repository and compared the classification properties to the new values.

**User action**

Determine if the processing by Egeria is the most efficient way to detect if an update is required to the classification and make adjustments to the caller's logic if appropriate.


----

### OMAG-GENERIC-HANDLERS-0011

> Template {0} was used to create new {1} element {2} with additional mapping to the following entities {3} and relationships {4}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.TEMPLATE_MAPPING_SUMMARY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

A new element has been created from a template.  The ma[ping of the entities and relationships is shown.

**User action**

Check that a complete mapping from the template to the new element is correct.


----

### OMAG-GENERIC-HANDLERS-0020

> Initializing a new engine action {0} for request type {1} to run on governance engine {2} with receivedGuards of {3}, mandatoryGuards of {4}, supplied with request parameters {5} and a start time of {6} at the request of {7}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.INITIATE_ENGINE_ACTION` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

A new EngineAction entity is created.  This will be picked up by the Open Metadata Stores running in the connected cohorts and passed onto their connected engine hosts.  These engine hosts will compete for the right to execute the engine action.

**User action**

Validate that this engine action should be initialized.  If so, check that the Governance Engine OMASs running in the connected cohorts publish the engine action to their connected engine host(s).


----

### OMAG-GENERIC-HANDLERS-0021

> Initializing a new engine action {0} from governance action process step {1} for request type {2} to run on governance engine {3} with receivedGuards of {4}, mandatoryGuards of {5}, supplied with request parameters {6} and a start time of {7} as part of process {8}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.INITIATE_ENGINE_ACTION_FROM_PROCESS_STEP` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}`, `{8}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

A new EngineAction entity is created using the definition of the governance action process step.  This will be picked up by the Governance Engine OMASs running in the connected cohorts and passed onto their connected engine hosts.  These engine hosts will compete for the right to execute the engine action.

**User action**

Validate that this engine action should be initialized using this type.  If so, check that the Governance Engine OMASs running in the connected cohorts publish the engine action to their connected engine host(s).


----

### OMAG-GENERIC-HANDLERS-0022

> Adding action target {0} ({1}) to engine action {2} ({3})

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.ADD_ACTION_TARGETS` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

The engine action is linked to the action target so that it is made available to the governance service when it runs.

**User action**

Validate that this action target should be added to the engine action.


----

### OMAG-GENERIC-HANDLERS-0024

> Governance engine with {0} userId has successfully claimed engine action {1}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.SUCCESSFUL_ACTION_CLAIM_REQUEST` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

The engine action is updated to show that the governance engine has claimed it and that its status is now WAITING.  This will be successful if the governance engine is the first to claim the engine action and it is in APPROVED status.

**User action**

Validate that only one of the governance engines successfully claims the engine action.


----

### OMAG-GENERIC-HANDLERS-0025

> Status changed from {0} to {1} for engine action {2} by governance engine with {3} userId

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.ENGINE_ACTION_STATUS_CHANGE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

The engine action's status has been updated as requested.

**User action**

Validate that the status change is valid.


----

### OMAG-GENERIC-HANDLERS-0027

> Engine action {0} has been cancelled by user {1}, it was in {2} status before the cancel request

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.ENGINE_ACTION_CANCELLED` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

The engine action is updated to show that it was cancelled.  If a governance service is running in an engine host, it is informed and it will attempt to stop the service as fast as possible.

**User action**

Monitor the shutdown of the request in the engine host.


----

### OMAG-GENERIC-HANDLERS-0028

> Method {0} was unable to receive list of entities due to a {1} exception with message {2}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.FAILED_TO_RETRIEVE_ENTITIES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |

**System action**

The generic handlers were unable to perform a bulk retrieval of the entities.  They will be retrieved individually.

**User action**

The bulk retrieval is more efficient.  However, one or more of the repositories in use may not support this request.  The individual retrieval still provides the same security protection - it is just slower to execute.


----

### OMAG-GENERIC-HANDLERS-0029

> Method {0} detected multiple {1} entities with a {2} of {3}; they have been linked with PeerDuplicateLink relationships with a status of DISCOVERED.  The entities are {4}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.DISCOVERED_DUPLICATES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/features/duplicate-management/overview/> |

**System action**

The duplicate entities are linked together to record that they have been detected.  No KnownDuplicate classifications are added, and the status of the new relationships means that the retrieval processing continues to return the entities separately.  The original request fails because the server is unable to determine which of the entities to use.

**User action**

Review the linked entities.  If they are genuine duplicates, add the KnownDuplicate classification to each of them and move the status of the PeerDuplicateLink relationships to VALIDATED so that the retrieval processing combines them.  If they are not duplicates, remove the relationships and correct the duplicated name.


----

### OMAG-GENERIC-HANDLERS-0030

> Method {0} was unable to link the duplicate entities {1} with PeerDuplicateLink relationships due to a {2} exception with message {3}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersAuditCode.UNABLE_TO_MARK_DUPLICATES` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/features/duplicate-management/overview/> |

**System action**

The duplicate entities were detected but they have not been linked together, so there is no record of the detection in the open metadata ecosystem.  The original request still fails because the server is unable to determine which of the entities to use.

**User action**

Use the details of the exception to determine why the relationships could not be created.  The duplicates themselves are listed in the message and can be linked manually.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
