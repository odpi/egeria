<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMAGCommonErrorCode

The OMAGCommonErrorCode is used to define first failure data capture (FFDC) for common errors. It belongs to the FFDC Services module and should not be used by other modules.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 32 |
| **Message identifiers begin** | `OMAG-COMMON-` |
| **Java class** | `org.odpi.openmetadata.commonservices.ffdc.OMAGCommonErrorCode` |
| **Module** | [open-metadata-implementation/common-services/ffdc-services](../../open-metadata-implementation/common-services/ffdc-services) |
| **Source** | [OMAGCommonErrorCode.java](../../open-metadata-implementation/common-services/ffdc-services/src/main/java/org/odpi/openmetadata/commonservices/ffdc/OMAGCommonErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/ffdc-services/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMAG-COMMON-400-001](#omag-common-400-001) | 400 | The OMAG Server Platform URL is null |
| [OMAG-COMMON-400-003](#omag-common-400-003) | 400 | The OMAG Server name is null |
| [OMAG-COMMON-400-004](#omag-common-400-004) | 400 | The user identifier (user id) passed on the {0} operation is null |
| [OMAG-COMMON-400-005](#omag-common-400-005) | 400 | The unique identifier (guid) passed on the {0} parameter of the {1} operation is null |
| [OMAG-COMMON-400-006](#omag-common-400-006) | 400 | The name passed on the {0} parameter of the {1} operation is null |
| [OMAG-COMMON-400-007](#omag-common-400-007) | 400 | The array value passed on the {0} parameter of the {1} operation is null or empty |
| [OMAG-COMMON-400-008](#omag-common-400-008) | 400 | The starting point for the results {0}, passed on the {1} parameter of the {2} operation, is negative |
| [OMAG-COMMON-400-009](#omag-common-400-009) | 400 | The page size {0} for the results, passed on the {1} parameter of the {2} operation, is negative |
| [OMAG-COMMON-400-010](#omag-common-400-010) | 400 | The number of records to return, {0}, passed on the {1} parameter of the {2} operation, is greater than the allowable maximum of {3} |
| [OMAG-COMMON-400-011](#omag-common-400-011) | 400 | The connection object passed on the {0} parameter of the {1} operation is null |
| [OMAG-COMMON-400-012](#omag-common-400-012) | 400 | The enumeration value passed on the {0} parameter of the {1} operation is null |
| [OMAG-COMMON-400-013](#omag-common-400-013) | 400 | The text field value passed on the {0} parameter of the {1} operation is null |
| [OMAG-COMMON-400-014](#omag-common-400-014) | 400 | OMAG server has been called with a null local server name |
| [OMAG-COMMON-400-015](#omag-common-400-015) | 400 | The object passed on the {0} parameter of the {1} operation is null |
| [OMAG-COMMON-400-016](#omag-common-400-016) | 400 | An unexpected {0} exception was caught by {1}; error message was {2} |
| [OMAG-COMMON-400-017](#omag-common-400-017) | 400 | An request by user {0} to method {1} on server {2} had no request body |
| [OMAG-COMMON-400-018](#omag-common-400-018) | 400 | The type name {0} passed on method {1} of service {2} is not recognized |
| [OMAG-COMMON-400-019](#omag-common-400-019) | 400 | The type name {0} passed on method {1} of service {2} is not a sub-type of {3} |
| [OMAG-COMMON-400-021](#omag-common-400-021) | 400 | The identifier {0} of the {1} passed by {2} to method {3} of service {4} is not recognized by the {5} server |
| [OMAG-COMMON-400-022](#omag-common-400-022) | 400 | The search string passed on the {0} parameter of the {1} operation is null |
| [OMAG-COMMON-400-023](#omag-common-400-023) | 400 | Method {0} of service {1} cannot delete {2} identified by {3} because it is still in use |
| [OMAG-COMMON-400-024](#omag-common-400-024) | 400 | The connection object passed on the {0} parameter of the {1} operation has a null connector type |
| [OMAG-COMMON-400-026](#omag-common-400-026) | 400 | The {0} element {1} is expected to be anchored to {2} but is in fact anchored to {3}. Method {4} cannot proceed |
| [OMAG-COMMON-400-028](#omag-common-400-028) | 400 | The search string passed on the {0} parameter of the {1} operation is invalid and results in a {2} exception when executed.  The error message is {3} |
| [OMAG-COMMON-400-029](#omag-common-400-029) | 400 | The properties object passed on the {0} operation is either null or not of the correct {1} class |
| [OMAG-COMMON-400-030](#omag-common-400-030) | 400 | The {0} operation is only supported by {1} servers and server {2} is a {3} |
| [OMAG-COMMON-400-031](#omag-common-400-031) | 400 | A request by user {0} to method {1} on server {2} had no request body.  Add a request body of type {3} |
| [OMAG-COMMON-400-032](#omag-common-400-032) | 400 | The value {0} passed on the {1} parameter of the {2} operation is invalid |
| [OMAG-COMMON-404-001](#omag-common-404-001) | 404 | The {0} method has retrieved an instance for unique identifier (guid) {1} which is of type {2} rather than type {3} |
| [OMAG-COMMON-409-001](#omag-common-409-001) | 409 | Method {0} of service {1} is not able to create an instance of type {2} because parameter name {3} is defined as a unique property and value {4} is not available for use |
| [OMAG-COMMON-500-001](#omag-common-500-001) | 500 | Method {0} called by user {1} to OMAG Server {2} is not implemented in service {3} |
| [OMAG-COMMON-503-001](#omag-common-503-001) | 503 | A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3} |

----

### OMAG-COMMON-400-001

> The OMAG Server Platform URL is null

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot identify the OMAG Server Platform.

**User action**

Create a new client and pass the URL for the server on the constructor.


----

### OMAG-COMMON-400-003

> The OMAG Server name is null

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.SERVER_NAME_NOT_SPECIFIED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot locate to the OMAG Server to fulfill any request.

**User action**

Create a new client and pass the correct name for the server on the constructor.


----

### OMAG-COMMON-400-004

> The user identifier (user id) passed on the {0} operation is null

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NULL_USER_ID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot process the request without a user id.

**User action**

Correct the code in the caller to provide the user id.


----

### OMAG-COMMON-400-005

> The unique identifier (guid) passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NULL_GUID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without a guid.

**User action**

Correct the code in the caller to provide the guid.


----

### OMAG-COMMON-400-006

> The name passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NULL_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without a name.

**User action**

Correct the code in the caller to provide the name on the parameter.


----

### OMAG-COMMON-400-007

> The array value passed on the {0} parameter of the {1} operation is null or empty

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NULL_ARRAY_PARAMETER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without this value.

**User action**

Correct the code in the caller to provide the array.


----

### OMAG-COMMON-400-008

> The starting point for the results {0}, passed on the {1} parameter of the {2} operation, is negative

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NEGATIVE_START_FROM` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request with this invalid value.  It should be zero for the start of the values, or a number greater than 0 to start partway down the list

**User action**

Correct the code in the caller to provide a non-negative value for the starting point.


----

### OMAG-COMMON-400-009

> The page size {0} for the results, passed on the {1} parameter of the {2} operation, is negative

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NEGATIVE_PAGE_SIZE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request with this invalid value.  It should be zero to return all the result, or greater than zero to set a maximum

**User action**

Correct the code in the caller to provide a non-negative value for the page size.


----

### OMAG-COMMON-400-010

> The number of records to return, {0}, passed on the {1} parameter of the {2} operation, is greater than the allowable maximum of {3}

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.MAX_PAGE_SIZE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request with this page size value.

**User action**

Correct the code in the caller to provide a smaller page size .


----

### OMAG-COMMON-400-011

> The connection object passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NULL_CONNECTION_PARAMETER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without this connection.

**User action**

Correct the code in the caller to provide the connection.


----

### OMAG-COMMON-400-012

> The enumeration value passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NULL_ENUM` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without this enumeration value.

**User action**

Correct the code in the caller to provide the enumeration value.


----

### OMAG-COMMON-400-013

> The text field value passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NULL_TEXT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without this text field value.

**User action**

Correct the code in the caller to provide a value in the text field.


----

### OMAG-COMMON-400-014

> OMAG server has been called with a null local server name

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NULL_LOCAL_SERVER_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot configure the local server without knowing what it is called.

**User action**

The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly.


----

### OMAG-COMMON-400-015

> The object passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NULL_OBJECT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without this object.

**User action**

Correct the code in the caller to provide the object.


----

### OMAG-COMMON-400-016

> An unexpected {0} exception was caught by {1}; error message was {2}

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request and has returned an exception to the caller.

**User action**

Review the error message.  Also look up its full message definition which includes the system action and user action.  This is most likely to describe the correct action to take to resolve the error.  If that does not help, look for other diagnostics created at the same time.  Also validate that the caller is a valid client of this server and is operating correctly.


----

### OMAG-COMMON-400-017

> An request by user {0} to method {1} on server {2} had no request body

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NO_REQUEST_BODY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request without the request body since it contains key information.

**User action**

Update the caller to provide the request body.


----

### OMAG-COMMON-400-018

> The type name {0} passed on method {1} of service {2} is not recognized

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.UNRECOGNIZED_TYPE_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request because it does not understand the type.

**User action**

Update the caller to provide a correct type name.


----

### OMAG-COMMON-400-019

> The type name {0} passed on method {1} of service {2} is not a sub-type of {3}

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.BAD_SUB_TYPE_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request because one of the parameters is not of the right type.

**User action**

Update the caller to provide a valid type name for this request.


----

### OMAG-COMMON-400-021

> The identifier {0} of the {1} passed by {2} to method {3} of service {4} is not recognized by the {5} server

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.UNKNOWN_ELEMENT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot process the request because the identifier is invalid.

**User action**

Update the caller to provide a correct identifier.


----

### OMAG-COMMON-400-022

> The search string passed on the {0} parameter of the {1} operation is null

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NULL_SEARCH_STRING` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without a search string.

**User action**

Correct the code in the caller to provide the search string.


----

### OMAG-COMMON-400-023

> Method {0} of service {1} cannot delete {2} identified by {3} because it is still in use

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.CANNOT_DELETE_ELEMENT_IN_USE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request because it may cause other processing to fail.

**User action**

Ensure the element is no longer in use before retrying the operation.


----

### OMAG-COMMON-400-024

> The connection object passed on the {0} parameter of the {1} operation has a null connector type

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NULL_CONNECTOR_TYPE_PARAMETER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request without knowing the type of the connector that the connection object is requesting.

**User action**

Correct the code in the caller to provide the connector type embedded in the connection.


----

### OMAG-COMMON-400-026

> The {0} element {1} is expected to be anchored to {2} but is in fact anchored to {3}. Method {4} cannot proceed

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.WRONG_ANCHOR_GUID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because the requested object is not anchored to the expected element.

**User action**

Check the code in the caller to verify it is providing either the correct identifier of the object or the correctanchor identifier since this is the most likely cause of the error.  However, it is possible that there is an error in the way that the anchor GUID was set up in the element.  If this is the case, it is necessary to trace back to find how the element was created and then look at where the error was introduced.


----

### OMAG-COMMON-400-028

> The search string passed on the {0} parameter of the {1} operation is invalid and results in a {2} exception when executed.  The error message is {3}

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.INVALID_SEARCH_STRING` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request with this search string.

**User action**

Correct the code in the caller to provide a valid regular expression search string.


----

### OMAG-COMMON-400-029

> The properties object passed on the {0} operation is either null or not of the correct {1} class

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.INVALID_PROPERTIES_OBJECT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot proceed because it can not interpret the properties needed to execute the request.

**User action**

Correct the code in the caller to provide a valid properties object.


----

### OMAG-COMMON-400-030

> The {0} operation is only supported by {1} servers and server {2} is a {3}

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.INVALID_CALL_FOR_SERVER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot proceed because the server called does not support the request.

**User action**

Correct the code in the caller to call the correct server.


----

### OMAG-COMMON-400-031

> A request by user {0} to method {1} on server {2} had no request body.  Add a request body of type {3}

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.NO_REQUEST_BODY_FOR_CLASS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request without the request body since it contains key information.  It returns with an exception.

**User action**

Update the caller to provide the request body of the recommended type.


----

### OMAG-COMMON-400-032

> The value {0} passed on the {1} parameter of the {2} operation is invalid

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.INVALID_PARAMETER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request without a valid value for this parameter.

**User action**

Correct the code in the caller to provide a valid value.


----

### OMAG-COMMON-404-001

> The {0} method has retrieved an instance for unique identifier (guid) {1} which is of type {2} rather than type {3}

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.INSTANCE_WRONG_TYPE_FOR_GUID` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The request fails because the requested object is not of the right type.

**User action**

Retry the request with the correct unique identifier (or a different request suitable for the type of instance requested).


----

### OMAG-COMMON-409-001

> Method {0} of service {1} is not able to create an instance of type {2} because parameter name {3} is defined as a unique property and value {4} is not available for use

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.UNIQUE_NAME_ALREADY_IN_USE` |
| **HTTP error code** | 409 - Conflict - the request clashes with the current state of the metadata |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because the unique property for this new entity is not permitted either because it is a reserved value, or it is already in use.

**User action**

Retry the request with a different unique parameter name.


----

### OMAG-COMMON-500-001

> Method {0} called by user {1} to OMAG Server {2} is not implemented in service {3}

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.METHOD_NOT_IMPLEMENTED` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The user has issued a valid call to an open metadata REST API that is currently not yet implemented.

**User action**

Look to become a contributor or advocate for the Egeria community to help get this method implemented as soon as possible.


----

### OMAG-COMMON-503-001

> A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OMAGCommonErrorCode.CLIENT_SIDE_REST_API_ERROR` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.

**User action**

Look for errors in the local server's console to understand and correct the source of the error.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
