<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# EngineHostServicesAuditCode

The EngineHostServicesAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 28 |
| **Message identifiers begin** | `ENGINE-HOST-SERVICES-` |
| **Java class** | `org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode` |
| **Module** | [open-metadata-implementation/governance-server-services/engine-host-services/engine-host-services-api](../../open-metadata-implementation/governance-server-services/engine-host-services/engine-host-services-api) |
| **Source** | [EngineHostServicesAuditCode.java](../../open-metadata-implementation/governance-server-services/engine-host-services/engine-host-services-api/src/main/java/org/odpi/openmetadata/governanceservers/enginehostservices/ffdc/EngineHostServicesAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/engine-host-services/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [ENGINE-HOST-SERVICES-0001](#engine-host-services-0001) | STARTUP | The engine host services are initializing in server {0} |
| [ENGINE-HOST-SERVICES-0002](#engine-host-services-0002) | STARTUP | The engine host {0} has initialized |
| [ENGINE-HOST-SERVICES-0003](#engine-host-services-0003) | EXCEPTION | The engine host services are unable to initialize a new instance of engine host {0}; exception {1} with message {2} |
| [ENGINE-HOST-SERVICES-0010](#engine-host-services-0010) | ERROR | The engine service {0} has been configured with a null admin class in engine host {1} |
| [ENGINE-HOST-SERVICES-0011](#engine-host-services-0011) | EXCEPTION | The engine service {0} has been configured with an admin class of {1} which can not be used by the class loader.  The {2} exception was returned with message {3} |
| [ENGINE-HOST-SERVICES-0012](#engine-host-services-0012) | STARTUP | The Open Metadata Engine Services (OMESs) are initializing in server {0} |
| [ENGINE-HOST-SERVICES-0014](#engine-host-services-0014) | STARTUP | {0} out of {1} Open Metadata Engine Services (OMESs) in engine host server {2} have initialized |
| [ENGINE-HOST-SERVICES-0015](#engine-host-services-0015) | SHUTDOWN | The governance engine {0} in server {1} is shutting down |
| [ENGINE-HOST-SERVICES-0016](#engine-host-services-0016) | STARTUP | The {0} engine service is disabled and will not be started |
| [ENGINE-HOST-SERVICES-0017](#engine-host-services-0017) | SHUTDOWN | The engine host {0} is shutting down |
| [ENGINE-HOST-SERVICES-0018](#engine-host-services-0018) | SHUTDOWN | The engine host {0} has completed shut down |
| [ENGINE-HOST-SERVICES-0019](#engine-host-services-0019) | SECURITY | Engine host server {0} is not authorized to call the Governance Engine OMAS running in server {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4} |
| [ENGINE-HOST-SERVICES-0020](#engine-host-services-0020) | EXCEPTION | Engine host server {0} failed to start.  The exception was {1} with message: {2} |
| [ENGINE-HOST-SERVICES-0021](#engine-host-services-0021) | INFO | Governance engine {0} in engine host server {1} is configured to process governance requests of type {2} |
| [ENGINE-HOST-SERVICES-0026](#engine-host-services-0026) | EXCEPTION | The engine host services are unable to retrieve the connection for the configuration listener for server {0} from metadata server {1}. Exception returned was {2} with error message {3} |
| [ENGINE-HOST-SERVICES-0027](#engine-host-services-0027) | STARTUP | The engine host services has registered the configuration listener for server {0} and governance engine {1}.  It will receive configuration updates from metadata access server {2} |
| [ENGINE-HOST-SERVICES-0028](#engine-host-services-0028) | INFO | All governance service configuration is being refreshed for governance engine {0} |
| [ENGINE-HOST-SERVICES-0029](#engine-host-services-0029) | INFO | All governance service configuration has been refreshed in governance engine {0} |
| [ENGINE-HOST-SERVICES-0030](#engine-host-services-0030) | INFO | Failed to refresh configuration for governance engine {0}'s registered governance service {1}, registered with the  properties {2}.  The exception was {3} with error message {4} |
| [ENGINE-HOST-SERVICES-0031](#engine-host-services-0031) | ERROR | Failed to refresh configuration for governance engine {0}.  The exception was {1} with error message {2} |
| [ENGINE-HOST-SERVICES-0033](#engine-host-services-0033) | SHUTDOWN | Engine action {0} running governance service {1} for governance engine {2} with request type {3} has recorded completion status of {4} and output guards of {5}.  Next engine action is given request parameters called {6} and action targets of {7}.  The completion message was {8} |
| [ENGINE-HOST-SERVICES-0034](#engine-host-services-0034) | EXCEPTION | Failed to execute engine action for governance engine {0}.  The exception was {1} with error message {2} |
| [ENGINE-HOST-SERVICES-0036](#engine-host-services-0036) | INFO | Governance engine {0} is cancelling running governance service for engine action {1}; thread name is {2} |
| [ENGINE-HOST-SERVICES-0037](#engine-host-services-0037) | EXCEPTION | The {0} governance engine handler for {1} has received an unexpected {2} exception during method {3}; the error message was: {4} |
| [ENGINE-HOST-SERVICES-0153](#engine-host-services-0153) | INFO | Refreshing governance engine {0} |
| [ENGINE-HOST-SERVICES-0154](#engine-host-services-0154) | INFO | Refreshing of governance engine {0} is complete |
| [ENGINE-HOST-SERVICES-2000](#engine-host-services-2000) | ERROR | {0} caught an exception {1} while processing engine action {2}; the error message was {3} |
| [ENGINE-HOST-SERVICES-2002](#engine-host-services-2002) | ERROR | {0} caught an exception {1} while restarting incomplete engine actions; the error message was {2} |

----

### ENGINE-HOST-SERVICES-0001

> The engine host services are initializing in server {0}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.SERVER_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

A new OMAG server has been started that is configured to run as an engine host.  Within the engine host are one or more Open Metadata Engine Services (OMESs) that host governance services (connectors) to actively govern open metadata and the digital landscape it represents.

**User action**

Verify that the start up sequence goes on to initialize the configured engine services and engines.


----

### ENGINE-HOST-SERVICES-0002

> The engine host {0} has initialized

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.SERVER_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The engine host services has completed initialization.

**User action**

Verify that all the configured engine services have successfully started, the configuration for their assigned governance engines has been retrieved from the Governance Engine OMAS by the engine host services and the engine services are able to connect to their partner OMAS.


----

### ENGINE-HOST-SERVICES-0003

> The engine host services are unable to initialize a new instance of engine host {0}; exception {1} with message {2}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The engine host services detected an error during the start up of a specific engine host instance.  Its integration services are not available.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the engine host.


----

### ENGINE-HOST-SERVICES-0010

> The engine service {0} has been configured with a null admin class in engine host {1}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.NULL_ENGINE_SERVICE_ADMIN_CLASS` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The engine service fails to start because the engine host can not initialize it.

**User action**

Each engine service registers itself using a static method call with the engine host astheir classes are loaded into the platform.  This is driven by the component scan for REST APIs implemented by the spring modules by the platform-chassis-spring module.  Ensure the engine service registers itself with the engine-host-services module and the platform-chassis-spring module has access to the engine service's spring module.


----

### ENGINE-HOST-SERVICES-0011

> The engine service {0} has been configured with an admin class of {1} which can not be used by the class loader.  The {2} exception was returned with message {3}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.BAD_ENGINE_SERVICE_ADMIN_CLASS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The engine service fails to start.  Its governance engines, if any, are not activated.

**User action**

Check that the jar containing the engine service's admin class is visible to the OMAG Server Platform through the class path - and that the class name specified includes the full, correct package name and class name.  Once the class is correctly set up, restart the engine host.  It will be necessary to restart the OMAG Server Platform if the class path needed adjustment.


----

### ENGINE-HOST-SERVICES-0012

> The Open Metadata Engine Services (OMESs) are initializing in server {0}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.STARTING_ENGINE_SERVICES` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

A new OMAG server has been started that is configured to run as an engine host.  Within the engine host are one or more Open Metadata Engine Services (OMESs) that host governance services (connectors) to actively govern open metadata and the digital landscape it represents.

**User action**

Verify that the start up sequence goes on to initialize the configured engine services and engines.


----

### ENGINE-HOST-SERVICES-0014

> {0} out of {1} Open Metadata Engine Services (OMESs) in engine host server {2} have initialized

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.ALL_ENGINE_SERVICES_STARTED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance engine has completed initialization and is ready to receive governance requests.

**User action**

Verify that the governance engine has been initialized wit the correct list of governance request types.


----

### ENGINE-HOST-SERVICES-0015

> The governance engine {0} in server {1} is shutting down

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.ENGINE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local administrator has requested shut down of this governance engine.  No more governance requests will be processed by this engine.

**User action**

Verify that this shutdown is intended and the governance engine is no longer needed.


----

### ENGINE-HOST-SERVICES-0016

> The {0} engine service is disabled and will not be started

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.SKIPPING_ENGINE_SERVICE` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

Although the engine service is not started, the initialization of the server continues.

**User action**

Engine services are typically disabled because the code is either incomplete or not working. It is necessary to connect with the Egeria community to find out when the service will be enabled.


----

### ENGINE-HOST-SERVICES-0017

> The engine host {0} is shutting down

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.SERVER_SHUTTING_DOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this engine host server.

**User action**

Verify that this server is no longer needed and the shutdown is expected.


----

### ENGINE-HOST-SERVICES-0018

> The engine host {0} has completed shut down

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.SERVER_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this engine host server and the operation has completed.

**User action**

Verify that all integration connectors that support the metadata exchange have shut down successfully.


----

### ENGINE-HOST-SERVICES-0019

> Engine host server {0} is not authorized to call the Governance Engine OMAS running in server {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.SERVER_NOT_AUTHORIZED` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

Some, or all the metadata definitions needed for the governance engines are not accessible from the openmetadata ecosystem.

**User action**

The userId comes from the engine host's configuration document.  It is stored as the localServerUserId.  The authorization failure may be limited to a single operation, or extend to all requests to the Governance Engine OMAS, specific metadata elements or the entire remote server.  Diagnose the extent of the authorization failure.  Then ensure the engine host's userId has sufficient access.


----

### ENGINE-HOST-SERVICES-0020

> Engine host server {0} failed to start.  The exception was {1} with message: {2}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.ENGINE_SERVICE_INSTANCE_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The server encountered a problem and has halted initialization of its services.

**User action**

Diagnose why the service cannot start using the messages logged to the audit log.


----

### ENGINE-HOST-SERVICES-0021

> Governance engine {0} in engine host server {1} is configured to process governance requests of type {2}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.SUPPORTED_REQUEST_TYPE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance engine has successfully retrieved the configuration to run requests for the named governance request type.  It is ready to run governance requests of this type

**User action**

Verify that this is an appropriate governance request type for the governance engine.


----

### ENGINE-HOST-SERVICES-0026

> The engine host services are unable to retrieve the connection for the configuration listener for server {0} from metadata server {1}. Exception returned was {2} with error message {3}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.NO_CONFIGURATION_LISTENER` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server continues to run.  The engine host services will start up the governance engines and they will operate with whatever configuration that they can retrieve.  Periodically the engine host services willretry the request to retrieve the connection information.  Without the connection, the engine host services will not be notified of changes to the governance engines' configuration

**User action**

This problem may be caused because the engine host services has been configured with the wrong location for the metadata server, or the metadata server is not running the Governance Engine OMAS service or the metadata server is not running at all.  Investigate the status of the metadata server to ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the refresh-config command or wait for the engine host services to retry the configuration request.


----

### ENGINE-HOST-SERVICES-0027

> The engine host services has registered the configuration listener for server {0} and governance engine {1}.  It will receive configuration updates from metadata access server {2}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.CONFIGURATION_LISTENER_REGISTERED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The engine host services continues to run.  The engine host services will start up the governance engines and they will operate with whatever configuration that they can retrieve.  Periodically the engine host services willretry the request to retrieve the connection information.  Without the connection, the engine host services will not be notified of changes to the governance engines' configuration

**User action**

This problem may be caused because the engine host services has been configured with the wrong location for the metadata server, or the metadata server is not running the Governance Engine OMAS service or the metadata server is not running at all.  Investigate the status of the metadata server to ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the refresh-config command or wait for the engine host services to retry the configuration request.


----

### ENGINE-HOST-SERVICES-0028

> All governance service configuration is being refreshed for governance engine {0}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.CLEARING_ALL_GOVERNANCE_SERVICE_CONFIG` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The engine host services will call the Governance Engine OMAS in the metadata server to retrieve details of all the governance services configured for this engine.During this process, some governance request may fail if the associated governanceservice is only partially configured.

**User action**

Monitor the engine host services to ensure all the governance services are retrieved. Then it is ready to process new governance requests.


----

### ENGINE-HOST-SERVICES-0029

> All governance service configuration has been refreshed in governance engine {0}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.FINISHED_ALL_GOVERNANCE_SERVICE_CONFIG` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The governance engine is ready to receive governance requests for all successfully loaded governance services.

**User action**

No action is required as long as all the expected governance services are loaded.If there are any governance services missing then validate the configuration ofthe governance engine in the metadata access server.


----

### ENGINE-HOST-SERVICES-0030

> Failed to refresh configuration for governance engine {0}'s registered governance service {1}, registered with the  properties {2}.  The exception was {3} with error message {4}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.GOVERNANCE_SERVICE_NO_CONFIG` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The governance engine cannot process governance request types for the failed governance service.

**User action**

Review the error messages and resolve the cause of the problem.  Then, either wait for the engine host services to refresh the configuration, or issue the refreshConfig call to request that the governance engine calls the Governance Engine OMAS to refresh the configuration for the governance service.


----

### ENGINE-HOST-SERVICES-0031

> Failed to refresh configuration for governance engine {0}.  The exception was {1} with error message {2}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.GOVERNANCE_ENGINE_NO_CONFIG` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance engine cannot process any governance requests until its configuration can be retrieved.

**User action**

Review the error messages and resolve the cause of the problem.  Either wait for the engine host services to refresh the configuration, or issue the refreshConfig call to request that the governance engine calls the Governance Engine OMAS to refresh the configuration for the governance service.


----

### ENGINE-HOST-SERVICES-0033

> Engine action {0} running governance service {1} for governance engine {2} with request type {3} has recorded completion status of {4} and output guards of {5}.  Next engine action is given request parameters called {6} and action targets of {7}.  The completion message was {8}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.ENGINE_ACTION_RECORD_COMPLETION` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}`, `{8}` |

**System action**

The governance engine shuts down this request to the governance service.

**User action**

Validate that the processing of this request is correct.


----

### ENGINE-HOST-SERVICES-0034

> Failed to execute engine action for governance engine {0}.  The exception was {1} with error message {2}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.ENGINE_ACTION_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance engine cannot process the engine action request.  The exception explains the reason. The engine action has been marked as FAILED.

**User action**

Review the error messages and resolve the cause of the problem.  Once resolved, it is possible to retry the governance action by updating its status back to REQUESTED status.


----

### ENGINE-HOST-SERVICES-0036

> Governance engine {0} is cancelling running governance service for engine action {1}; thread name is {2}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.ENGINE_ACTION_CANCELLED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance engine has been requested to stop the execution of a governance service by a cancel request issued by an external user.

**User action**

Validate that this request should have been cancelled.  Check it shutdown correctly.  Rerun the request if necessary.


----

### ENGINE-HOST-SERVICES-0037

> The {0} governance engine handler for {1} has received an unexpected {2} exception during method {3}; the error message was: {4}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The service cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### ENGINE-HOST-SERVICES-0153

> Refreshing governance engine {0}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.CLEARING_ALL_GOVERNANCE_ENGINE_CONFIG` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The engine host services will call the Governance Engine OMAS in the metadata server to retrieve details of this governance engine.

**User action**

Monitor the engine host services for errors.


----

### ENGINE-HOST-SERVICES-0154

> Refreshing of governance engine {0} is complete

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.FINISHED_ALL_GOVERNANCE_ENGINE_CONFIG` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

This governance engine is ready to receive governance requests for all successfully loaded governance services.

**User action**

No action is required as long as there are no errors reported.


----

### ENGINE-HOST-SERVICES-2000

> {0} caught an exception {1} while processing engine action {2}; the error message was {3}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.ACTION_PROCESSING_ERROR` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server is not able to start or complete the requested processing related to the governance service for this engine action.

**User action**

Follow the instructions for the message associated with the exception.


----

### ENGINE-HOST-SERVICES-2002

> {0} caught an exception {1} while restarting incomplete engine actions; the error message was {2}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesAuditCode.UNEXPECTED_EXCEPTION_DURING_RESTART` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The server is not able to complete the restart processing.

**User action**

Follow the instructions for the message associated with the exception to resolve the error.  You may need to restart the engine host.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
