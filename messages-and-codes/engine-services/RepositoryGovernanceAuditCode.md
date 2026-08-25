<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# RepositoryGovernanceAuditCode

The RepositoryGovernanceAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 10 |
| **Message identifiers begin** | `OMES-REPOSITORY-GOVERNANCE-` |
| **Java class** | `org.odpi.openmetadata.engineservices.repositorygovernance.ffdc.RepositoryGovernanceAuditCode` |
| **Module** | [open-metadata-implementation/engine-services/repository-governance/repository-governance-api](../../open-metadata-implementation/engine-services/repository-governance/repository-governance-api) |
| **Source** | [RepositoryGovernanceAuditCode.java](../../open-metadata-implementation/engine-services/repository-governance/repository-governance-api/src/main/java/org/odpi/openmetadata/engineservices/repositorygovernance/ffdc/RepositoryGovernanceAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/omes/repository-governance/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMES-REPOSITORY-GOVERNANCE-0001](#omes-repository-governance-0001) | STARTUP | The Repository Governance engine services are initializing in server {0} |
| [OMES-REPOSITORY-GOVERNANCE-0012](#omes-repository-governance-0012) | ERROR | The Repository Governance OMES cannot initialize a new instance of itself in server {0}; error message is {1} |
| [OMES-REPOSITORY-GOVERNANCE-0014](#omes-repository-governance-0014) | SHUTDOWN | The Repository Governance OMES in server {0} is shutting down |
| [OMES-REPOSITORY-GOVERNANCE-0015](#omes-repository-governance-0015) | SHUTDOWN | The Repository Governance OMES in server {0} has completed shutdown |
| [OMES-REPOSITORY-GOVERNANCE-0016](#omes-repository-governance-0016) | STARTUP | The repository governance service {0} is starting with repository governance request type {1} in repository governance engine {2} (guid={3}) |
| [OMES-REPOSITORY-GOVERNANCE-0017](#omes-repository-governance-0017) | INFO | The repository governance service {0} for request type {1} is continuing to run in a background thread |
| [OMES-REPOSITORY-GOVERNANCE-0018](#omes-repository-governance-0018) | EXCEPTION | The repository governance service {0} threw a {1} exception during repository governance request type {2} in repository governance engine {3} (guid={4}). The error message was {5} |
| [OMES-REPOSITORY-GOVERNANCE-0019](#omes-repository-governance-0019) | SHUTDOWN | The repository governance service {0} has completed repository governance request type {1} in {2} milliseconds |
| [OMES-REPOSITORY-GOVERNANCE-0021](#omes-repository-governance-0021) | EXCEPTION | RepositoryGovernance engine {0} cannot update the status for repository governance service {1}.  The exception was {2} with error message {3} |
| [OMES-REPOSITORY-GOVERNANCE-0029](#omes-repository-governance-0029) | EXCEPTION | The repository governance service {0} linked to repository governance request type {1} can not be started.  The {2} exception was returned with message {3} |

----

### OMES-REPOSITORY-GOVERNANCE-0001

> The Repository Governance engine services are initializing in server {0}

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceAuditCode.ENGINE_SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

A new OMAG server has been started that is configured to run the Repository Governance OMES.  Within this engine service are one or more repository governance engines that analyze the content of assets on demand and create annotation metadata. The configuration for the repository governance engines is retrieved from the metadata server and the repository governance engines are initialized.

**User action**

Verify that the start up sequence goes on to initialize the configured repository governance engines.


----

### OMES-REPOSITORY-GOVERNANCE-0012

> The Repository Governance OMES cannot initialize a new instance of itself in server {0}; error message is {1}

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The engine services detected an error during the start up of a specific engine host server instance.  Its repository governance services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMES-REPOSITORY-GOVERNANCE-0014

> The Repository Governance OMES in server {0} is shutting down

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceAuditCode.SERVER_SHUTTING_DOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this engine service.

**User action**

Verify that this server is no longer needed and the shutdown is expected.


----

### OMES-REPOSITORY-GOVERNANCE-0015

> The Repository Governance OMES in server {0} has completed shutdown

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceAuditCode.SERVER_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this engine service and the operation has completed.

**User action**

Verify that all configured repository governance engines shut down successfully.


----

### OMES-REPOSITORY-GOVERNANCE-0016

> The repository governance service {0} is starting with repository governance request type {1} in repository governance engine {2} (guid={3})

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceAuditCode.REPOSITORY_GOVERNANCE_SERVICE_STARTING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

A new repository governance request is being processed.

**User action**

Verify that the repository governance service ran to completion.


----

### OMES-REPOSITORY-GOVERNANCE-0017

> The repository governance service {0} for request type {1} is continuing to run in a background thread

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceAuditCode.REPOSITORY_GOVERNANCE_SERVICE_RETURNED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

An repository governance service has returned from the start() method and without setting up the completion status prior to returning.

**User action**

Validate that this governance action service should still be running.  Typically you would expect an repository governance service tostill be running at this stage because it will have registered a listener.


----

### OMES-REPOSITORY-GOVERNANCE-0018

> The repository governance service {0} threw a {1} exception during repository governance request type {2} in repository governance engine {3} (guid={4}). The error message was {5}

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceAuditCode.REPOSITORY_GOVERNANCE_SERVICE_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

A repository governance service failed to complete the maintenance of an repository governance.

**User action**

Review the exception to determine the cause of the error.


----

### OMES-REPOSITORY-GOVERNANCE-0019

> The repository governance service {0} has completed repository governance request type {1} in {2} milliseconds

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceAuditCode.REPOSITORY_GOVERNANCE_SERVICE_COMPLETE` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

A repository governance request has completed.

**User action**

It is possible to query the result of the repository governance request through the Governance Engine OMAS's REST API.


----

### OMES-REPOSITORY-GOVERNANCE-0021

> RepositoryGovernance engine {0} cannot update the status for repository governance service {1}.  The exception was {2} with error message {3}

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceAuditCode.EXC_ON_ERROR_STATUS_UPDATE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server is not able to record the failed result for a repository governance request. The repository governance report status is not updated.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, retry the repository governance request.


----

### OMES-REPOSITORY-GOVERNANCE-0029

> The repository governance service {0} linked to repository governance request type {1} can not be started.  The {2} exception was returned with message {3}

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceAuditCode.INVALID_REPOSITORY_GOVERNANCE_SERVICE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The repository governance request is not run and an error is returned to the caller.

**User action**

This may be an error in the repository governance service's logic or the repository governance service may not be properly deployed or there is a configuration error related to the repository governance engine.  The configuration that defines the repository governance request type in the repository governance engine and links it to the repository governance service is maintained in the metadata server by the RepositoryGovernance Engine OMAS's configuration API.Verify that this configuration is correct.  If it is then validate that the jar file containing the repository governance service's implementation has been deployed so the Repository Governance OMES can load it.  If all this is true this it is likely to be a code error in the repository governance service in which case, raise an issue with the author of the repository governance service to get it fixed.  Once the cause is resolved, retry the repository governance request.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
