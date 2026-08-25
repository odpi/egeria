<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMAGServerInstanceErrorCode

The OMAGServerInstanceErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with OMAG Server instances within the OMAG Server Platform It is used in conjunction with all multi-tenant exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 12 |
| **Message identifiers begin** | `OMAG-MULTI-TENANT-` |
| **Java class** | `org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode` |
| **Module** | [open-metadata-implementation/common-services/multi-tenant](../../open-metadata-implementation/common-services/multi-tenant) |
| **Source** | [OMAGServerInstanceErrorCode.java](../../open-metadata-implementation/common-services/multi-tenant/src/main/java/org/odpi/openmetadata/commonservices/multitenant/ffdc/OMAGServerInstanceErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/multi-tenant/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMAG-MULTI-TENANT-400-002](#omag-multi-tenant-400-002) | 400 | The OMAG server {0} has been requested to shutdown but the following services are still running: {1} |
| [OMAG-MULTI-TENANT-400-003](#omag-multi-tenant-400-003) | 400 | Method {0} called on behalf of the {1} service cannot create a client-side open metadata topic connection because the topic name is not configured in the configuration for this service. |
| [OMAG-MULTI-TENANT-400-004](#omag-multi-tenant-400-004) | 400 | The connector provider class name {0} does not create a connector of class {1} which is required for the {2} |
| [OMAG-MULTI-TENANT-400-005](#omag-multi-tenant-400-005) | 400 | The URL marker {0} is not recognized |
| [OMAG-MULTI-TENANT-400-006](#omag-multi-tenant-400-006) | 400 | Generic view service {0} is not configured for this server and can only be called with the URL marker of a configured view service; {1} is not configured |
| [OMAG-MULTI-TENANT-404-001](#omag-multi-tenant-404-001) | 404 | The OMAG Server {0} is not available to service a request from user {1} |
| [OMAG-MULTI-TENANT-404-002](#omag-multi-tenant-404-002) | 404 | The {0} service is not available on OMAG Server {1} to handle a request from user {2} |
| [OMAG-MULTI-TENANT-404-003](#omag-multi-tenant-404-003) | 404 | The server name is not available for the {0} operation |
| [OMAG-MULTI-TENANT-404-004](#omag-multi-tenant-404-004) | 404 | The open metadata repository services are not initialized for the {0} operation |
| [OMAG-MULTI-TENANT-404-005](#omag-multi-tenant-404-005) | 404 | The open metadata repository services are not available for the {0} operation |
| [OMAG-MULTI-TENANT-500-001](#omag-multi-tenant-500-001) | 500 | An unsupported bean class named {0} was passed to the OMAG Server Platform by the {1} request for open metadata view service {2} on server {3}; error message was: {4} |
| [OMAG-MULTI-TENANT-500-003](#omag-multi-tenant-500-003) | 500 | Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connection because the connector provider is incorrect.  The error message was {3} |

----

### OMAG-MULTI-TENANT-400-002

> The OMAG server {0} has been requested to shutdown but the following services are still running: {1}

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.SERVICES_NOT_SHUTDOWN` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot shutdown the server correctly.

**User action**

Review other error messages to determine the cause of the problem.  This is likely to be a logic error in the services listed in the message


----

### OMAG-MULTI-TENANT-400-003

> Method {0} called on behalf of the {1} service cannot create a client-side open metadata topic connection because the topic name is not configured in the configuration for this service.

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.NO_TOPIC_INFORMATION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

This is a configuration error and an exception is sent to the requester.

**User action**

Correct the configuration of the access service to include the name of the topic.


----

### OMAG-MULTI-TENANT-400-004

> The connector provider class name {0} does not create a connector of class {1} which is required for the {2}

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.NOT_CORRECT_CONNECTOR_PROVIDER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

An invalid parameter exception is returned to the caller.

**User action**

Either change the connector or the hosting environment because the current combination is not compatible.


----

### OMAG-MULTI-TENANT-400-005

> The URL marker {0} is not recognized

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.INVALID_URL_MARKER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot continue with the request because the supplied URL marker does not match the registered services.

**User action**

Update the parameters passed on the request to either remove the URL marker, or set it to a URL marker that is recognized by the OMAG Server Platform.


----

### OMAG-MULTI-TENANT-400-006

> Generic view service {0} is not configured for this server and can only be called with the URL marker of a configured view service; {1} is not configured

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.UNCONFIGURED_URL_MARKER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot continue with the request because the supplied URL marker does not match the configured view services.  This generic service is running only as a support service for other, configured services.

**User action**

Update the parameters passed on the request to use a URL Marker for a configured view service, or add this view service to the configuration document for this server.


----

### OMAG-MULTI-TENANT-404-001

> The OMAG Server {0} is not available to service a request from user {1}

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.SERVER_NOT_AVAILABLE` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the request because the server is not running on the called platform.

**User action**

Verify that the correct server is being called on the correct platform and that this server is running. Retry the request when the server is available.


----

### OMAG-MULTI-TENANT-404-002

> The {0} service is not available on OMAG Server {1} to handle a request from user {2}

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.SERVICE_NOT_AVAILABLE` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request because the service is not available.

**User action**

Verify that the correct server is being called on the correct platform and that the requested service is configured to run there.  Once the correct environment is in place, retry the request.


----

### OMAG-MULTI-TENANT-404-003

> The server name is not available for the {0} operation

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.SERVER_NAME_NOT_AVAILABLE` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The system cannot return the server name because it is not available.

**User action**

Check that the server where the access service is running initialized correctly.  Correct any errors discovered and retry the request when the open metadata services are available.


----

### OMAG-MULTI-TENANT-404-004

> The open metadata repository services are not initialized for the {0} operation

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.OMRS_NOT_INITIALIZED` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The system cannot connect to the open metadata repository services because they are not running in this server.

**User action**

Check that the server where the called service is running initialized correctly.  Correct any errors discovered and retry the request when the open metadata services are available.


----

### OMAG-MULTI-TENANT-404-005

> The open metadata repository services are not available for the {0} operation

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.OMRS_NOT_AVAILABLE` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The system cannot connect to the open metadata repository services because they are not in the correct state to be called.

**User action**

Check that the server where the called service is running initialized correctly and is not in the process of shutting down.  Correct any errors discovered and retry the request when the open metadata repository services are available.


----

### OMAG-MULTI-TENANT-500-001

> An unsupported bean class named {0} was passed to the OMAG Server Platform by the {1} request for open metadata view service {2} on server {3}; error message was: {4}

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.INVALID_BEAN_CLASS` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because it is not able to instantiate the bean.

**User action**

Correct the code that initializes the converter during server start up.


----

### OMAG-MULTI-TENANT-500-003

> Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connection because the connector provider is incorrect.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceErrorCode.BAD_TOPIC_CONNECTOR_PROVIDER` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

This is an internal error.  The access service is not using a valid connector provider.

**User action**

Raise an issue on Egeria's GitHub and work with the Egeria community to resolve.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
