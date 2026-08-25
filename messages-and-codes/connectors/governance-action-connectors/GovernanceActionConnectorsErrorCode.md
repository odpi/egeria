<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# GovernanceActionConnectorsErrorCode

The GovernanceActionConnectorsErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Basic File Connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 4 |
| **Message identifiers begin** | `GOVERNANCE-ACTION-CONNECTORS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/governance-action-connectors](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors) |
| **Source** | [GovernanceActionConnectorsErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/governanceactions/ffdc/GovernanceActionConnectorsErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/governance-action-service/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [GOVERNANCE-ACTION-CONNECTORS-400-006](#governance-action-connectors-400-006) | 400 | The {0} governance action service has been called without a source file name to work with |
| [GOVERNANCE-ACTION-CONNECTORS-404-002](#governance-action-connectors-404-002) | 404 | A FileFolder element with a path name of {0} is not found in the open metadata ecosystem |
| [GOVERNANCE-ACTION-CONNECTORS-500-003](#governance-action-connectors-500-003) | 500 | The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception message is: {2} |
| [GOVERNANCE-ACTION-CONNECTORS-500-004](#governance-action-connectors-500-004) | 500 | The {0} governance action service received an unexpected exception {1} during its processing; the error message was: {2} |

----

### GOVERNANCE-ACTION-CONNECTORS-400-006

> The {0} governance action service has been called without a source file name to work with

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsErrorCode.NO_SOURCE_FILE_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The provisioning governance action service connector is designed to manage files on request.  It cannot operate without the name of the source file and so it terminates with a FAILED completion status.

**User action**

The source file is passed to the governance action service through the request parameters or via the TargetForAction relationship.  Correct the information passed to the governance service and rerun the request


----

### GOVERNANCE-ACTION-CONNECTORS-404-002

> A FileFolder element with a path name of {0} is not found in the open metadata ecosystem

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsErrorCode.FOLDER_ELEMENT_NOT_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The governance action service is not able to proceed until the element has been created.

**User action**

The path name of the folder is passed either in the folderName configuration property; folderName request parameters or folderTarget action target.


----

### GOVERNANCE-ACTION-CONNECTORS-500-003

> The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception message is: {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsErrorCode.UNABLE_TO_REGISTER_LISTENER` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action throws a GovernanceServiceException in the hope that the .

**User action**

This is likely to be a configuration error.  Review the description of the exception's message to understand what is not set up correctly and and follow its instructions.


----

### GOVERNANCE-ACTION-CONNECTORS-500-004

> The {0} governance action service received an unexpected exception {1} during its processing; the error message was: {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action returns an exception to the Governance Action Engine.

**User action**

Use details from the error message to determine the cause of the error and retry the service call once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
