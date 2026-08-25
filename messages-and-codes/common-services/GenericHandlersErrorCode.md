<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# GenericHandlersErrorCode

The GenericHandlersErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Repository Handler Services. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 25 |
| **Message identifiers begin** | `OMAG-GENERIC-HANDLERS-` |
| **Java class** | `org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode` |
| **Module** | [open-metadata-implementation/common-services/generic-handlers](../../open-metadata-implementation/common-services/generic-handlers) |
| **Source** | [GenericHandlersErrorCode.java](../../open-metadata-implementation/common-services/generic-handlers/src/main/java/org/odpi/openmetadata/commonservices/generichandlers/ffdc/GenericHandlersErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/generic-handlers/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMAG-GENERIC-HANDLERS-400-005](#omag-generic-handlers-400-005) | 400 | Governance Engine with unique name of {0} is not found by calling service {1} running in server {2} |
| [OMAG-GENERIC-HANDLERS-400-006](#omag-generic-handlers-400-006) | 400 | Unable to initiate an instance of an engine action because the governance action process step {0} does not have a Governance Engine linked via the {1} relationship |
| [OMAG-GENERIC-HANDLERS-400-007](#omag-generic-handlers-400-007) | 400 | Unable to initiate an instance of the {0} governance action process because there is no first governance action process step defined |
| [OMAG-GENERIC-HANDLERS-400-009](#omag-generic-handlers-400-009) | 400 | Governance Engine {0} ({1}) does not support request type {2}; requested via service {3} running in server {4} |
| [OMAG-GENERIC-HANDLERS-400-010](#omag-generic-handlers-400-010) | 400 | Governance Engine {0} ({1}) does not support any request types and so it cannot run request type {2}; requested via service {3} running in server {4} |
| [OMAG-GENERIC-HANDLERS-400-011](#omag-generic-handlers-400-011) | 400 | Unable to initiate an instance of the {0} governance action process because the name is not recognized |
| [OMAG-GENERIC-HANDLERS-400-013](#omag-generic-handlers-400-013) | 400 | Unable to initiate an instance of the {0} governance action type because the name is not recognized |
| [OMAG-GENERIC-HANDLERS-400-014](#omag-generic-handlers-400-014) | 400 | The {0} element carries the TemplateSubstitute classification but has no SourcedFrom relationship to the template it stands in for, so the {1} request has no template to work from |
| [OMAG-GENERIC-HANDLERS-403-001](#omag-generic-handlers-403-001) | 403 | The {0} method cannot delete the requested relationship between {1} {2} and {3} {4} because it was not created by the requesting user {5} |
| [OMAG-GENERIC-HANDLERS-403-002](#omag-generic-handlers-403-002) | 403 | Engine Host OMAG Server with a userId of {0} is not allowed to issue request {1} for engine action {2} because it is already being processed by Engine Host OMAG Server with a userId of {3} |
| [OMAG-GENERIC-HANDLERS-403-003](#omag-generic-handlers-403-003) | 403 | Engine Host OMAG Server with a userId of {0} is not allowed claim the engine action {1} because it is already being processed by Engine Host OMAG Server with a userId of {2} and is in status {3} |
| [OMAG-GENERIC-HANDLERS-403-004](#omag-generic-handlers-403-004) | 403 | A delete of {0} data asset {1} is not permitted because it is being used by {2} data set {3} |
| [OMAG-GENERIC-HANDLERS-403-005](#omag-generic-handlers-403-005) | 403 | A delete of {0} element {1} is not permitted because it still has a dependent {2} element {3} |
| [OMAG-GENERIC-HANDLERS-404-002](#omag-generic-handlers-404-002) | 404 | Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter is {4} and the server is {5} |
| [OMAG-GENERIC-HANDLERS-404-004](#omag-generic-handlers-404-004) | 404 | Multiple {0} relationships where found between {1} entity {2} and {3} entity {4}: the identifiers of the returned relationships are {5}; the calling method is {6} and the server is {7} |
| [OMAG-GENERIC-HANDLERS-500-001](#omag-generic-handlers-500-001) | 500 | An unsupported bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; error message was: {4} |
| [OMAG-GENERIC-HANDLERS-500-002](#omag-generic-handlers-500-002) | 500 | The {0} service has not implemented the {1} method in a subclass of the {2} converter class for bean class {3} and so is unable to create the bean for method {4} |
| [OMAG-GENERIC-HANDLERS-500-003](#omag-generic-handlers-500-003) | 500 | An unexpected bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; the expected class name is: {4} |
| [OMAG-GENERIC-HANDLERS-500-004](#omag-generic-handlers-500-004) | 500 | One of the converters for the {0} service is not able to populate a bean of type {1} because a metadata instance of type {2} has not passed to method {3} |
| [OMAG-GENERIC-HANDLERS-500-005](#omag-generic-handlers-500-005) | 500 | One of the converters for the {0} service is not able to populate a bean of type {1} because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4} |
| [OMAG-GENERIC-HANDLERS-500-007](#omag-generic-handlers-500-007) | 500 | The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} is null |
| [OMAG-GENERIC-HANDLERS-500-008](#omag-generic-handlers-500-008) | 500 | The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} has null properties |
| [OMAG-GENERIC-HANDLERS-500-009](#omag-generic-handlers-500-009) | 500 | An anchor GUID of &lt;unknown&gt; has been passed to local method {0} by the {1} service through method {2} |
| [OMAG-GENERIC-HANDLERS-500-011](#omag-generic-handlers-500-011) | 500 | An entity has been retrieved by method {0} from service {1} that has an invalid header: {2} |
| [OMAG-GENERIC-HANDLERS-500-013](#omag-generic-handlers-500-013) | 500 | A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2} |

----

### OMAG-GENERIC-HANDLERS-400-005

> Governance Engine with unique name of {0} is not found by calling service {1} running in server {2}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.UNKNOWN_ENGINE_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot initiate an engine action because the nominated governance engine is not found in the metadata repository.

**User action**

Investigate whether the requested name is incorrect or the definition is missing. Then retry the request once the issue is resolved.


----

### OMAG-GENERIC-HANDLERS-400-006

> Unable to initiate an instance of an engine action because the governance action process step {0} does not have a Governance Engine linked via the {1} relationship

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.UNKNOWN_EXECUTOR` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot initiate a governance action process because is its implementation definition is incomplete.

**User action**

Update the definition of the first governance action process step so that it is linked to a governance engine to execute the requested action. Then retry the request once the definition is corrected.


----

### OMAG-GENERIC-HANDLERS-400-007

> Unable to initiate an instance of the {0} governance action process because there is no first governance action process step defined

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.NO_PROCESS_IMPLEMENTATION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot initiate a governance action process because its implementation definition is missing.

**User action**

Link a governance action process step to the governance action process.  If the process is to have multiple steps to it, link additional governance action process steps to this first one to describe the execution flow. Then retry the request once the definition is corrected.


----

### OMAG-GENERIC-HANDLERS-400-009

> Governance Engine {0} ({1}) does not support request type {2}; requested via service {3} running in server {4}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.UNKNOWN_REQUEST_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot initiate a engine action because the nominated request type is not found in the metadata repository.

**User action**

Investigate whether the request type is incorrect or the definition is missing. Then retry the request once the issue is resolved.


----

### OMAG-GENERIC-HANDLERS-400-010

> Governance Engine {0} ({1}) does not support any request types and so it cannot run request type {2}; requested via service {3} running in server {4}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.NO_REQUEST_TYPE_FOR_ENGINE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot initiate an engine action because the nominated governance engine has no supported governance services.

**User action**

Investigate why there are no supported governance services for the governance engine. Then retry the request once the issue is resolved.


----

### OMAG-GENERIC-HANDLERS-400-011

> Unable to initiate an instance of the {0} governance action process because the name is not recognized

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.UNKNOWN_PROCESS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot initiate a governance action process because its definition is missing.

**User action**

Verify that the process name (qualifiedName of a GovernanceActionProcess entity) is correct.  Either set up the caller to use the correct name or create a GovernanceActionProcess entity with the requested qualifiedName.  Then retry the request once the definition is added.


----

### OMAG-GENERIC-HANDLERS-400-013

> Unable to initiate an instance of the {0} governance action type because the name is not recognized

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.UNKNOWN_GOVERNANCE_ACTION_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot initiate a governance action type because its definition is missing.

**User action**

Verify that the name (qualifiedName of a GovernanceActionType entity) is correct.  Either set up the caller to use the correct name or create a GovernanceActionType entity with the requested qualifiedName.  Then retry the request once the definition is added.


----

### OMAG-GENERIC-HANDLERS-400-014

> The {0} element carries the TemplateSubstitute classification but has no SourcedFrom relationship to the template it stands in for, so the {1} request has no template to work from

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.NO_SUBSTITUTE_TEMPLATE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot create an element from this template because the TemplateSubstitute classification directs it to the element the substitute is sourced from, and there is no such element.

**User action**

Either attach a SourcedFrom relationship from the substitute to the template it stands in for, or remove the TemplateSubstitute classification so that the element is used as a template in its own right.


----

### OMAG-GENERIC-HANDLERS-403-001

> The {0} method cannot delete the requested relationship between {1} {2} and {3} {4} because it was not created by the requesting user {5}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.ONLY_CREATOR_CAN_DELETE` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The request fails because the user does not have the rights to take this action.

**User action**

Retry the request with a relationship created with this user, or request that the user who created the relationship issues the delete request.


----

### OMAG-GENERIC-HANDLERS-403-002

> Engine Host OMAG Server with a userId of {0} is not allowed to issue request {1} for engine action {2} because it is already being processed by Engine Host OMAG Server with a userId of {3}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.INVALID_PROCESSING_USER` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot update an engine action because the requester has not claimed the engine action.

**User action**

Investigate why the Engine Host OMAG Server is attempting to process this engine action.  If you have multiple Engine Host OMAG Servers running the same governance engines then it is possible that they both attempted to claim the engine action at the same time.  If this is the case, validate that the engine action is processed successful by the victorious engine host.  If this happens frequently, it may be necessary to separate the workload amongst distinct governance engines that support the same governance services.


----

### OMAG-GENERIC-HANDLERS-403-003

> Engine Host OMAG Server with a userId of {0} is not allowed claim the engine action {1} because it is already being processed by Engine Host OMAG Server with a userId of {2} and is in status {3}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.INVALID_ENGINE_ACTION_STATUS` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot claim an engine action because another Engine Host OMAG Server has got there first.

**User action**

This is a normal event if there are more than one Engine Host OMAG Server running the same governance engine.


----

### OMAG-GENERIC-HANDLERS-403-004

> A delete of {0} data asset {1} is not permitted because it is being used by {2} data set {3}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.DATA_STORE_IN_USE` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot delete a data asset because it is connected to a data set that is using it to supply its data content.

**User action**

This call requires a cascaded delete to allow an element that is in use, or with dependent elements to be removed.  Either delete the relationship to the data set, or use the cascaded delete option.


----

### OMAG-GENERIC-HANDLERS-403-005

> A delete of {0} element {1} is not permitted because it still has a dependent {2} element {3}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.DEPENDENT_ELEMENTS_FOUND` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot delete an element because it is connected to other elements that are dependent on it.

**User action**

This call requires a cascaded delete to allow an element that with these dependent elements, or with dependent elements to be removed.  Either delete the dependent elements, or use the cascaded delete option.


----

### OMAG-GENERIC-HANDLERS-404-002

> Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter is {4} and the server is {5}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.MULTIPLE_ENTITIES_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot process a request because multiple entities have been discovered and it is unsure which entity to use.

**User action**

Investigate why multiple entities exist.  Then retry the request once the issue is resolved.


----

### OMAG-GENERIC-HANDLERS-404-004

> Multiple {0} relationships where found between {1} entity {2} and {3} entity {4}: the identifiers of the returned relationships are {5}; the calling method is {6} and the server is {7}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.MULTIPLE_RELATIONSHIPS_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}` |

**System action**

The system cannot process a request because multiple relationships have been discovered and it is unsure which relationship to use.

**User action**

Investigate why multiple relationship exist.  Then retry the request once the issue is resolved.


----

### OMAG-GENERIC-HANDLERS-500-001

> An unsupported bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; error message was: {4}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.INVALID_BEAN_CLASS` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because it is not able to instantiate the bean.

**User action**

Correct the code that initializes the converter during server start up.


----

### OMAG-GENERIC-HANDLERS-500-002

> The {0} service has not implemented the {1} method in a subclass of the {2} converter class for bean class {3} and so is unable to create the bean for method {4}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.MISSING_CONVERTER_METHOD` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because it is not able to populate the bean.

**User action**

Correct the converter implementation as part of this module.


----

### OMAG-GENERIC-HANDLERS-500-003

> An unexpected bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; the expected class name is: {4}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.UNEXPECTED_BEAN_CLASS` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because it is not able to support the bean's methods.

**User action**

Correct the code that sets up the converter as part of this service.


----

### OMAG-GENERIC-HANDLERS-500-004

> One of the converters for the {0} service is not able to populate a bean of type {1} because a metadata instance of type {2} has not passed to method {3}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.MISSING_METADATA_INSTANCE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request because it is missing one or more metadata elementsneeded to instantiate the bean.

**User action**

Correct the handler code that calls the converter as part of this request since it has not passed sufficient metadata instances to the converter.  Alternatively, these instances may not be in the repositories (legitimately) and the converter needs to be able to handle that variation.


----

### OMAG-GENERIC-HANDLERS-500-005

> One of the converters for the {0} service is not able to populate a bean of type {1} because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.BAD_INSTANCE_TYPE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because the wrong type of instances have been retrieved from the metadata repositories.

**User action**

The error is likely to be either in the handler code that called the converter, or more likely, in the way that the handler and the converter were initialized at server start up.


----

### OMAG-GENERIC-HANDLERS-500-007

> The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} is null

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.MISSING_ENGINE_ACTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request because the handler has failed to retrieve the entity for the identifier.  Normally this would result in an InvalidParameterException and it is curious that it did not.

**User action**

The error is likely to be in one of the repository connectors, but it may be either in the handler code or the governance engines managing the engine action entities.


----

### OMAG-GENERIC-HANDLERS-500-008

> The entity for identifier {0} supplied on the {1} parameter by the {2} service on method {3} has null properties

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.MISSING_ENGINE_ACTION_PROPERTIES` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request because the handler has retrieved an engine action entity that has no properties.  The handler does not know how to proceed.

**User action**

The error is likely to be in one of the repository connectors or the governance engines managing the engine action entities.


----

### OMAG-GENERIC-HANDLERS-500-009

> An anchor GUID of &lt;unknown&gt; has been passed to local method {0} by the {1} service through method {2}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.UNKNOWN_ANCHOR_GUID` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request because the handler has an invalid anchor GUID.

**User action**

Gather diagnostics and add them to issue #4680.


----

### OMAG-GENERIC-HANDLERS-500-011

> An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.BAD_ENTITY` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot format all or part of the response because the repositories have returned an invalid entity.

**User action**

Use knowledge of the request and the contents of the repositories to track down and correct the invalid entity.  There is probably an error in the implementation of the repository that originated the entity.


----

### OMAG-GENERIC-HANDLERS-500-013

> A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}

|  |  |
|---|---|
| **Java constant** | `GenericHandlersErrorCode.BAD_RELATIONSHIP` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot format all or part of the response because the repositories have returned an invalid relationship.

**User action**

Use knowledge of the request and the contents of the repositories to track down and correct the invalid relationship.  There is probably an error in the implementation of the repository that originated the relationship.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
