<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# CSVLineageImporterAuditCode

The CSVLineageImporterAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `CSV-LINEAGE-IMPORTER-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter.ffdc.CSVLineageImporterAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/csv-lineage-import-integration-connector](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/csv-lineage-import-integration-connector) |
| **Source** | [CSVLineageImporterAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/csv-lineage-import-integration-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/csvlineageimporter/ffdc/CSVLineageImporterAuditCode.java) |
| **Further reading** | <https://egeria-project.org/features/lineage-management/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [CSV-LINEAGE-IMPORTER-0004](#csv-lineage-importer-0004) | ERROR | An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the FileFolder asset for directory {3} (absolute path {4}).  The error message was {5} |

----

### CSV-LINEAGE-IMPORTER-0004

> An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the FileFolder asset for directory {3} (absolute path {4}).  The error message was {5}

|  |  |
|---|---|
| **Java constant** | `CSVLineageImporterAuditCode.UNEXPECTED_EXC_RETRIEVING_FOLDER` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.

**User action**

Use the message in the nested exception to determine the root cause of the error. Once this is resolved, follow the instructions in the messages produced by the integration daemon to restart this connector.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
