<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMAGCommonAuditCode

The OMAGCommonAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `OMAG-COMMON-` |
| **Java class** | `org.odpi.openmetadata.commonservices.ffdc.OMAGCommonAuditCode` |
| **Module** | [open-metadata-implementation/common-services/ffdc-services](../../open-metadata-implementation/common-services/ffdc-services) |
| **Source** | [OMAGCommonAuditCode.java](../../open-metadata-implementation/common-services/ffdc-services/src/main/java/org/odpi/openmetadata/commonservices/ffdc/OMAGCommonAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/ffdc-services/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMAG-COMMON-0001](#omag-common-0001) | EXCEPTION | The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2} |
| [OMAG-COMMON-0002](#omag-common-0002) | ERROR | A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3} |

----

### OMAG-COMMON-0001

> The Open Metadata Service has generated an unexpected {0} exception during method {1}.  The message was: {2}

|  |  |
|---|---|
| **Java constant** | `OMAGCommonAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The request returns a PropertyServerException.

**User action**

This is probably a logic error. Review the stack trace to identify where the error occurred and work to resolve the cause.


----

### OMAG-COMMON-0002

> A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OMAGCommonAuditCode.CLIENT_SIDE_REST_API_ERROR` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.

**User action**

Look for errors in the local server's console to understand and correct the source of the error.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
