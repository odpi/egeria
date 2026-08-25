<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode

The DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.ffdc.DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-directory-connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-directory-connector) |
| **Source** | [DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-directory-connector/src/main/java/org/odpi/openmetadata/adapters/repositoryservices/archiveconnector/directory/ffdc/DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/open-metadata-archive-store-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-0002](#ocf-directory-open-metadata-archive-store-connector-0002) | EXCEPTION | Unable to open directory "{0}".  Message from {1} exception was {2} |

----

### OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-0002

> Unable to open directory "{0}".  Message from {1} exception was {2}

|  |  |
|---|---|
| **Java constant** | `DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The caller is cannot open an open metadata archive.

**User action**

Use the information from the exception to determine the cause of the error.  For example, is the directory (folder) name correct?  Look particularly for extraneous quotes, incorrect directory name (relative files are read from the perspective of the caller's home directory) or incorrect characters.  Does the server have permission to access the directory?  Once the cause of the error is corrected, restart the caller.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
