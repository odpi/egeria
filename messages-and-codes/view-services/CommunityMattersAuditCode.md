<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# CommunityMattersAuditCode

The CommunityMattersAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `OMVS-COMMUNITY-MATTERS-` |
| **Java class** | `org.odpi.openmetadata.viewservices.communitymatters.ffdc.CommunityMattersAuditCode` |
| **Module** | [open-metadata-implementation/view-services/community-matters/community-matters-server](../../open-metadata-implementation/view-services/community-matters/community-matters-server) |
| **Source** | [CommunityMattersAuditCode.java](../../open-metadata-implementation/view-services/community-matters/community-matters-server/src/main/java/org/odpi/openmetadata/viewservices/communitymatters/ffdc/CommunityMattersAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/omvs/community-matters/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMVS-COMMUNITY-MATTERS-0001](#omvs-community-matters-0001) | STARTUP | The Community Matters Open Metadata View Service (OMVS) is initializing |
| [OMVS-COMMUNITY-MATTERS-0002](#omvs-community-matters-0002) | STARTUP | The Community Matters Open Metadata View Service (OMVS) is initialized |
| [OMVS-COMMUNITY-MATTERS-0003](#omvs-community-matters-0003) | SHUTDOWN | The Community Matters Open Metadata View Service (OMVS) is shutting down |
| [OMVS-COMMUNITY-MATTERS-0004](#omvs-community-matters-0004) | EXCEPTION | The Community Matters Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0} |
| [OMVS-COMMUNITY-MATTERS-0005](#omvs-community-matters-0005) | SHUTDOWN | The Community Matters Open Metadata View Service (OMVS) is shutting down server instance {0} |

----

### OMVS-COMMUNITY-MATTERS-0001

> The Community Matters Open Metadata View Service (OMVS) is initializing

|  |  |
|---|---|
| **Java constant** | `CommunityMattersAuditCode.SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server is initializing the Community Matters Open Metadata View Service. If the initialization is successful then audit message OMVS-COMMUNITY-MATTERS-0002 will be issued, if there were errors then they should be shown in the audit log.

**User action**

No action is required. This is part of the normal operation of the Community Matters Open Metadata View Service.


----

### OMVS-COMMUNITY-MATTERS-0002

> The Community Matters Open Metadata View Service (OMVS) is initialized

|  |  |
|---|---|
| **Java constant** | `CommunityMattersAuditCode.SERVICE_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The Community Matters OMVS has completed initialization. Calls will be accepted by this service, if OMRS is also configured and the view server has been started.

**User action**

No action is required.  This is part of the normal operation of the Community Matters Open Metadata View Service. Once the OMRS is configured and the server is started, Community Matters view service requests can be accepted.


----

### OMVS-COMMUNITY-MATTERS-0003

> The Community Matters Open Metadata View Service (OMVS) is shutting down

|  |  |
|---|---|
| **Java constant** | `CommunityMattersAuditCode.SERVICE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | none |

**System action**

The local server has requested shutdown of the Community Matters OMVS.

**User action**

No action is required. The operator should verify that shutdown was intended. This is part of the normal operation of the Community Matters OMVS.


----

### OMVS-COMMUNITY-MATTERS-0004

> The Community Matters Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0}

|  |  |
|---|---|
| **Java constant** | `CommunityMattersAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMVS-COMMUNITY-MATTERS-0005

> The Community Matters Open Metadata View Service (OMVS) is shutting down server instance {0}

|  |  |
|---|---|
| **Java constant** | `CommunityMattersAuditCode.SERVICE_TERMINATING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local handler has requested shut down of the Community Matters OMVS.

**User action**

No action is required. This is part of the normal operation of the service.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
