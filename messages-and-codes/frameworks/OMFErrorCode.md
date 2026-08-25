<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMFErrorCode

The OMF error code is used to define first failure data capture (FFDC) for errors that occur when working with OMF Components. It is used in conjunction with the OMFCheckedException and OMFRuntimeException.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 31 |
| **Message identifiers begin** | `OPEN-METADATA-` |
| **Java class** | `org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFErrorCode` |
| **Module** | [open-metadata-implementation/frameworks/open-metadata-framework](../../open-metadata-implementation/frameworks/open-metadata-framework) |
| **Source** | [OMFErrorCode.java](../../open-metadata-implementation/frameworks/open-metadata-framework/src/main/java/org/odpi/openmetadata/frameworks/openmetadata/ffdc/OMFErrorCode.java) |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OPEN-METADATA-400-001](#open-metadata-400-001) | 400 | The {0} survey action service has been disconnected - either due to its own actions or a cancel request |
| [OPEN-METADATA-400-002](#open-metadata-400-002) | 400 | The object passed on the {0} parameter of the {1} operation is null |
| [OPEN-METADATA-400-003](#open-metadata-400-003) | 400 | The listener manager received an unexpected IO exception when reading the file named {0}; the error message was: {1} |
| [OPEN-METADATA-400-004](#open-metadata-400-004) | 400 | The name passed on the {0} parameter of the {1} operation is null |
| [OPEN-METADATA-400-005](#open-metadata-400-005) | 400 | The unique identifier (guid) passed on the {0} parameter of the {1} operation is null |
| [OPEN-METADATA-400-009](#open-metadata-400-009) | 400 | {0} cannot add a new element to location {1} of an array of size {2} value |
| [OPEN-METADATA-400-011](#open-metadata-400-011) | 400 | The valid metadata value {0} for property {1} is not found |
| [OPEN-METADATA-400-012](#open-metadata-400-012) | 400 | The unique identifier (guid) passed on the {0} parameter of the {1} operation contains invalid characters |
| [OPEN-METADATA-400-013](#open-metadata-400-013) | 400 | The objects passed on the replacement attributes and placeholder properties of the {0} operation are both null; the template has no new values to map |
| [OPEN-METADATA-400-020](#open-metadata-400-020) | 400 | The user identifier (user id) passed on the {0} operation is null |
| [OPEN-METADATA-400-021](#open-metadata-400-021) | 400 | The text field value passed on the {0} parameter of the {1} operation is null |
| [OPEN-METADATA-400-022](#open-metadata-400-022) | 400 | The search string passed on the {0} parameter of the {1} operation is null |
| [OPEN-METADATA-400-023](#open-metadata-400-023) | 400 | The starting point for the results {0}, passed on the {1} parameter of the {2} operation, is negative |
| [OPEN-METADATA-400-024](#open-metadata-400-024) | 400 | The page size {0} for the results, passed on the {1} parameter of the {2} operation, is negative |
| [OPEN-METADATA-400-025](#open-metadata-400-025) | 400 | The number of records to return, {0}, passed on the {1} parameter of the {2} operation, is greater than the allowable maximum of {3} |
| [OPEN-METADATA-400-026](#open-metadata-400-026) | 400 | The {0} element is of type {1} rather than the expected type of {2} |
| [OPEN-METADATA-400-027](#open-metadata-400-027) | 400 | The {0} file passed on method {1} by connector {2} is not a directory |
| [OPEN-METADATA-404-002](#open-metadata-404-002) | 404 | Multiple {0} elements where found with the unique name of {1}: the identifiers of the returned elements are {2}; the calling method is {3}, the name parameter is {4} and the server is {5} |
| [OPEN-METADATA-404-007](#open-metadata-404-007) | 404 | The {0} connector can not retrieve the correlation information for {1} open metadata element {2} linked via metadata collection {3} to external element {4} |
| [OPEN-METADATA-500-001](#open-metadata-500-001) | 500 | Unexpected {0} exception in service {1} detected by method {2}.  The error message was {3} |
| [OPEN-METADATA-500-002](#open-metadata-500-002) | 500 | The Java class {0} for PrimitiveTypeCategory {1} is not known |
| [OPEN-METADATA-500-003](#open-metadata-500-003) | 500 | The primitive value should be stored in Java class {0} rather than {1} since it is of PrimitiveTypeCategory {2} |
| [OPEN-METADATA-500-004](#open-metadata-500-004) | 500 | There is a problem in the definition of primitive type {0} |
| [OPEN-METADATA-500-005](#open-metadata-500-005) | 500 | The value supplied for an attribute of PrimitiveTypeCategory {0} is expected as Java class {1} but was supplied as Java class {2} |
| [OPEN-METADATA-500-006](#open-metadata-500-006) | 500 | An unsupported bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; error message was: {4} |
| [OPEN-METADATA-500-007](#open-metadata-500-007) | 500 | The {0} service has not implemented the {1} method in a subclass of the {2} converter class for bean class {3} and so is unable to create the bean for method {4} |
| [OPEN-METADATA-500-009](#open-metadata-500-009) | 500 | One of the converters for the {0} service is not able to populate a bean of type {1} because a metadata instance of type {2} has not passed to method {3} |
| [OPEN-METADATA-500-011](#open-metadata-500-011) | 500 | An entity has been retrieved by method {0} from service {1} that has an invalid header: {2} |
| [OPEN-METADATA-500-013](#open-metadata-500-013) | 500 | A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2} |
| [OPEN-METADATA-500-025](#open-metadata-500-025) | 500 | The Open Metadata Store has received an unexpected {0} exception while formatting a response during method {1} for service {2}.  The message was: {3} |
| [OPEN-METADATA-503-002](#open-metadata-503-002) | 503 | A caller {0} has passed an invalid parameter to the property helper {1} operation as part of the {2} request resulting in an unexpected {3} exception with message {4} |

----

### OPEN-METADATA-400-001

> The {0} survey action service has been disconnected - either due to its own actions or a cancel request

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.DISCONNECT_DETECTED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The survey action framework will attempt to stop the work of the survey action framework

**User action**

Monitor the shutdown of the survey action service.


----

### OPEN-METADATA-400-002

> The object passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.NULL_OBJECT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without this object.

**User action**

Correct the code in the caller to provide the object.


----

### OPEN-METADATA-400-003

> The listener manager received an unexpected IO exception when reading the file named {0}; the error message was: {1}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.UNEXPECTED_IO_EXCEPTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The listener manager attempted to retrieve the canonical file name and an IO exception occurred.  It is therefore unable to monitor the file.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### OPEN-METADATA-400-004

> The name passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.NULL_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without a name.

**User action**

Correct the code in the caller to provide the name on the parameter.


----

### OPEN-METADATA-400-005

> The unique identifier (guid) passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.NULL_GUID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without a guid.

**User action**

Correct the code in the caller to provide the guid.


----

### OPEN-METADATA-400-009

> {0} cannot add a new element to location {1} of an array of size {2} value

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.ARRAY_OUT_OF_BOUNDS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is an error in the update of an ArrayTypePropertyValue.

**User action**

Recode the call to the property object with a valid element location and retry.


----

### OPEN-METADATA-400-011

> The valid metadata value {0} for property {1} is not found

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.VALID_METADATA_MISSING` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The metadata element for this valid metadata value is not stored in the repository.

**User action**

Check the parameter of the call to make sure there name and value have been properly defined.


----

### OPEN-METADATA-400-012

> The unique identifier (guid) passed on the {0} parameter of the {1} operation contains invalid characters

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.INVALID_GUID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request with this guid.

**User action**

Correct the code in the caller to provide the correct guid.  GUIDs are of this form '1a27f402-4638-4002-8e5c-74143661ebb4'.


----

### OPEN-METADATA-400-013

> The objects passed on the replacement attributes and placeholder properties of the {0} operation are both null; the template has no new values to map

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.NULL_TEMPLATE_INSERTS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot process the request without some additional values.

**User action**

Correct the code in the caller to provide either replacement attributes and/or placeholder properties.


----

### OPEN-METADATA-400-020

> The user identifier (user id) passed on the {0} operation is null

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.NULL_USER_ID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot process the request without a user id.

**User action**

Correct the code in the caller to provide the user id.


----

### OPEN-METADATA-400-021

> The text field value passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.NULL_TEXT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without this text field value.

**User action**

Correct the code in the caller to provide a value in the text field.


----

### OPEN-METADATA-400-022

> The search string passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.NULL_SEARCH_STRING` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without a search string.

**User action**

Correct the code in the caller to provide the search string.


----

### OPEN-METADATA-400-023

> The starting point for the results {0}, passed on the {1} parameter of the {2} operation, is negative

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.NEGATIVE_START_FROM` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request with this invalid value.  It should be zero for the start of the values, or a number greater than 0 to start partway down the list

**User action**

Correct the code in the caller to provide a non-negative value for the starting point.


----

### OPEN-METADATA-400-024

> The page size {0} for the results, passed on the {1} parameter of the {2} operation, is negative

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.NEGATIVE_PAGE_SIZE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request with this invalid value.  It should be zero to return all the result, or greater than zero to set a maximum

**User action**

Correct the code in the caller to provide a non-negative value for the page size.


----

### OPEN-METADATA-400-025

> The number of records to return, {0}, passed on the {1} parameter of the {2} operation, is greater than the allowable maximum of {3}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.MAX_PAGE_SIZE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request with this page size value.

**User action**

Correct the code in the caller to provide a smaller page size.


----

### OPEN-METADATA-400-026

> The {0} element is of type {1} rather than the expected type of {2}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.WRONG_TYPE_FOR_ELEMENT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system has retrieved an element that is not of the same type as expected.  The expected type is either supplied by the caller in the 'metadataElementTypeName' requests body field or the service uses its default value.

**User action**

Correct the code in the caller to provide a suitable type name, or use a different service.


----

### OPEN-METADATA-400-027

> The {0} file passed on method {1} by connector {2} is not a directory

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.NOT_DIRECTORY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector has passed a file rather than a directory when registering a file listener.

**User action**

Correct the code in the connector to provide a suitable directory, or use a different service.


----

### OPEN-METADATA-404-002

> Multiple {0} elements where found with the unique name of {1}: the identifiers of the returned elements are {2}; the calling method is {3}, the name parameter is {4} and the server is {5}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.MULTIPLE_ENTITIES_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot process a request because multiple elements have been discovered and it is unsure which one to use.

**User action**

Investigate why multiple elements exist.  Then retry the request once the issue is resolved.


----

### OPEN-METADATA-404-007

> The {0} connector can not retrieve the correlation information for {1} open metadata element {2} linked via metadata collection {3} to external element {4}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.MISSING_CORRELATION` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The correlation information that should be associated with the open metadata element is missing and the connector is not able to confidently synchronize it with the element from the external system.

**User action**

Review the audit log to determine if there were errors detected when the open metadata entity was created.  The simplest resolution is to add the correlation information to the open metadata entity to allow the synchronization to continue.


----

### OPEN-METADATA-500-001

> Unexpected {0} exception in service {1} detected by method {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The named service failed during its operation.

**User action**

This may be a configuration or a code error.  Look for other error messages and review the code of the governance action service. Once the cause is resolved, retry the governance request.


----

### OPEN-METADATA-500-002

> The Java class {0} for PrimitiveTypeCategory {1} is not known

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.INVALID_PRIMITIVE_CLASS_NAME` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

There is an internal error in Java class PrimitiveTypeCategory as it has been set up with an invalid class.

**User action**

Raise a Github issue to get this fixed.


----

### OPEN-METADATA-500-003

> The primitive value should be stored in Java class {0} rather than {1} since it is of PrimitiveTypeCategory {2}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.INVALID_PRIMITIVE_VALUE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is an internal error in the creation of a PrimitiveTypeValue.

**User action**

Open an issue on GitHub to get this addressed.


----

### OPEN-METADATA-500-004

> There is a problem in the definition of primitive type {0}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.INVALID_PRIMITIVE_CATEGORY` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

There is an internal error during the creation of a PrimitiveTypeValue.

**User action**

Open a Github issue to get this looked into.


----

### OPEN-METADATA-500-005

> The value supplied for an attribute of PrimitiveTypeCategory {0} is expected as Java class {1} but was supplied as Java class {2}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.INVALID_PRIMITIVE_TYPE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is an internal error - code that sets a primitive property value is using an incorrect Java class.

**User action**

Report as a Github issue to get this addressed.


----

### OPEN-METADATA-500-006

> An unsupported bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on server {3}; error message was: {4}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.INVALID_BEAN_CLASS` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because it is not able to instantiate the bean.

**User action**

Correct the code that initializes the converter during server start up.


----

### OPEN-METADATA-500-007

> The {0} service has not implemented the {1} method in a subclass of the {2} converter class for bean class {3} and so is unable to create the bean for method {4}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.MISSING_CONVERTER_METHOD` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because it is not able to populate the bean.

**User action**

Correct the converter implementation as part of this module.


----

### OPEN-METADATA-500-009

> One of the converters for the {0} service is not able to populate a bean of type {1} because a metadata instance of type {2} has not passed to method {3}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.MISSING_METADATA_INSTANCE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request because it is missing one or more metadata elementsneeded to instantiate the bean.

**User action**

Correct the handler code that calls the converter as part of this request since it has not passed sufficient metadata instances to the converter.  Alternatively, these instances may not be in the repositories (legitimately) and the converter needs to be able to handle that variation.


----

### OPEN-METADATA-500-011

> An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.BAD_ENTITY` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot format all or part of the response because the repositories have returned an invalid entity.

**User action**

Use knowledge of the request and the contents of the repositories to track down and correct the invalid entity.  There is probably an error in the implementation of the repository that originated the entity.


----

### OPEN-METADATA-500-013

> A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.BAD_RELATIONSHIP` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot format all or part of the response because the repositories have returned an invalid relationship.

**User action**

Use knowledge of the request and the contents of the repositories to track down and correct the invalid relationship.  There is probably an error in the implementation of the repository that originated the relationship.


----

### OPEN-METADATA-500-025

> The Open Metadata Store has received an unexpected {0} exception while formatting a response during method {1} for service {2}.  The message was: {3}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.UNEXPECTED_CONVERTER_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The request returns with this exception to indicate there has been an internal server error. The server also created a detailed error message and stack trace in the audit log.

**User action**

Review the stack trace to identify where the error occurred and work to resolve the cause.


----

### OPEN-METADATA-503-002

> A caller {0} has passed an invalid parameter to the property helper {1} operation as part of the {2} request resulting in an unexpected {3} exception with message {4}

|  |  |
|---|---|
| **Java constant** | `OMFErrorCode.HELPER_LOGIC_EXCEPTION` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The open metadata component has invoked the property helper operations in the wrong sequence or has a similar logic error.

**User action**

Review the code around the original exception to detect the source of the error.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
