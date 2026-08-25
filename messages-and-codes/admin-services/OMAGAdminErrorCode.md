<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMAGAdminErrorCode

The OMAGAdminErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMAG Server It is used in conjunction with OMAG Exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 45 |
| **Message identifiers begin** | `OMAG-ADMIN-` |
| **Java class** | `org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode` |
| **Module** | [open-metadata-implementation/admin-services/admin-services-api](../../open-metadata-implementation/admin-services/admin-services-api) |
| **Source** | [OMAGAdminErrorCode.java](../../open-metadata-implementation/admin-services/admin-services-api/src/main/java/org/odpi/openmetadata/adminservices/ffdc/OMAGAdminErrorCode.java) |
| **Further reading** | <https://egeria-project.org/guides/admin/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMAG-ADMIN-400-001](#omag-admin-400-001) | 400 | OMAG server has been called with a null local server name |
| [OMAG-ADMIN-400-002](#omag-admin-400-002) | 400 | OMAG Server Platform was requested to start OMAG Server {0} but the configuration document retrieved for it has the server name set to {1} |
| [OMAG-ADMIN-400-004](#omag-admin-400-004) | 400 | A REST API call to OMAG server {0} has been made with a null user identifier (userId) |
| [OMAG-ADMIN-400-005](#omag-admin-400-005) | 400 | Unable to configure server {0} since access service {1} is not registered in this OMAG Server Platform |
| [OMAG-ADMIN-400-006](#omag-admin-400-006) | 400 | Unable to configure server {0} since access service {1} is not enabled in this OMAG Server Platform |
| [OMAG-ADMIN-400-007](#omag-admin-400-007) | 400 | OMAG server {0} has been configured with a null cohort name |
| [OMAG-ADMIN-400-008](#omag-admin-400-008) | 400 | The local repository mode has not been set for OMAG server {0} |
| [OMAG-ADMIN-400-009](#omag-admin-400-009) | 400 | The OMAG server {0} has been passed null configuration |
| [OMAG-ADMIN-400-010](#omag-admin-400-010) | 400 | The OMAG server {0} has been passed a configuration document with no open metadata repository services configuration |
| [OMAG-ADMIN-400-011](#omag-admin-400-011) | 400 | No configuration document was found for OMAG server {0} |
| [OMAG-ADMIN-400-012](#omag-admin-400-012) | 400 | Unable to parse configuration document for OMAG server {0} due to exception {1} with message {2} |
| [OMAG-ADMIN-400-013](#omag-admin-400-013) | 400 | The OMAG server {0} is not able to open its configuration file {1} due to the following error: {2} which included a message {3} |
| [OMAG-ADMIN-400-014](#omag-admin-400-014) | 400 | The OMAG server {0} has been passed an invalid maximum page size of {1} |
| [OMAG-ADMIN-400-017](#omag-admin-400-017) | 400 | The OMAG server {0} cannot add open metadata services until the event bus is configured |
| [OMAG-ADMIN-400-018](#omag-admin-400-018) | 400 | OMAG server {0} has been called with a null metadata collection name |
| [OMAG-ADMIN-400-019](#omag-admin-400-019) | 400 | OMAG server {0} has been called with a configuration document that has no services configured |
| [OMAG-ADMIN-400-020](#omag-admin-400-020) | 400 | The {0} service of OMAG server {1} has been configured with a null root URL for the remote {2} access service |
| [OMAG-ADMIN-400-021](#omag-admin-400-021) | 400 | The {0} service of OMAG server {1} has been configured with a null server name for the remote {2} access service |
| [OMAG-ADMIN-400-022](#omag-admin-400-022) | 400 | OMAG server {0} has been configured with a null file name for an Open Metadata Archive |
| [OMAG-ADMIN-400-023](#omag-admin-400-023) | 400 | The configuration document for OMAG server {0} is at version {1} which is not compatible with this OMAG Server Platform which supports versions {2} |
| [OMAG-ADMIN-400-024](#omag-admin-400-024) | 400 | The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property.  The resulting {3} exception included the following message: {4} |
| [OMAG-ADMIN-400-025](#omag-admin-400-025) | 400 | The {0} Open Metadata Access Service (OMAS) has been passed a null enterprise OMRS topic for server {1} |
| [OMAG-ADMIN-400-026](#omag-admin-400-026) | 400 | Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connector.  The error message was {3} |
| [OMAG-ADMIN-400-027](#omag-admin-400-027) | 400 | OMAG server {0} has been called with a null connection for method {1} |
| [OMAG-ADMIN-400-028](#omag-admin-400-028) | 400 | The OMAG Server Platform has been called with a null connection for method {0} |
| [OMAG-ADMIN-400-031](#omag-admin-400-031) | 400 | The configuration document for server {0} includes configuration for a {1} but also has configuration for the {2} subsystem which is not a compatible combination |
| [OMAG-ADMIN-400-032](#omag-admin-400-032) | 400 | The supplied configuration for server {0} was not accepted because there is no value provided for property {1} |
| [OMAG-ADMIN-400-033](#omag-admin-400-033) | 400 | The OMAG server {0} cannot override the cohort topic until the {1} cohort is set up |
| [OMAG-ADMIN-400-034](#omag-admin-400-034) | 400 | The OMAG server {0} cannot override the cohort topic for the {1} cohort because the contents of the topic connection do not follow the expected pattern |
| [OMAG-ADMIN-400-035](#omag-admin-400-035) | 400 | Unable to classify the type of server for OMAG server {0} from its configuration document |
| [OMAG-ADMIN-400-036](#omag-admin-400-036) | 400 | Unable to configure server {0} since view service {1} is not registered in this OMAG Server Platform |
| [OMAG-ADMIN-400-037](#omag-admin-400-037) | 400 | Unable to configure server {0} since view service {1} is not enabled in this OMAG Server Platform |
| [OMAG-ADMIN-400-038](#omag-admin-400-038) | 400 | OMAG server {0} has been called by {1} with a null client config |
| [OMAG-ADMIN-400-039](#omag-admin-400-039) | 400 | The {0} service of OMAG server {1} has been configured with a null root URL for its remote OMAG Server |
| [OMAG-ADMIN-400-040](#omag-admin-400-040) | 400 | The {0} service of server {1} has been configured with a null name for the remote server |
| [OMAG-ADMIN-400-041](#omag-admin-400-041) | 400 | The connection passed to the {0} method does not describe a valid connector.  Connection object is: {1}.  The resulting exception {2} had message of {3}, system action of {4} and user action of {5} |
| [OMAG-ADMIN-400-042](#omag-admin-400-042) | 400 | The {0} Open Metadata View Service (OMVS) has been passed an invalid configuration of {1} in the {2} property |
| [OMAG-ADMIN-400-044](#omag-admin-400-044) | 400 | User {0} has attempted to obtain a server config store to be able to retrieve the OMAG server stored configurations but an error occurred |
| [OMAG-ADMIN-400-046](#omag-admin-400-046) | 400 | Unable to configure server {0} since engine service {1} is not registered in this OMAG Server Platform |
| [OMAG-ADMIN-400-047](#omag-admin-400-047) | 400 | Unable to configure server {0} since engine service {1} is not enabled in this OMAG Server Platform |
| [OMAG-ADMIN-400-052](#omag-admin-400-052) | 400 | Unable to configure an event mapper for OMAG server {0} because its local repository mode is set to {1} |
| [OMAG-ADMIN-404-100](#omag-admin-404-100) | 404 | The {0} audit log destination connection name does not exist, so the requested {1} operation cannot proceed |
| [OMAG-ADMIN-500-003](#omag-admin-500-003) | 500 | Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connection because the connector provider is incorrect.  The error message was {3} |
| [OMAG-ADMIN-500-004](#omag-admin-500-004) | 500 | The {0} service detected an unexpected {1} exception with message {2} during initialization |
| [OMAG-ADMIN-503-001](#omag-admin-503-001) | 503 | A client-side exception was received from API call {0} to OMAG Server Platform at {1}.  The error message was {2} |

----

### OMAG-ADMIN-400-001

> OMAG server has been called with a null local server name

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_LOCAL_SERVER_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot configure the local server.

**User action**

The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly.


----

### OMAG-ADMIN-400-002

> OMAG Server Platform was requested to start OMAG Server {0} but the configuration document retrieved for it has the server name set to {1}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.INCOMPATIBLE_SERVER_NAMES` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot start the OMAG server because it can not retrieve the correct configuration document.

**User action**

The configuration is retrieved from the configuration document store connector.  This connector is set up for the OMAG Server Platform.  It is either not configured correctly, or there is an error in its implementation because it is not retrieving the correct configuration document forthe requested server.


----

### OMAG-ADMIN-400-004

> A REST API call to OMAG server {0} has been made with a null user identifier (userId)

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_USER_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The server rejects the request.

**User action**

The user name is supplied in a parameter (typically called userID) in the call to the OMAG server. This parameter needs to be changes to a valid user identifier before the request can operate correctly.


----

### OMAG-ADMIN-400-005

> Unable to configure server {0} since access service {1} is not registered in this OMAG Server Platform

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.ACCESS_SERVICE_NOT_RECOGNIZED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot add this access service to the server's configuration document.

**User action**

Check that the name of the access service is correctly specified in the configuration request.  If you are not sure, issue the call to list the registered access services and verify the values you are using.  If the name is right but the access service should be registered,then the developer of the access service needs to add this registration to the code of the access service. An access service is registered in the OMAG Server Platform by adding a description of the access service to the access service registration (look for OMAGAccessServiceRegistration.registerAccessService() in existing access service modules to see this code pattern). Once the access service being requested is registered, retry the configuration request.


----

### OMAG-ADMIN-400-006

> Unable to configure server {0} since access service {1} is not enabled in this OMAG Server Platform

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.ACCESS_SERVICE_NOT_ENABLED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot configure the local server with this access service.

**User action**

Choose a different access service or enable the access service in this platform.


----

### OMAG-ADMIN-400-007

> OMAG server {0} has been configured with a null cohort name

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_COHORT_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot configure the local server with access to this cohort.

**User action**

The cohort name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can be configured to register with the cohort.


----

### OMAG-ADMIN-400-008

> The local repository mode has not been set for OMAG server {0}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.LOCAL_REPOSITORY_MODE_NOT_SET` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The local repository mode must be enabled before the event mapper connection, local metadata collection id or local metadata collection name is set.

**User action**

Set up a local repository for this server, then rerun the failing request.


----

### OMAG-ADMIN-400-009

> The OMAG server {0} has been passed null configuration

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_SERVER_CONFIG` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot initialize the local server instance without any configuration.

**User action**

Retry the request with server configuration.


----

### OMAG-ADMIN-400-010

> The OMAG server {0} has been passed a configuration document with no open metadata repository services configuration

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_REPOSITORY_CONFIG` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot initialize the local server instance because all servers need at least an audit log which is supported by the repository services.

**User action**

Use the administration services to add the repository services configuration.


----

### OMAG-ADMIN-400-011

> No configuration document was found for OMAG server {0}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NO_CONFIG_DOCUMENT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot initialize the local server instance without a configuration document.

**User action**

Use the administration services to build up the definition of the server into a configuration document.


----

### OMAG-ADMIN-400-012

> Unable to parse configuration document for OMAG server {0} due to exception {1} with message {2}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.CONFIG_DOCUMENT_PARSE_ERROR` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process a configuration document.

**User action**

Review the error message to understand why the parsing error occurred.


----

### OMAG-ADMIN-400-013

> The OMAG server {0} is not able to open its configuration file {1} due to the following error: {2} which included a message {3}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.BAD_CONFIG_FILE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot initialize the server.

**User action**

Review the error message to determine the cause of the problem.


----

### OMAG-ADMIN-400-014

> The OMAG server {0} has been passed an invalid maximum page size of {1}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.BAD_MAX_PAGE_SIZE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server failed to start.

**User action**

The maximum page size sets an upper limit on how many results a caller can request on a paged REST API call.  If it is set to zero then it means there is no limit, a positive number is the maximum paging size allowed.  Set the maximum page size in the configuration document to an appropriate value and restart the server.


----

### OMAG-ADMIN-400-017

> The OMAG server {0} cannot add open metadata services until the event bus is configured

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NO_EVENT_BUS_SET` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

No change has occurred in this server's configuration document.

**User action**

Add the event bus configuration using the administration services and retry the request.


----

### OMAG-ADMIN-400-018

> OMAG server {0} has been called with a null metadata collection name

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_METADATA_COLLECTION_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot add this metadata collection name to the configuration document for the local server.

**User action**

The metadata collection name is optional.  If it is not set up then the local server name is used instead.


----

### OMAG-ADMIN-400-019

> OMAG server {0} has been called with a configuration document that has no services configured

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.EMPTY_CONFIGURATION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The requested server provides no function.

**User action**

Use the administration services to add configuration for OMAG services to the server's configuration document.


----

### OMAG-ADMIN-400-020

> The {0} service of OMAG server {1} has been configured with a null root URL for the remote {2} access service

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_ACCESS_SERVICE_ROOT_URL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot accept this value in the configuration document because it needs this value to be able to call the correct server platform where the access service is running.

**User action**

The root URL is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly.


----

### OMAG-ADMIN-400-021

> The {0} service of OMAG server {1} has been configured with a null server name for the remote {2} access service

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_ACCESS_SERVICE_SERVER_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot accept this value in the configuration document because it needs this value to be able to call the correct server where the access service is running.

**User action**

The server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly.


----

### OMAG-ADMIN-400-022

> OMAG server {0} has been configured with a null file name for an Open Metadata Archive

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_FILE_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot configure the local server to load this Open Metadata Archive file.

**User action**

The file name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can load the open metadata archive.


----

### OMAG-ADMIN-400-023

> The configuration document for OMAG server {0} is at version {1} which is not compatible with this OMAG Server Platform which supports versions {2}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.INCOMPATIBLE_CONFIG_FILE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot configure the local server because it can not read the configuration document.

**User action**

Migrate the configuration document to a compatible version (or delete and recreate it).  See https://egeria-project.org/guides/


----

### OMAG-ADMIN-400-024

> The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property.  The resulting {3} exception included the following message: {4}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.BAD_CONFIG_PROPERTIES` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The access service has not been passed valid configuration .

**User action**

Correct the value of the failing configuration property and restart the server.


----

### OMAG-ADMIN-400-025

> The {0} Open Metadata Access Service (OMAS) has been passed a null enterprise OMRS topic for server {1}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NO_ENTERPRISE_TOPIC` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The access service has not been passed valid configuration for its enterprise repository services.   It needs this value to retrieve metadata from the open metadata repositories.

**User action**

Correct the configuration for the enterprise repository services and restart the server.


----

### OMAG-ADMIN-400-026

> Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connector.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.BAD_TOPIC_CONNECTOR` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The access service has not been passed valid configuration.  It needs the topic connector to send and receive events.

**User action**

Correct the configuration for the topic connector and restart the server.


----

### OMAG-ADMIN-400-027

> OMAG server {0} has been called with a null connection for method {1}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_CONNECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot add this connection to the server's configuration document.

**User action**

Change the call to pass a valid connection for the server.  If you want to clear the connection use the clear version of the method.


----

### OMAG-ADMIN-400-028

> The OMAG Server Platform has been called with a null connection for method {0}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_PLATFORM_CONNECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The admin services cannot add this connection to the platform runtime.

**User action**

Change the call to pass a valid connection for the platform.  If you want to clear the connection use the clear version of the method.


----

### OMAG-ADMIN-400-031

> The configuration document for server {0} includes configuration for a {1} but also has configuration for the {2} subsystem which is not a compatible combination

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.INCOMPATIBLE_SUBSYSTEMS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The server fails to initialize and an exception is returned to the caller.

**User action**

Reconfigure the server to include a compatible combination of subsystems.


----

### OMAG-ADMIN-400-032

> The supplied configuration for server {0} was not accepted because there is no value provided for property {1}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.MISSING_CONFIGURATION_PROPERTY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system returns an exception and does not update the configuration document for the server.

**User action**

Retry the configuration request with the property value set up correctly.


----

### OMAG-ADMIN-400-033

> The OMAG server {0} cannot override the cohort topic until the {1} cohort is set up

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.COHORT_NOT_KNOWN` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

No change has occurred in this server's configuration document because the admin services .

**User action**

Add the cohort configuration using the administration services and retry the request.


----

### OMAG-ADMIN-400-034

> The OMAG server {0} cannot override the cohort topic for the {1} cohort because the contents of the topic connection do not follow the expected pattern

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.COHORT_TOPIC_STRANGE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

No change has occurred in this server's configuration document because the topic connection in the cohort configuration does not follow the same structure as Egeria expects and so any update may have unexpected consequences.

**User action**

Use the setCohortConfig() method to manually update the cohort topic in the cohort configuration.


----

### OMAG-ADMIN-400-035

> Unable to classify the type of server for OMAG server {0} from its configuration document

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.UNCLASSIFIABLE_SERVER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot initialize the local server instance.

**User action**

Analyse the server's configuration document to determine why the type of server it requests is not identified.  Update the server's configuration document to provide a valid server configuration.


----

### OMAG-ADMIN-400-036

> Unable to configure server {0} since view service {1} is not registered in this OMAG Server Platform

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.VIEW_SERVICE_NOT_RECOGNIZED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot add this view service to the server's configuration document.

**User action**

Check that the name of the view service is correctly specified in the configuration request.  If you are not sure, issue the call to list the registered view services and verify the values you are using.  If the name is right, but the view service should be registered,then the developer of the view service needs to add this registration to the code of the view service. A view service is registered in the OMAG Server Platform by adding a description of the view service to the view service registration. Once the view service being requested is registered, retry the configuration request.


----

### OMAG-ADMIN-400-037

> Unable to configure server {0} since view service {1} is not enabled in this OMAG Server Platform

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.VIEW_SERVICE_NOT_ENABLED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot configure the local server with this view service.

**User action**

Validate and correct the name of the view service URL marker or enable the view service in this platform.


----

### OMAG-ADMIN-400-038

> OMAG server {0} has been called by {1} with a null client config

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_CLIENT_CONFIG` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot configure the local server with the governance service because it needs to be able to call a metadata access point or metadata server.

**User action**

The client config is supplied by the caller to the OMAG server. This call needs to be supplied, including the name and URL of the OMAG server, before the server can operate correctly.


----

### OMAG-ADMIN-400-039

> The {0} service of OMAG server {1} has been configured with a null root URL for its remote OMAG Server

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_OMAG_SERVER_ROOT_URL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot accept this value in the configuration document because the server would not be able to operate correctly.

**User action**

The root URL is supplied by the caller to the OMAG server. This URL value needs to be corrected before the server can operate correctly.


----

### OMAG-ADMIN-400-040

> The {0} service of server {1} has been configured with a null name for the remote server

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.NULL_OMAG_SERVER_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot accept a null value for this property in the configuration document because the server would not be able to operate properly.

**User action**

The OMAG Server name is supplied by the caller to the OMAG server. This remote server name needs to be corrected before the server can operate correctly.


----

### OMAG-ADMIN-400-041

> The connection passed to the {0} method does not describe a valid connector.  Connection object is: {1}.  The resulting exception {2} had message of {3}, system action of {4} and user action of {5}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.BAD_CONNECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The connection was tested by the Open Connector Framework (OCF) Connector Broker and it was unable to create aconnector for this connection and returned a detailed exception. Because of this exception, the system is unable to accept an invalid connection object and so the request is rejected.  No change is made to the configuration.

**User action**

Use the detail messages from the connector broker to work out what is wrong with the connection object.  Once the connection object is corrected, retry the request.


----

### OMAG-ADMIN-400-042

> The {0} Open Metadata View Service (OMVS) has been passed an invalid configuration of {1} in the {2} property

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.VIEW_SERVICE_CONFIG` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The view service has not been passed valid configuration.

**User action**

Check whether the view service expects SolutionViewServiceConfiguration or IntegrationViewServiceConfiguration, correct the configuration and restart the server.


----

### OMAG-ADMIN-400-044

> User {0} has attempted to obtain a server config store to be able to retrieve the OMAG server stored configurations but an error occurred

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.UNABLE_TO_OBTAIN_SERVER_CONFIG_STORE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The retrieve all server configurations operation is rejected, as the OMAG Server Configuration store could not be obtained.

**User action**

Check that the OMAG Server configuration connector has been specified correctly.


----

### OMAG-ADMIN-400-046

> Unable to configure server {0} since engine service {1} is not registered in this OMAG Server Platform

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.ENGINE_SERVICE_NOT_RECOGNIZED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot add this engine service to the server's configuration document.

**User action**

Check that the name of the engine service is correctly specified in the configuration request.  If you are not sure, issue the call to list the registered engine services and verify the values you are using.  If the name is right, but the engine service should be registered,then the developer of the engine service needs to add this registration to the code of the engine service. An engine service is registered in the OMAG Server Platform by adding a description of the engine service to the engine service registration. Once the engine service being requested is registered, retry the configuration request.


----

### OMAG-ADMIN-400-047

> Unable to configure server {0} since engine service {1} is not enabled in this OMAG Server Platform

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.ENGINE_SERVICE_NOT_ENABLED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot configure the local server with this engine service.

**User action**

Validate and correct the name of the engine service URL marker or enable the engine service in this platform.


----

### OMAG-ADMIN-400-052

> Unable to configure an event mapper for OMAG server {0} because its local repository mode is set to {1}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.LOCAL_REPOSITORY_MODE_NOT_PROXY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local repository mode must be set to repository proxy before the event mapper connection is set.  The system cannot configure the local server.

**User action**

The local repository mode is supplied by the caller to the OMAG server when the repository connection is set up.  This call to enable the repository connection needs to be made before the call to set the event mapper connection.


----

### OMAG-ADMIN-404-100

> The {0} audit log destination connection name does not exist, so the requested {1} operation cannot proceed

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.AUDIT_LOG_DESTINATION_NOT_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The audit log destination is not changed.

**User action**

Amend the request so it refers to an audit destination log connection name that exists.


----

### OMAG-ADMIN-500-003

> Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connection because the connector provider is incorrect.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.BAD_TOPIC_CONNECTOR_PROVIDER` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

This is an internal error.  The access service is not using a valid connector provider.

**User action**

Raise an issue on Egeria's GitHub and work with the Egeria community to resolve.


----

### OMAG-ADMIN-500-004

> The {0} service detected an unexpected {1} exception with message {2} during initialization

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot start the service in the OMAG server.

**User action**

This is likely to be either an operational or logic error. Look for other errors in the audit log.  Validate the request.  If you are stuck, raise an issue.


----

### OMAG-ADMIN-503-001

> A client-side exception was received from API call {0} to OMAG Server Platform at {1}.  The error message was {2}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminErrorCode.CLIENT_SIDE_REST_API_ERROR` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The server has issued a call to the open metadata admin service REST API in a remote server and has received an exception from the local client libraries.

**User action**

Look for errors in the local client's console to understand and correct the source of the error.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
