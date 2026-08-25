<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# RepositoryHandlerAuditCode

The RepositoryHandlerAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 9 |
| **Message identifiers begin** | `OMAG-REPOSITORY-HANDLER-` |
| **Java class** | `org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandlerAuditCode` |
| **Module** | [open-metadata-implementation/common-services/repository-handler](../../open-metadata-implementation/common-services/repository-handler) |
| **Source** | [RepositoryHandlerAuditCode.java](../../open-metadata-implementation/common-services/repository-handler/src/main/java/org/odpi/openmetadata/commonservices/repositoryhandler/RepositoryHandlerAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/repository-handler/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMAG-REPOSITORY-HANDLER-0001](#omag-repository-handler-0001) | INFO | The Open Metadata Service has purged entity {0} of type {1} ({2}) during method {3} because its home repository {4} does not support soft-delete |
| [OMAG-REPOSITORY-HANDLER-0003](#omag-repository-handler-0003) | EXCEPTION | An unexpected error {4} was returned to {5} by the metadata server during {1} request for open metadata access service {2} on server {3}; message was {0} |
| [OMAG-REPOSITORY-HANDLER-0004](#omag-repository-handler-0004) | EXCEPTION | The Open Metadata Service {0} is not able to set the Anchors classification on a new entity of type {1} during method {2}. The resulting exception was {3} with error message {4} |
| [OMAG-REPOSITORY-HANDLER-0009](#omag-repository-handler-0009) | TRACE | A {0} entity with unique identifier {1} has been retrieved by method {2} from service {3} but it is not visible to the caller {4}: effective time is {5}; entity is effective from {6} to {7} with classifications {8} and call parameters of forLineage={9} and forDuplicateProcessing={10} |
| [OMAG-REPOSITORY-HANDLER-0010](#omag-repository-handler-0010) | ERROR | Method {0} called from {1} for service {2} is using function that not supported by any of the metadata repositories connected to {3} - error message is: {4} |
| [OMAG-REPOSITORY-HANDLER-0011](#omag-repository-handler-0011) | TRACE | The Open Metadata Service has soft-deleted entity {0} of type {1} ({2}) during method {3} |
| [OMAG-REPOSITORY-HANDLER-0012](#omag-repository-handler-0012) | TRACE | The Open Metadata Service has soft-deleted relationship {0} of type {1} ({2}) between entity {3} of type {4} ({5}) and entity {6} of type {7} ({8}) during method {9} |
| [OMAG-REPOSITORY-HANDLER-0013](#omag-repository-handler-0013) | INFO | The Open Metadata Service is retrying the {0} classification of entity {1} due to a race condition.  The original {2} exception returned from the classification request had an error message of {3} |
| [OMAG-REPOSITORY-HANDLER-0014](#omag-repository-handler-0014) | INFO | Successfully deduplicated relationships {0} down to {1} |

----

### OMAG-REPOSITORY-HANDLER-0001

> The Open Metadata Service has purged entity {0} of type {1} ({2}) during method {3} because its home repository {4} does not support soft-delete

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerAuditCode.ENTITY_PURGED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

Repository where this entity is mastered does not support the soft-delete function and so a purge operation was performed. This means that the delete can not be undone.

**User action**

No specific action is required.  This message is to highlight that the entity can no longer be restored.  If this behavior is unacceptable, then it is possible to re-home the entity to a repository that supports soft-delete.


----

### OMAG-REPOSITORY-HANDLER-0003

> An unexpected error {4} was returned to {5} by the metadata server during {1} request for open metadata access service {2} on server {3}; message was {0}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerAuditCode.PROPERTY_SERVER_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot process the request because of an internal error.

**User action**

Verify the sanity of the server.  This is probably a logic error.  If you can not work out what happened, ask the Egeria community for help.


----

### OMAG-REPOSITORY-HANDLER-0004

> The Open Metadata Service {0} is not able to set the Anchors classification on a new entity of type {1} during method {2}. The resulting exception was {3} with error message {4}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerAuditCode.UNABLE_TO_SET_ANCHORS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The server was attempting to add Anchors classifications to a collection of metadata instances that are logically part of the same object.  This classification is used to optimize the retrieval and maintenance of complex objects.  It is optional function.  The server continues to process the original request which will complete successfully unless something else goes wrong.

**User action**

No specific action is required.  This message is to highlight that the retrieval and management of metadata is not optimalbecause none of the repositories in the cohort support the Anchors classification.  To enable the optimization provided through the Anchors classification, add an Egeria native metadata server to the cohort.  This will provide the support for the Anchors classification.


----

### OMAG-REPOSITORY-HANDLER-0009

> A {0} entity with unique identifier {1} has been retrieved by method {2} from service {3} but it is not visible to the caller {4}: effective time is {5}; entity is effective from {6} to {7} with classifications {8} and call parameters of forLineage={9} and forDuplicateProcessing={10}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerAuditCode.UNAVAILABLE_ENTITY` |
| **Severity** | TRACE - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}`, `{8}`, `{9}`, `{10}` |

**System action**

The system cannot format all or part of the response because the entity either has effectivity dates that are not effective for the time that the entity is retrieved or it is classified as a memento.

**User action**

Use knowledge of the request and the contents of the repositories to determine if the entity is set up correctly or needs to be updated.


----

### OMAG-REPOSITORY-HANDLER-0010

> Method {0} called from {1} for service {2} is using function that not supported by any of the metadata repositories connected to {3} - error message is: {4}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerAuditCode.FUNCTION_NOT_SUPPORTED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because none of the members of the connected cohort(s) support this function.

**User action**

Add an Egeria native metadata repository to one of the connected cohorts.  This will provide the support that you need.


----

### OMAG-REPOSITORY-HANDLER-0011

> The Open Metadata Service has soft-deleted entity {0} of type {1} ({2}) during method {3}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerAuditCode.ENTITY_DELETED` |
| **Severity** | TRACE - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The entity has been put into DELETED status. If is no longer available for normal queries.

**User action**

No specific action is required.  This message is to highlight that the entity can no longer be retrieved until it is restored.


----

### OMAG-REPOSITORY-HANDLER-0012

> The Open Metadata Service has soft-deleted relationship {0} of type {1} ({2}) between entity {3} of type {4} ({5}) and entity {6} of type {7} ({8}) during method {9}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerAuditCode.RELATIONSHIP_DELETED` |
| **Severity** | TRACE - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}`, `{8}`, `{9}` |

**System action**

The relationship has been put into DELETED status. If is no longer available for normal queries.

**User action**

No specific action is required.  This message is to highlight that the relationship can no longer be retrieved until it is restored.


----

### OMAG-REPOSITORY-HANDLER-0013

> The Open Metadata Service is retrying the {0} classification of entity {1} due to a race condition.  The original {2} exception returned from the classification request had an error message of {3}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerAuditCode.CLASSIFICATION_RETRY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

A classification request is being retried because of a race condition between two threads, both trying to add the first instance of a classification to an entity.  The classification will be reapplied.

**User action**

Check that the resulting classification of the entity is what is required.


----

### OMAG-REPOSITORY-HANDLER-0014

> Successfully deduplicated relationships {0} down to {1}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerAuditCode.RELATION_DEDUP_SUMMARY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The relationship accumulator has successfully removed deduplicated relationships.

**User action**

Check that these are valid duplicates.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
