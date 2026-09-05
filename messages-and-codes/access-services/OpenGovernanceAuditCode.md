<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenGovernanceAuditCode

The OpenGovernanceAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 9 |
| **Message identifiers begin** | `OPEN-GOVERNANCE-` |
| **Java class** | `org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenGovernanceAuditCode` |
| **Module** | [open-metadata-implementation/access-services/gaf-metadata-management/gaf-metadata-api](../../open-metadata-implementation/access-services/gaf-metadata-management/gaf-metadata-api) |
| **Source** | [OpenGovernanceAuditCode.java](../../open-metadata-implementation/access-services/gaf-metadata-management/gaf-metadata-api/src/main/java/org/odpi/openmetadata/frameworkservices/gaf/ffdc/OpenGovernanceAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/gaf-metadata-management/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OPEN-GOVERNANCE-0001](#open-governance-0001) | STARTUP | The Open Metadata Store Services are initializing a new server instance |
| [OPEN-GOVERNANCE-0002](#open-governance-0002) | STARTUP | The open integration context manager is being initialized for calls to server {0} on platform {1} |
| [OPEN-GOVERNANCE-0003](#open-governance-0003) | STARTUP | The Open Governance service is ready to publish notifications to topic {0} |
| [OPEN-GOVERNANCE-0005](#open-governance-0005) | STARTUP | The Open Metadata Store Services has initialized a new instance for server {0} |
| [OPEN-GOVERNANCE-0006](#open-governance-0006) | ERROR | The Open Metadata Store Services are unable to initialize a new instance; error message is {0} |
| [OPEN-GOVERNANCE-0011](#open-governance-0011) | SHUTDOWN | The Open Governance Service caught an unexpected {0} exception whilst shutting down the out topic listeners. The error message was: {1} |
| [OPEN-GOVERNANCE-0012](#open-governance-0012) | SHUTDOWN | The Open Metadata Store Services are shutting down its instance for server {0} |
| [OPEN-GOVERNANCE-0015](#open-governance-0015) | ERROR | The Open Governance Framework (OGF) received an {0} exception from the {1} governance action service while it was processing a watchdog event of type {2}; error message is {3} |
| [OPEN-GOVERNANCE-0021](#open-governance-0021) | ERROR | Failed to publish watchdog event to Watchdog Governance Action Service for governance engine {0}.  The exception was {1} with error message {2} |

----

### OPEN-GOVERNANCE-0001

> The Open Metadata Store Services are initializing a new server instance

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceAuditCode.SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server has started up a new instance of the Open Metadata Store Services.  It will support open metadata store REST requests.

**User action**

This is part of the normal start up of the service.  No action is required if this service startup was intentional.


----

### OPEN-GOVERNANCE-0002

> The open integration context manager is being initialized for calls to server {0} on platform {1}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceAuditCode.CONTEXT_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration daemon is initializing its context manager.

**User action**

Verify that the start up sequence goes on to initialize the context for each connector configured for this service.


----

### OPEN-GOVERNANCE-0003

> The Open Governance service is ready to publish notifications to topic {0}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceAuditCode.SERVICE_PUBLISHING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The local server has started up the event publisher for the GAF Services.  It will begin publishing metadata changes to its out topic.

**User action**

This is part of the normal start up of the service. Check that there are no errors from the event bus.


----

### OPEN-GOVERNANCE-0005

> The Open Metadata Store Services has initialized a new instance for server {0}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceAuditCode.SERVICE_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The Open Metadata Store Services has completed initialization of a new server instance.

**User action**

Verify that there are no error messages logged by the service.  If there are none it means that all parts of the service initialized successfully.


----

### OPEN-GOVERNANCE-0006

> The Open Metadata Store Services are unable to initialize a new instance; error message is {0}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OPEN-GOVERNANCE-0011

> The Open Governance Service caught an unexpected {0} exception whilst shutting down the out topic listeners. The error message was: {1}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceAuditCode.EVENT_SHUTDOWN_ERROR` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local administrator has requested shutdown of the engine host.  No more events will be received, although, due to this exception, the connection to the event bus may not be released properly.

**User action**

This is part of the normal shutdown of the engine host. However, an exception is not expected at this point unless it is the consequence of a previous error. Review the error message and any other reported failures to determine if this exception needs special attention.


----

### OPEN-GOVERNANCE-0012

> The Open Metadata Store Services are shutting down its instance for server {0}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceAuditCode.SERVICE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of an Open Metadata Store Services instance.  The open metadata store interfaces are no longer available and no configuration events will be published to the out topic

**User action**

This is part of the normal shutdown of the service.  Verify that all resources have been released.


----

### OPEN-GOVERNANCE-0015

> The Open Governance Framework (OGF) received an {0} exception from the {1} governance action service while it was processing a watchdog event of type {2}; error message is {3}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceAuditCode.WATCHDOG_EVENT_FAILURE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The engine services detected an error while processing a watchdog event.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  It may also be necessary to initiate the action that did not occur due to the failure to process this event.


----

### OPEN-GOVERNANCE-0021

> Failed to publish watchdog event to Watchdog Governance Action Service for governance engine {0}.  The exception was {1} with error message {2}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceAuditCode.WATCHDOG_LISTENER_EXCEPTION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

An open watchdog governance action service has raised an exception while processing an incoming watchdog event.  The exception explains the reason.

**User action**

Review the error messages and resolve the cause of the problem if needed.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
