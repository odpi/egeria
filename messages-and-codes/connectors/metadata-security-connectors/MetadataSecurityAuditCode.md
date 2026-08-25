<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# MetadataSecurityAuditCode

The MetadataSecurityAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `OPEN-METADATA-ACCESS-SECURITY-` |
| **Java class** | `org.odpi.openmetadata.metadatasecurity.accessconnector.ffdc.MetadataSecurityAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/metadata-security-connectors/open-metadata-access-security-connector](../../../open-metadata-implementation/adapters/open-connectors/metadata-security-connectors/open-metadata-access-security-connector) |
| **Source** | [MetadataSecurityAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/metadata-security-connectors/open-metadata-access-security-connector/src/main/java/org/odpi/openmetadata/metadatasecurity/accessconnector/ffdc/MetadataSecurityAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/server-metadata-security-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OPEN-METADATA-ACCESS-SECURITY-0001](#open-metadata-access-security-0001) | EXCEPTION | The {0} open metadata security connector encountered an {1} exception when attempting to retrieve information for user {2} and group {3} from the secrets store; the error message was {4} |

----

### OPEN-METADATA-ACCESS-SECURITY-0001

> The {0} open metadata security connector encountered an {1} exception when attempting to retrieve information for user {2} and group {3} from the secrets store; the error message was {4}

|  |  |
|---|---|
| **Java constant** | `MetadataSecurityAuditCode.SECRETS_STORE_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector is not able to confirm access to the resource through this secrets store

**User action**

This message contains the exception that was the original cause of the problem.  Use the diagnostics .


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
