<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# MultiLanguageAuditCode

The MultiLanguageAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `OMVS-MULTI-LANGUAGE-` |
| **Java class** | `org.odpi.openmetadata.viewservices.multilanguage.ffdc.MultiLanguageAuditCode` |
| **Module** | [open-metadata-implementation/view-server-generic-services/multi-language/multi-language-server](../../open-metadata-implementation/view-server-generic-services/multi-language/multi-language-server) |
| **Source** | [MultiLanguageAuditCode.java](../../open-metadata-implementation/view-server-generic-services/multi-language/multi-language-server/src/main/java/org/odpi/openmetadata/viewservices/multilanguage/ffdc/MultiLanguageAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/omvs/multi-language/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMVS-MULTI-LANGUAGE-0001](#omvs-multi-language-0001) | STARTUP | The Multi Language Open Metadata View Service (OMVS) is initializing |
| [OMVS-MULTI-LANGUAGE-0002](#omvs-multi-language-0002) | STARTUP | The Multi Language Open Metadata View Service (OMVS) is initialized |
| [OMVS-MULTI-LANGUAGE-0003](#omvs-multi-language-0003) | SHUTDOWN | The Multi Language Open Metadata View Service (OMVS) is shutting down |
| [OMVS-MULTI-LANGUAGE-0004](#omvs-multi-language-0004) | EXCEPTION | The Multi Language Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0} |
| [OMVS-MULTI-LANGUAGE-0005](#omvs-multi-language-0005) | SHUTDOWN | The Multi Language Open Metadata View Service (OMVS) is shutting down server instance {0} |

----

### OMVS-MULTI-LANGUAGE-0001

> The Multi Language Open Metadata View Service (OMVS) is initializing

|  |  |
|---|---|
| **Java constant** | `MultiLanguageAuditCode.SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server is initializing the Multi Language Open Metadata View Service. If the initialization is successful then audit message OMVS-MULTI-LANGUAGE-0002 will be issued, if there were errors then they should be shown in the audit log.

**User action**

No action is required. This is part of the normal operation of the Multi Language Open Metadata View Service.


----

### OMVS-MULTI-LANGUAGE-0002

> The Multi Language Open Metadata View Service (OMVS) is initialized

|  |  |
|---|---|
| **Java constant** | `MultiLanguageAuditCode.SERVICE_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The Multi Language OMVS has completed initialization. Calls will be accepted by this service, if OMRS is also configured and the view server has been started.

**User action**

No action is required.  This is part of the normal operation of the Multi Language Open Metadata View Service. Once the OMRS is configured and the server is started, Multi Language view service requests can be accepted.


----

### OMVS-MULTI-LANGUAGE-0003

> The Multi Language Open Metadata View Service (OMVS) is shutting down

|  |  |
|---|---|
| **Java constant** | `MultiLanguageAuditCode.SERVICE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | none |

**System action**

The local server has requested shutdown of the Multi Language OMVS.

**User action**

No action is required. The operator should verify that shutdown was intended. This is part of the normal operation of the Multi Language OMVS.


----

### OMVS-MULTI-LANGUAGE-0004

> The Multi Language Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0}

|  |  |
|---|---|
| **Java constant** | `MultiLanguageAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMVS-MULTI-LANGUAGE-0005

> The Multi Language Open Metadata View Service (OMVS) is shutting down server instance {0}

|  |  |
|---|---|
| **Java constant** | `MultiLanguageAuditCode.SERVICE_TERMINATING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local handler has requested shut down of the Multi Language OMVS.

**User action**

No action is required. This is part of the normal operation of the service.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
