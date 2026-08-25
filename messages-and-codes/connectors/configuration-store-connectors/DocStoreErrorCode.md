<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# DocStoreErrorCode

The DocStoreErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the clear text file based doc store. It is used in conjunction with all Exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `FILE-DOC-STORE-400-` |
| **Java class** | `org.odpi.openmetadata.adapters.adminservices.configurationstore.file.DocStoreErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/configuration-store-connectors/configuration-file-store-connector](../../../open-metadata-implementation/adapters/open-connectors/configuration-store-connectors/configuration-file-store-connector) |
| **Source** | [DocStoreErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/configuration-store-connectors/configuration-file-store-connector/src/main/java/org/odpi/openmetadata/adapters/adminservices/configurationstore/file/DocStoreErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/configuration-document-store-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [FILE-DOC-STORE-400-001](#file-doc-store-400-001) | 400 | Unable to retrieve the configuration files; exception was {0} with message {1}, while attempting access file {2} |
| [FILE-DOC-STORE-400-002](#file-doc-store-400-002) | 400 | Unable to retrieve the configuration files because the store template name {0}. It needs only 1 or 2 inserts that are in separate segments |

----

### FILE-DOC-STORE-400-001

> Unable to retrieve the configuration files; exception was {0} with message {1}, while attempting access file {2}

|  |  |
|---|---|
| **Java constant** | `DocStoreErrorCode.CONFIG_RETRIEVE_ALL_ERROR` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system was unable to retrieve the configuration files.

**User action**

Review the full stack trace in the logs to troubleshoot further. Then retry the request.


----

### FILE-DOC-STORE-400-002

> Unable to retrieve the configuration files because the store template name {0}. It needs only 1 or 2 inserts that are in separate segments

|  |  |
|---|---|
| **Java constant** | `DocStoreErrorCode.CONFIG_RETRIEVE_ALL_ERROR_INVALID_TEMPLATE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system was unable to retrieve the configuration files as the template was invalid.

**User action**

Either use the default store template or specify a valid template.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
