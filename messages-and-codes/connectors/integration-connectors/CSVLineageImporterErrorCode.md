<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# CSVLineageImporterErrorCode

The CSVLineageImporterErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Basic File Connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `CSV-LINEAGE-IMPORTER-400-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter.ffdc.CSVLineageImporterErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/csv-lineage-import-integration-connector](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/csv-lineage-import-integration-connector) |
| **Source** | [CSVLineageImporterErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/csv-lineage-import-integration-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/csvlineageimporter/ffdc/CSVLineageImporterErrorCode.java) |
| **Further reading** | <https://egeria-project.org/features/lineage-management/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [CSV-LINEAGE-IMPORTER-400-004](#csv-lineage-importer-400-004) | 400 | An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the FileFolder asset for directory {3} (absolute path {4}).  The error message was {5} |

----

### CSV-LINEAGE-IMPORTER-400-004

> An unexpected {0} exception was returned to the {1} integration connector by the {2} method when trying to retrieve the FileFolder asset for directory {3} (absolute path {4}).  The error message was {5}

|  |  |
|---|---|
| **Java constant** | `CSVLineageImporterErrorCode.UNEXPECTED_EXC_RETRIEVING_FOLDER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.

**User action**

Use the message in the nested exception to determine the root cause of the error. Once this is resolved, follow the instructions in the messages produced by the integration daemon to restart the connector.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
