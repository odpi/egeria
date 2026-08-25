<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# SchemaMakerAuditCode

The SchemaMakerAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `OMVS-SCHEMA-MAKER-` |
| **Java class** | `org.odpi.openmetadata.viewservices.schemamaker.ffdc.SchemaMakerAuditCode` |
| **Module** | [open-metadata-implementation/view-server-generic-services/schema-maker/schema-maker-server](../../open-metadata-implementation/view-server-generic-services/schema-maker/schema-maker-server) |
| **Source** | [SchemaMakerAuditCode.java](../../open-metadata-implementation/view-server-generic-services/schema-maker/schema-maker-server/src/main/java/org/odpi/openmetadata/viewservices/schemamaker/ffdc/SchemaMakerAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/omvs/schema-maker/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMVS-SCHEMA-MAKER-0001](#omvs-schema-maker-0001) | STARTUP | The Schema Maker Open Metadata View Service (OMVS) is initializing |
| [OMVS-SCHEMA-MAKER-0002](#omvs-schema-maker-0002) | STARTUP | The Schema Maker Open Metadata View Service (OMVS) is initialized |
| [OMVS-SCHEMA-MAKER-0003](#omvs-schema-maker-0003) | SHUTDOWN | The Schema Maker Open Metadata View Service (OMVS) is shutting down |
| [OMVS-SCHEMA-MAKER-0004](#omvs-schema-maker-0004) | EXCEPTION | The Schema Maker Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0} |
| [OMVS-SCHEMA-MAKER-0005](#omvs-schema-maker-0005) | SHUTDOWN | The Schema Maker Open Metadata View Service (OMVS) is shutting down server instance {0} |

----

### OMVS-SCHEMA-MAKER-0001

> The Schema Maker Open Metadata View Service (OMVS) is initializing

|  |  |
|---|---|
| **Java constant** | `SchemaMakerAuditCode.SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server is initializing the Schema Maker Open Metadata View Service. If the initialization is successful then audit message OMVS-SCHEMA-MAKER-0002 will be issued, if there were errors then they should be shown in the audit log.

**User action**

No action is required. This is part of the normal operation of the Schema Maker Open Metadata View Service.


----

### OMVS-SCHEMA-MAKER-0002

> The Schema Maker Open Metadata View Service (OMVS) is initialized

|  |  |
|---|---|
| **Java constant** | `SchemaMakerAuditCode.SERVICE_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The Schema Maker OMVS has completed initialization. Calls will be accepted by this service, if OMRS is also configured and the view server has been started.

**User action**

No action is required.  This is part of the normal operation of the Schema Maker Open Metadata View Service. Once the OMRS is configured and the server is started, Schema Maker view service requests can be accepted.


----

### OMVS-SCHEMA-MAKER-0003

> The Schema Maker Open Metadata View Service (OMVS) is shutting down

|  |  |
|---|---|
| **Java constant** | `SchemaMakerAuditCode.SERVICE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | none |

**System action**

The local server has requested shutdown of the Schema Maker OMVS.

**User action**

No action is required. The operator should verify that shutdown was intended. This is part of the normal operation of the Schema Maker OMVS.


----

### OMVS-SCHEMA-MAKER-0004

> The Schema Maker Open Metadata View Service (OMVS) cannot initialize a new instance; error message is {0}

|  |  |
|---|---|
| **Java constant** | `SchemaMakerAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The view service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMVS-SCHEMA-MAKER-0005

> The Schema Maker Open Metadata View Service (OMVS) is shutting down server instance {0}

|  |  |
|---|---|
| **Java constant** | `SchemaMakerAuditCode.SERVICE_TERMINATING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local handler has requested shut down of the Schema Maker OMVS.

**User action**

No action is required. This is part of the normal operation of the service.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
