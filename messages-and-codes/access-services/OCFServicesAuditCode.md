<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OCFServicesAuditCode

The OCFServicesAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 4 |
| **Message identifiers begin** | `CONNECTED-ASSET-SERVICES-` |
| **Java class** | `org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.ffdc.OCFServicesAuditCode` |
| **Module** | [open-metadata-implementation/access-services/ocf-metadata-management/ocf-metadata-api](../../open-metadata-implementation/access-services/ocf-metadata-management/ocf-metadata-api) |
| **Source** | [OCFServicesAuditCode.java](../../open-metadata-implementation/access-services/ocf-metadata-management/ocf-metadata-api/src/main/java/org/odpi/openmetadata/frameworkservices/ocf/metadatamanagement/ffdc/OCFServicesAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/ocf-metadata-management/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [CONNECTED-ASSET-SERVICES-0001](#connected-asset-services-0001) | STARTUP | The Open Connector Framework (OCF) Metadata Management Service is initializing the connected asset services in a new server instance |
| [CONNECTED-ASSET-SERVICES-0003](#connected-asset-services-0003) | STARTUP | The Open Connector Framework (OCF) Metadata Management Service has initialized a new instance for server {0} |
| [CONNECTED-ASSET-SERVICES-0004](#connected-asset-services-0004) | SHUTDOWN | The Open Connector Framework (OCF) Metadata Management Service is shutting down its instance of the connected asset services for server {0} |
| [CONNECTED-ASSET-SERVICES-0005](#connected-asset-services-0005) | ERROR | The Open Connector Framework (OCF) Metadata Management Service cannot initialize a new instance of the connected asset services; error message is {0} |

----

### CONNECTED-ASSET-SERVICES-0001

> The Open Connector Framework (OCF) Metadata Management Service is initializing the connected asset services in a new server instance

|  |  |
|---|---|
| **Java constant** | `OCFServicesAuditCode.SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server has started up a new instance of the service which provides the metadata lookup services for OCF Connectors.

**User action**

No action is required.  This is part of the normal operation of the service.


----

### CONNECTED-ASSET-SERVICES-0003

> The Open Connector Framework (OCF) Metadata Management Service has initialized a new instance for server {0}

|  |  |
|---|---|
| **Java constant** | `OCFServicesAuditCode.SERVICE_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The service has completed initialization of a new server instance.

**User action**

Verify that the service has started correctly.


----

### CONNECTED-ASSET-SERVICES-0004

> The Open Connector Framework (OCF) Metadata Management Service is shutting down its instance of the connected asset services for server {0}

|  |  |
|---|---|
| **Java constant** | `OCFServicesAuditCode.SERVICE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of a server instance.

**User action**

No action is required if the server is shutting down.


----

### CONNECTED-ASSET-SERVICES-0005

> The Open Connector Framework (OCF) Metadata Management Service cannot initialize a new instance of the connected asset services; error message is {0}

|  |  |
|---|---|
| **Java constant** | `OCFServicesAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
