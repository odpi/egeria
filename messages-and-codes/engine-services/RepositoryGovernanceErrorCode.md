<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# RepositoryGovernanceErrorCode

The RepositoryGovernanceErrorCode error code is used to define first failure data capture (FFDC) for errors that occur when working with the RepositoryGovernance Engine Services. It is used in conjunction with all exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 4 |
| **Message identifiers begin** | `OMES-REPOSITORY-GOVERNANCE-` |
| **Java class** | `org.odpi.openmetadata.engineservices.repositorygovernance.ffdc.RepositoryGovernanceErrorCode` |
| **Module** | [open-metadata-implementation/engine-services/repository-governance/repository-governance-api](../../open-metadata-implementation/engine-services/repository-governance/repository-governance-api) |
| **Source** | [RepositoryGovernanceErrorCode.java](../../open-metadata-implementation/engine-services/repository-governance/repository-governance-api/src/main/java/org/odpi/openmetadata/engineservices/repositorygovernance/ffdc/RepositoryGovernanceErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/omes/repository-governance/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMES-REPOSITORY-GOVERNANCE-400-001](#omes-repository-governance-400-001) | 400 | No repository governance context supplied to the repository governance service {0} |
| [OMES-REPOSITORY-GOVERNANCE-400-008](#omes-repository-governance-400-008) | 400 | The Repository Governance OMES are unable to initialize a new instance in server {0}; error message is {1} |
| [OMES-REPOSITORY-GOVERNANCE-400-022](#omes-repository-governance-400-022) | 400 | The repository governance service {0} linked to repository governance request type {1} can not be started.  The {2} exception was returned with message {3} |
| [OMES-REPOSITORY-GOVERNANCE-500-001](#omes-repository-governance-500-001) | 500 | Unexpected {0} exception in repository governance service {1} of type {2} detected by method {3}.  The error message was {4} |

----

### OMES-REPOSITORY-GOVERNANCE-400-001

> No repository governance context supplied to the repository governance service {0}

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceErrorCode.NULL_REPOSITORY_GOVERNANCE_CONTEXT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The repository governance service has no access to open metadata, the request type and request parameters.

**User action**

This may be a configuration or, more likely a code error in the repository governance engine.  Look for other error messages and review the code of the repository governance service.  Once the cause is resolved, retry the repository governance request.


----

### OMES-REPOSITORY-GOVERNANCE-400-008

> The Repository Governance OMES are unable to initialize a new instance in server {0}; error message is {1}

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceErrorCode.SERVICE_INSTANCE_FAILURE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The Repository Governance OMES detected an error during the start up of a specific server instance.  No repository governance services are available in the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMES-REPOSITORY-GOVERNANCE-400-022

> The repository governance service {0} linked to repository governance request type {1} can not be started.  The {2} exception was returned with message {3}

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceErrorCode.INVALID_REPOSITORY_GOVERNANCE_SERVICE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The repository governance request is not run and an error is returned to the caller.

**User action**

This may be an error in the repository governance services's logic or the repository governance service may not be properly deployed or there is a configuration error related to the repository governance engine.  The configuration that defines the repository governance request type in the repository governance engine and links it to the repository governance service is maintained in the metadata server by the RepositoryGovernance Engine OMAS's configuration API.Verify that this configuration is correct.  If it is then validate that the jar file containing the repository governance service's implementation has been deployed so the Repository Governance OMES can load it.  If all this is true this it is likely to be a code error in the repository governance service in which case, raise an issue with the author of the repository governance service to get it fixed.  Once the cause is resolved, retry the repository governance request.


----

### OMES-REPOSITORY-GOVERNANCE-500-001

> Unexpected {0} exception in repository governance service {1} of type {2} detected by method {3}.  The error message was {4}

|  |  |
|---|---|
| **Java constant** | `RepositoryGovernanceErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The repository governance service failed during its operation.

**User action**

This may be a configuration or a code error.  Look for other error messages and review the code of the repository governance service. Once the cause is resolved, retry the repository governance request.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
