<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OWFErrorCode

The OWF error code is used to define first failure data capture (FFDC) for errors that occur when working with OWF Services. It is used in conjunction with all OWF Exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `OPEN-WATCHDOG-` |
| **Java class** | `org.odpi.openmetadata.frameworks.openwatchdog.ffdc.OWFErrorCode` |
| **Module** | [open-metadata-implementation/frameworks/open-watchdog-framework](../../open-metadata-implementation/frameworks/open-watchdog-framework) |
| **Source** | [OWFErrorCode.java](../../open-metadata-implementation/frameworks/open-watchdog-framework/src/main/java/org/odpi/openmetadata/frameworks/openwatchdog/ffdc/OWFErrorCode.java) |
| **Further reading** | <https://egeria-project.org/frameworks/owf/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OPEN-WATCHDOG-400-001](#open-watchdog-400-001) | 400 | No context supplied to the watchdog action service {0} |
| [OPEN-WATCHDOG-500-001](#open-watchdog-500-001) | 500 | Unexpected exception in watchdog action service {0} of type {1} detected by method {2}.  The error message was {3} |

----

### OPEN-WATCHDOG-400-001

> No context supplied to the watchdog action service {0}

|  |  |
|---|---|
| **Java constant** | `OWFErrorCode.NULL_WATCHDOG_CONTEXT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The watchdog action service is not able to determine which asset to analyze.

**User action**

This may be a configuration or a code error.  Look for other error messages and review the code of the watchdog action service.  Once the cause is resolved, retry the watchdog action request.


----

### OPEN-WATCHDOG-500-001

> Unexpected exception in watchdog action service {0} of type {1} detected by method {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OWFErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The watchdog action service failed during its operation.

**User action**

Look for other error messages and review the code of the watchdog action service.  Once the cause is resolved, retry the watchdog action request.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
