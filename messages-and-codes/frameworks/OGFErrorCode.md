<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OGFErrorCode

The GAF error code is used to define first failure data capture (FFDC) for errors that occur when working with GAF Components. It is used in conjunction with the GAFCheckedException and GAFRuntimeException.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `OPEN-GOVERNANCE-ACTION-` |
| **Java class** | `org.odpi.openmetadata.frameworks.opengovernance.ffdc.OGFErrorCode` |
| **Module** | [open-metadata-implementation/frameworks/open-governance-framework](../../open-metadata-implementation/frameworks/open-governance-framework) |
| **Source** | [OGFErrorCode.java](../../open-metadata-implementation/frameworks/open-governance-framework/src/main/java/org/odpi/openmetadata/frameworks/opengovernance/ffdc/OGFErrorCode.java) |
| **Further reading** | <https://egeria-project.org/frameworks/ogf/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OPEN-GOVERNANCE-ACTION-400-001](#open-governance-action-400-001) | 400 | No governance context supplied to the governance action service {0} |
| [OPEN-GOVERNANCE-ACTION-500-001](#open-governance-action-500-001) | 500 | Unexpected {0} exception in governance action service {1} of type {2} detected by method {3}.  The error message was {4} |

----

### OPEN-GOVERNANCE-ACTION-400-001

> No governance context supplied to the governance action service {0}

|  |  |
|---|---|
| **Java constant** | `OGFErrorCode.NULL_GOVERNANCE_CONTEXT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The governance action service has no access to open metadata, the request type and request parameters.

**User action**

This may be a configuration or, more likely a code error in the governance engine.  Look for other error messages and review the code of the governance action service.  Once the cause is resolved, retry the governance request.


----

### OPEN-GOVERNANCE-ACTION-500-001

> Unexpected {0} exception in governance action service {1} of type {2} detected by method {3}.  The error message was {4}

|  |  |
|---|---|
| **Java constant** | `OGFErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The governance action service failed during its operation.

**User action**

This may be a configuration or a code error.  Look for other error messages and review the code of the governance action service. Once the cause is resolved, retry the governance request.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
