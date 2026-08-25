<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# WatchdogActionAuditCode

The WatchdogActionAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 11 |
| **Message identifiers begin** | `OMES-WATCHDOG-ACTION-` |
| **Java class** | `org.odpi.openmetadata.engineservices.watchdogaction.ffdc.WatchdogActionAuditCode` |
| **Module** | [open-metadata-implementation/engine-services/watchdog-action/watchdog-action-api](../../open-metadata-implementation/engine-services/watchdog-action/watchdog-action-api) |
| **Source** | [WatchdogActionAuditCode.java](../../open-metadata-implementation/engine-services/watchdog-action/watchdog-action-api/src/main/java/org/odpi/openmetadata/engineservices/watchdogaction/ffdc/WatchdogActionAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/omes/watchdog-action/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMES-WATCHDOG-ACTION-0001](#omes-watchdog-action-0001) | STARTUP | The Watchdog Action engine services are initializing in server {0} |
| [OMES-WATCHDOG-ACTION-0012](#omes-watchdog-action-0012) | ERROR | The Watchdog Action OMES cannot initialize a new instance of itself in server {0}; error message is {1} |
| [OMES-WATCHDOG-ACTION-0014](#omes-watchdog-action-0014) | SHUTDOWN | The Watchdog Action OMES in server {0} is shutting down |
| [OMES-WATCHDOG-ACTION-0016](#omes-watchdog-action-0016) | SHUTDOWN | The Watchdog Action OMES in server {0} has completed shutdown |
| [OMES-WATCHDOG-ACTION-0017](#omes-watchdog-action-0017) | STARTUP | The watchdog action service {0} is starting with request type {1} in watchdog action engine {2} (guid={3}) |
| [OMES-WATCHDOG-ACTION-0018](#omes-watchdog-action-0018) | EXCEPTION | The watchdog action service {0} threw a {1} exception during the processing for request type {2} in watchdog action engine {3} (guid={4}). The error message was {5} |
| [OMES-WATCHDOG-ACTION-0019](#omes-watchdog-action-0019) | SHUTDOWN | The watchdog action service {0} has completed request type {1} in {2} milliseconds |
| [OMES-WATCHDOG-ACTION-0020](#omes-watchdog-action-0020) | INFO | The {0} watchdog action service for request type {1} is continuing to run in a background thread |
| [OMES-WATCHDOG-ACTION-0022](#omes-watchdog-action-0022) | EXCEPTION | Watchdog action engine {0} cannot update the status for watchdog action service {1}.  The exception was {2} with error message {3} |
| [OMES-WATCHDOG-ACTION-0029](#omes-watchdog-action-0029) | EXCEPTION | The watchdog action service {0} linked to request type {1} can not be started.  The {2} exception was returned with message {3} |
| [OMES-WATCHDOG-ACTION-0033](#omes-watchdog-action-0033) | EXCEPTION | The Watchdog Action OMES has received an unexpected {0} exception during method {1}; the error message was: {2} |

----

### OMES-WATCHDOG-ACTION-0001

> The Watchdog Action engine services are initializing in server {0}

|  |  |
|---|---|
| **Java constant** | `WatchdogActionAuditCode.ENGINE_SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

A new OMAG server has been started that is configured to run the Watchdog Action OMES.  Within this engine service are one or more watchdog action engines that analyze the content of assets on demand and create annotation metadata. The configuration for the watchdog action engines is retrieved from the metadata server and the watchdog action engines are initialized.

**User action**

Verify that the start up sequence goes on to initialize the configured watchdog action engines.


----

### OMES-WATCHDOG-ACTION-0012

> The Watchdog Action OMES cannot initialize a new instance of itself in server {0}; error message is {1}

|  |  |
|---|---|
| **Java constant** | `WatchdogActionAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The engine services detected an error during the start up of a specific engine host server instance.  Its watchdog action services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMES-WATCHDOG-ACTION-0014

> The Watchdog Action OMES in server {0} is shutting down

|  |  |
|---|---|
| **Java constant** | `WatchdogActionAuditCode.SERVER_SHUTTING_DOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this engine service.

**User action**

Verify that this server is no longer needed and the shutdown is expected.


----

### OMES-WATCHDOG-ACTION-0016

> The Watchdog Action OMES in server {0} has completed shutdown

|  |  |
|---|---|
| **Java constant** | `WatchdogActionAuditCode.SERVER_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this engine service and the operation has completed.

**User action**

Verify that all configured watchdog action engines shut down successfully.


----

### OMES-WATCHDOG-ACTION-0017

> The watchdog action service {0} is starting with request type {1} in watchdog action engine {2} (guid={3})

|  |  |
|---|---|
| **Java constant** | `WatchdogActionAuditCode.WATCHDOG_ACTION_SERVICE_STARTING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

A new watchdog request is being processed.

**User action**

Verify that the watchdog action service ran to completion.


----

### OMES-WATCHDOG-ACTION-0018

> The watchdog action service {0} threw a {1} exception during the processing for request type {2} in watchdog action engine {3} (guid={4}). The error message was {5}

|  |  |
|---|---|
| **Java constant** | `WatchdogActionAuditCode.WATCHDOG_ACTION_SERVICE_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

A watchdog action service failed to complete the notification/actioning of a subscriber.

**User action**

Review the exception to determine the cause of the error.


----

### OMES-WATCHDOG-ACTION-0019

> The watchdog action service {0} has completed request type {1} in {2} milliseconds

|  |  |
|---|---|
| **Java constant** | `WatchdogActionAuditCode.WATCHDOG_ACTION_SERVICE_COMPLETE` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

A monitoring request has completed.

**User action**

It is possible to query the result of the monitoring request through Egeria's Open Metadata REST APIs.


----

### OMES-WATCHDOG-ACTION-0020

> The {0} watchdog action service for request type {1} is continuing to run in a background thread

|  |  |
|---|---|
| **Java constant** | `WatchdogActionAuditCode.WATCHDOG_ACTION_SERVICE_RETURNED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

A watchdog action service has returned from the start() method without setting up the completion status prior to returning.

**User action**

Validate that this watchdog action service should still be running.  Typically you would expect a Watchdog action service tostill be running at this stage because it will have registered a listener.


----

### OMES-WATCHDOG-ACTION-0022

> Watchdog action engine {0} cannot update the status for watchdog action service {1}.  The exception was {2} with error message {3}

|  |  |
|---|---|
| **Java constant** | `WatchdogActionAuditCode.EXC_ON_ERROR_STATUS_UPDATE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server is not able to record the failed result for a monitoring watchdog request. The subscribers are not notified.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the monitoring request.


----

### OMES-WATCHDOG-ACTION-0029

> The watchdog action service {0} linked to request type {1} can not be started.  The {2} exception was returned with message {3}

|  |  |
|---|---|
| **Java constant** | `WatchdogActionAuditCode.INVALID_WATCHDOG_ACTION_SERVICE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The monitoring watchdog request is not run and an error is returned to the caller.

**User action**

This may be an error in the watchdog action service's logic or the watchdog action service may not be properly deployed or there is a configuration error related to the watchdog action engine.  The configuration that defines the request type in the watchdog action engine and links it to the watchdog action service is maintained in the metadata server by the Governance Engine OMAS's configuration API.Verify that this configuration is correct.  If it is then validate that the jar file containing the watchdog action service's implementation has been deployed so the Watchdog Action OMES can load it.  If all this is true this it is likely to be a code error in the watchdog action service in which case, raise an issue with the author of the watchdog action service to get it fixed.  Once the cause is resolved, retry the monitoring request.


----

### OMES-WATCHDOG-ACTION-0033

> The Watchdog Action OMES has received an unexpected {0} exception during method {1}; the error message was: {2}

|  |  |
|---|---|
| **Java constant** | `WatchdogActionAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The service cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
