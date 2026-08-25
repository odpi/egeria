<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ActorManagerAuditCode

The ActorManagerAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `OMVS-ACTOR-MANAGER-` |
| **Java class** | `org.odpi.openmetadata.viewservices.actormanager.ffdc.ActorManagerAuditCode` |
| **Module** | [open-metadata-implementation/view-server-generic-services/actor-manager/actor-manager-server](../../open-metadata-implementation/view-server-generic-services/actor-manager/actor-manager-server) |
| **Source** | [ActorManagerAuditCode.java](../../open-metadata-implementation/view-server-generic-services/actor-manager/actor-manager-server/src/main/java/org/odpi/openmetadata/viewservices/actormanager/ffdc/ActorManagerAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/omvs/actor-manager/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMVS-ACTOR-MANAGER-0001](#omvs-actor-manager-0001) | STARTUP | The Actor Manager Open Metadata View Service (OMVS) is initializing |
| [OMVS-ACTOR-MANAGER-0002](#omvs-actor-manager-0002) | STARTUP | The Actor Manager Open Metadata View Service (OMVS) is initialized |
| [OMVS-ACTOR-MANAGER-0003](#omvs-actor-manager-0003) | SHUTDOWN | The Actor Manager Open Metadata View Service (OMVS) is shutting down |
| [OMVS-ACTOR-MANAGER-0004](#omvs-actor-manager-0004) | EXCEPTION | The Actor Manager Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0} |
| [OMVS-ACTOR-MANAGER-0005](#omvs-actor-manager-0005) | SHUTDOWN | The Actor Manager Open Metadata View Service (OMVS) is shutting down server instance {0} |

----

### OMVS-ACTOR-MANAGER-0001

> The Actor Manager Open Metadata View Service (OMVS) is initializing

|  |  |
|---|---|
| **Java constant** | `ActorManagerAuditCode.SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server is initializing the Actor Manager Open Metadata View Service. If the initialization is successful then audit message OMVS-ACTOR-MANAGER-0002 will be issued, if there were errors then they should be shown in the audit log.

**User action**

No action is required. This is part of the normal operation of the Actor Manager Open Metadata View Service.


----

### OMVS-ACTOR-MANAGER-0002

> The Actor Manager Open Metadata View Service (OMVS) is initialized

|  |  |
|---|---|
| **Java constant** | `ActorManagerAuditCode.SERVICE_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The Actor Manager OMVS has completed initialization. Calls will be accepted by this service, if OMRS is also configured and the view server has been started.

**User action**

No action is required.  This is part of the normal operation of the Actor Manager Open Metadata View Service. Once the OMRS is configured and the server is started, Actor Manager view service requests can be accepted.


----

### OMVS-ACTOR-MANAGER-0003

> The Actor Manager Open Metadata View Service (OMVS) is shutting down

|  |  |
|---|---|
| **Java constant** | `ActorManagerAuditCode.SERVICE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | none |

**System action**

The local server has requested shutdown of the Actor Manager OMVS.

**User action**

No action is required. The operator should verify that shutdown was intended. This is part of the normal operation of the Actor Manager OMVS.


----

### OMVS-ACTOR-MANAGER-0004

> The Actor Manager Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0}

|  |  |
|---|---|
| **Java constant** | `ActorManagerAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMVS-ACTOR-MANAGER-0005

> The Actor Manager Open Metadata View Service (OMVS) is shutting down server instance {0}

|  |  |
|---|---|
| **Java constant** | `ActorManagerAuditCode.SERVICE_TERMINATING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local handler has requested shut down of the Actor Manager OMVS.

**User action**

No action is required. This is part of the normal operation of the service.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
