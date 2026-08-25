<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# GovernanceActionAuditCode

The GovernanceActionAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 14 |
| **Message identifiers begin** | `OMES-GOVERNANCE-ACTION-` |
| **Java class** | `org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionAuditCode` |
| **Module** | [open-metadata-implementation/engine-services/governance-action/governance-action-api](../../open-metadata-implementation/engine-services/governance-action/governance-action-api) |
| **Source** | [GovernanceActionAuditCode.java](../../open-metadata-implementation/engine-services/governance-action/governance-action-api/src/main/java/org/odpi/openmetadata/engineservices/governanceaction/ffdc/GovernanceActionAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/omes/governance-action/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMES-GOVERNANCE-ACTION-0001](#omes-governance-action-0001) | STARTUP | The Governance Action engine services are initializing in server {0} |
| [OMES-GOVERNANCE-ACTION-0002](#omes-governance-action-0002) | ERROR | The Governance Action OMES cannot initialize a new instance of itself in server {0}; error message is {1} |
| [OMES-GOVERNANCE-ACTION-0014](#omes-governance-action-0014) | SHUTDOWN | The Governance Action OMES in server {0} is shutting down |
| [OMES-GOVERNANCE-ACTION-0016](#omes-governance-action-0016) | SHUTDOWN | The Governance Action OMES in server {0} has completed shutdown |
| [OMES-GOVERNANCE-ACTION-0017](#omes-governance-action-0017) | STARTUP | The {0} governance action service {1} is starting with request type {2} in governance action engine {3} (guid={4}) |
| [OMES-GOVERNANCE-ACTION-0018](#omes-governance-action-0018) | EXCEPTION | The {0} governance action service {1} threw a {2} exception in governance action engine {3} (guid={4}). The error message was {5} |
| [OMES-GOVERNANCE-ACTION-0019](#omes-governance-action-0019) | SHUTDOWN | The {0} governance action service {1} for request type {2} has completed with status {3} in {4} milliseconds |
| [OMES-GOVERNANCE-ACTION-0020](#omes-governance-action-0020) | INFO | The {0} governance action service {1} for request type {2} is continuing to run in a background thread |
| [OMES-GOVERNANCE-ACTION-0028](#omes-governance-action-0028) | EXCEPTION | Governance Action engine {0} cannot update the completion status for governance action service {1}.  The exception was {2} with error message {3} |
| [OMES-GOVERNANCE-ACTION-0029](#omes-governance-action-0029) | EXCEPTION | The governance action service {0} linked to request type {1} can not be started.  The {2} exception was returned with message {3} |
| [OMES-GOVERNANCE-ACTION-0030](#omes-governance-action-0030) | ERROR | The governance action service {0} linked to request type {1} can not be started because the Governance Action OMES does not support the {2} type of governance action service. |
| [OMES-GOVERNANCE-ACTION-0031](#omes-governance-action-0031) | ERROR | The governance action service {0} linked to request type {1} can not be started because it is not a governance action service.  Its class is {2} rather than a subclass of {3} |
| [OMES-GOVERNANCE-ACTION-0032](#omes-governance-action-0032) | STARTUP | {0} governance service ({1}) with request type {2} has initialized in governance engine {3} |
| [OMES-GOVERNANCE-ACTION-0033](#omes-governance-action-0033) | EXCEPTION | The Governance Action OMES has received an unexpected {0} exception during method {1}; the error message was: {2} |

----

### OMES-GOVERNANCE-ACTION-0001

> The Governance Action engine services are initializing in server {0}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.ENGINE_SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

A new OMAG server has been started that is configured to run the Governance Action OMES.  Within this engine service are one or more governance action engines that analyze the content of assets on demand and create annotation metadata. The configuration for the governance action engines is retrieved from the metadata server and the governance action engines are initialized.

**User action**

Verify that the start up sequence goes on to initialize the configured governance action engines.


----

### OMES-GOVERNANCE-ACTION-0002

> The Governance Action OMES cannot initialize a new instance of itself in server {0}; error message is {1}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The engine services detected an error during the start up of a specific engine host server instance.  Its governance action services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMES-GOVERNANCE-ACTION-0014

> The Governance Action OMES in server {0} is shutting down

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.SERVER_SHUTTING_DOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this engine service.

**User action**

Verify that this server is no longer needed and the shutdown is expected.


----

### OMES-GOVERNANCE-ACTION-0016

> The Governance Action OMES in server {0} has completed shutdown

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.SERVER_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this engine service and the operation has completed.

**User action**

Verify that all configured governance action engines shut down successfully.


----

### OMES-GOVERNANCE-ACTION-0017

> The {0} governance action service {1} is starting with request type {2} in governance action engine {3} (guid={4})

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.GOVERNANCE_ACTION_SERVICE_STARTING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

A new governance action service is starting to process a new request.

**User action**

Verify that the governance action service is correctly configured, this action is intended. Verify that this service runs successfully.


----

### OMES-GOVERNANCE-ACTION-0018

> The {0} governance action service {1} threw a {2} exception in governance action engine {3} (guid={4}). The error message was {5}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.GOVERNANCE_ACTION_SERVICE_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

A governance action service produced an un expected exception.

**User action**

Review the exception to determine the cause of the error.  It may be a coding error or configuration error.


----

### OMES-GOVERNANCE-ACTION-0019

> The {0} governance action service {1} for request type {2} has completed with status {3} in {4} milliseconds

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.GOVERNANCE_ACTION_SERVICE_COMPLETE` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

A governance action service has returned from the start() method and set up the completion status prior to returning.  The Governance Action OMES will call disconnect() on the governance action service since it is complete.  The Governance Action entity in the metadata store will be updated to reflect the completion status

**User action**

It is possible to query the result of the governance action through the Governance Engine OMAS REST API.


----

### OMES-GOVERNANCE-ACTION-0020

> The {0} governance action service {1} for request type {2} is continuing to run in a background thread

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.GOVERNANCE_ACTION_SERVICE_RETURNED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

A governance action service has returned from the start() method without setting up the completion status prior to returning.

**User action**

Validate that this governance action service should still be running.  Typically you would expect a GovernanceActionService using Watchdog events tostill be running at this stage because it will have registered a listener. The other types of governance action services should have completed during start() unless they are managing their own thread(s).


----

### OMES-GOVERNANCE-ACTION-0028

> Governance Action engine {0} cannot update the completion status for governance action service {1}.  The exception was {2} with error message {3}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.EXC_ON_ERROR_STATUS_UPDATE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server is not able to record the failed result for a governance action request. The governance action report status is not updated.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the governance action request.


----

### OMES-GOVERNANCE-ACTION-0029

> The governance action service {0} linked to request type {1} can not be started.  The {2} exception was returned with message {3}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.INVALID_GOVERNANCE_ACTION_SERVICE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The governance action request is not run and an error is returned to the caller.

**User action**

This may be an error in the governance action service's logic or the governance action service may not be properly deployed or there is a configuration error related to the governance action engine.  The configuration that defines the governance action request type in the governance action engine and links it to the governance action service is maintained in the metadata server by the Governance Action Engine OMAS's configuration API.Verify that this configuration is correct.  If it is then validate that the jar file containing the governance action service's implementation has been deployed so the Governance Action OMES can load it.  If all this is true this it is likely to be a code error in the governance action service in which case, raise an issue with the author of the governance action service to get it fixed.  Once the cause is resolved, retry the governance action request.


----

### OMES-GOVERNANCE-ACTION-0030

> The governance action service {0} linked to request type {1} can not be started because the Governance Action OMES does not support the {2} type of governance action service.

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.UNKNOWN_GOVERNANCE_ACTION_SERVICE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action request is not run and an error is returned to the caller.  Subsequent requests to this governance action service will also fail.

**User action**

This version of the Governance Engine OMES does not support this type of governance action service.  It is likely that you need a future version of Egeria or another platform that supports this type of governance action service.


----

### OMES-GOVERNANCE-ACTION-0031

> The governance action service {0} linked to request type {1} can not be started because it is not a governance action service.  Its class is {2} rather than a subclass of {3}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.NOT_GOVERNANCE_ACTION_SERVICE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The governance action request is not run and an error is returned to the caller.  Subsequent calls to this service will fail in the same way

**User action**

Correct the configuration for the Governance Action OMES to only include valid governance action service implementations.


----

### OMES-GOVERNANCE-ACTION-0032

> {0} governance service ({1}) with request type {2} has initialized in governance engine {3}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.GOVERNANCE_ACTION_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The governance engine is starting a governance action request.

**User action**

Validate that the governance action ran to successful completion.


----

### OMES-GOVERNANCE-ACTION-0033

> The Governance Action OMES has received an unexpected {0} exception during method {1}; the error message was: {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionAuditCode.UNEXPECTED_EXCEPTION` |
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
