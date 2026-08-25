<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMFServicesAuditCode

The OMFServicesAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 11 |
| **Message identifiers begin** | `OMF-SERVICES-` |
| **Java class** | `org.odpi.openmetadata.frameworkservices.omf.ffdc.OMFServicesAuditCode` |
| **Module** | [open-metadata-implementation/access-services/omf-metadata-management/omf-metadata-api](../../open-metadata-implementation/access-services/omf-metadata-management/omf-metadata-api) |
| **Source** | [OMFServicesAuditCode.java](../../open-metadata-implementation/access-services/omf-metadata-management/omf-metadata-api/src/main/java/org/odpi/openmetadata/frameworkservices/omf/ffdc/OMFServicesAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/framework-services/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMF-SERVICES-0001](#omf-services-0001) | STARTUP | The Open Metadata Store Services are initializing a new server instance |
| [OMF-SERVICES-0002](#omf-services-0002) | STARTUP | The Open Metadata Store is ready to publish  notifications to topic {0} |
| [OMF-SERVICES-0005](#omf-services-0005) | STARTUP | The Open Metadata Store Services has initialized a new instance for server {0} |
| [OMF-SERVICES-0006](#omf-services-0006) | ERROR | The Open Metadata Store Services are unable to initialize a new instance; error message is {0} |
| [OMF-SERVICES-0007](#omf-services-0007) | SHUTDOWN | The Open Metadata Store is no longer publishing events to topic {0} |
| [OMF-SERVICES-0008](#omf-services-0008) | SHUTDOWN | The Open Metadata Store caught an unexpected {0} exception whilst shutting down the out topic {1}. The error message was: {2} |
| [OMF-SERVICES-0011](#omf-services-0011) | SHUTDOWN | The Open Metadata Store caught an unexpected {0} exception whilst shutting down the out topic listeners. The error message was: {1} |
| [OMF-SERVICES-0012](#omf-services-0012) | SHUTDOWN | The Open Metadata Store Services are shutting down its instance for server {0} |
| [OMF-SERVICES-0013](#omf-services-0013) | EVENT | The OMF Services has sent event of type: {0} |
| [OMF-SERVICES-0014](#omf-services-0014) | EXCEPTION | Event {0} could not be published due to {1} exception with message: {2} |
| [OMF-SERVICES-0020](#omf-services-0020) | INFO | Log message for asset {0} from governance service {1}: {2} |

----

### OMF-SERVICES-0001

> The Open Metadata Store Services are initializing a new server instance

|  |  |
|---|---|
| **Java constant** | `OMFServicesAuditCode.SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server has started up a new instance of the Open Metadata Store Services.  It will support open metadata store REST requests.

**User action**

This is part of the normal start up of the service.  No action is required if this service startup was intentional.


----

### OMF-SERVICES-0002

> The Open Metadata Store is ready to publish  notifications to topic {0}

|  |  |
|---|---|
| **Java constant** | `OMFServicesAuditCode.SERVICE_PUBLISHING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The local server has started up the event publisher for the OMF Services.  It will begin publishing metadata changes to its out topic.

**User action**

This is part of the normal start up of the service. Check that there are no errors from the event bus.


----

### OMF-SERVICES-0005

> The Open Metadata Store Services has initialized a new instance for server {0}

|  |  |
|---|---|
| **Java constant** | `OMFServicesAuditCode.SERVICE_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The Open Metadata Store Services has completed initialization of a new server instance.

**User action**

Verify that there are no error messages logged by the service.  If there are none it means that all parts of the service initialized successfully.


----

### OMF-SERVICES-0006

> The Open Metadata Store Services are unable to initialize a new instance; error message is {0}

|  |  |
|---|---|
| **Java constant** | `OMFServicesAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMF-SERVICES-0007

> The Open Metadata Store is no longer publishing events to topic {0}

|  |  |
|---|---|
| **Java constant** | `OMFServicesAuditCode.PUBLISHING_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of an OMF Services instance.  No more events will be published to the named topic.

**User action**

This is part of the normal shutdown of the service.   No action is required if this is serviceshutdown was intentional.


----

### OMF-SERVICES-0008

> The Open Metadata Store caught an unexpected {0} exception whilst shutting down the out topic {1}. The error message was: {2}

|  |  |
|---|---|
| **Java constant** | `OMFServicesAuditCode.PUBLISHING_SHUTDOWN_ERROR` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local administrator has requested shut down of an OMF Services instance.  No more events will be published to the named topic, although the connection to the event bus may not be released properly.

**User action**

This is part of the normal shutdown of the service. However, an exception is not expected at this point unless it is the consequence of a previous error. Review the error message and any other reported failures to determine if this exception needs special attention.


----

### OMF-SERVICES-0011

> The Open Metadata Store caught an unexpected {0} exception whilst shutting down the out topic listeners. The error message was: {1}

|  |  |
|---|---|
| **Java constant** | `OMFServicesAuditCode.EVENT_SHUTDOWN_ERROR` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local administrator has requested shut down of the engine host.  No more events will be received, although, due to this exception, the connection to the event bus may not be released properly.

**User action**

This is part of the normal shutdown of the engine host. However, an exception is not expected at this point unless it is the consequence of a previous error. Review the error message and any other reported failures to determine if this exception needs special attention.


----

### OMF-SERVICES-0012

> The Open Metadata Store Services are shutting down its instance for server {0}

|  |  |
|---|---|
| **Java constant** | `OMFServicesAuditCode.SERVICE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of an Open Metadata Store Services instance.  The open metadata store interfaces are no longer available and no configuration events will be published to the out topic

**User action**

This is part of the normal shutdown of the service.  Verify that all resources have been released.


----

### OMF-SERVICES-0013

> The OMF Services has sent event of type: {0}

|  |  |
|---|---|
| **Java constant** | `OMFServicesAuditCode.OUT_TOPIC_EVENT` |
| **Severity** | EVENT - An event was sent to or received from another participant in the server's ecosystem. |
| **Message inserts** | `{0}` |

**System action**

The service sends out notifications about changes to open metadata.  This message is to create a record of the events that are being published.

**User action**

This event indicates that one of the open metadata elements, relationships or classifications has changed.


----

### OMF-SERVICES-0014

> Event {0} could not be published due to {1} exception with message: {2}

|  |  |
|---|---|
| **Java constant** | `OMFServicesAuditCode.PROCESS_EVENT_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot publish the event to the OMF Services' OutTopic.

**User action**

Verify the topic configuration and that the event broker is running.


----

### OMF-SERVICES-0020

> Log message for asset {0} from governance service {1}: {2}

|  |  |
|---|---|
| **Java constant** | `OMFServicesAuditCode.ASSET_AUDIT_LOG` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

A governance service has logged a message about an asset.

**User action**

Review the message to ensure no action is required.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
