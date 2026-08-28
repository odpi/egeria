<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# MendelErrorCode

The MendelErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Mendel Automated Duplicate Manager. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `MENDEL-DUPLICATE-MANAGER-500-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.mendel.ffdc.MendelErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [MendelErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/mendel/ffdc/MendelErrorCode.java) |
| **Further reading** | <https://egeria-project.org/features/duplicate-management/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [MENDEL-DUPLICATE-MANAGER-500-001](#mendel-duplicate-manager-500-001) | 500 | The {0} watchdog action service received an unexpected exception {1} during method {2}; the error message was: {3} |
| [MENDEL-DUPLICATE-MANAGER-500-002](#mendel-duplicate-manager-500-002) | 500 | The {0} watchdog action service is unable to register a listener for open metadata events due to a {1} exception with message {2} |

----

### MENDEL-DUPLICATE-MANAGER-500-001

> The {0} watchdog action service received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `MendelErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The service is unable to manage one or more duplicate links.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### MENDEL-DUPLICATE-MANAGER-500-002

> The {0} watchdog action service is unable to register a listener for open metadata events due to a {1} exception with message {2}

|  |  |
|---|---|
| **Java constant** | `MendelErrorCode.UNABLE_TO_REGISTER_LISTENER` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The service is unable to start because it receives no notification of new or updated duplicate links.

**User action**

Use the details from the error message to determine the cause of the error and restart the service once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
