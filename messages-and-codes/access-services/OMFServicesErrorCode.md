<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMFServicesErrorCode

The OMFServicesErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Open Metadata Framework (OMF) Services. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 7 |
| **Message identifiers begin** | `OMF-SERVICES-` |
| **Java class** | `org.odpi.openmetadata.frameworkservices.omf.ffdc.OMFServicesErrorCode` |
| **Module** | [open-metadata-implementation/access-services/omf-metadata-management/omf-metadata-api](../../open-metadata-implementation/access-services/omf-metadata-management/omf-metadata-api) |
| **Source** | [OMFServicesErrorCode.java](../../open-metadata-implementation/access-services/omf-metadata-management/omf-metadata-api/src/main/java/org/odpi/openmetadata/frameworkservices/omf/ffdc/OMFServicesErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/framework-services/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMF-SERVICES-404-001](#omf-services-404-001) | 404 | The open metadata repository services are not initialized for the {0} operation |
| [OMF-SERVICES-409-001](#omf-services-409-001) | 404 | Multiple {0} relationships are attached to metadata element {1} |
| [OMF-SERVICES-500-001](#omf-services-500-001) | 500 | A null topic listener has been passed by user {0} on method {1} |
| [OMF-SERVICES-500-004](#omf-services-500-004) | 500 | An unexpected exception occurred when sending an event through connector {0} to the OMF Services out topic.  The failing event was {1}, the exception was {2} with message {2} |
| [OMF-SERVICES-500-006](#omf-services-500-006) | 500 | The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service running in OMAG Server {2} at {3} |
| [OMF-SERVICES-500-007](#omf-services-500-007) | 500 | The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2} at {3} is not of the required type. It should be an instance of {4} |
| [OMF-SERVICES-500-008](#omf-services-500-008) | 500 | The OMF Services has received an unexpected {0} exception during method {1} for service {2}.  The message was: {3} |

----

### OMF-SERVICES-404-001

> The open metadata repository services are not initialized for the {0} operation

|  |  |
|---|---|
| **Java constant** | `OMFServicesErrorCode.OMRS_NOT_INITIALIZED` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The system cannot connect to the open metadata property server.

**User action**

Check that the server where the Open Metadata Store Services are running initialized correctly.  Correct any errors discovered and retry the request when the open metadata services are available.


----

### OMF-SERVICES-409-001

> Multiple {0} relationships are attached to metadata element {1}

|  |  |
|---|---|
| **Java constant** | `OMFServicesErrorCode.MULTIPLE_RELATIONSHIPS_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}` |

**System action**

This relationship type is a singleton, which means that only once relationship of this type can be attached to an element.  The system cannot retrieve the singleton relationship because there are more than one relationship defined.

**User action**

Using a different method, retrieve all of the relationships of this type for this element and either delete/archive the relationships no longer needed, or adjust their effectivity date(s) so that only one relationship is effective at any one time.


----

### OMF-SERVICES-500-001

> A null topic listener has been passed by user {0} on method {1}

|  |  |
|---|---|
| **Java constant** | `OMFServicesErrorCode.NULL_LISTENER` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

There is a coding error in the caller to the OMF Services.

**User action**

Correct the caller logic and retry the request.


----

### OMF-SERVICES-500-004

> An unexpected exception occurred when sending an event through connector {0} to the OMF Services out topic.  The failing event was {1}, the exception was {2} with message {2}

|  |  |
|---|---|
| **Java constant** | `OMFServicesErrorCode.UNABLE_TO_SEND_EVENT` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system has issued a call to an open metadata access service REST API in a remote server and has received a null response.

**User action**

Look for errors in the remote server's audit log and console to understand and correct the source of the error.


----

### OMF-SERVICES-500-006

> The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service running in OMAG Server {2} at {3}

|  |  |
|---|---|
| **Java constant** | `OMFServicesErrorCode.NULL_CONNECTOR_RETURNED` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot create a connector which means some of its services will not work.

**User action**

This problem is likely to be caused by an incorrect connection object.  Check the settings on the Connectionand correct if necessary.  If the connection is correct, contact the Egeria community for help.


----

### OMF-SERVICES-500-007

> The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2} at {3} is not of the required type. It should be an instance of {4}

|  |  |
|---|---|
| **Java constant** | `OMFServicesErrorCode.WRONG_TYPE_OF_CONNECTOR` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot create the required connector which means some of its services will not work.

**User action**

Verify that the OMAG server is running and the OMAS service is correctly configured.


----

### OMF-SERVICES-500-008

> The OMF Services has received an unexpected {0} exception during method {1} for service {2}.  The message was: {3}

|  |  |
|---|---|
| **Java constant** | `OMFServicesErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The request returns with a PropertyServerException to indicate there has been an internal server error. The server also created a detailed error message and stack trace in the audit log.

**User action**

Review the stack trace to identify where the error occurred and work to resolve the cause.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
