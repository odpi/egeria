<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ReferenceDataAuditCode

The ReferenceDataAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `OMVS-REFERENCE-DATA-` |
| **Java class** | `org.odpi.openmetadata.viewservices.referencedata.ffdc.ReferenceDataAuditCode` |
| **Module** | [open-metadata-implementation/view-services/reference-data/reference-data-server](../../open-metadata-implementation/view-services/reference-data/reference-data-server) |
| **Source** | [ReferenceDataAuditCode.java](../../open-metadata-implementation/view-services/reference-data/reference-data-server/src/main/java/org/odpi/openmetadata/viewservices/referencedata/ffdc/ReferenceDataAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/omvs/reference-data/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMVS-REFERENCE-DATA-0001](#omvs-reference-data-0001) | STARTUP | The Reference Data Open Metadata View Service (OMVS) is initializing |
| [OMVS-REFERENCE-DATA-0002](#omvs-reference-data-0002) | STARTUP | The Reference Data Open Metadata View Service (OMVS) is initialized |
| [OMVS-REFERENCE-DATA-0003](#omvs-reference-data-0003) | SHUTDOWN | The Reference Data Open Metadata View Service (OMVS) is shutting down |
| [OMVS-REFERENCE-DATA-0004](#omvs-reference-data-0004) | EXCEPTION | The Reference Data Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0} |
| [OMVS-REFERENCE-DATA-0005](#omvs-reference-data-0005) | SHUTDOWN | The Reference Data Open Metadata View Service (OMVS) is shutting down server instance {0} |

----

### OMVS-REFERENCE-DATA-0001

> The Reference Data Open Metadata View Service (OMVS) is initializing

|  |  |
|---|---|
| **Java constant** | `ReferenceDataAuditCode.SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server is initializing the Reference Data Open Metadata View Service. If the initialization is successful then audit message OMVS-REFERENCE-DATA-0002 will be issued, if there were errors then they should be shown in the audit log.

**User action**

No action is required. This is part of the normal operation of the Reference Data Open Metadata View Service.


----

### OMVS-REFERENCE-DATA-0002

> The Reference Data Open Metadata View Service (OMVS) is initialized

|  |  |
|---|---|
| **Java constant** | `ReferenceDataAuditCode.SERVICE_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The Reference Data OMVS has completed initialization. Calls will be accepted by this service, if OMRS is also configured and the view server has been started.

**User action**

No action is required.  This is part of the normal operation of the Reference Data Open Metadata View Service. Once the OMRS is configured and the server is started, Reference Data view service requests can be accepted.


----

### OMVS-REFERENCE-DATA-0003

> The Reference Data Open Metadata View Service (OMVS) is shutting down

|  |  |
|---|---|
| **Java constant** | `ReferenceDataAuditCode.SERVICE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | none |

**System action**

The local server has requested shutdown of the Reference Data OMVS.

**User action**

No action is required. The operator should verify that shutdown was intended. This is part of the normal operation of the Reference Data OMVS.


----

### OMVS-REFERENCE-DATA-0004

> The Reference Data Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0}

|  |  |
|---|---|
| **Java constant** | `ReferenceDataAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMVS-REFERENCE-DATA-0005

> The Reference Data Open Metadata View Service (OMVS) is shutting down server instance {0}

|  |  |
|---|---|
| **Java constant** | `ReferenceDataAuditCode.SERVICE_TERMINATING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local handler has requested shut down of the Reference Data OMVS.

**User action**

No action is required. This is part of the normal operation of the service.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
