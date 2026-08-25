<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode

The DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur within the DirectoryBasedOpenMetadataArchiveStoreConnector. It is used in conjunction with all exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-` |
| **Java class** | `org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.ffdc.DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-directory-connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-directory-connector) |
| **Source** | [DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-directory-connector/src/main/java/org/odpi/openmetadata/adapters/repositoryservices/archiveconnector/directory/ffdc/DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/open-metadata-archive-store-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-013](#ocf-directory-open-metadata-archive-store-connector-400-013) | 400 | An unexpected {0} exception was caught by {1}; error message was {2} |
| [OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-014](#ocf-directory-open-metadata-archive-store-connector-400-014) | 400 | Method {0} cannot locate an instance with guid {1} in the archive |

----

### OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-013

> An unexpected {0} exception was caught by {1}; error message was {2}

|  |  |
|---|---|
| **Java constant** | `DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system is not able to take action on the request.

**User action**

Review the error message and other diagnostics created at the same time.


----

### OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-014

> Method {0} cannot locate an instance with guid {1} in the archive

|  |  |
|---|---|
| **Java constant** | `DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode.UNKNOWN_GUID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the incoming request.

**User action**

Check the error message and other diagnostics created at the same time.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
