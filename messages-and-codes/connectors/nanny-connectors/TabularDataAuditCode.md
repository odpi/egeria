<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# TabularDataAuditCode

The TabularDataAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `TABULAR-METADATA-CONNECTORS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [TabularDataAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/jacquard/tabulardatasets/ffdc/TabularDataAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/tabular-data-set-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [TABULAR-METADATA-CONNECTORS-0001](#tabular-metadata-connectors-0001) | ERROR | The {0} Tabular Metadata Connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### TABULAR-METADATA-CONNECTORS-0001

> The {0} Tabular Metadata Connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `TabularDataAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot connector the the OMAG Infrastructure.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
