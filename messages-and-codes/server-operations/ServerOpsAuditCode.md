<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ServerOpsAuditCode

The ServerOpsAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 24 |
| **Message identifiers begin** | `SERVER-OPS-` |
| **Java class** | `org.odpi.openmetadata.serveroperations.ffdc.ServerOpsAuditCode` |
| **Module** | [open-metadata-implementation/server-operations/server-operations-api](../../open-metadata-implementation/server-operations/server-operations-api) |
| **Source** | [ServerOpsAuditCode.java](../../open-metadata-implementation/server-operations/server-operations-api/src/main/java/org/odpi/openmetadata/serveroperations/ffdc/ServerOpsAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/server-operations/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [SERVER-OPS-0001](#server-ops-0001) | STARTUP | The {0} server is configured with a max page size of {1} |
| [SERVER-OPS-0002](#server-ops-0002) | STARTUP | The {0} server is configured with an unlimited maximum page size |
| [SERVER-OPS-0003](#server-ops-0003) | EXCEPTION | The {0} server is configured with an invalid max page size of {1} |
| [SERVER-OPS-0004](#server-ops-0004) | STARTUP | The {0} server has successfully completed start up.  The following services are running: {1} |
| [SERVER-OPS-0005](#server-ops-0005) | SHUTDOWN | The {0} server has begun the shutdown process |
| [SERVER-OPS-0006](#server-ops-0006) | SHUTDOWN | The {0} server has completed shutdown |
| [SERVER-OPS-0007](#server-ops-0007) | EXCEPTION | The {0} server has detected an {1} exception during server shutdown.  The error message was {2} |
| [SERVER-OPS-0010](#server-ops-0010) | STARTUP | The Open Metadata Access Services (OMASs) are starting |
| [SERVER-OPS-0011](#server-ops-0011) | STARTUP | The {0} is marked as DISABLED and so will not be started in the {1} server |
| [SERVER-OPS-0012](#server-ops-0012) | STARTUP | {0} out of {1} configured Open Metadata Access Services (OMASs) have started |
| [SERVER-OPS-0015](#server-ops-0015) | EXCEPTION | The {0} access service cannot initialize a new instance; error message is {1} |
| [SERVER-OPS-0016](#server-ops-0016) | EXCEPTION | The admin services are not able to start the {0} access service because the admin service class {1} is invalid; error message is {2} |
| [SERVER-OPS-0018](#server-ops-0018) | EXCEPTION | The OMAG server {0} has been passed a null admin services class name for access service {1} |
| [SERVER-OPS-0020](#server-ops-0020) | STARTUP | The Open Metadata View Services (OMVSs) are starting |
| [SERVER-OPS-0021](#server-ops-0021) | STARTUP | The {0} is marked as DISABLED and so will not be started in the {1} view server |
| [SERVER-OPS-0022](#server-ops-0022) | STARTUP | {0} out of {1} configured Open Metadata View Services (OMVSs) have started; the active urlMarkers are: {2} |
| [SERVER-OPS-0025](#server-ops-0025) | EXCEPTION | The {0} view service cannot initialize a new instance; error message is {1} |
| [SERVER-OPS-0026](#server-ops-0026) | EXCEPTION | The admin services are not able to start the {0} view service because the admin service class {1} is invalid; error message is {2} |
| [SERVER-OPS-0028](#server-ops-0028) | EXCEPTION | The OMAG server {0} has been passed a null admin services class name for view service {1} |
| [SERVER-OPS-0029](#server-ops-0029) | INFO | The OMAG server {0} is activating generic view services that are not configured; these view services can only be called using a urlMarker of a configured service |
| [SERVER-OPS-0030](#server-ops-0030) | STARTUP | {0} unconfigured generic Open Metadata View Services (OMVSs) have started |
| [SERVER-OPS-0100](#server-ops-0100) | STARTUP | The governance services subsystem for the {0} called {1} is about to start |
| [SERVER-OPS-0101](#server-ops-0101) | STARTUP | The governance services subsystem for the {0} called {1} has completed start up |
| [SERVER-OPS-0102](#server-ops-0102) | EXCEPTION | The admin services caught an {0} exception whilst initializing the governance services subsystem for the {1} called {2}; error message is {3} |

----

### SERVER-OPS-0001

> The {0} server is configured with a max page size of {1}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.MAX_PAGE_SIZE` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server has been configured with a maximum page size.  This is a recommended approach.  The maximum page size value sets an upper limit on the number of results that a caller can request on any paging REST API to this server.  Setting maximum page size helps to prevent a denial of service attack that uses very large requests to overwhelm the server.

**User action**

Validate that the setting of this value is adequate for the users of this server.  If the number is too small, callers will receive invalid parameter exceptions if they specify a maximum page size that is larger than this configured value.


----

### SERVER-OPS-0002

> The {0} server is configured with an unlimited maximum page size

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.UNLIMITED_MAX_PAGE_SIZE` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The server has been configured with a maximum page size of zero.  This means a requester can use any paging size that they need on a REST API call.  The down-side of this approach is that a server does not haveany defense against a denial of service attack that uses large requests to overwhelm the server.  It is not recommended for a production environment.

**User action**

It is recommended that this parameter is set to a positive integer that is large enough to satisfy legitimate callers to the server.  The parameter is set in the server's configuration document.


----

### SERVER-OPS-0003

> The {0} server is configured with an invalid max page size of {1}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.INVALID_MAX_PAGE_SIZE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server has been configured with a negative maximum page size.  The maximum page size value sets an upper limit on the number of results that a caller can request on aREST API call to this server.  Limiting this value to a negative number does not make any sense.

**User action**

Update this parameter in the configuration document for this server.  It should be set to a positive integer that is large enough to satisfy legitimate callers to the server.


----

### SERVER-OPS-0004

> The {0} server has successfully completed start up.  The following services are running: {1}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.SERVER_STARTUP_SUCCESS` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The request to start the server returns with a list of the services that were started.

**User action**

Review the start up messages to ensure that all the correct services have been started and the are operating without errors.


----

### SERVER-OPS-0005

> The {0} server has begun the shutdown process

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.SERVER_SHUTDOWN_STARTED` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The request to stop the server has been issued, either through an explicit command, or because theOMAG Server Platform is shutting down.  The operational admin services will sequentially shutdown each of the server's running subsystems.

**User action**

Review the shutdown messages to ensure that all the services are shutting down without errors.


----

### SERVER-OPS-0006

> The {0} server has completed shutdown

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.SERVER_SHUTDOWN_SUCCESS` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The request to shutdown the server has completed.  No REST API calls nor events will be processed by this server until it is restarted.

**User action**

Review the shutdown messages to ensure that all the subsystems have successfully released theresources that they were using.


----

### SERVER-OPS-0007

> The {0} server has detected an {1} exception during server shutdown.  The error message was {2}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.SERVER_SHUTDOWN_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The request to shutdown the server has failed with an exception.  The server is in an undetermined state.

**User action**

Review the shutdown messages to ensure that all the subsystems have successfully released theresources that they were using.  Restart the server whenever its services are needed again.


----

### SERVER-OPS-0010

> The Open Metadata Access Services (OMASs) are starting

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.STARTING_ACCESS_SERVICES` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The operational admin services are initializing the access service subsystems in a metadata server instance.  These provide specialist APIs for accessing open metadata.  Many of the access services support both a REST API and event-based interaction through a topic.  They also support options that control their behavior and the scope of the metadata that they work with.  The access service subsystems are started one at a time.  A fatal error in any of them prevents the server from starting.

**User action**

The server's configuration document lists the access services that should be started in this server.  Verify that the expected access services are started and that they each report that their components are working correctly.


----

### SERVER-OPS-0011

> The {0} is marked as DISABLED and so will not be started in the {1} server

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.SKIPPING_ACCESS_SERVICE` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The operational admin services will skip the initialization of the access service subsystem in this metadata server because it is marked as disabled in the configuration document.

**User action**

The server's configuration document lists the access services that should be started in this server.  Verify that this access service should be disabled. If it should be enabled then change the definitionof the access service in the configuration document to be enabled and restart the server.


----

### SERVER-OPS-0012

> {0} out of {1} configured Open Metadata Access Services (OMASs) have started

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.ALL_ACCESS_SERVICES_STARTED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The operational admin services have completed the initialization of all the access service subsystems enabled in the metadata server.  They are ready for use.  An access services is configured by adding its configuration to the server's configuration document.  By default a newly configured access service is also ENABLED. A configured access service may be temporarily disabled in the configurationdocument.  In which case the start up sequence skips it and the number of started access servicesis less than the number of configured access services.

**User action**

Review the start up messages to ensure that all the correct access services have been started and they are operating without errors.


----

### SERVER-OPS-0015

> The {0} access service cannot initialize a new instance; error message is {1}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.ACCESS_SERVICE_INSTANCE_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### SERVER-OPS-0016

> The admin services are not able to start the {0} access service because the admin service class {1} is invalid; error message is {2}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.BAD_ACCESS_SERVICE_ADMIN_CLASS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The admin services was unable to create an instance of the admin service class for the access service during the start up of a specific server instance.  The server fails to start.

**User action**

Review the error message and the other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### SERVER-OPS-0018

> The OMAG server {0} has been passed a null admin services class name for access service {1}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.NULL_ACCESS_SERVICE_ADMIN_CLASS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot initialize this access service. The server failed to start.

**User action**

If the access service should be initialized then set up the appropriate admin services class name in the access service's configuration and restart the server instance. Otherwise, remove the configuration for this access service and restart the server.


----

### SERVER-OPS-0020

> The Open Metadata View Services (OMVSs) are starting

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.STARTING_VIEW_SERVICES` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The operational admin services are initializing the view service subsystems in a metadata server instance.  These provide specialist task orientated APIs for viewing open metadata.  The view services support a REST API. The view service subsystems are started one at a time.  A fatal error in any of them prevents the server from starting.

**User action**

The server's configuration document lists the view services that should be started in this server.  Verify that the expected view services are started and that they each report that their components are working correctly.


----

### SERVER-OPS-0021

> The {0} is marked as DISABLED and so will not be started in the {1} view server

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.SKIPPING_VIEW_SERVICE` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The operational admin services will skip the initialization of the view service subsystem in this view server because it is marked as disabled in the configuration document.

**User action**

The server's configuration document lists the view services that should be started in this server.  Verify that this view service should be disabled. If it should be enabled then change the definitionof the view service in the configuration document to be enabled and restart the server.


----

### SERVER-OPS-0022

> {0} out of {1} configured Open Metadata View Services (OMVSs) have started; the active urlMarkers are: {2}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.ALL_CONFIGURED_VIEW_SERVICES_STARTED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The operational admin services have completed the initialization of all the configured view service subsystems enabled in the view server.  They are ready for use.  An view service is configured by adding its configuration to the server's configuration document.  By default a newly configured view service is also ENABLED. A configured view service may be temporarily disabled in the configurationdocument.  In which case the start up sequence skips it and the number of started view servicesis less than the number of configured view services.

**User action**

Review the start up messages to ensure that all the correct view services have been started and they are operating without errors.


----

### SERVER-OPS-0025

> The {0} view service cannot initialize a new instance; error message is {1}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.VIEW_SERVICE_INSTANCE_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem with the view service.  Once this is resolved, restart the view server.


----

### SERVER-OPS-0026

> The admin services are not able to start the {0} view service because the admin service class {1} is invalid; error message is {2}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.BAD_VIEW_SERVICE_ADMIN_CLASS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The admin services are unable to create an instance of the view service's admin class during the start up of a specific server instance.  The server fails to start.

**User action**

Review the error message and the other reported failures to determine the cause of the problem.  Once this is resolved, restart the view server.


----

### SERVER-OPS-0028

> The OMAG server {0} has been passed a null admin services class name for view service {1}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.NULL_VIEW_SERVICE_ADMIN_CLASS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot initialize this view service. The server failed to start.

**User action**

If the view service should be initialized then set up the appropriate admin services class name in the view service's configuration and restart the server instance. Otherwise, remove the configuration for this view service and restart the view server.


----

### SERVER-OPS-0029

> The OMAG server {0} is activating generic view services that are not configured; these view services can only be called using a urlMarker of a configured service

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.ACTIVATING_UNCONFIGURED_GENERIC_VIEW_SERVICES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The system is initializing any of the generic view services that are not configured.  These services can only be used if called using a urlMarker from one of the configured services.

**User action**

Check whether these view services should be properly configured.


----

### SERVER-OPS-0030

> {0} unconfigured generic Open Metadata View Services (OMVSs) have started

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.ALL_UNCONFIGURED_GENERIC_VIEW_SERVICES_STARTED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The operational admin services have completed the initialization of all the unconfigured generic view service subsystems enabled in the view server.  They are ready for use, but they may only be called using a urlMarker of a configured view service.  An view service is configured by adding its configuration to the server's configuration document.

**User action**

Review the start up messages to ensure that all the view services have been started and they are operating without errors.


----

### SERVER-OPS-0100

> The governance services subsystem for the {0} called {1} is about to start

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.STARTING_GOVERNANCE_SERVICES` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The admin services are about to start the governance services subsystem.  It will begin to initialize, logging start up messages to confirm that its internal components have successfully initialized.

**User action**

Review the start up messages as they occur to ensure the correct capability has been initialized in the governance server.


----

### SERVER-OPS-0101

> The governance services subsystem for the {0} called {1} has completed start up

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.GOVERNANCE_SERVICES_STARTED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance services subsystem has completed its start up and reported no fatal errors.  Its capability is operational.

**User action**

Review the start up messages from the governance services to ensure all expected components have started and are reporting no problems.  If no start up messages are produced by the governance services, it could be that the governance services failed silently.  Try calling the external services to see if it is operating.  Whether it is running successfully or failed silently, raise an issue with the Egeria community to get the start up messages improved.


----

### SERVER-OPS-0102

> The admin services caught an {0} exception whilst initializing the governance services subsystem for the {1} called {2}; error message is {3}

|  |  |
|---|---|
| **Java constant** | `ServerOpsAuditCode.GOVERNANCE_SERVICE_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The governance services subsystem detected an error during the start up of a specific server instance. It has logged information about the type of error.  Its services are not available and since these services are fundamental to the operation of the server, the server fails to start.  An exception is returned to the external caller of this request to start the server.

**User action**

Review the error message and the other reported failures from the governance services to determine the cause of the problem.  Typically you are looking for either incorrect configuration or one of the resources it wasexpecting is not available.  If there are no additional error messages then raise an issue with the Egeria community to get this improved.  Once the root cause of the problem is resolved, restart the server.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
