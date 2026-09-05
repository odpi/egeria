<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMFAuditCode

The OMFAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 14 |
| **Message identifiers begin** | `OPEN-METADATA-` |
| **Java class** | `org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFAuditCode` |
| **Module** | [open-metadata-implementation/frameworks/open-metadata-framework](../../open-metadata-implementation/frameworks/open-metadata-framework) |
| **Source** | [OMFAuditCode.java](../../open-metadata-implementation/frameworks/open-metadata-framework/src/main/java/org/odpi/openmetadata/frameworks/openmetadata/ffdc/OMFAuditCode.java) |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OPEN-METADATA-0001](#open-metadata-0001) | INFO | The {0} connector is initiating the monitoring of file directory {1} |
| [OPEN-METADATA-0003](#open-metadata-0003) | INFO | The {0} connector is stopping the monitoring of directory {1} |
| [OPEN-METADATA-0004](#open-metadata-0004) | ERROR | An unexpected {0} exception was returned to the {1} connector by the by the Java file monitoring service.  The error message was {2} |
| [OPEN-METADATA-0005](#open-metadata-0005) | INFO | The {0} connector has stopped all of its file system monitoring and is shutting down |
| [OPEN-METADATA-0006](#open-metadata-0006) | INFO | The {0} connector has been disconnected - either due to its own actions or a cancel request |
| [OPEN-METADATA-0007](#open-metadata-0007) | ERROR | The {0} connector can not retrieve the correlation information for {1} open metadata element {2} linked via metadata collection {3} to external element {4} |
| [OPEN-METADATA-0008](#open-metadata-0008) | INFO | The valid metadata value {0} for property {1} has been created/updated in metadata element {2} |
| [OPEN-METADATA-0013](#open-metadata-0013) | EXCEPTION | The {0} connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [OPEN-METADATA-0015](#open-metadata-0015) | ERROR | The valid metadata value {0} for property {1} is not found |
| [OPEN-METADATA-0016](#open-metadata-0016) | INFO | Thread interrupt for connector {0} with message {1} |
| [OPEN-METADATA-0017](#open-metadata-0017) | INFO | File change notification for connector {0} of kind {1} for path {2} |
| [OPEN-METADATA-0018](#open-metadata-0018) | INFO | The {0} service is not yet monitoring notification type {1} because it is planned to start at {2} |
| [OPEN-METADATA-0019](#open-metadata-0019) | INFO | The {0} service has stopped monitoring notification type {1} because it has passed its completion date of {2} |
| [OPEN-METADATA-0025](#open-metadata-0025) | EXCEPTION | The Open Metadata Store has received an unexpected {0} exception while formatting a response during method {1} for service {2}.  The message was: {3} |

----

### OPEN-METADATA-0001

> The {0} connector is initiating the monitoring of file directory {1}

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.DIRECTORY_MONITORING_STARTING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

The connector is calling the Java file system monitoring library. This will start a background thread to monitor the file directory.  Any changes to the files in the directory will be reported to this connector.

**User action**

No action is required unless there are errors that follow indicating that the monitoring of the directory failed to start.


----

### OPEN-METADATA-0003

> The {0} connector is stopping the monitoring of directory {1}

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.DIRECTORY_MONITORING_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

The connector is unregistering the file system watch services for the directory. This will terminate the background thread set up to monitor the directory.  Any further changes to the files in this directory will no longer be notified to this connector.

**User action**

No action is required unless there are errors that follow indicating that the monitoring of the files failed to stop.


----

### OPEN-METADATA-0004

> An unexpected {0} exception was returned to the {1} connector by the by the Java file monitoring service.  The error message was {2}

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.UNEXPECTED_FILE_MONITORING_EXCEPTION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

The exception is logged and the connector continues to synchronize metadata through the refresh process.

**User action**

Use the message in the unexpected exception to determine the root cause of the error. Once this is resolved, follow the instructions in the messages produced by the server to restart the connector. Then validate that the monitoring starts successfully.


----

### OPEN-METADATA-0005

> The {0} connector has stopped all of its file system monitoring and is shutting down

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.FILE_SYSTEM_MONITORING_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

The file system monitor connector is disconnecting.

**User action**

No action is required unless there are errors that follow indicating that there were problems shutting down the connector.


----

### OPEN-METADATA-0006

> The {0} connector has been disconnected - either due to its own actions or a cancel request

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.DISCONNECT_DETECTED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

Egeria will attempt to stop the work of the connector

**User action**

Monitor the shutdown of the connector.


----

### OPEN-METADATA-0007

> The {0} connector can not retrieve the correlation information for {1} open metadata element {2} linked via metadata collection {3} to external element {4}

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.MISSING_CORRELATION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

The correlation information that should be associated with the open metadata element is missing and the connector is not able to confidently synchronize it with the element from the external system.

**User action**

Review the audit log to determine if there were errors detected when the open metadata entity was created.  The simplest resolution is to add the correlation information to the open metadata entity to allow the synchronization to continue.


----

### OPEN-METADATA-0008

> The valid metadata value {0} for property {1} has been created/updated in metadata element {2}

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.VALID_METADATA_UPDATE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

The new or updated element is stored in the metadata repository.

**User action**

This is an informational message to provide an audit log of changes to the open metadata valid values. If this is of interest then these messages can be captured and retained.


----

### OPEN-METADATA-0013

> The {0} connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

The connector records the error anf tries to continue; subsequent errors may occur as a result of this initial failure

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### OPEN-METADATA-0015

> The valid metadata value {0} for property {1} is not found

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.VALID_METADATA_MISSING` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

The metadata element for this valid metadata value is not stored in the repository.

**User action**

Check the parameter of the call to make sure there name and value have been properly defined.


----

### OPEN-METADATA-0016

> Thread interrupt for connector {0} with message {1}

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.THREAD_INTERRUPT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

The thread performing processing for the named connector has been interrupted.  This may indicate that shutdown is in progress.

**User action**

Thread interrupts are part of normal processing.  Look for other errors.


----

### OPEN-METADATA-0017

> File change notification for connector {0} of kind {1} for path {2}

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.FILE_CHANGE_EVENT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

A file change event has been received. If appropriate, it wil lbe passed to the connector.

**User action**

Check that the connector reacts correctly to this event.


----

### OPEN-METADATA-0018

> The {0} service is not yet monitoring notification type {1} because it is planned to start at {2}

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.NOTIFICATION_TYPE_NOT_STARTED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/concepts/notification-type/> |

**System action**

The notification type is skipped until its planned start date has passed.

**User action**

No action is required if the start date is intended.  A notification type whose subscribers are waiting for data has the wrong start date.


----

### OPEN-METADATA-0019

> The {0} service has stopped monitoring notification type {1} because it has passed its completion date of {2}

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.NOTIFICATION_TYPE_COMPLETED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/concepts/notification-type/> |

**System action**

The notification type is no longer monitored and its subscribers receive no further notifications.

**User action**

No action is required if the completion date is intended.  Extend it if the notification type should still be delivering to its subscribers.


----

### OPEN-METADATA-0025

> The Open Metadata Store has received an unexpected {0} exception while formatting a response during method {1} for service {2}.  The message was: {3}

|  |  |
|---|---|
| **Java constant** | `OMFAuditCode.UNEXPECTED_CONVERTER_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/frameworks/omf/overview/> |

**System action**

The request returns an exception detailing the cause of the error.

**User action**

Review the stack trace to identify where the error occurred and work to resolve the cause.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
