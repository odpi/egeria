<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenGovernanceErrorCode

The OpenGovernanceErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Open Governance Framework (OGF) Services. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `OPEN-GOVERNANCE-` |
| **Java class** | `org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenGovernanceErrorCode` |
| **Module** | [open-metadata-implementation/access-services/gaf-metadata-management/gaf-metadata-api](../../open-metadata-implementation/access-services/gaf-metadata-management/gaf-metadata-api) |
| **Source** | [OpenGovernanceErrorCode.java](../../open-metadata-implementation/access-services/gaf-metadata-management/gaf-metadata-api/src/main/java/org/odpi/openmetadata/frameworkservices/gaf/ffdc/OpenGovernanceErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/gaf-metadata-management/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OPEN-GOVERNANCE-404-001](#open-governance-404-001) | 404 | The open metadata repository services are not initialized for the {0} operation |
| [OPEN-GOVERNANCE-500-001](#open-governance-500-001) | 500 | A null topic listener has been passed by user {0} on method {1} |
| [OPEN-GOVERNANCE-500-006](#open-governance-500-006) | 500 | The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service running in OMAG Server {2} at {3} |
| [OPEN-GOVERNANCE-500-007](#open-governance-500-007) | 500 | The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2} at {3} is not of the required type. It should be an instance of {4} |
| [OPEN-GOVERNANCE-500-008](#open-governance-500-008) | 500 | The OMF Services has received an unexpected {0} exception during method {1} for service {2}.  The message was: {3} |

----

### OPEN-GOVERNANCE-404-001

> The open metadata repository services are not initialized for the {0} operation

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceErrorCode.OMRS_NOT_INITIALIZED` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The system cannot connect to the open metadata property server.

**User action**

Check that the server where the Open Metadata Store Services are running initialized correctly.  Correct any errors discovered and retry the request when the open metadata services are available.


----

### OPEN-GOVERNANCE-500-001

> A null topic listener has been passed by user {0} on method {1}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceErrorCode.NULL_LISTENER` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

There is a coding error in the caller to the OMF Services.

**User action**

Correct the caller logic and retry the request.


----

### OPEN-GOVERNANCE-500-006

> The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service running in OMAG Server {2} at {3}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceErrorCode.NULL_CONNECTOR_RETURNED` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot create a connector which means some of its services will not work.

**User action**

This problem is likely to be caused by an incorrect connection object.  Check the settings on the Connectionand correct if necessary.  If the connection is correct, contact the Egeria community for help.


----

### OPEN-GOVERNANCE-500-007

> The connector generated from the connection named {0} return by the {1} service running in OMAG Server {2} at {3} is not of the required type. It should be an instance of {4}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceErrorCode.WRONG_TYPE_OF_CONNECTOR` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot create the required connector which means some of its services will not work.

**User action**

Verify that the OMAG server is running and the OMAS service is correctly configured.


----

### OPEN-GOVERNANCE-500-008

> The OMF Services has received an unexpected {0} exception during method {1} for service {2}.  The message was: {3}

|  |  |
|---|---|
| **Java constant** | `OpenGovernanceErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The request returns with a PropertyServerException to indicate there has been an internal server error. The server also created a detailed error message and stack trace in the audit log.

**User action**

Review the stack trace to identify where the error occurred and work to resolve the cause.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
