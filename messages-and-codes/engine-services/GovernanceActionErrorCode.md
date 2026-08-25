<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# GovernanceActionErrorCode

The GovernanceActionErrorCode error code is used to define first failure data capture (FFDC) for errors that occur when working with the Governance Action Engine Services. It is used in conjunction with all exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 4 |
| **Message identifiers begin** | `OMES-GOVERNANCE-ACTION-400-` |
| **Java class** | `org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionErrorCode` |
| **Module** | [open-metadata-implementation/engine-services/governance-action/governance-action-api](../../open-metadata-implementation/engine-services/governance-action/governance-action-api) |
| **Source** | [GovernanceActionErrorCode.java](../../open-metadata-implementation/engine-services/governance-action/governance-action-api/src/main/java/org/odpi/openmetadata/engineservices/governanceaction/ffdc/GovernanceActionErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/omes/governance-action/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMES-GOVERNANCE-ACTION-400-008](#omes-governance-action-400-008) | 400 | The Governance Action OMES are unable to initialize a new instance in server {0}; error message is {1} |
| [OMES-GOVERNANCE-ACTION-400-022](#omes-governance-action-400-022) | 400 | The governance action service {0} linked to governance action request type {1} can not be started.  The {2} exception was returned with message {3} |
| [OMES-GOVERNANCE-ACTION-400-030](#omes-governance-action-400-030) | 400 | The governance action service {0} linked to request type {1} can not be started because the Governance Action OMES does not support the {2} type of governance action service. |
| [OMES-GOVERNANCE-ACTION-400-031](#omes-governance-action-400-031) | 400 | The governance action service {0} linked to request type {1} can not be started because it is not a governance action service.  Its class is {2} rather than a subclass of {3} |

----

### OMES-GOVERNANCE-ACTION-400-008

> The Governance Action OMES are unable to initialize a new instance in server {0}; error message is {1}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionErrorCode.SERVICE_INSTANCE_FAILURE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The Governance Action OMES detected an error during the start up of a specific server instance.  No governance action services are available in the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMES-GOVERNANCE-ACTION-400-022

> The governance action service {0} linked to governance action request type {1} can not be started.  The {2} exception was returned with message {3}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionErrorCode.INVALID_GOVERNANCE_ACTION_SERVICE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The governance action request is not run and an error is returned to the caller.

**User action**

This may be an error in the governance action services's logic or the governance action service may not be properly deployed or there is a configuration error related to the governance action engine.  The configuration that defines the governance action request type in the governance action engine and links it to the governance action service is maintained in the metadata server by the Governance Action Engine OMAS's configuration API.Verify that this configuration is correct.  If it is then validate that the jar file containing the governance action service's implementation has been deployed so the Governance Action OMES can load it.  If all this is true this it is likely to be a code error in the governance action service in which case, raise an issue with the author of the governance action service to get it fixed.  Once the cause is resolved, retry the governance action request.


----

### OMES-GOVERNANCE-ACTION-400-030

> The governance action service {0} linked to request type {1} can not be started because the Governance Action OMES does not support the {2} type of governance action service.

|  |  |
|---|---|
| **Java constant** | `GovernanceActionErrorCode.UNKNOWN_GOVERNANCE_ACTION_SERVICE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action request is not run and an error is returned to the caller.  Subsequent requests to this governance action service will also fail.

**User action**

This version of the Governance Engine OMES does not support this type of governance action service.  It is likely that you need a future version of Egeria or another platform that supports this type of governance action service.


----

### OMES-GOVERNANCE-ACTION-400-031

> The governance action service {0} linked to request type {1} can not be started because it is not a governance action service.  Its class is {2} rather than a subclass of {3}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionErrorCode.NOT_GOVERNANCE_ACTION_SERVICE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The governance action request is not run and an error is returned to the caller.  Subsequent calls to this service will fail in the same way

**User action**

Correct the configuration for the Governance Action OMES to only include valid governance action service implementations.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
