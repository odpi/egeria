<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# FileBasedOpenMetadataArchiveStoreConnectorErrorCode

The FileBasedOpenMetadataArchiveStoreConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur within the FileBasedOpenMetadataArchiveStoreConnector. It is used in conjunction with all exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-` |
| **Java class** | `org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.ffdc.FileBasedOpenMetadataArchiveStoreConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-file-connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-file-connector) |
| **Source** | [FileBasedOpenMetadataArchiveStoreConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-file-connector/src/main/java/org/odpi/openmetadata/adapters/repositoryservices/archiveconnector/file/ffdc/FileBasedOpenMetadataArchiveStoreConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/open-metadata-archive-store-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-002](#file-open-metadata-archive-store-connector-400-002) | 400 | Unable to open file {0}.  Message from {1} exception was {2} |

----

### FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-002

> Unable to open file {0}.  Message from {1} exception was {2}

|  |  |
|---|---|
| **Java constant** | `FileBasedOpenMetadataArchiveStoreConnectorErrorCode.BAD_FILE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The server is cannot open an open metadata archive store.

**User action**

Use the information from the exception to determine the cause of the error.  For example, is the filename correct?  Does this runtime have permission to access the file?  Once the cause of the error is corrected, restart the caller.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
