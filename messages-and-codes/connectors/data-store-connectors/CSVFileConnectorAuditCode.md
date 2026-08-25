<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# CSVFileConnectorAuditCode

The CSVFileConnectorAuditCode is used to define audi log messages.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `CSV-FILE-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.datastore.csvfile.ffdc.CSVFileConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector) |
| **Source** | [CSVFileConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/datastore/csvfile/ffdc/CSVFileConnectorAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/digital-resource-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [CSV-FILE-CONNECTOR-0003](#csv-file-connector-0003) | EXCEPTION | The {0} CSV File connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### CSV-FILE-CONNECTOR-0003

> The {0} CSV File connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `CSVFileConnectorAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
