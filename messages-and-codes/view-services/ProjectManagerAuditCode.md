<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ProjectManagerAuditCode

The ProjectManagerAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `OMVS-PROJECT-MANAGER-` |
| **Java class** | `org.odpi.openmetadata.viewservices.projectmanager.ffdc.ProjectManagerAuditCode` |
| **Module** | [open-metadata-implementation/view-services/project-manager/project-manager-server](../../open-metadata-implementation/view-services/project-manager/project-manager-server) |
| **Source** | [ProjectManagerAuditCode.java](../../open-metadata-implementation/view-services/project-manager/project-manager-server/src/main/java/org/odpi/openmetadata/viewservices/projectmanager/ffdc/ProjectManagerAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/omvs/project-manager/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMVS-PROJECT-MANAGER-0001](#omvs-project-manager-0001) | STARTUP | The Project Manager Open Metadata View Service (OMVS) is initializing |
| [OMVS-PROJECT-MANAGER-0002](#omvs-project-manager-0002) | STARTUP | The Project Manager Open Metadata View Service (OMVS) is initialized |
| [OMVS-PROJECT-MANAGER-0003](#omvs-project-manager-0003) | SHUTDOWN | The Project Manager Open Metadata View Service (OMVS) is shutting down |
| [OMVS-PROJECT-MANAGER-0004](#omvs-project-manager-0004) | EXCEPTION | The Project Manager Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0} |
| [OMVS-PROJECT-MANAGER-0005](#omvs-project-manager-0005) | SHUTDOWN | The Project Manager Open Metadata View Service (OMVS) is shutting down server instance {0} |

----

### OMVS-PROJECT-MANAGER-0001

> The Project Manager Open Metadata View Service (OMVS) is initializing

|  |  |
|---|---|
| **Java constant** | `ProjectManagerAuditCode.SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server is initializing the Project Manager Open Metadata View Service. If the initialization is successful then audit message OMVS-PROJECT-MANAGER-0002 will be issued, if there were errors then they should be shown in the audit log.

**User action**

No action is required. This is part of the normal operation of the Project Manager Open Metadata View Service.


----

### OMVS-PROJECT-MANAGER-0002

> The Project Manager Open Metadata View Service (OMVS) is initialized

|  |  |
|---|---|
| **Java constant** | `ProjectManagerAuditCode.SERVICE_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The Project Manager OMVS has completed initialization. Calls will be accepted by this service, if OMRS is also configured and the view server has been started.

**User action**

No action is required.  This is part of the normal operation of the Project Manager Open Metadata View Service. Once the OMRS is configured and the server is started, Project Managerview service requests can be accepted.


----

### OMVS-PROJECT-MANAGER-0003

> The Project Manager Open Metadata View Service (OMVS) is shutting down

|  |  |
|---|---|
| **Java constant** | `ProjectManagerAuditCode.SERVICE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | none |

**System action**

The local server has requested shutdown of the Project Manager OMVS.

**User action**

No action is required. The operator should verify that shutdown was intended. This is part of the normal operation of the Project Manager OMVS.


----

### OMVS-PROJECT-MANAGER-0004

> The Project Manager Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0}

|  |  |
|---|---|
| **Java constant** | `ProjectManagerAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMVS-PROJECT-MANAGER-0005

> The Project Manager Open Metadata View Service (OMVS) is shutting down server instance {0}

|  |  |
|---|---|
| **Java constant** | `ProjectManagerAuditCode.SERVICE_TERMINATING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local handler has requested shut down of the Project Manager OMVS.

**User action**

No action is required. This is part of the normal operation of the service.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
