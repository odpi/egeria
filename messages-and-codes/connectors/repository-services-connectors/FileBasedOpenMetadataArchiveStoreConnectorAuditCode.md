<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# FileBasedOpenMetadataArchiveStoreConnectorAuditCode

The FileBasedOpenMetadataArchiveStoreConnectorAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.ffdc.FileBasedOpenMetadataArchiveStoreConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-file-connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-file-connector) |
| **Source** | [FileBasedOpenMetadataArchiveStoreConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-file-connector/src/main/java/org/odpi/openmetadata/adapters/repositoryservices/archiveconnector/file/ffdc/FileBasedOpenMetadataArchiveStoreConnectorAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/open-metadata-archive-store-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-0001](#file-open-metadata-archive-store-connector-0001) | STARTUP | Opening file {0} for Open Metadata Archive Store |
| [FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-0002](#file-open-metadata-archive-store-connector-0002) | ERROR | Unable to open file {0}.  Message from {1} exception was {2} |

----

### FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-0001

> Opening file {0} for Open Metadata Archive Store

|  |  |
|---|---|
| **Java constant** | `FileBasedOpenMetadataArchiveStoreConnectorAuditCode.OPENING_FILE` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The local server is requesting the contents of the open metadata archive store which is located in the named file.

**User action**

Validate that the file name is correct.  Look particularly for extraneous quotes, incorrect directory name (relative files are read from the perspective of the server's home directory) or incorrect characters.  Once the file name is corrected (either in the server's configuration or the command that loaded the archive) then retry themechanism that loads the archive.


----

### FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-0002

> Unable to open file {0}.  Message from {1} exception was {2}

|  |  |
|---|---|
| **Java constant** | `FileBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The server is cannot open an open metadata archive store.

**User action**

Use the information from the exception to determine the cause of the error.  For example, is the filename correct?  Does this runtime have permission to access the file?  Once the cause of the error is corrected, restart the caller.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
