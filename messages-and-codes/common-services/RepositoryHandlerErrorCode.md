<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# RepositoryHandlerErrorCode

The RepositoryHandlerErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Repository Handler Services. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 26 |
| **Message identifiers begin** | `OMAG-REPOSITORY-HANDLER-` |
| **Java class** | `org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandlerErrorCode` |
| **Module** | [open-metadata-implementation/common-services/repository-handler](../../open-metadata-implementation/common-services/repository-handler) |
| **Source** | [RepositoryHandlerErrorCode.java](../../open-metadata-implementation/common-services/repository-handler/src/main/java/org/odpi/openmetadata/commonservices/repositoryhandler/RepositoryHandlerErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/repository-handler/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMAG-REPOSITORY-HANDLER-400-001](#omag-repository-handler-400-001) | 400 | An unsupported property named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; error message was: {4} |
| [OMAG-REPOSITORY-HANDLER-400-004](#omag-repository-handler-400-004) | 404 | An unsupported type named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; error message was: {4} |
| [OMAG-REPOSITORY-HANDLER-400-005](#omag-repository-handler-400-005) | 400 | The property named {0} with value of {1} supplied on method {2} does not match the stored value of {3} for entity {4} |
| [OMAG-REPOSITORY-HANDLER-400-007](#omag-repository-handler-400-007) | 400 | Method {0} running on behalf of external source {1} ({2}) cannot modify {3} instance {4} because it has metadata provenance of {5} with an externalSourceGUID of {6} and an externalSourceName of {7} |
| [OMAG-REPOSITORY-HANDLER-400-008](#omag-repository-handler-400-008) | 400 | Method {0} cannot modify {1} instance {2} because it has a metadata provenance of {3} with an externalSourceGUID of {4} and an externalSourceName of {5} and user {6} issued a request with the Local Cohort metadata provenance set |
| [OMAG-REPOSITORY-HANDLER-400-009](#omag-repository-handler-400-009) | 400 | The property named {0} with value of {1} supplied on method {2} is not found in entity {3} |
| [OMAG-REPOSITORY-HANDLER-400-010](#omag-repository-handler-400-010) | 400 | A {0} entity with unique identifier {1} has been retrieved by method {2} from service {3} but it is not visible to the caller {4}: effective time is {5}; entity is effective from {6} to {7} with classifications {8} and call parameters of forLineage={9} and forDuplicateProcessing={10} |
| [OMAG-REPOSITORY-HANDLER-403-001](#omag-repository-handler-403-001) | 403 | User {0} is not authorized to issue the {1} request for open metadata access service {2} on server {3} |
| [OMAG-REPOSITORY-HANDLER-404-001](#omag-repository-handler-404-001) | 404 | The {0} method has retrieved an object for unique identifier (guid) {1} which is of type {2} rather than type {3} on behalf of method {4} |
| [OMAG-REPOSITORY-HANDLER-404-002](#omag-repository-handler-404-002) | 404 | The open metadata repository services are not initialized for the {0} operation |
| [OMAG-REPOSITORY-HANDLER-404-003](#omag-repository-handler-404-003) | 404 | The open metadata repository services are not available for the {0} operation |
| [OMAG-REPOSITORY-HANDLER-404-004](#omag-repository-handler-404-004) | 404 | The repository connector {0} is not returning a metadata collection object |
| [OMAG-REPOSITORY-HANDLER-404-005](#omag-repository-handler-404-005) | 404 | Only an entity proxy for requested {0} object with unique identifier (guid) {1} is found in the open metadata server {2}, error message was: {3} |
| [OMAG-REPOSITORY-HANDLER-404-006](#omag-repository-handler-404-006) | 404 | Multiple {0} relationships are connected to the {1} entity with unique identifier {2}: the relationship identifiers are {3}; the calling method is {4} and the server is {5} |
| [OMAG-REPOSITORY-HANDLER-404-007](#omag-repository-handler-404-007) | 404 | The {0} entity with unique identifier {1} is not found for method {2} of access service {3} in open metadata server {4}, error message was: {5} |
| [OMAG-REPOSITORY-HANDLER-404-009](#omag-repository-handler-404-009) | 404 | A null entity was returned to method {0} of server {1} during a request for entity of type {2} (guid {3}) and properties of: {4} |
| [OMAG-REPOSITORY-HANDLER-404-010](#omag-repository-handler-404-010) | 404 | Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter is {4} and the server is {5} |
| [OMAG-REPOSITORY-HANDLER-404-011](#omag-repository-handler-404-011) | 404 | A null entity was returned to method {0} of server {1} during a request to add a classification of type {4} (guid {3}) to entity {2} with properties of: {5} |
| [OMAG-REPOSITORY-HANDLER-404-012](#omag-repository-handler-404-012) | 404 | The {0} relationship with unique identifier {1} is not found for method {2} of access service {3} in open metadata server {4}, error message was: {5} |
| [OMAG-REPOSITORY-HANDLER-404-013](#omag-repository-handler-404-013) | 404 | The {0} element with unique identifier {1} is found for method {2} of access service {3} in open metadata server {4} however its effectivity dates are from: {5} to {6} and the requested effective date was {7} |
| [OMAG-REPOSITORY-HANDLER-404-015](#omag-repository-handler-404-015) | 404 | The {0} relationship with unique identifier {1} claims all effective dates which makes it broader than the requested effective dates of {2} to {3} |
| [OMAG-REPOSITORY-HANDLER-404-016](#omag-repository-handler-404-016) | 404 | The {0} relationship with unique identifier {1} has narrower effective dates of {2} to {3} than the requested effective dates of {4} to {5} |
| [OMAG-REPOSITORY-HANDLER-404-017](#omag-repository-handler-404-017) | 404 | The {0} relationship with unique identifier {1} has overlapping effective dates of {2} to {3} than the requested effective dates of {4} to {5} |
| [OMAG-REPOSITORY-HANDLER-404-018](#omag-repository-handler-404-018) | 404 | Method {0} for service {1} is not supported by any of the metadata repositories connected to {2} |
| [OMAG-REPOSITORY-HANDLER-500-001](#omag-repository-handler-500-001) | 500 | An unexpected error {4} was returned to {5} by the metadata server during {1} request for open metadata access service {2} on server {3}; message was {0} |
| [OMAG-REPOSITORY-HANDLER-500-002](#omag-repository-handler-500-002) | 500 | The Open Metadata Service {0} is not able to set the Anchors classification on a new entity of type {1} during method {2}. The resulting exception was {3} with error message {4} |

----

### OMAG-REPOSITORY-HANDLER-400-001

> An unsupported property named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; error message was: {4}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.INVALID_PROPERTY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because it has no place to store the property.

**User action**

Correct the types and property names of the properties passed on the request.


----

### OMAG-REPOSITORY-HANDLER-400-004

> An unsupported type named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; error message was: {4}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.INVALID_TYPE` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because the repository services are unable to store the supplied information.

**User action**

Change the call being made - or look to expand the collective capabilities of the available repositories by connecting an Egeria metadata server to the open metadata repository cohort that this server is connected to.


----

### OMAG-REPOSITORY-HANDLER-400-005

> The property named {0} with value of {1} supplied on method {2} does not match the stored value of {3} for entity {4}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.INVALID_PROPERTY_VALUE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because there is a possibility that the caller is requesting changes to the wrong object.

**User action**

Correct the values of the properties passed on the request and retry.


----

### OMAG-REPOSITORY-HANDLER-400-007

> Method {0} running on behalf of external source {1} ({2}) cannot modify {3} instance {4} because it has metadata provenance of {5} with an externalSourceGUID of {6} and an externalSourceName of {7}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.WRONG_EXTERNAL_SOURCE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}` |

**System action**

The system cannot modify the requested instance because it does not have the correct ownership rights to the instance.

**User action**

Route the request through a different process that is set up to use the correct external source identifiers.


----

### OMAG-REPOSITORY-HANDLER-400-008

> Method {0} cannot modify {1} instance {2} because it has a metadata provenance of {3} with an externalSourceGUID of {4} and an externalSourceName of {5} and user {6} issued a request with the Local Cohort metadata provenance set

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.LOCAL_CANNOT_CHANGE_EXTERNAL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The system cannot modify the requested instance because it does not have ownership rights to the instance.

**User action**

Route the request through a process that is set up to use the correct external source identifiers.  To understand more about this behavior, lookup Metadata Provenance in Egeria's Glossary.


----

### OMAG-REPOSITORY-HANDLER-400-009

> The property named {0} with value of {1} supplied on method {2} is not found in entity {3}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.UNRECOGNIZED_PROPERTY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system does no process the request because there is a possibility that the caller is requesting changes to the wrong object.

**User action**

Correct the value of the property passed on the request and retry.


----

### OMAG-REPOSITORY-HANDLER-400-010

> A {0} entity with unique identifier {1} has been retrieved by method {2} from service {3} but it is not visible to the caller {4}: effective time is {5}; entity is effective from {6} to {7} with classifications {8} and call parameters of forLineage={9} and forDuplicateProcessing={10}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.UNAVAILABLE_ENTITY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}`, `{8}`, `{9}`, `{10}` |

**System action**

The system cannot format all or part of the response because the entity either has effectivity dates that are not effective for the time that the entity is retrieved or it is classified as a memento.

**User action**

Use knowledge of the request and the contents of the repositories to determine if the entity is set up correctly or needs to be updated.


----

### OMAG-REPOSITORY-HANDLER-403-001

> User {0} is not authorized to issue the {1} request for open metadata access service {2} on server {3}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.USER_NOT_AUTHORIZED` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request because the user should not be making this request.

**User action**

Verify the access rights of the user.


----

### OMAG-REPOSITORY-HANDLER-404-001

> The {0} method has retrieved an object for unique identifier (guid) {1} which is of type {2} rather than type {3} on behalf of method {4}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.INSTANCE_WRONG_TYPE_FOR_GUID` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The service is not able to return the requested object.

**User action**

Check that the unique identifier is correct and the metadata server(s) supporting the service is running.


----

### OMAG-REPOSITORY-HANDLER-404-002

> The open metadata repository services are not initialized for the {0} operation

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.OMRS_NOT_INITIALIZED` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The system cannot connect to an open metadata repository.

**User action**

Check that the server initialized correctly.  Correct any errors discovered and retry the request when the open metadata services are available.


----

### OMAG-REPOSITORY-HANDLER-404-003

> The open metadata repository services are not available for the {0} operation

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.OMRS_NOT_AVAILABLE` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The system called a function that has not been enabled in this open metadata server.

**User action**

Check that the server initialized correctly and is not shutting down.  Correct any errors discovered and retry the request when the requested server, and its respective services are available.


----

### OMAG-REPOSITORY-HANDLER-404-004

> The repository connector {0} is not returning a metadata collection object

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.NO_METADATA_COLLECTION` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The system cannot access any metadata from the open metadata repositories because it does not have access to the API it needs.

**User action**

Check that the open metadata server URL is correct and the server is running.  Report the error to the system administrator.


----

### OMAG-REPOSITORY-HANDLER-404-005

> Only an entity proxy for requested {0} object with unique identifier (guid) {1} is found in the open metadata server {2}, error message was: {3}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.PROXY_ENTITY_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot populate the requested connection object.

**User action**

Check that the connection name and the OMAS Server URL are correct.  Retry the request when the connection is available in the OMAS Service


----

### OMAG-REPOSITORY-HANDLER-404-006

> Multiple {0} relationships are connected to the {1} entity with unique identifier {2}: the relationship identifiers are {3}; the calling method is {4} and the server is {5}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.MULTIPLE_RELATIONSHIPS_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot process a request because multiple relationships have been discovered and it is unsure which relationship to follow.

**User action**

Investigate why multiple relationships exist.  Then retry the request once the issue is resolved.


----

### OMAG-REPOSITORY-HANDLER-404-007

> The {0} entity with unique identifier {1} is not found for method {2} of access service {3} in open metadata server {4}, error message was: {5}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.UNKNOWN_ENTITY` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot retrieve information associated with the entity because none of the connected open metadata repositories recognize the entity's unique identifier.

**User action**

The unique identifier of the entity is supplied by the caller.  Verify that the caller's logic is correct, and that there are no errors being reported by the open metadata repository. Once all errors have been resolved, retry the request.


----

### OMAG-REPOSITORY-HANDLER-404-009

> A null entity was returned to method {0} of server {1} during a request for entity of type {2} (guid {3}) and properties of: {4}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.NULL_ENTITY_RETURNED` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process a request because it can not find the requested entity.

**User action**

This may be a logic error in the caller or the server.  Alternatively the cohort may not be sharing information correctly.  Look for errors in the server's audit log and console to understand and correct the source of the error.


----

### OMAG-REPOSITORY-HANDLER-404-010

> Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter is {4} and the server is {5}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.MULTIPLE_ENTITIES_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot process a request because multiple entities have been discovered and it is unsure which entity to use.

**User action**

Investigate why multiple entities exist.  Then retry the request once the issue is resolved.


----

### OMAG-REPOSITORY-HANDLER-404-011

> A null entity was returned to method {0} of server {1} during a request to add a classification of type {4} (guid {3}) to entity {2} with properties of: {5}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.NULL_ENTITY_RETURNED_FOR_CLASSIFICATION` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot process a request because it can not find the requested entity to update.

**User action**

This may be a logic error or a configuration error (such as the cohort does not contain the correct members.  Look for errors in the server's audit log and console to understand and correct the source of any error.


----

### OMAG-REPOSITORY-HANDLER-404-012

> The {0} relationship with unique identifier {1} is not found for method {2} of access service {3} in open metadata server {4}, error message was: {5}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.UNKNOWN_RELATIONSHIP` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot update information associated with the relationship because none of the connected open metadata repositories recognize the relationship's unique identifier.

**User action**

The unique identifier of the relationship is supplied by the caller.  Verify that the caller's logic is correct, and that there are no errors being reported by the open metadata repository. Once all errors have been resolved, retry the request.


----

### OMAG-REPOSITORY-HANDLER-404-013

> The {0} element with unique identifier {1} is found for method {2} of access service {3} in open metadata server {4} however its effectivity dates are from: {5} to {6} and the requested effective date was {7}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.NOT_EFFECTIVE_ELEMENT` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}` |

**System action**

The system cannot return the element because the element is not active at this time.

**User action**

The unique identifier of the element is supplied by the caller.  Verify that the caller's logic is correct, and that the effectivity dates of the element are as expected. Once all errors have been resolved, and the time is right, retry the request.


----

### OMAG-REPOSITORY-HANDLER-404-015

> The {0} relationship with unique identifier {1} claims all effective dates which makes it broader than the requested effective dates of {2} to {3}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.BROADER_EFFECTIVE_RELATIONSHIP` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process with the request because the requested effectivity dates are incompatible with the existing relationships.

**User action**

The effectivity dates of the relationship are supplied by the caller.  Verify that the caller's logic is correct, and that the effectivity dates of the request and retrieved relationship are as expected. Once all errors have been resolved, retry the request.


----

### OMAG-REPOSITORY-HANDLER-404-016

> The {0} relationship with unique identifier {1} has narrower effective dates of {2} to {3} than the requested effective dates of {4} to {5}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.NARROWER_EFFECTIVE_RELATIONSHIP` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot proceed because two relationships are attempting to occupying the same effectivity times.

**User action**

The effectivity dates are supplied by the caller.  Verify that the caller's logic is correct, and that the effectivity dates of the relationship are as expected. If the command is to update the effectivity dates, rather than the relationship properties, use the specialist method for this purpose.  Once all errors have been resolved, and the time is right, retry the request.


----

### OMAG-REPOSITORY-HANDLER-404-017

> The {0} relationship with unique identifier {1} has overlapping effective dates of {2} to {3} than the requested effective dates of {4} to {5}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.OVERLAPPING_EFFECTIVE_RELATIONSHIPS` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot continue processing with these incompatible values.

**User action**

The effectivity dates are supplied by the caller.  Verify that the caller's logic is correct, and that the effectivity dates of the retrieved relationship are also correct.  If the command is to update the effectivity dates, rather than the relationship properties, use the specialist method for this purpose.  Once all errors have been resolved, and the time is right, retry the request.


----

### OMAG-REPOSITORY-HANDLER-404-018

> Method {0} for service {1} is not supported by any of the metadata repositories connected to {2}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.FUNCTION_NOT_SUPPORTED` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request because none of the members of the connected cohort(s) support this function.

**User action**

Add an Egeria native metadata repository to one of the connected cohorts.  This will provide the support that you need.


----

### OMAG-REPOSITORY-HANDLER-500-001

> An unexpected error {4} was returned to {5} by the metadata server during {1} request for open metadata access service {2} on server {3}; message was {0}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.PROPERTY_SERVER_ERROR` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot process the request because of an internal error.

**User action**

Verify the sanity of the server.  This is probably a logic error.  If you can not work out what happened, ask the Egeria community for help.


----

### OMAG-REPOSITORY-HANDLER-500-002

> The Open Metadata Service {0} is not able to set the Anchors classification on a new entity of type {1} during method {2}. The resulting exception was {3} with error message {4}

|  |  |
|---|---|
| **Java constant** | `RepositoryHandlerErrorCode.UNABLE_TO_SET_ANCHORS` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The server was attempting to add Anchors classifications to a collection of metadata instances that are logically part of the same object.  This classification is used to optimize the retrieval and maintenance of complex objects.  It is optional function.  The server continues to process the original request which will complete successfully unless something else goes wrong.

**User action**

No specific action is required.  This message is to highlight that the retrieval and management of metadata is not optimalbecause none of the repositories in the cohort support the Anchors classification.  To enable the optimization provided through the Anchors classification, add an Egeria native metadata server to the cohort.  This will provide the support for the Anchors classification.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
